/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.gateway.core.service.api.AttSiteList;
import com.gas.gateway.core.service.api.IAttSiteService;
import com.gas.gateway.core.service.attb.AttBDumb;
import com.gas.gateway.core.service.donor.DonorDumb;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.*;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class BPServiceTest {

    private IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);
    
    public BPServiceTest() {
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
    
    @Test
    public void testGetDonors(){
        System.out.println("testGetDonors");
        String[][] reactionList = {            
            {"pDONR221-P5P2.gb", "pDONR221-P1P5r.gb"}, // pro2
            {"pDONR221-P3P2.gb","pDONR221-P4rP3r.gb", "pDONR221-P1P4.gb"}, // pro3
            {"pDONR221-P5P4.gb","pDONR221-P3P2.gb","pDONR221-P4rP3r.gb", "pDONR221-P1P5r.gb"}, // pro4
        };
        BPService instance = new BPService();
        for(String[] names: reactionList){
            List<AnnotatedSeq> donors = new ArrayList<AnnotatedSeq>();
            for(String name: names){
                AnnotatedSeq d = AnnotatedSeqParser.singleParse(DonorDumb.class, name, new FlexGenbankFormat());
                donors.add(d);
            }
            
            Collections.sort(donors, new Sorters.DonorSorter());
            
            for(AnnotatedSeq donor: donors){
                AttSiteList siteList = attSiteService.getAttPSites(donor);
                System.out.println(siteList.getShortNames());
            }
        }
    }

    @Test
    public void testCreateEntryClone_AnnotatedSeq_AnnotatedSeq() {
        System.out.println("testCreateEntryClone_AnnotatedSeq_AnnotatedSeq");
        String[][] reactionList = {
            {"attB1-attB2.gb", "pDONR221.gb", "entryClone-L1-L2.gb"}, // pro1, frag3
            {"attB1-attB5r.gb", "pDONR221-P1P5r.gb", "entryClone-L1-R5.gb"}, // pro 2, pro 4
            {"attB5-attB2.gb", "pDONR221-P5P2.gb",   "entryClone-L5-L2.gb"}, // pro2
            {"attB1-attB4.gb", "pDONR221-P1P4.gb",   "entryClone-L1-L4.gb"}, // pro3
            {"attB4r-attB3r.gb","pDONR221-P4rP3r.gb","entryClone-R4-R3.gb"}, // pro3, pro4
            {"attB3-attB2.gb", "pDONR221-P3P2.gb",   "entryClone-L3-L2.gb"}, // pro3, pro4
            {"attB5-attB4.gb", "pDONR221-P5P4.gb",   "entryClone-L5-L4.gb"}, // pro4
            {"attB4-attB1r.gb", "pDONR-P4P1r.gb",    "enntryClone-L4-R1.gb"}, // frag3
            {"attB2r-attB3.gb", "pDONR-P2rP3.gb",    "entryClone-R2-L3.gb"} // frag3
        };
        for (String[] reaction : reactionList) {
            String insertName = reaction[0];
            String donorName = reaction[1];
            String cloneName = reaction[2];

            AnnotatedSeq as = null;
            AnnotatedSeq donorVector = null;
            as = AnnotatedSeqParser.singleParse(AttBDumb.class, insertName, new FlexGenbankFormat());
            donorVector = AnnotatedSeqParser.singleParse(DonorDumb.class, donorName, new FlexGenbankFormat());
            BPService instance = new BPService();
            AnnotatedSeq entryClone = instance.createEntryClone(as, donorVector, true);
            AnnotatedSeqWriter.toFile(entryClone, new File("D:\\tmp\\" + cloneName));
        }
    }
}
