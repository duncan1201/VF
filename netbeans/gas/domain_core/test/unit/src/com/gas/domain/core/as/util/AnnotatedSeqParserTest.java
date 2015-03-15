/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import com.gas.domain.core.as.Reference;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.util.Enumeration;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class AnnotatedSeqParserTest {

    static AnnotatedSeq NM_053396_modified_gb;
    static AnnotatedSeq NM_001206524_gb;
    static AnnotatedSeq AAB07223_gp;
    static AnnotatedSeq pUC19_gb;
    static AnnotatedSeq FW361640_gb;

    public AnnotatedSeqParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        NM_053396_modified_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqParserTest.class, "data/NM_053396_modified.gb", new FlexGenbankFormat());
        AAB07223_gp = AnnotatedSeqParser.singleParse(AnnotatedSeqParserTest.class, "data/AAB07223.gp", new FlexGenbankFormat());
        pUC19_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqParserTest.class, "data/pUC19.gb", new FlexGenbankFormat());
        FW361640_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqParserTest.class, "data/FW361640.gb", new FlexGenbankFormat());
        System.out.print("");
    }

    //@Test
    public void testAccession() {
        System.out.println("testAccession()");
        String accession = NM_053396_modified_gb.getAccession();
        Assert.assertEquals("NM_053396", accession);

        accession = pUC19_gb.getAccession();
        Assert.assertEquals("pUC19", accession);

        Set<Reference> refs = NM_053396_modified_gb.getReferences();
        Assert.assertEquals(5, refs.size());


        //Assert.assertEquals(NM_001206524_gb.getAccession(), "NM_001206524");
    }

    //@Test
    public void testLocation() {
        System.out.println("testLocation()");
        String str = AnnotatedSeqWriter.toString(NM_053396_modified_gb);
        System.out.println(str);
    }

    //@Test
    public void testReference() {
        System.out.println("testReference()");
        Set<Reference> refs = NM_053396_modified_gb.getReferences();
        Assert.assertEquals(5, refs.size());

        List<String> accessions = new ArrayList<String>();
        accessions.add("19549762");
        accessions.add("18948702");
        accessions.add("12711600");
        accessions.add("10719090");
        accessions.add("9314580");

        Iterator<Reference> refItr = refs.iterator();
        while (refItr.hasNext()) {
            Reference ref = refItr.next();

            String accession = ref.getAccession();

            Assert.assertTrue(accessions.contains(accession));
        }
    }

    @Test
    public void testSingleParse_3args() {
        
        System.out.println(FW361640_gb.isNucleotide());
        System.out.println(FW361640_gb.isRNA());
        System.out.println(FW361640_gb.getSiquence().getData());
        String str = AnnotatedSeqWriter.toString(FW361640_gb);
        System.out.println(str);
    }
}
