/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.progress;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class ProgressHelperTest {

    public ProgressHelperTest() {
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
     * Test of showProgressDialogAndRun method, of class ProgressHelper.
     */
    @Test
    public void testShowProgressDialogAndRun() {
        System.out.println("showProgressDialogAndRun");
        //1. Create the frame.
        final JFrame frame = new JFrame("WideComboBoxTest");

        JButton instance = new JButton("Start");

        final ProgRunnable xxx = new ProgRunnable() {
            Integer i;

            @Override
            public void run(ProgressHandle handle) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void done(ProgressHandle handle) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        instance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProgressHelper.showProgressDialogAndRun(frame, xxx, "What!!!");
            }
        });



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
