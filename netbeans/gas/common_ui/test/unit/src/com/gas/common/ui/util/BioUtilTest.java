/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import com.gas.common.ui.util.BioUtil;
import java.util.List;
import java.util.Locale;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class BioUtilTest {

    public BioUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetNonAmbiguousDNAs() {
        List<String> ret = BioUtil.getNonAmbiguousDNAs("ANC");
        for(String s: ret){
            System.out.println(s);
        }
    }

    //@Test
    public void testReverseComplement() {
        String exepcted = "cgat";
        String result = BioUtil.reverseComplement("ATCG");
        //Assert.assertEquals(exepcted, result);
        Assert.assertEquals(exepcted.toUpperCase(Locale.ENGLISH), result.toUpperCase(Locale.ENGLISH));
    }

    //@Test
    public void testComplement() {
        String exepcted = "";

        for (int i = 0; i < BioUtil.DNAs.length; i++) {
            String dna = BioUtil.DNAs[i];
            String result = null;
            if (!dna.equals("U")) {
                result = BioUtil.complement(dna);
            } else {
                result = BioUtil.complement("T");
            }
            //System.out.println(String.format("dnaCompMap.put(\"%s\", \"%s\");", dna, result.toUpperCase(Locale.ENGLISH)));

        }

        //Assert.assertEquals(exepcted, result);
    }
}
