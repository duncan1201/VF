/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.dialog;

import javax.swing.JFrame;
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
public class AutoCloseDialogTest {
    
    public AutoCloseDialogTest() {
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
     * Test of main method, of class AutoCloseDialog.
     */
    @Test
    public void testMain() {
JFrame frame = new JFrame();
        AutoCloseDialog d = new AutoCloseDialog(null, "aaaaaaaaaaaaaaaaaaatest");
        
        d.pack();
        d.setVisible(true);
        while(true){
        }    }
}