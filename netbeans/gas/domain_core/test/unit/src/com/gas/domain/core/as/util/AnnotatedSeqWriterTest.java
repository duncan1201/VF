/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Pozition;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.flexrs.IFlexRichSequence;
import java.util.Date;
import java.util.Iterator;
import junit.framework.Assert;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;
import org.biojavax.bio.seq.CompoundRichLocation;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.SimpleRichLocation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class AnnotatedSeqWriterTest {

    static AnnotatedSeq NM_053396_gb;
    static AnnotatedSeq NM_053396_modified_gb;
    static AnnotatedSeq swiss_prot_seq_gp;
    static AnnotatedSeq DQ642038_gb;
    static AnnotatedSeq FW361640_gb;

    public AnnotatedSeqWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        NM_053396_modified_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/NM_053396_modified.gb", new FlexGenbankFormat());
        NM_053396_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/NM_053396.gb", new FlexGenbankFormat());
        swiss_prot_seq_gp = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/swiss_prot_seq.gp", new FlexGenbankFormat());
        DQ642038_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/DQ642038.gb", new FlexGenbankFormat());
        FW361640_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/FW361640.gb", new FlexGenbankFormat());
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testToFile() {
    }

    //@Test
    public void testToString2() {
        final String name = "gene";
        IFlexRichSequence richSeq = RichSequenceParser.singleParse(this.getClass(), "data/pUC19_modified.gb", new FlexGenbankFormat());
        Iterator<Feature> fItr = richSeq.getFeatureSet().iterator();
        while (fItr.hasNext()) {
            Feature feature = fItr.next();
            if (feature.getType().equalsIgnoreCase(name)) {
                Location location = feature.getLocation();
                Iterator bItr = location.blockIterator();
                while (bItr.hasNext()) {
                    SimpleRichLocation richLoc = (SimpleRichLocation) bItr.next();
                    int min = richLoc.getMin();
                    int max = richLoc.getMax();
                    System.out.println("min." + min + ";max." + max);
                }
                System.out.print("");
            }
        }

        AnnotatedSeq as = FlexRichSequence2AnnotatedSeqConverter.to(richSeq.getAccession(), new Date(), new Date(), richSeq);
        Iterator<Feture> fetureItr = as.getFetureSet().getByFeturesByKeys(name).iterator();
        while (fetureItr.hasNext()) {
            Feture feture = fetureItr.next();
            Lucation lucation = feture.getLucation();
            int start = lucation.getStart();
            int end = lucation.getEnd();
            System.out.println("start:" + start + ";end:" + end + ";crossingOrigin:" + lucation.isCrossingOrigin());
            Iterator<Pozition> pItr = lucation.getPozitionsItr();
            while (pItr.hasNext()) {
                Pozition poz = pItr.next();
                System.out.println("min:" + poz.getStart() + ";max;" + poz.getEnd());
            }
            System.out.print("");

        }
        RichSequence richSequence = AnnotatedSeqWriter.Converter.toRichSequence(as);
        Iterator<Feature> itr = richSequence.getFeatureSet().iterator();
        while (itr.hasNext()) {
            Feature feature = itr.next();
            Location location = feature.getLocation();
            if (feature.getType().equalsIgnoreCase(name)) {
                Iterator bItr = location.blockIterator();
                while (bItr.hasNext()) {
                    SimpleRichLocation richLoc = (SimpleRichLocation) bItr.next();
                    int min = richLoc.getMin();
                    int max = richLoc.getMax();
                    System.out.println("min=" + min + ";max=" + max);
                }
                System.out.print("");
            }
        }
        String str = RichSequenceWriter.toString(richSequence, new FlexGenbankFormat());
        System.out.println(str);
    }

    @Test
    public void testToString() {
        //Assert.assertTrue(AsHelper.isRNA(NM_053396_gb));
        //Assert.assertTrue(AsHelper.isAminoAcid(swiss_prot_seq_gp));
        String ret ;
        //ret = = AnnotatedSeqWriter.toString(NM_053396_gb);
        //ret = AnnotatedSeqWriter.toString(swiss_prot_seq_gp);
        //System.out.println(ret);
        System.out.print("");
        //ret = AnnotatedSeqWriter.toString(DQ642038_gb);
        //System.out.println(ret);
        ret = AnnotatedSeqWriter.toString(FW361640_gb);
        System.out.println(ret);
    }
}
