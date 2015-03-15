/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JFrame;

/**
 *
 * @author dq
 */
public class TestUtil {
    public static void testUI(Component instance){
        testUI("", instance);
    }
    
    public static void testUI(String title, Component instance){
        UIUtil.setNimbusLookAndFeel();
    //1. Create the frame.
        JFrame frame = new JFrame(title);

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(instance);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        frame.setSize(200, 140);
        frame.setVisible(true);
        while (true) {
        }
    }
}
