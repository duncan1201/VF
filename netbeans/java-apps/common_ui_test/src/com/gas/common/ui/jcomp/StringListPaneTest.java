/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author dunqiang
 */
public class StringListPaneTest {
    public static void main(String[] args){
        JFrame frame = new JFrame("SringListPanel");
        
        StringListPanel panel = new StringListPanel();
        for(Integer i = 0; i < 15; i++){
            panel.addString(i.toString());
        }
        
        frame.setContentPane(panel);
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        frame.setVisible(true);
    }
}
