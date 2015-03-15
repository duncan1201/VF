/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.service;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.TasmParser;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TIGRSettings;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.core.tigr.util.KromatogramHelper;
import com.gas.domain.core.tigr.util.KromatogramParser;
import com.gas.domain.core.tigr.util.TigrProjectIO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class TigrServiceTest {

    public TigrServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testAssembly() {
                byte[] seq = null;
        
        File scfDir = new File("D:\\tmp\\abi\\HS354_C02\\ABI");
        File[] files = scfDir.listFiles();
        
        List<Kromatogram> ks = new ArrayList<Kromatogram>();
        for(File f: files){
            Kromatogram k = KromatogramParser.parse(f);
            ks.add(k);
        }
        
        Fasta fasta = KromatogramHelper.toFasta(ks);
        String fastaStr = fasta.toString(60);
        FileHelper.toFile(new File("D:\\tmp\\tigr\\fasta_str.txt"), fastaStr);
        seq = fastaStr.getBytes();
         
        InputStream seqInputStream = TigrServiceTest.class.getResourceAsStream("201.seq");

        //seq = FileHelper.toBytesQuitely(seqInputStream);

        
        InputStream qualInputStream = TigrServiceTest.class.getResourceAsStream("201.qual");
        byte[] qual = null;
        //qual = FileHelper.toBytesQuitely(qualInputStream);
        
        
        TIGRSettings settings = new TIGRSettings();
        settings.setGenerateRepeatFile(true);
        settings.setGenerateCoverage(true);

        TigrExecuteService w = new TigrExecuteService();

        settings.setGenerateACE(true);
        byte[] ret = w.assembly(seq, qual, settings, byte[].class);
        List<Condig> contigList3 = w.assembly(seq, qual, settings, List.class);
        FileHelper.toFile(new File("D:\\tmp\\tigr\\myoutput.txt"), ret);
        File aceFile = w.getAceFile();
        if (aceFile != null) {
            FileHelper.copy(aceFile, new File("D:\\tmp\\tigr\\my.ace"));
        }
        File coverageFile = w.getCoverageFile();
        if (coverageFile != null){
            FileHelper.copy(coverageFile, new File("D:\\tmp\\tigr\\coverage.txt"));
        }
        File repeatFile = w.getRepeatFile();
        if(repeatFile != null){
            FileHelper.copy(repeatFile, new File("D:\\tmp\\tigr\\repeat.txt"));
        }

        System.out.println("Output:");
        System.out.println(ret);
        List<Condig> contigList2 = TasmParser.parse(ret);
        //ACE ace = TasmUtil.convert(contigList2);
        
        TigrProject tigrProject = new TigrProject();
        tigrProject.addCondigs(contigList2);
        tigrProject.addKromatograms(ks);
        tigrProject.setSettings(settings);
        tigrProject.setCreationDate(new Date());
        tigrProject.setLastModifiedDate(new Date());
        tigrProject.setName("Sample Assembly Project");
        tigrProject.setDesc("Sample Assembly Project");        
        
        tigrProject.createAlteredKromatograms();
        
        File file = new File("D:\\tmp\\tigr\\tigr_project.ser");
        TigrProjectIO.write(tigrProject, file);
    }        
}
