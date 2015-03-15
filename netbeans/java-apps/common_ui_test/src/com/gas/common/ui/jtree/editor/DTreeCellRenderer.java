/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jtree.editor;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreeCellRenderer;

public class DTreeCellRenderer extends JPanel implements TreeCellRenderer {

    JCheckBox check = new JCheckBox("Check me");
    JTextField textField = new JTextField();

    public DTreeCellRenderer() {
        setLayout(new GridLayout(2, 1));
        add(check);
        add(textField);
        //setBorder(new EmptyBorder(5, 0, 5, 0));
        //setBackground(UIManager.getColor("Tree.background"));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        textField.setText(tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus));
        return this;
    }
}
