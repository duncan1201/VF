package com.gas.common.ui.misc;

import com.gas.common.ui.button.DropDownButton;
import java.awt.BorderLayout;
import javax.swing.*;


public class DropDownButtonTest  {


    
    public static void main(String[] args){
        JFrame frame = new JFrame("Test");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JButton mainButton = new JButton("Main");
        DropDownButton b = new DropDownButton(mainButton);
        JMenuItem jMenuItem = new JMenuItem("Menu Item 1");
        b.getMenu().add(jMenuItem);
        b.setEnabled(true);


        panel.add(b, BorderLayout.CENTER);

        
        frame.setContentPane(panel);
        
        frame.pack();

        frame.setVisible(true);     
    }
}