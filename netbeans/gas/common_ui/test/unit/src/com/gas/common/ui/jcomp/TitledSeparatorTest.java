/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.BorderLayout;
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
public class TitledSeparatorTest {
    
    public TitledSeparatorTest() {
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
     * Test of setTitle method, of class TitledSeparator.
     */
    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        String title = "TITLE";
        TitledSeparator instance = new TitledSeparator(title);
        instance.setTitle(title);
        // TODO review the generated test code and remove the default call to fail.
        //1. Create the frame.
        JFrame frame = new JFrame("TitledSeparator");

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        frame.setSize(200, 140);
        frame.setVisible(true);
        while (true) {
        }
    }
}