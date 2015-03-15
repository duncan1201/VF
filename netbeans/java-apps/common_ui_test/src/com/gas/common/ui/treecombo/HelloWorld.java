/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.treecombo;

import javax.swing.*;
import java.awt.*;
import javax.swing.tree.DefaultTreeCellRenderer;

public class HelloWorld {

    public static void main(String args[]) {
        JFrame FF = new JFrame();
        FF.setLayout(new GridLayout(0, 2));

        JComboBox comboBox = JTreeComboFactory.createTreeCombo(new JTree().getModel());
        
        comboBox.setSelectedIndex(0);
        FF.getContentPane().add(comboBox, BorderLayout.SOUTH);

        FF.setBounds(100, 100, 300, 100);
        FF.setVisible(true);
        FF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
