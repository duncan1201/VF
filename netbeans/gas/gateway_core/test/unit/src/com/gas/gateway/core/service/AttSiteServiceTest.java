/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.gateway.core.service.api.AttSite;
import com.gas.gateway.core.service.api.AttSiteList;
import com.gas.gateway.core.service.api.PrimerAdapter;
import com.gas.gateway.core.service.attb.AttBDumb;
import com.gas.gateway.core.service.dest.DestDumb;
import com.gas.gateway.core.service.donor.DonorDumb;
import com.gas.gateway.core.service.entry.EntryDumb;
import com.gas.gateway.core.service.myentry.EntryDumb2;
import java.io.File;
import java.util.*;
import org.junit.*;

/**
 *
 * @author dq
 */
public class AttSiteServiceTest {

    String[] pcr = {"attB1-attB2.gb", "attB1-attB4.gb", "attB1-attB5r.gb", "attB2r-attB3.gb",
        "attB3-attB2.gb", "attB4-attB1r.gb",
        "attB4r-attB3r.gb", "attB5-attB2.gb", "attB5-attB4.gb"};
    String[] destVectors = {"pcDNA3-2-V5-DEST.gb", "pDEST10.gb", "pDEST14.gb", "pDEST15.gb", "pDEST17.gb", "pDEST20.gb",
        "pDEST22.gb", "pDEST24.gb", "pDEST26.gb", "pDEST27.gb", "pDEST32.gb", "pDEST8.gb",
        "pDESTR4-R3 Vector II rc.gb", "pDESTR4-R3 Vector II.gb", "pDESTR4-R3.gb"};
    String[] donorVectors = {"pDONR-Zeo.gb", "pDONR201.gb", "pDONR207.gb", "pDONR221-P1P4.gb",
        "pDONR221-P1P5r.gb", "pDONR221-P3P2.gb", "pDONR221-P4rP3r.gb", "pDONR221-P5P2.gb",
        "pDONR221-P5P4.gb", "pDONR221.gb", "pDONR-P2rP3.gb", "pDONR-P4P1r.gb"};
    String[] entryVectors = {"pCR8-GW-TOPO.gb", "pENTR L1-pLac-LacZalpha-L4.gb",
        "pENTR L1-pLac-LacZalpha-R5.gb", "pENTR L3-pLac-Tet-L2.gb", "pENTR L5-lacI-L4.gb",
        "pENTR L5-pLac-Spec-L2.gb", "pENTR R4-pLac-Spect-R3.gb", "pENTR-D-TOPO.gb",
        "pENTR-SD-D-TOPO.gb", "pENTR1A DUAL SELECTION VECTOR.gb", "pENTR221.gb","pENTR2B DUAL SELECTION VECTOR.gb",
        "pENTR3C DUAL SELECTION VECTOR.gb", "pENTR4 DUAL SELECTION VECTOR.gb", "pENTR_TEV-D-TOPO.gb"};
    String[] someEntryVectors = {"pENTR L1-pLac-LacZalpha-R5.gb",
        "pENTR L5-pLac-Spec-L2.gb", "pENTR R4-pLac-Spect-R3.gb"};
    String[] myEntryClones = {"entryClone-L1-L2.gb", "entryClone-L1-L4.gb", "entryClone-L1-R5.gb", "entryClone-L3-L2.gb"
    , "entryClone-L4-R1.gb", "entryClone-L5-L2.gb", "entryClone-L5-L4.gb", "entryClone-R2-L3.gb", "entryClone-R4-R3.gb"};
    AnnotatedSeq house_mouse_gene = AnnotatedSeqParser.singleParse(BPServiceTest.class, "house_mouse_gene.gb", new FlexGenbankFormat());

    public AttSiteServiceTest() {
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
    
    //@Test
    public void testGetAttLSites_AnnotatedSeq_myEntryClone() {
        System.out.println("testGetAttLSites_AnnotatedSeq_myEntryClone");
        Map<Class, String[]> map = new HashMap<Class, String[]>();
        map.put(EntryDumb2.class, myEntryClones);

        Iterator<Class> clazzItr = map.keySet().iterator();
        while (clazzItr.hasNext()) {
            Class clazz = clazzItr.next();
            String[] vectors = map.get(clazz);
            for (String v : vectors) {
                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, v, new FlexGenbankFormat());
                AttSiteService instance = new AttSiteService();

                List<AttSite> resultL = instance.getAttLSites(as);
                List<AttSite> resultR = instance.getAttRSites(as);
                List<AttSite> result = new ArrayList<AttSite>();
                result.addAll(resultL);
                result.addAll(resultR);
                Iterator<AttSite> itr = result.iterator();
                System.out.println(v);
                while (itr.hasNext()) {
                    AttSite site = itr.next();
                    System.out.println(String.format("%s%s(%s)", site.getName(),site.getVariation(), site.getLoc()));
                }
                System.out.print("");
            }
        }        
    }
    
    @Test
    public void testGetAttBSites_AnnotatedSeq_String_B() {
        System.out.println("testGetAttBSites_AnnotatedSeq_String_B");
        Map<Class, String[]> map = new HashMap<Class, String[]>();
        map.put(AttBDumb.class, pcr);

        Iterator<Class> clazzItr = map.keySet().iterator();
        while (clazzItr.hasNext()) {
            Class clazz = clazzItr.next();
            String[] vectors = map.get(clazz);
            for (String v : vectors) {
                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, v, new FlexGenbankFormat());
                AttSiteService instance = new AttSiteService();

                AttSiteList siteList = instance.getAttBSites(as, true);
                String shortNames = siteList.getNames(true, false);
                System.out.println(shortNames);
                List<AttSite> result = siteList;
                Iterator<AttSite> itr = result.iterator();
                System.out.println(v);
                while (itr.hasNext()) {
                    AttSite site = itr.next();
                    //System.out.println(String.format("%s(%s)", site.getName(), site.getLoc()));
                }
                System.out.print("");
            }
        }
    }

    /**
     * Test of getAttSites method, of class AttSiteService.
     */
    //@Test
    public void testGetAttSites() {
        System.out.println("testGetAttSites");
        Map<Class, String[]> map = new HashMap<Class, String[]>();
        map.put(DonorDumb.class, donorVectors);
        map.put(EntryDumb.class, entryVectors);
        map.put(AttBDumb.class, pcr);
        map.put(DestDumb.class, destVectors);

        Iterator<Class> clazzItr = map.keySet().iterator();
        while (clazzItr.hasNext()) {
            Class clazz = clazzItr.next();
            String[] vectors = map.get(clazz);
            for (String v : vectors) {
                if (v.equalsIgnoreCase("pDONR221.gb")) {
                    System.out.print("");
                }
                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, v, new FlexGenbankFormat());

                AttSiteService instance = new AttSiteService();

                List<AttSite> result = instance.getAttSites(as, false);
                System.out.println(v);
                for (AttSite site : result) {
                    System.out.println(String.format("%s%s(%s)", site.getName(), site.getVariation(), site.getLoc()));
                }
                System.out.print("");
            }
        }
    }

    /**
     * Test of getAttSites method, of class AttSiteService.
     */
    //@Test
    public void testGetAttSites_AnnotatedSeq_String_R() {
        System.out.println("testGetAttSites_AnnotatedSeq_String_R");
        Map<Class, String[]> map = new HashMap<Class, String[]>();
        map.put(EntryDumb.class, someEntryVectors);
        map.put(DestDumb.class, destVectors);

        Iterator<Class> clazzItr = map.keySet().iterator();
        while (clazzItr.hasNext()) {
            Class clazz = clazzItr.next();
            String[] vectors = map.get(clazz);
            for (String v : vectors) {

                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, v, new FlexGenbankFormat());

                AttSiteService instance = new AttSiteService();

                List<AttSite> result = instance.getAttLRSites(as);
                System.out.println(v);
                for (AttSite site : result) {
                    System.out.println(String.format("%s%s(%s)", site.getName(), site.getVariation(), site.getLoc()));
                }
                System.out.print("");
            }
        }
    }

    /**
     * Test of getAttSites method, of class AttSiteService.
     */
    //@Test
    public void testGetAttSites_AnnotatedSeq_String_P() {
        System.out.println("testGetAttSites_AnnotatedSeq_String_P");
        Map<Class, String[]> map = new HashMap<Class, String[]>();
        map.put(DonorDumb.class, donorVectors);

        Iterator<Class> clazzItr = map.keySet().iterator();
        while (clazzItr.hasNext()) {
            Class clazz = clazzItr.next();
            String[] vectors = map.get(clazz);
            for (String v : vectors) {

                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, v, new FlexGenbankFormat());

                AttSiteService instance = new AttSiteService();

                AttSiteList siteList = instance.getAttPSites(as);
                System.out.println(v);
                System.out.println(siteList.getNames(false, false));
                /*
                List<AttSite> result = siteList.getAttSites();
                System.out.println(v);
                for (AttSite site : result) {
                    System.out.println(String.format("%s%s (%s)", site.getName(), site.getVariation(), site.getLoc()));
                }
                */
            }
        }
    }

    //@Test
    public void testGetAttPSites() {
        System.out.println("testGetAttPSites");
        Map<Class, String[]> map = new HashMap<Class, String[]>();
        map.put(DonorDumb.class, donorVectors);
        Iterator<Class> clazzItr = map.keySet().iterator();
        while (clazzItr.hasNext()) {
            Class clazz = clazzItr.next();
            String[] vectors = map.get(clazz);
            for (String v : vectors) {

                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, v, new FlexGenbankFormat());

                AttSiteService instance = new AttSiteService();

                List<AttSite> result = instance.getAttPSites(as);
                System.out.println(v);
                for (AttSite site : result) {
                    System.out.println(String.format("%s(%s)", site.getName(), site.getLoc()));
                }
                System.out.print("");
            }
        }
    }

    /**
     * Test of getAttSites method, of class AttSiteService.
     */
    //@Test
    public void testGetAttSites_AnnotatedSeq_String_L() {
        System.out.println("testGetAttSites_AnnotatedSeq_String_L");
        Map<Class, String[]> map = new HashMap<Class, String[]>();
        map.put(EntryDumb.class, entryVectors);

        Iterator<Class> clazzItr = map.keySet().iterator();
        while (clazzItr.hasNext()) {
            Class clazz = clazzItr.next();
            String[] vectors = map.get(clazz);
            for (String v : vectors) {

                AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, v, new FlexGenbankFormat());

                AttSiteService instance = new AttSiteService();

                List<AttSite> result = instance.getAttSites(as, 'L', false);
                System.out.println(v);
                for (AttSite site : result) {
                    System.out.println(String.format("%s%s(%s)", site.getName(),site.getVariation(), site.getLoc()));
                }
                System.out.print("");
            }
        }
    }

    // pro1
    //@Test
    public void testAddAttBSite_b1_b2() {
        System.out.println("testAddAttBSite_b1_b2");
        AttSiteService instance = new AttSiteService();
        PrimerAdapter[] left = {PrimerAdapter.attB1,
            PrimerAdapter.attB1,
            PrimerAdapter.attB5,
            PrimerAdapter.attB1,
            PrimerAdapter.attB4r,
            PrimerAdapter.attB3,
            PrimerAdapter.attB5,
            PrimerAdapter.attB4_3FRAG,
            PrimerAdapter.attB2r
        };
        PrimerAdapter[] right = {PrimerAdapter.attB2,
            PrimerAdapter.attB5r,
            PrimerAdapter.attB2,
            PrimerAdapter.attB4,
            PrimerAdapter.attB3r,
            PrimerAdapter.attB2,
            PrimerAdapter.attB4,
            PrimerAdapter.attB1r,
            PrimerAdapter.attB3_3FRAG
        };
        String[] names = {"attB1-attB2", // pro1
            "attB1-attB5r", // pro2, pro4
            "attB5-attB2", // pro2
            "attB1-attB4", // pro3
            "attB4r-attB3r", // pro3, pro4
            "attB3-attB2", // pro3, pro4
            "attB5-attB4", // pro4
            "attB4-attB1r", // 3 frag
            "attB2r-attB3" // 3 frag
        };
        for (int i = 0; i < left.length; i++) {
            PrimerAdapter leftAdapter = left[i];
            PrimerAdapter rightAdapter = right[i];
            String name = names[i];
            System.out.println(leftAdapter.getName() + "-" + rightAdapter.getName());
            AnnotatedSeq ret = instance.addAttBSite(house_mouse_gene, leftAdapter, false, false, true, false, rightAdapter, false, true);
            AnnotatedSeqWriter.toFile(ret, new File("D:\\tmp\\attb\\" + name + ".gb"));
        }
    }
}
