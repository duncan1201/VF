/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.treecombo;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 *
 * @author dq
 */
public class TreeListCellRenderer extends JLabel implements ListCellRenderer {

    private static final JTree tree = new JTree();
    TreeModel treeModel;
    TreeCellRenderer treeRenderer;
    IndentBorder indentBorder = new IndentBorder();

    public TreeListCellRenderer(TreeModel treeModel, TreeCellRenderer treeRenderer) {
        this.treeModel = treeModel;
        this.treeRenderer = treeRenderer;
        setBorder(indentBorder);
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) { //if selected value is null           
            return this;
        }

        boolean leaf = treeModel.isLeaf(value);
        JLabel comp = (JLabel) treeRenderer.getTreeCellRendererComponent(tree, value, isSelected, true, leaf, index, cellHasFocus);

        if (isSelected) {
            setBackground(UIManager.getColor("Tree.selectionBackground"));
            setForeground(UIManager.getColor("Tree.selectionForeground"));
        } else {
            setBackground(UIManager.getColor("Tree.background"));
            setForeground(UIManager.getColor("Tree.foreground"));
        }

        setText(comp.getText());
        setIcon(ImageHelper.createImageIcon(ImageNames.FOLDER_16));

        // compute the depth of value 
        PreorderEnumeration enumer = new PreorderEnumeration(treeModel);
        for (int i = 0; i <= index; i++) {
            enumer.nextElement();
        }
        indentBorder.setDepth(enumer.getDepth());

        return this;
    }
}