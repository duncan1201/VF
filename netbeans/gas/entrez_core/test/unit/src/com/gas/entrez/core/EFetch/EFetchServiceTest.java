/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EFetch;

import com.gas.common.ui.util.FileHelper;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class EFetchServiceTest {
    
    public EFetchServiceTest() {
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
     * Test of getRettype method, of class EFetchService.
     */
    //@Test
    public void testSendRequest() {
        System.out.println("getRettype");
        EFetchService instance = new EFetchService();
        instance.setDb("structure");
        instance.setIds("75593");
        List<String> ret = instance.sendRequest(String.class);
        FileHelper.toFile(new File("d:\\tmp\\structure_fetch.txt"), ret.get(0));
    }
    
    //@Test
    public void testSendRequest_genome(){
        EFetchService instance = new EFetchService();
        instance.setDb("genome");
        instance.setIds("6115");
        List<String> ret = instance.sendRequest(String.class);
        System.out.println(ret.get(0));
        FileHelper.toFile(new File("d:\\tmp\\genome_fetch.xml"), ret.get(0));
    }
    
    @Test
    public void testSendRequest_bioproject(){
    }

}
