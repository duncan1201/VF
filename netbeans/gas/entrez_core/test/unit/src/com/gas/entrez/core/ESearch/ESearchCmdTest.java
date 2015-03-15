/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ESearch;

import java.io.File;
import com.gas.common.ui.util.FileHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class ESearchCmdTest {
    
    public ESearchCmdTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    //@Test
    public void testSendRequest_structure() throws Exception {
        ESearchCmd cmd = new ESearchCmd();
        cmd.setTerm("brca2");
        cmd.setDb("structure");
        cmd.setRetmax(100);
        String retStr = cmd.sendRequest(String.class);
        FileHelper.toFile(new File("D:\\tmp\\structure_search_01.xml"), retStr);
    }
    
    @Test
    public void testSendRequest_genome() throws Exception {
        ESearchCmd cmd = new ESearchCmd();
        cmd.setTerm("Abaca bunchy top virus");
        cmd.setDb("genome");
        cmd.setRetmax(100);
        String retStr = cmd.sendRequest(String.class);
        //System.out.println(retStr); //6115
        FileHelper.toFile(new File("D:\\tmp\\genome_search_01.xml"), retStr);
    }
}
