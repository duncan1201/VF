/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.gateway.core.service.attb.AttBDumb;
import com.gas.gateway.core.service.dest.DestDumb;
import com.gas.gateway.core.service.donor.DonorDumb;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;

/**
 *
 * @author dq
 */
public class OneClickServiceTest {

    public OneClickServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createExpressionClone method, of class OneClickService.
     */
    @Test
    public void testCreateExpressionClone() {
        System.out.println("testCreateExpressionClone");
        Object[][] testcases = {
            {AttBDumb.class, "attB1-attB2.gb", DonorDumb.class, "pDONR221.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", new File("D:\\tmp\\expression_clone_pro1_1_click.gb")},// pro1
            {AttBDumb.class, "attB1-attB5r.gb", "attB5-attB2.gb", DonorDumb.class, "pDONR221-P1P5r.gb", "pDONR221-P5P2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", new File("D:\\tmp\\expression_clone_pro2_1_click.gb")},// pro2
            {AttBDumb.class, "attB1-attB4.gb", "attB4r-attB3r.gb", "attB3-attB2.gb",DonorDumb.class, "pDONR221-P1P4.gb", "pDONR221-P4rP3r.gb", "pDONR221-P3P2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", new File("D:\\tmp\\expression_clone_pro3_1_click.gb")},// pro3
            {AttBDumb.class, "attB1-attB5r.gb", "attB5-attB4.gb", "attB4r-attB3r.gb", "attB3-attB2.gb",DonorDumb.class, "pDONR221-P1P5r.gb", "pDONR221-P5P4.gb", "pDONR221-P4rP3r.gb", "pDONR221-P3P2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", new File("D:\\tmp\\expression_clone_pro4_1_click.gb")},// pro4
            {AttBDumb.class, "attB4-attB1r.gb", "attB1-attB2.gb", "attB2r-attB3.gb", DonorDumb.class, "pDONR-P4P1r.gb", "pDONR221.gb", "pDONR-P2rP3.gb", DestDumb.class, "pDESTR4-R3 Vector II.gb", new File("D:\\tmp\\expression_clone_frag3_1_click.gb")},// frag3
        };
        for (Object[] testcase : testcases) {
            List<AnnotatedSeq> seqs = getSeqList(testcase);
            OneClickService instance = new OneClickService();
            AnnotatedSeq result = instance.createExpressionClone(seqs);
            File file = (File)testcase[testcase.length - 1];
            AnnotatedSeqWriter.toFile(result, file);
        }


    }

    protected List<AnnotatedSeq> getSeqList(Object[] oList) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        Class clazz = null;
        for (Object o : oList) {
            if (o instanceof Class) {
                clazz = (Class) o;
            } else if (o instanceof String) {
                String s = (String) o;
                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, s, new FlexGenbankFormat());
                ret.add(as);
            }
        }
        return ret;
    }
}
