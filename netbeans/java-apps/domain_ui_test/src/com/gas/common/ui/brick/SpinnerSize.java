/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.brick;

import java.awt.*;  
import javax.swing.*;  
   
public class SpinnerSize  
{  
    public SpinnerSize()  
    {  
        String[] suits = { "spades", "diamonds", "clubs", "hearts" };  
        SpinnerListModel suitModel = new SpinnerListModel(suits);  
        JSpinner suitSpinner = new JSpinner(suitModel);  
   
        // set the preferred size  
        Dimension d = suitSpinner.getPreferredSize();  
        d.width = 100;  
        suitSpinner.setPreferredSize(d);  
   
        // modify the JTextField appearance  
        JSpinner.ListEditor editor = new JSpinner.ListEditor(suitSpinner);  
        JTextField tf = editor.getTextField();  
        //tf.setHorizontalAlignment(JTextField.CENTER);  
        //tf.setFont(new Font("lucida sans regular", Font.PLAIN, 16));  
        //suitSpinner.setEditor(editor);  
   
        JPanel panel = new JPanel();  
        panel.add(suitSpinner);  
        JFrame f = new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.getContentPane().add(panel);  
        f.setSize(300,100);  
        f.setLocation(200,200);  
        f.setVisible(true);  
    }  
   
    public static void main(String[] args)  
    {  
        new SpinnerSize();  
    }  
}  