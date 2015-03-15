/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.pdb.core.fetch;

import java.io.File;
import com.gas.common.ui.util.FileHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class FetchPDBServiceTest {
    
    public FetchPDBServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSendRequest() {
        System.out.println("sendRequest");
        String pdbId = "2FMM";
        Class<String> retType = String.class;
        FetchPDBService instance = new FetchPDBService();
        Object expResult = null;
        String result = instance.sendRequest(pdbId, retType);
        FileHelper.toFile(new File("D:\\tmp\\pdb\\fetch-2fmm.pdb"), result);
        
    }
}
