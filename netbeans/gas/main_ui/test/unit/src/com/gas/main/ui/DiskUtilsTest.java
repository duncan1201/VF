/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class DiskUtilsTest {
    
    public DiskUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSerialNumber method, of class DiskUtils.
     */
    @Test
    public void testGetComputerFingerprint() throws IOException {
        System.out.println("getSerialNumber");        
        DiskUtil instance = new DiskUtil();
        File[] roots = File.listRoots();
        for(File root: roots){
            System.out.println(root.getCanonicalPath());
            System.out.println(root.getAbsolutePath());
        }
        String expResult = "";
        String result = instance.getComputerFingerprint();
        System.out.println(result);
    }
}