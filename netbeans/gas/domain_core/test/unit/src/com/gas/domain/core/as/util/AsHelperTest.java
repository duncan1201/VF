/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.util.Iterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class AsHelperTest {

    static AnnotatedSeq NM_053396_gb;
    static AnnotatedSeq swiss_prot_seq_gp;
    static AnnotatedSeq DQ642038_gb;
    static AnnotatedSeq pUC19_modified;
    static AnnotatedSeq pDONR222;
    static AnnotatedSeq pDONR222_modified;

    @BeforeClass
    public static void setUpClass() throws Exception {
        NM_053396_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/NM_053396.gb", new FlexGenbankFormat());
        swiss_prot_seq_gp = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/swiss_prot_seq.gp", new FlexGenbankFormat());
        DQ642038_gb = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/DQ642038.gb", new FlexGenbankFormat());
        pUC19_modified = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/pUC19_modified.gb", new FlexGenbankFormat());
        pDONR222 = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/pDONR222.gb", new FlexGenbankFormat());
        pDONR222_modified = AnnotatedSeqParser.singleParse(AnnotatedSeqWriterTest.class, "data/pDONR222_modified.gb", new FlexGenbankFormat());        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    //@Test
    public void testRemoveSeq_pUC19_modified() {
        AsHelper.print(pUC19_modified);
        AsHelper.removeSeq(pUC19_modified, 1, 1, true);
        AsHelper.print(pUC19_modified);

    }

    //@Test
    public void testRemoveSeq() {
        AsHelper.print(DQ642038_gb);
        int start = 3500;
        int end = 3509;
        AsHelper.removeSeq(DQ642038_gb, start, end, true);
        System.out.println(String.format("%d %d", start, end));
        AsHelper.print(DQ642038_gb);
    }

    //@Test
    public void testRemoveSeq_2() {
        AsHelper.print(DQ642038_gb);
        int start = 5500;
        int end = 5700;
        AsHelper.removeSeq(DQ642038_gb, start, end, true);
        System.out.println(String.format("----%d-%d----", start, end));
        AsHelper.print(DQ642038_gb);
    }

    //@Test
    public void testRemoveSeq_3() {
        AsHelper.print(DQ642038_gb);
        int start = 7600;
        int end = 1;
        AsHelper.removeSeq(DQ642038_gb, start, end, true);
        System.out.println(String.format("----%d-%d----", start, end));
        AsHelper.print(DQ642038_gb);
    }

    //@Test
    public void testSubAs() {
        AsHelper.print(DQ642038_gb);
        int start = 500;
        int end = 900;
        AnnotatedSeq sub = AsHelper.subAs(DQ642038_gb, start, end);
        System.out.println(String.format("----%d-%d----", start, end));
        AsHelper.print(sub);
    }
    
    @Test
    public void testRemoveSeq_pDONR222_modified(){
        System.out.println(AnnotatedSeqWriter.toString(pDONR222_modified));
        AnnotatedSeq ret = AsHelper.createByRemoving(pDONR222_modified, 442, 2692, true);
        System.out.println(AnnotatedSeqWriter.toString(ret));
        final int insertLength = 73;
        //ret.getFetureSet().translate(insertLength, 442, false, ret.getLength() + insertLength);
        //System.out.println(AnnotatedSeqWriter.toString(ret));
    }
}
