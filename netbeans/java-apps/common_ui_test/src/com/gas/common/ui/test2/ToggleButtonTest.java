/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.test2;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

/**
 *
 * @author dunqiang
 */
public class ToggleButtonTest {
    public static void main(String[] args){
        JFrame frame = new JFrame("Toggle button test");
        
        final JPanel panel = new JPanel();        
        
        //panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
        
        panel.setLayout(flowLayout);
        
        final JToggleButton[] btns = new JToggleButton[15];
        for(int i = 0; i < btns.length; i++){
            btns[i] = new JToggleButton("H " + i);
            panel.add(btns[i]);
        }
        
        btns[0].addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < btns.length; i++){
                    Dimension size = btns[i].getSize();
                    System.out.println(size);
                    //btns[i].setSize(59, 26);                    
                    btns[i].setPreferredSize(new Dimension(59, 26));
                }   
                panel.revalidate();
            }
        });

        frame.setContentPane(panel);
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        frame.setVisible(true);
    }
}
