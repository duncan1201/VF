/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr.util;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.tigr.Kromatogram;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class KromatogramHelperTest {

    public KromatogramHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testToFasta() {
        File scfDir = new File("D:\\tmp\\scf");
        File[] files = scfDir.listFiles();
        List<Kromatogram> ks = new ArrayList<Kromatogram>();
        for (File f : files) {
            Kromatogram k = KromatogramParser.parse(f);
            ks.add(k);
        }

        Fasta fasta = KromatogramHelper.toFasta(ks);
        String fastaStr = fasta.toString(60);
        FileHelper.toFile(new File("D:\\tmp\\tigr\\fasta_str.txt"), fastaStr);
        //seq = fastaStr.getBytes();
    }
}
