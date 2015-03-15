/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr.util;

import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.tigr.Kromatogram;
import java.io.File;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class KromatogramParserTest {

    public KromatogramParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testParse_abi() throws Exception {
        File dir = new File("D:\\tmp\\abi\\Demo Projects");        
        File[] files = dir.listFiles();
        for(File file: files){
            String fileName = file.getName();
            if(fileName.endsWith(".abi")){
                System.out.println(fileName);
                Kromatogram result = KromatogramParser.parse(file);
                System.out.println(result.getLength());
                System.out.println("quality values="+result.getQualityValues().size());
                System.out.println();
            }
        }
    }

    //@Test
    public void testParse_ab1() throws Exception {
        File file = new File("d:\\tmp\\abi\\F\\HS354_C02_484_F_0.b.ab1");
        Kromatogram expResult = null;
        Kromatogram result = KromatogramParser.parse(file);
        System.out.print("");
    }

    //@Test
    public void testParse_scf() throws Exception {
        File file = new File("d:\\tmp\\scf\\HS354_C02_484_F_0.b.scf");
        Kromatogram expResult = null;
        Kromatogram result = KromatogramParser.parse(file);
        System.out.print("");
    }

    //@Test
    public void testParse_abi2_files() {
        File scfDir = new File("D:\\tmp\\abi2");
        File[] files = scfDir.listFiles();
        for (File f : files) {
            Kromatogram k = KromatogramParser.parse(f);
            Fasta.Record r = k.toFastaRecord();
            System.out.print("");
        }
    }

    //@Test
    public void testParse_scf_files() {
        File scfDir = new File("D:\\tmp\\scf");
        File[] files = scfDir.listFiles();
        for (File f : files) {
            Kromatogram k = KromatogramParser.parse(f);
            Fasta.Record r = k.toFastaRecord();
            System.out.print("");
        }
    }
}
