/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.mafft.service;

import com.gas.mafft.service.api.MafftParams;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class MafftServiceTest {
    
    public MafftServiceTest() {
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
     * Test of align method, of class MafftService.
     */
    @Test
    public void testAlign() {
        System.out.println("align");
        MafftParams params = null;
        MafftService instance = new MafftService();
        instance.align(params);
        
    }
}
