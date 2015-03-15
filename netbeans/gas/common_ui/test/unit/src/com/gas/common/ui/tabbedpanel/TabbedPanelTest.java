/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class TabbedPanelTest {

    public TabbedPanelTest() {
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
     * Test of setSelectedTab method, of class TabbedPanel.
     */
    @Test
    public void testSetSelectedTab() {
        JFrame frame = new JFrame("OutlookTabbedPaneTest");

        TabbedPanel tabbedPanel = new TabbedPanel(SwingConstants.BOTTOM);

        tabbedPanel.addTab("Test", null, new JButton("Test1"));
        tabbedPanel.addTab("Test2", null, new JButton("Test2"));
        tabbedPanel.insertTab("Test3", null, new JButton("Test3"), 0);
        GridBagConstraints c = new GridBagConstraints();
        tabbedPanel.setDecorator(new JButton("AAAA"), c);
        frame.getContentPane().add(tabbedPanel);

        frame.setSize(300, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width / 2 - 400, d.height / 2 - 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
        }
    }
}
