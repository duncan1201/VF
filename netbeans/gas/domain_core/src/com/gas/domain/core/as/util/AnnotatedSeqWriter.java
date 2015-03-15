package com.gas.domain.core.as.util;

import com.gas.domain.core.as.AsHelper;
import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Comment;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.LocationHelper;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Qualifier;
import com.gas.domain.core.as.Reference;
import com.gas.domain.core.as.StructuredComment;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.flexrs.FlexSimpleRichSequence;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.SymbolList;
import org.biojavax.Namespace;
import org.biojavax.RichAnnotation;
import org.biojavax.RichObjectFactory;
import org.biojavax.SimpleComment;
import org.biojavax.SimpleCrossRef;
import org.biojavax.SimpleDocRef;
import org.biojavax.SimpleNote;
import org.biojavax.SimpleRankedCrossRef;
import org.biojavax.SimpleRankedDocRef;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.SimpleRichFeature;
import org.biojavax.bio.taxa.NCBITaxon;
import org.biojavax.bio.taxa.SimpleNCBITaxon;
import org.biojavax.ontology.ComparableTerm;

public class AnnotatedSeqWriter {

    public static void toFile(AnnotatedSeq as, File file) {
        String str = toString(as);
        FileHelper.toFile(file, str);
    }

    public static String toString(AnnotatedSeq as) {
        RichSequence rs = Converter.toRichSequence(as);
        String ret = RichSequenceWriter.toString(rs, new FlexGenbankFormat());
        return ret;
    }

    public static class Converter {

        public static RichSequence toRichSequence(AnnotatedSeq as) {
            String accession = as.getAccession();
            String name = as.getAccession();
            int version = 1;

            SymbolList sl = AsHelper.toSymbolList(as);
            double seqVersion = 1.0;
            Namespace ns = RichObjectFactory.getDefaultNamespace();
            FlexSimpleRichSequence rs = new FlexSimpleRichSequence(ns, name, accession, version, sl, seqVersion);
            rs.setDescription(as.getDesc());
            
            if(as.getTaxonName() != null){
                NCBITaxon taxo = new SimpleNCBITaxon(as.getTaxonId());
                taxo.addName(NCBITaxon.COMMON, as.getTaxonName());
                rs.setTaxon(taxo);
            }
            
            rs.setCircular(as.isCircular());

            rs.getFeatureSet().addAll(toFeatureSet(as));

            rs.getKeywords().addAll(as.getKeywords());

            convertComments(rs, as);

            convertReferences(rs, as);

            rs.setLocus(as.getLocus());

            return rs;
        }

        private static void convertReferences(RichSequence rs, AnnotatedSeq as) {
            Iterator<Reference> refItr = as.getReferences().iterator();
            while (refItr.hasNext()) {
                Reference ref = refItr.next();

                Integer start = ref.getStart();
                Integer end = ref.getEnd();
                int rank = ref.getRank();
                String authors = ref.getAuthors();
                String location = ref.getLocation();
                String title = ref.getTitle();

                SimpleDocRef docRef = new SimpleDocRef(authors, location, title);

                SimpleRankedDocRef srRef = new SimpleRankedDocRef(docRef, start, end, rank);

                rs.getRankedDocRefs().add(srRef);
            }
        }

        private static Set<Feature> toFeatureSet(AnnotatedSeq as) {
            Set<Feature> ret = new HashSet<Feature>();
            List<Feture> fetures = AsHelper.getAllFetures(as, false);
            Collections.sort(fetures, new Feture.StartComparator());
            Iterator<Feture> fetureItr = fetures.iterator();
            while (fetureItr.hasNext()) {
                Feture feture = fetureItr.next();
                Feature feature = toFeature(feture);

                ret.add(feature);
            }
            return ret;
        }

        private static Feature toFeature(Feture feture) {
            SimpleRichFeature ret = (SimpleRichFeature) RichFeature.Tools.makeEmptyFeature();
            if (!feture.isKeylegal()) {
                ComparableTerm term = RichObjectFactory.getDefaultOntology().getOrCreateTerm("note");
                String value = String.format("%s:%s", Qualifier.VF_TYPE, feture.getKey());
                SimpleNote note = new SimpleNote(term, value, 1);
                ((RichAnnotation) ret.getAnnotation()).addNote(note);
                ret.setType("misc_feature");
            } else {
                ret.setType(feture.getKey());
            }

            Lucation lucation = feture.getLucation();

            Location loc = LocationHelper.toLocation(lucation);

            ret.setLocation(loc);

            Iterator<Qualifier> qualifierItr = feture.getQualifierSet().iterator();
            while (qualifierItr.hasNext()) {
                Qualifier qualifier = qualifierItr.next();

                String key = qualifier.getKey();
                String value = qualifier.getValue();

                ComparableTerm term = RichObjectFactory.getDefaultOntology().getOrCreateTerm(key.toString());
                if (key.equalsIgnoreCase("db_xref")) {
                    String[] dbRef = value.split(":");
                    if (dbRef.length == 2) {
                        SimpleCrossRef crossRef = new SimpleCrossRef(dbRef[0], dbRef[1], 1);
                        SimpleRankedCrossRef ref = new SimpleRankedCrossRef(crossRef, 1);
                        ret.addRankedCrossRef(ref);
                    }
                } else {
                    SimpleNote note = new SimpleNote(term, value, 1);
                    ((RichAnnotation) ret.getAnnotation()).addNote(note);
                }
            }

            return ret;
        }

        private static void convertComments(RichSequence rs, AnnotatedSeq as) {
            Comment comment = as.getComment();
            if (comment != null || !as.getStructuredComments().isEmpty()) {
                String data = "";
                if (comment != null) {
                    data = comment.getData();
                }
                if (!as.getStructuredComments().isEmpty()) {
                    String scStr = StructuredComment.toString(as.getStructuredComments());
                    data += "\n\n" + scStr;
                }
                SimpleComment simpleComment = new SimpleComment(data, 10);
                rs.getComments().add(simpleComment);
            }
        }
    }
}
