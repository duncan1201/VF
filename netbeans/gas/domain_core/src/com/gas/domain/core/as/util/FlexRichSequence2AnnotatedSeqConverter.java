/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import com.gas.domain.core.as.*;
import com.gas.domain.core.flexrs.IFlexRichSequence;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.biojava.bio.seq.Feature;
import org.biojavax.*;
import org.biojavax.bio.seq.RichLocation;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.SimpleRichFeature;
import org.biojavax.bio.seq.SimpleRichSequence;
import org.biojavax.bio.taxa.NCBITaxon;

/**
 *
 * @author dq
 */
public class FlexRichSequence2AnnotatedSeqConverter {

    public static AnnotatedSeq to(String name, Date importedDate, Date lastModified, IFlexRichSequence rs) {
        AnnotatedSeq ret = to(rs);
        //ret.setName(name);
        ret.setCreationDate(importedDate);
        ret.setLastModifiedDate(lastModified);
        return ret;
    }

    private static AnnotatedSeq to(IFlexRichSequence rs) {
        AnnotatedSeq ret = new AnnotatedSeq();

        initGeneralInfo(ret, rs);

        initKeywords(ret, rs);

        initDbnames(ret, rs);

        initSequenceProperties(ret, rs);

        initReference(ret, rs);

        initFetures(ret, rs);

        initComments(ret, rs);

        return ret;
    }

    private static void initGeneralInfo(AnnotatedSeq as, IFlexRichSequence richSequence) {

        String accession = richSequence.getAccession();
        as.setAccession(accession);
        as.setLocus(richSequence.getLocus());

        NCBITaxon taxon = richSequence.getTaxon();
        if (taxon != null) {
            int taxonId = taxon.getNCBITaxID();
            as.setTaxonId(taxonId);
            String taxonName = taxon.getDisplayName();
            as.setTaxonName(taxonName);
        }
        String desc = richSequence.getDescription();
        as.setDesc(desc);
        String division = richSequence.getDivision();
        as.setDivision(division);

        String seqData = richSequence.getInternalSymbolList().seqString();
        int length = seqData.length();
        as.setLength(length);
        as.setSequence(seqData);

        Boolean circular = richSequence.getCircular();
        as.setCircular(circular);
    }

    private static void initKeywords(AnnotatedSeq as, IFlexRichSequence richSequence) {
        // _keywords
        Set<String> k = ((IFlexRichSequence) richSequence).getKeywords();
        as.setKeywords(k);
    }

    private static void initDbnames(AnnotatedSeq as, IFlexRichSequence richSequence) {
        Map<String, String> dbsources = ((IFlexRichSequence) richSequence).getDbsources();
        Iterator<String> dbnames = dbsources.keySet().iterator();
        while (dbnames.hasNext()) {
            String dbname = dbnames.next();
            String value = dbsources.get(dbname);


            if (dbname.equals("xrefs")) {
                String[] entries = value.split(",");
                for (String entry : entries) {
                    Dbref ref = new Dbref();
                    ref.setDb(dbname);
                    ref.setEntry(entry);
                    as.getDbrefs().add(ref);
                }
            } else if (dbname.equals("xrefs (non-sequence databases)")) {
                String[] entries = value.split(",");
                for (String entry : entries) {
                    String[] kv = entry.split(":", 2);
                    if (kv.length > 1) {
                        if (kv.length == 2) {
                            Dbref ref = new Dbref();
                            ref.setDb(kv[0]);
                            ref.setEntry(kv[1]);
                            as.getDbrefs().add(ref);
                        }
                    }

                }
            }
        }
    }

    private static void initSequenceProperties(AnnotatedSeq as, IFlexRichSequence richSequence) {
        // notes
        Iterator noteSets = richSequence.getNoteSet().iterator();
        while (noteSets.hasNext()) {
            SimpleNote obj = (SimpleNote) noteSets.next();
            String key = obj.getTerm().getName();
            String value = obj.getValue();
            as.getSequenceProperties().put(key, value);
        }
    }

    private static void initReference(AnnotatedSeq as, IFlexRichSequence richSequence) {
        Iterator itr = richSequence.getRankedDocRefs().iterator();
        while (itr.hasNext()) {
            SimpleRankedDocRef rankedDocRef = (SimpleRankedDocRef) itr.next();
            SimpleDocRef docRef = (SimpleDocRef) rankedDocRef.getDocumentReference();
            SimpleCrossRef crossRef = (SimpleCrossRef) docRef.getCrossref();

            Reference ref = new Reference();
            ref.setTitle(docRef.getTitle());
            ref.setRank(rankedDocRef.getRank());
            ref.setStart(rankedDocRef.getStart());
            ref.setEnd(rankedDocRef.getEnd());
            if (crossRef != null) {
                ref.setAccession(crossRef.getAccession());
                ref.setDb(crossRef.getDbname());
            }

            ref.setAuthors(docRef.getAuthors());
            ref.setLocation(docRef.getLocation());
            ref.setRemark(docRef.getRemark());
            as.getReferences().add(ref);
        }
    }

    private static String getVfType(Feature feature) {
        String ret = null;
        SimpleRichAnnotation anno = (SimpleRichAnnotation) feature.getAnnotation();
        Set notes = anno.getNoteSet();
        Iterator noteItr = notes.iterator();
        while (noteItr.hasNext()) {
            SimpleNote note = (SimpleNote) noteItr.next();
            String name = note.getTerm().getName();
            String value = note.getValue();
            if (name.equalsIgnoreCase("note") && value.contains(Qualifier.VF_TYPE)) {
                if(value.startsWith("\"")){
                    value = value.substring(1);
                }
                if(value.endsWith("\"")){
                    value = value.substring(0, value.length() - 1);
                }
                ret = value.substring(value.indexOf(":") + 1).trim();
                notes.remove(note);
                break;
            }
        }
        return ret;
    }

    private static void initFetures(AnnotatedSeq as, IFlexRichSequence richSequence) {
        RichSequence rs = (RichSequence) richSequence;
        Iterator<Feature> features = rs.getFeatureSet().iterator();
        while (features.hasNext()) {
            Feature feature = features.next();
            String vfType = getVfType(feature);
            
            Feture feture = new Feture();
            if(vfType != null && !vfType.isEmpty()){
                feture.setKey(vfType);
            }else{
                feture.setKey(feature.getType());
            }
            RichLocation richLocation = (RichLocation) feature.getLocation();

            Lucation lucation = LocationHelper.toLucation(richLocation);

            feture.setLucation(lucation);

            SimpleRichAnnotation anno = (SimpleRichAnnotation) feature.getAnnotation();
            Iterator noteSets = anno.getNoteSet().iterator();
            while (noteSets.hasNext()) {
                SimpleNote note = (SimpleNote) noteSets.next();
                String name = note.getTerm().getName();
                String value = note.getValue();
                if (name != null && value != null) {
                    feture.getQualifierSet().add(new Qualifier(name, value));
                }
            }

            SimpleRichFeature srf = (SimpleRichFeature) feature;
            Iterator itr = srf.getRankedCrossRefs().iterator();
            while (itr.hasNext()) {
                SimpleRankedCrossRef ref = (SimpleRankedCrossRef) itr.next();
                String accession = ref.getCrossRef().getAccession();
                String dbName = ref.getCrossRef().getDbname();
                feture.getQualifierSet().add(new Qualifier("db_xref", dbName + ":" + accession));
            }
            as.getFetureSet().add(feture);
        }
    }

    private static void initComments(AnnotatedSeq as, IFlexRichSequence richSequence) {
        RichSequence rs = (RichSequence) richSequence;
        Iterator objects = rs.getComments().iterator();
        while (objects.hasNext()) {
            SimpleComment obj = (SimpleComment) objects.next();
            com.gas.domain.core.as.Comment comment = new com.gas.domain.core.as.Comment();

            String data = obj.getComment();

            String dataNoStructured = StructuredComment.removeStructuredComments(data);
            comment.setData(dataNoStructured);
            comment.setRank(obj.getRank());
            as.setComment(comment);

            List<StructuredComment> scs = StructuredComment.parse(data);
            if (!scs.isEmpty()) {
                as.getStructuredComments().addAll(scs);
            }
        }
    }
}
