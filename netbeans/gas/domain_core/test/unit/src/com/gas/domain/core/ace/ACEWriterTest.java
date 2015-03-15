/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ace;

import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.TasmParser;
import com.gas.domain.core.tasm.TasmUtil;
import java.io.File;
import java.util.List;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class ACEWriterTest {

    public ACEWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testToBytes() {
    }

    @Test
    public void testToFile_02() {
        System.out.println("toFile");
        File fromFile = new File("D:\\tmp\\tigr\\myoutput.txt");
        List<Condig> condigs = TasmParser.parse(fromFile);
        ACE ace = TasmUtil.convert(condigs);
        File file = new File("D:\\tmp\\tigr\\myoutput_copy.ace");
        boolean result = ACEWriter.toFile(ace, file);
        Assert.assertTrue(result);
        System.out.print("");
    }

    @Test
    public void testToFile() {
        System.out.println("toFile");
        ACE ace = ACEParser.parse(new File("D:\\tmp\\tigr\\xyz.cap.ace"));
        File file = new File("D:\\tmp\\tigr\\xyz_copy.ace");
        boolean result = ACEWriter.toFile(ace, file);
        Assert.assertTrue(result);
        System.out.print("");
    }

    @Test
    public void testToFile_ligr() {
        System.out.println("toFile");
        File fromFile = new File("D:\\tmp\\tigr\\ligr_output.txt");
        List<Condig> condigs = TasmParser.parse(fromFile);
        ACE ace = TasmUtil.convert(condigs);
        File file = new File("D:\\tmp\\tigr\\ligr_output.ace");
        boolean result = ACEWriter.toFile(ace, file);
        Assert.assertTrue(result);
        System.out.print("");
    }
}
