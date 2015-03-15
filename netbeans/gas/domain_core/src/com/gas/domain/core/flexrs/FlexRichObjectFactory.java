package com.gas.domain.core.flexrs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.biojavax.CrossReferenceResolver;
import org.biojavax.DummyCrossReferenceResolver;
import org.biojavax.Namespace;
import org.biojavax.RichObjectBuilder;
import org.biojavax.SimpleNamespace;
import org.biojavax.SimpleRichObjectBuilder;
import org.biojavax.bio.seq.DummyRichSequenceHandler;
import org.biojavax.bio.seq.PositionResolver;
import org.biojavax.bio.seq.RichSequenceHandler;
import org.biojavax.bio.seq.PositionResolver.AverageResolver;
import org.biojavax.ontology.ComparableOntology;
import org.biojavax.ontology.SimpleComparableOntology;

/**
 * Runs a service that builds rich objects, and provides some default values for
 * things like default ontology, default namespace, etc.
 *
 * @author Richard Holland
 * @since 1.5
 */
public class FlexRichObjectFactory {

    private static RichObjectBuilder builder = new SimpleRichObjectBuilder();
    private static String defaultOntologyName = "biojavax";
    private static String defaultNamespaceName = "lcl";
    private static PositionResolver defaultPositionResolver = new AverageResolver();
    private static CrossReferenceResolver defaultCrossRefResolver = new DummyCrossReferenceResolver();
    private static RichSequenceHandler defaultRichSequenceHandler = new DummyRichSequenceHandler();
    // the LRU cache - keys are classes, entries are maps of param sets to objects
    private static int defaultLRUcacheSize = 20;
    private static Map LRUcacheSizes = new HashMap();
    //private static Map cache = new HashMap();
    private final static Map applicationClassMap = new HashMap();

    // Constructor is private as this is all static.
    private FlexRichObjectFactory() {
    }

    /**
     * Sets the builder to use when instantiating new Rich objects. The basic,
     * default, one is a SimpleRichObjectBuilder, which just calls the
     * constructor. Another useful one is BioSQLRichObjectBuilder, which
     * attempts to load objects from the database. The latter is required if you
     * are working with Hibernate as it will not work without it.
     *
     * @param b the builder to use.
     * @see SimpleRichObjectBuilder
     * @see org.biojavax.bio.db.biosql.BioSQLRichObjectBuilder
     */
    public static synchronized void setRichObjectBuilder(RichObjectBuilder b) {
        builder = b;
    }

    /**
     * Delegates to a RichObjectBuilder to construct/retrieve the object, and
     * returns it. To increase efficiency, it keeps a list of recently requested
     * objects. If it receives further requests for the same object, it returns
     * them from the cache. The size of the cache can be altered using
     * setLRUCacheSize(). The default cache size is 20 objects for each type of
     * class requested.
     *
     * @param clazz the class to build
     * @param params the parameters to pass to the class' constructor
     * @return the instantiated object
     */
    public static synchronized Object getObject(final Class clazz, Object[] params) {
        List paramsList = Arrays.asList(params);
        final Class applicationClass = getApplicationClass(clazz);


        return builder.buildObject(applicationClass, paramsList);
    }

    private final static Map getApplicationClassMap() {
        return applicationClassMap;
    }

    /**
     * Allow application to override the default biojava class created in
     * getObject - subclass restriction is checked in the builder.
     *
     * @param theBiojavaClass one of the well-known builder classes:
     * SimpleNamespace, SimpleComparableOntology, SimpleNCBITaxon,
     * SimpleCrossRef, or SimpleDocRef
     * @param theApplicationClass - a subclass of theBiojavaClass
     */
    public final static void setApplicationClass(final Class theBiojavaClass, final Class theApplicationClass) {
        if (theApplicationClass == null || theBiojavaClass.isAssignableFrom(theApplicationClass) == false) {
            throw new IllegalArgumentException("RichObjectFactory.setApplicationClass-theApplicationClass: <" + theApplicationClass + "> must be assignable to the biojava class: <" + theBiojavaClass + ">");
        }
        getApplicationClassMap().put(theBiojavaClass, theApplicationClass);
    }

    private final static Class getApplicationClass(final Class theBiojavaClass) {
        final Class applicationClass = (Class) getApplicationClassMap().get(theBiojavaClass);
        return applicationClass != null ? applicationClass : theBiojavaClass;
    }

    /**
     * Sets the size of the LRU cache. This is the size per class of object
     * requested, so if you set it to 20 and request 3 different types of
     * object, you will get 20*3=60 entries in the cache. The default cache size
     * is 20. Setting this value will undo any previous changes made using the
     * setLRUCacheSize(Class,int) method below, but will not override future
     * ones.
     *
     * @param size the size of the cache.
     */
    public static void setLRUCacheSize(int size) {
        defaultLRUcacheSize = size;
        for (Iterator i = LRUcacheSizes.keySet().iterator(); i.hasNext();) {
            LRUcacheSizes.put(i.next(), new Integer(size));
        }
    }

    /**
     * Sets the size of the LRU cache. This is the size for the specific class
     * of object requested, so does not affect the size of caches of other
     * objects. If this method is not called, then the cache size defaults to
     * 20, or whatever value was passed to setLRUCacheSize(int) above.
     *
     * @param size the size of the cache.
     */
    public static void setLRUCacheSize(Class clazz, int size) {
        LRUcacheSizes.put(clazz, new Integer(size));
    }

    /**
     * Sets the default namespace name to use when loading sequences. Defaults
     * to "lcl".
     *
     * @param name the namespace name to use.
     */
    public static void setDefaultNamespaceName(String name) {
        defaultNamespaceName = name;
    }

    /**
     * Sets the default ontology name to use when loading sequences. Defaults to
     * "biojavax".
     *
     * @param name the ontology name to use.
     */
    public static void setDefaultOntologyName(String name) {
        defaultOntologyName = name;
    }

    /**
     * Sets the default position resolver to use when creating new rich feature
     * locations. Defaults to the AverageResolver
     *
     * @param pr the position resolver to use.
     * @see org.biojavax.bio.seq.PositionResolver
     * @see org.biojavax.bio.seq.PositionResolver.AverageResolver
     * @see org.biojavax.bio.seq.RichLocation
     */
    public static void setDefaultPositionResolver(PositionResolver pr) {
        defaultPositionResolver = pr;
    }

    /**
     * Sets the default crossref resolver to use when resolving remote entries.
     * Defaults to the DummyCrossReferenceResolver.
     *
     * @param crr the resolver to use.
     * @see org.biojavax.CrossReferenceResolver
     * @see org.biojavax.DummyCrossReferenceResolver
     */
    public static void setDefaultCrossReferenceResolver(CrossReferenceResolver crr) {
        defaultCrossRefResolver = crr;
    }

    /**
     * Sets the default sequence handler to use when performing sequence
     * manipulation. Defaults to the DummyRichSequenceHandler.
     *
     * @param rsh the resolver to use.
     * @see org.biojavax.bio.seq.RichSequenceHandler
     * @see org.biojavax.bio.seq.DummyRichSequenceHandler
     */
    public static void setDefaultRichSequenceHandler(RichSequenceHandler rsh) {
        defaultRichSequenceHandler = rsh;
    }

    /**
     * Returns the default namespace object. Defaults to "lcl".
     *
     * @return the default namespace.
     */
    public static Namespace getDefaultNamespace() {
        return (Namespace) getObject(SimpleNamespace.class, new Object[]{defaultNamespaceName});
    }

    /**
     * Returns the default ontology object. Defaults to "biojavax".
     *
     * @return the default ontology.
     */
    public static ComparableOntology getDefaultOntology() {
        return (ComparableOntology) getObject(SimpleComparableOntology.class, new Object[]{defaultOntologyName});
    }

    /**
     * Returns the default position resolver object. Defaults to
     * PositionResolver.AverageResolver
     *
     * @return the default position resolver.
     * @see org.biojavax.bio.seq.PositionResolver.AverageResolver
     */
    public static PositionResolver getDefaultPositionResolver() {
        return defaultPositionResolver;
    }

    /**
     * Returns the default cross ref resolver object. Defaults to
     * DummyCrossReferenceResolver
     *
     * @return the default resolver.
     * @see org.biojavax.DummyCrossReferenceResolver
     */
    public static CrossReferenceResolver getDefaultCrossReferenceResolver() {
        return defaultCrossRefResolver;
    }

    /**
     * Returns the default sequence resolver object. Defaults to
     * DummyRichSequenceHandler.
     *
     * @return the default resolver.
     * @see org.biojavax.bio.seq.DummyRichSequenceHandler
     */
    public static RichSequenceHandler getDefaultRichSequenceHandler() {
        return defaultRichSequenceHandler;
    }
// commenting out for the moment, since it prevents core from compiling.
//    /** 
//     * A utility method that configures the RichObjectFactory for use with a Hibernate session.
//     * @param session an object containing a Hibernate session.
//     */
//    public static void connectToBioSQL(Object session) {
//    	clearLRUCache();
//        RichObjectFactory.setRichObjectBuilder(new BioSQLRichObjectBuilder(session));
//        RichObjectFactory.setDefaultCrossReferenceResolver(new BioSQLCrossReferenceResolver(session));      
//        RichObjectFactory.setDefaultRichSequenceHandler(new BioSQLRichSequenceHandler(session));        
//    }
}
