/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class TreeNodeLengthTest {
    
    public TreeNodeLengthTest() {
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
     * Test of getAttributeNames method, of class TreeNodeLength.
     */
    @Test
    public void testGetAttributeNames() {
        System.out.println("getAttributeNames");
        TreeNodeLength instance = new TreeNodeLength();
        instance.setLength(1.4f);        
        String[] result = instance.getAttributeNames();
        String[] expected = {"length"};
        Assert.assertArrayEquals(expected, result);
        
        instance.setLengthMean(1.5f);
        result = instance.getAttributeNames();
        expected = new String[]{"length", "lengthMean"};
        Assert.assertArrayEquals(expected, result);
        
        
        Object length = instance.getLength();
        assertEquals(length, 1.4f);
                
        Object lengthMean = instance.getLengthMean();
        assertEquals(lengthMean, 1.5f);
    }
}
