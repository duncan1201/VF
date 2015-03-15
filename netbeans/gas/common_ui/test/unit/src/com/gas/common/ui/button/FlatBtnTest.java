/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.button;

import com.gas.common.ui.util.TestUtil;
import javax.swing.JButton;
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
public class FlatBtnTest {
    
    public FlatBtnTest() {
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
     * Test of decorate method, of class FlatBtn.
     */
    @Test
    public void testDecorate() {
        System.out.println("decorate");       
        FlatBtn instance = new FlatBtn("hello");
        // TODO review the generated test code and remove the default call to fail.
        TestUtil.testUI(instance);
    }
}