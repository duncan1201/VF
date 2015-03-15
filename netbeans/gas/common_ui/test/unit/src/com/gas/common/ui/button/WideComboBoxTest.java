/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.button;

import com.gas.common.ui.util.TestUtil;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class WideComboBoxTest {

    public WideComboBoxTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
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
     * Test of getSize method, of class WideComboBox.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        Object[] items = {"short", "1234567", "12345678766511111111111111111"};
        WideComboBox instance = new WideComboBox(items);

        TestUtil.testUI(instance);
    }
}
