/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.gateway.core.service.api.ILRService;
import com.gas.gateway.core.service.dest.DestDumb;
import com.gas.gateway.core.service.myentry.EntryDumb2;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class LRServiceTest {

    public LRServiceTest() {
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
     * Test of LR method, of class LRService.
     */
    @Test
    public void testLR() {
        System.out.println("LR");
        ILRService service = Lookup.getDefault().lookup(ILRService.class);
        String[][] namesList = {
            {"entryClone-L1-L2.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro1.gb"},
            {"entryClone-L1-R5.gb", "entryClone-L5-L2.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro2.gb"},
            {"entryClone-R4-R3.gb", "entryClone-L1-L4.gb", "entryClone-L3-L2.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro3.gb"},
            {"entryClone-L1-R5.gb", "entryClone-L5-L4.gb", "entryClone-R4-R3.gb", "entryClone-L3-L2.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro4.gb"},
            {"entryClone-L4-R1.gb", "entryClone-L1-L2.gb", "entryClone-R2-L3.gb", "pDESTR4-R3 Vector II.gb", "expression-clone-frag3.gb"}
        };

        for (String[] names : namesList) {
            List<AnnotatedSeq> entryClones = new ArrayList<AnnotatedSeq>();
            AnnotatedSeq dest = null;
            for (int i = 0; i < names.length; i++) {
                AnnotatedSeq as = null;
                if (i < names.length - 2) {
                    as = AnnotatedSeqParser.singleParse(EntryDumb2.class, names[i], new FlexGenbankFormat());
                    entryClones.add(as);
                } else if(i == names.length - 2){
                    as = AnnotatedSeqParser.singleParse(DestDumb.class, names[i], new FlexGenbankFormat());
                    dest = as;
                }
            }
            List<AnnotatedSeq> seqs = new ArrayList<AnnotatedSeq>();
            seqs.addAll(entryClones);
            seqs.add(dest);
            AnnotatedSeq exp = service.createExpressionClone(seqs);
            String fileName = names[names.length - 1];
            AnnotatedSeqWriter.toFile(exp, new File("D:\\tmp\\"+fileName));
        }
    }
    
    //@Test
    public void testLR_entryClone_wrong_order() {
        System.out.println("testLR_entryClone_wrong_order");
        ILRService service = Lookup.getDefault().lookup(ILRService.class);
        String[][] namesList = {
            {"entryClone-L1-L2.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro1_wrong_order.gb"},
            {"entryClone-L5-L2.gb", "entryClone-L1-R5.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro2_wrong_order.gb"},
            {"entryClone-R4-R3.gb", "entryClone-L1-L4.gb", "entryClone-L3-L2.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro3_wrong_order.gb"},
            {"entryClone-L5-L4.gb", "entryClone-R4-R3.gb", "entryClone-L1-R5.gb","entryClone-L3-L2.gb", "pcDNA3-2-V5-DEST.gb", "expression-clone-pro4_wrong_order.gb"}
        };

        for (String[] names : namesList) {
            List<AnnotatedSeq> entryClones = new ArrayList<AnnotatedSeq>();
            AnnotatedSeq dest = null;
            for (int i = 0; i < names.length; i++) {
                AnnotatedSeq as = null;
                if (i < names.length - 2) {
                    as = AnnotatedSeqParser.singleParse(EntryDumb2.class, names[i], new FlexGenbankFormat());
                    entryClones.add(as);
                } else if(i == names.length - 2){
                    as = AnnotatedSeqParser.singleParse(DestDumb.class, names[i], new FlexGenbankFormat());
                    dest = as;
                }
            }
            AnnotatedSeq exp = null;
            //exp = service.createExpressionClone(entryClones, dest);
            List<AnnotatedSeq> seqs = new ArrayList<AnnotatedSeq>();
            seqs.addAll(entryClones);
            seqs.add(dest);
            exp = service.createExpressionClone(seqs);
            String fileName = names[names.length - 1];
            AnnotatedSeqWriter.toFile(exp, new File("D:\\tmp\\"+fileName));
        }
    }
}
