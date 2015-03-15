/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui;

import com.gas.msa.ui.alignment.AlignPaneTest;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import org.junit.*;

/**
 *
 * @author dq
 */
public class MSAPaneTest {

    public MSAPaneTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        AlignPaneTest.setUpClass();
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
     * Test of setMsa method, of class MSAPane.
     */
    @Test
    public void testSetMsa() {
        System.out.println("setMsa");
        MSAPane instance = new MSAPane();

        instance.setMsa(AlignPaneTest.msaDNA);

//1. Create the frame.
        JFrame frame = new JFrame("MSAPaneTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width - 250, size.height - 300);
        frame.setVisible(true);
        while (true) {
        }
    }
}
