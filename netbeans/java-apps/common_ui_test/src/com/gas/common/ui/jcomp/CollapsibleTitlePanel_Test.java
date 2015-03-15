/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author dunqiang
 */
public class CollapsibleTitlePanel_Test {
    public static void main(String[] args){
        JFrame frame = new JFrame("CollapsibleTitlePanel_Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        
        
        CollapsibleTitlePanel panel = new CollapsibleTitlePanel();
        panel.setTitle("Title");
        panel.getContentPane().add(new JLabel("BorderLayout.CENTER"), BorderLayout.CENTER);
        
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
