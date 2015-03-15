/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.treecombo;

import javax.swing.JComboBox;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 *
 * @author dq
 */
public class JTreeComboFactory {

    public static JComboBox createTreeCombo(TreeModel treeModel) {
        JComboBox comboBox = new JComboBox(new TreeListModel(treeModel));

        comboBox.setRenderer(new TreeListCellRenderer(treeModel, new DefaultTreeCellRenderer()));
        comboBox.setOpaque(true);
        return comboBox;
    }
}
