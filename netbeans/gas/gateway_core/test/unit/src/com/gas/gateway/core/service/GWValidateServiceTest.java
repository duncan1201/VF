/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.gateway.core.service.api.GW_STATE;
import com.gas.gateway.core.service.attb.AttBDumb;
import com.gas.gateway.core.service.dest.DestDumb;
import com.gas.gateway.core.service.donor.DonorDumb;
import com.gas.gateway.core.service.myentry.EntryDumb2;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;

/**
 *
 * @author dq
 */
public class GWValidateServiceTest {

    Object[][] bpHappyLists = {
        {AttBDumb.class, "attB1-attB2.gb", DonorDumb.class, "pDONR221.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB1-attB4.gb", DonorDumb.class, "pDONR221-P1P4.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB1-attB5r.gb", DonorDumb.class, "pDONR221-P1P5r.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB2r-attB3.gb", DonorDumb.class, "pDONR-P2rP3.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB3-attB2.gb", DonorDumb.class, "pDONR221-P3P2.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB4-attB1r.gb", DonorDumb.class, "pDONR-P4P1r.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB4r-attB3r.gb", DonorDumb.class, "pDONR221-P4rP3r.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB5-attB2.gb", DonorDumb.class, "pDONR221-P5P2.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB5-attB4.gb", DonorDumb.class, "pDONR221-P5P4.gb", GW_STATE.VALID},};
    Object[][] bpFailLists = {
        {AttBDumb.class, "attB1-attB2.gb", DonorDumb.class, GW_STATE.DONOR_NOT_FOUND},
        {AttBDumb.class, "attB1-attB4.gb", "attB1-attB2.gb", DonorDumb.class, "pDONR221-P1P4.gb", GW_STATE.DONOR_INSERT_COUNT_NO_MATCH},
        {AttBDumb.class, "attB1-attB5r.gb", DonorDumb.class, "pDONR221-P1P4.gb", GW_STATE.DONOR_INSERT_NOT_MATCHING},
        {AttBDumb.class, "attB4-attB2-invalid.gb", DonorDumb.class, "pDONR-P2rP3.gb", GW_STATE.INSERT_NOT_FOUND},
        {AttBDumb.class, "attB3-attB2.gb", DonorDumb.class, "pDONR221-P3P2.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB4-attB1r.gb", DonorDumb.class, "pDONR-P4P1r.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB4r-attB3r.gb", DonorDumb.class, "pDONR221-P4rP3r.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB5-attB2.gb", DonorDumb.class, "pDONR221-P5P2.gb", GW_STATE.VALID},
        {AttBDumb.class, "attB5-attB4.gb", DonorDumb.class, "pDONR221-P5P4.gb", GW_STATE.VALID},};
    Object[][] lrHappyLists = {
        {EntryDumb2.class, "entryClone-L1-L2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", GW_STATE.VALID},
        {EntryDumb2.class, "entryClone-L1-R5.gb", "entryClone-L5-L2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", GW_STATE.VALID},
        {EntryDumb2.class, "entryClone-L1-L4.gb", "entryClone-R4-R3.gb", "entryClone-L3-L2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", GW_STATE.VALID},
        {EntryDumb2.class, "entryClone-L1-R5.gb", "entryClone-L5-L4.gb", "entryClone-R4-R3.gb", "entryClone-L3-L2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", GW_STATE.VALID},
        {EntryDumb2.class, "entryClone-L4-R1.gb", "entryClone-L1-L2.gb", "entryClone-R2-L3.gb", DestDumb.class, "pDESTR4-R3 Vector II.gb", GW_STATE.VALID}
    };
    Object[][] lrUnhappyLists = {
        {EntryDumb2.class, DestDumb.class, "pcDNA3-2-V5-DEST.gb", GW_STATE.ENTRY_NOT_FOUND},
        {EntryDumb2.class, "entryClone-L1-R5.gb", "entryClone-L5-L2.gb", DestDumb.class, GW_STATE.DEST_NOT_FOUND},
        {EntryDumb2.class, "entryClone-L1-L4.gb", "entryClone-L1-L4.gb", "entryClone-L1-L4.gb", "entryClone-R4-R3.gb", "entryClone-L3-L2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", GW_STATE.ENTRY_TOO_MANY},
        {EntryDumb2.class, "entryClone-L4-R1.gb", "entryClone-L1-L2.gb", DestDumb.class, "pDESTR4-R3 Vector II.gb", GW_STATE.ENTRY_TOO_LITTLE},
        {EntryDumb2.class, "entryClone-L1-R5.gb", "entryClone-L1-L4.gb", "entryClone-R4-R3.gb", "entryClone-L3-L2.gb", DestDumb.class, "pcDNA3-2-V5-DEST.gb", GW_STATE.ENTRY_WRONG_SITE}
    };

    public GWValidateServiceTest() {
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
     * Test of indexOfDestVector method, of class GWValidateService.
     */
    @Test
    public void testIndexOfDestVector_happy_cases() {
        System.out.println("testIndexOfDestVector_happy_cases");
        Object[][] destLists = {
            {EntryDumb2.class, "entryClone-L1-L2.gb", "entryClone-L1-L4.gb", "entryClone-L4-R1.gb", "entryClone-R4-R3.gb", 3},
            {DestDumb.class, "pDEST10.gb", "pDESTR4-R3.gb", "pcDNA3-2-V5-DEST.gb", 0},
            {AttBDumb.class, "attB1-attB2.gb", "attB1-attB4.gb", "attB1-attB5r.gb", "attB2r-attB3.gb", "attB3-attB2.gb", -1},
            {DonorDumb.class, "pDONR-P2rP3.gb", "pDONR-P4P1r.gb", "pDONR-Zeo.gb", "pDONR201.gb", "pDONR207.gb",
                "pDONR221-P1P4.gb", "pDONR221-P1P5r.gb", "pDONR221-P3P2.gb", "pDONR221-P4rP3r.gb", "pDONR221-P5P2.gb",
                "pDONR221-P5P4.gb", "pDONR221.gb", -1},};
        for (Object[] list : destLists) {
            List<AnnotatedSeq> seqList = getSeqList(list);
            GWValidateService instance = new GWValidateService();
            Integer expectedResult = (Integer) list[list.length - 1];
            Integer result = instance.indexOfDestVector(seqList);
            if (expectedResult != null) {
                Assert.assertEquals(expectedResult, result);
            }
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

    private void processLRList(Object[][] xLists, boolean equals) {
        for (Object[] oList : xLists) {
            List<AnnotatedSeq> seqs = new ArrayList<AnnotatedSeq>();
            GW_STATE expectedState = null;
            Class clazz = null;
            for (Object o : oList) {
                if (o instanceof Class) {
                    clazz = (Class) o;
                } else if (o instanceof String) {
                    String s = (String) o;
                    AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, s, new FlexGenbankFormat());
                    seqs.add(as);
                } else if (o instanceof GW_STATE) {
                    expectedState = (GW_STATE) o;
                }
            }
            GWValidateService instance = new GWValidateService();
            GW_STATE state = instance.validateLR(seqs);
            if (expectedState != null) {
                if (equals) {
                    Assert.assertEquals(expectedState, state);
                } else {
                    Assert.assertNotSame(expectedState, state);
                }
            }
            System.out.println(state);
        }
    }

    private void processBPList(Object[][] xLists) {
        for (Object[] oList : xLists) {
            List<AnnotatedSeq> seqs = new ArrayList<AnnotatedSeq>();
            GW_STATE expectedState = null;
            Class clazz = null;
            for (Object o : oList) {
                if (o instanceof Class) {
                    clazz = (Class) o;
                } else if (o instanceof String) {
                    String s = (String) o;
                    AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, s, new FlexGenbankFormat());
                    seqs.add(as);
                } else if (o instanceof GW_STATE) {
                    expectedState = (GW_STATE) o;
                }
            }
            GWValidateService instance = new GWValidateService();
            GW_STATE state = instance.validateBP(seqs);
            Assert.assertEquals(expectedState, state);
            System.out.println(state);
        }
    }

    @Test
    public void testValidateBP_happy_cases() {
        System.out.println("testValidateBP_happy_cases");
        processBPList(bpHappyLists);
    }

    @Test
    public void testValidateBP_fail_cases() {
        System.out.println("testValidateBP_fail_cases");
        processBPList(bpFailLists);
    }

    @Test
    public void testValidateLR_happy_cases() {
        System.out.println("testValidateLR_happy_cases");
        processLRList(lrHappyLists, true);
    }

    @Test
    public void testValidateLR_unhappy_cases() {
        System.out.println("testValidateLR_unhappy_cases");
        processLRList(lrUnhappyLists, true);
    }
}
