/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import org.biojava.bio.seq.Feature;
import java.util.Iterator;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.flexrs.IFlexRichSequence;
import junit.framework.Assert;
import org.biojava.bio.symbol.Location;
import org.biojavax.SimpleNote;
import org.biojavax.SimpleRankedCrossRef;
import org.biojavax.SimpleRichAnnotation;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.SimpleRichFeature;
import org.biojavax.bio.seq.SimpleRichLocation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class RichSequenceParserTest {

    static RichSequence NM_053396_gb;
    static RichSequence NM_001206524_gb;
    static RichSequence Q9Y6R4_gp;
    static RichSequence pUC19_modified_gb;

    public RichSequenceParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //NM_053396_gb = RichSequenceParser.singleParse(RichSequenceParserTest.class, "data/NM_053396.gb", new FlexGenbankFormat());
        //NM_001206524_gb = RichSequenceParser.singleParse(RichSequenceParserTest.class, "data/NM_001206524.gb", new FlexGenbankFormat());
        //Q9Y6R4_gp = RichSequenceParser.singleParse(RichSequenceParserTest.class, "data/Q9Y6R4.gp", new FlexGenbankFormat());
        //pUC19_modified_gb = RichSequenceParser.singleParse(RichSequenceParserTest.class, "data/pUC19_modified_gb", new FlexGenbankFormat());
    }

    //@Test
    public void testParse_String_RichSequenceFormat() {
        Iterator<Feature> featureItr = NM_053396_gb.getFeatureSet().iterator();
        while (featureItr.hasNext()) {
            SimpleRichFeature feature = (SimpleRichFeature) featureItr.next();
            if (feature.getType().equalsIgnoreCase("CDS")) {
                int refSize = feature.getRankedCrossRefs().size();

                Assert.assertTrue(refSize > 0);

                SimpleRichAnnotation anno = (SimpleRichAnnotation) feature.getAnnotation();
                int size = anno.getNoteSet().size();
                //System.out.println(feature.getClass());
                //System.out.println("size="+size);
                Iterator noteSets = anno.getNoteSet().iterator();
                while (noteSets.hasNext()) {
                    SimpleNote note = (SimpleNote) noteSets.next();
                    String name = note.getTerm().getName();
                    String value = note.getValue();
                    if (name != null && value != null) {
                        System.out.println("name=" + name + "\tvalue=" + value);
                    }
                }
            }
        }

        Assert.assertEquals(NM_001206524_gb.getAccession(), "NM_001206524");

        featureItr = Q9Y6R4_gp.getFeatureSet().iterator();
        while (featureItr.hasNext()) {
            Feature feature = featureItr.next();
            SimpleRichLocation loc = (SimpleRichLocation) feature.getLocation();
            //System.out.println(loc + "\t" + loc.isContiguous());
            //System.out.println(loc.getClass());
            //System.out.println(loc.getMinPosition() + "\t" + loc.getMinPosition().getClass());
            //System.out.println(loc.getMax());
            //System.out.print("");
        }
    }

    @Test
    public void testParse_InputStream_RichSequenceFormat() {
        IFlexRichSequence richSeq = RichSequenceParser.singleParse(this.getClass(), "data/pUC19_modified.gb", new FlexGenbankFormat());
        String str = RichSequenceWriter.toString(richSeq, new FlexGenbankFormat());
        System.out.println(str);
    }
}
