/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import java.awt.Color;
import java.awt.Image;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class ArrowImageCreatorTest {

    public ArrowImageCreatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testAaa() {
        System.out.println("aaa");
        Color seedColor = Color.RED;
        Image expResult = null;
        Image result = ArrowImageCreator.createImage(seedColor, 30, 20);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
