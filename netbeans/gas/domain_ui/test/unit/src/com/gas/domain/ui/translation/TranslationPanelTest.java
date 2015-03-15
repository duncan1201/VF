/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.translation;

import com.gas.domain.ui.translation.TranslationPanel;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class TranslationPanelTest {

    public TranslationPanelTest() {
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
     * Test of cleanup method, of class TranslationPanel.
     */
    @Test
    public void testCleanup() {
        System.out.println("cleanup");
        TranslationPanel instance = new TranslationPanel();
        instance.cleanup();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getColorCombo method, of class TranslationPanel.
     */
    @Test
    public void testGetColorCombo() {
        System.out.println("getColorCombo");
        TranslationPanel instance = new TranslationPanel();

        //1. Create the frame.
        JFrame frame = new JFrame("WideComboBoxTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        frame.setSize(130, 300);
        frame.setVisible(true);
        while (true) {
        }
    }
}
