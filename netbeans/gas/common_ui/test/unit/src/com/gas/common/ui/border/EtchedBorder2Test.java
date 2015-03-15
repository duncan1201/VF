/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.border;

import com.gas.common.ui.util.TestUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JPanel;
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
public class EtchedBorder2Test {
    
    public EtchedBorder2Test() {
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
     * Test of paintBorder method, of class EtchedBorder2.
     */
    @Test
    public void testPaintBorder() {
        System.out.println("paintBorder");
        JPanel test = new JPanel();
        
        TestUtil.testUI("Border test", null);
    }
}