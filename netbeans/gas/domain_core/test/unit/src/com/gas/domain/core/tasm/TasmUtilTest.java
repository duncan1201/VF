/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tasm;

import com.gas.common.ui.core.VariantMapMdl;
import junit.framework.Assert;
import com.gas.domain.core.ace.ACEWriter;
import java.io.File;
import com.gas.domain.core.ace.ACE;
import com.gas.domain.core.shotgun.Coverage;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class TasmUtilTest {

    static List<Condig> condigs;

    public TasmUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        File fromFile = new File("D:\\tmp\\tigr\\myoutput.txt");
        condigs = TasmParser.parse(fromFile);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testToCoverage() {
        System.out.println("toCoverage");
        TasmUtil instance = new TasmUtil();
        Coverage expResult = null;

        for (int i = 0; i < condigs.size(); i++) {
            Condig condig = condigs.get(i);
            //if( i == 2){
            Coverage result = instance.toCoverage(condig);
            System.out.println(result.toString());
            //}
            //break;
        }

    }

    @Test
    public void testConvert() {
        ACE ace = TasmUtil.convert(condigs);
        File file = new File("D:\\tmp\\tigr\\myoutput_copy.ace");
        boolean result = ACEWriter.toFile(ace, file);
        Assert.assertTrue(result);
        System.out.print("");
    }

    @Test
    public void testConvertVariantMapMdl() {
        for (Condig condig : condigs) {
            if (condig.getRids().size() == 3) {
                VariantMapMdl mdl = TasmUtil.toVariantMapMdl(condig, true, true, true, true, true);
                System.out.println(mdl);
            }
        }
    }
}
