/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ESummary;

import java.io.File;
import com.gas.common.ui.util.FileHelper;
import com.gas.entrez.core.ESummary.api.ESummaryResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class ESummaryCmdTest {
    
    public ESummaryCmdTest() {
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



    /**
     * Test of sendRequest method, of class ESummaryCmd.
     */
    @Test
    public void testSendRequest() {
        System.out.println("sendRequest");
        ESummaryCmd instance = new ESummaryCmd();
        instance.setDb("structure");
        instance.setId("20593,74297");
        ESummaryResult expResult = null;
        String result = instance.sendRequest(String.class);
        FileHelper.toFile(new File("D:\\tmp\\strucure_summary"), result);
        System.out.print("");
    }

}
