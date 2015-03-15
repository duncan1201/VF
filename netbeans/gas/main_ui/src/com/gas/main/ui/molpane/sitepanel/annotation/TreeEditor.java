/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.domain.core.as.Qualifier;
import com.gas.domain.core.as.AsPref;
import java.awt.Component;
import java.awt.event.*;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

/**
 *
 * @author dq
 */
public class TreeEditor extends AbstractCellEditor implements TreeCellEditor {

    private JTree tree;
    private TreeRenderer render = new TreeRenderer();

    public TreeEditor(JTree tree) {
        this.tree = tree;
    }

    @Override
    public Object getCellEditorValue() {
        Object ret = null;
        TreePath treePath = tree.getSelectionPath();
        if(treePath != null){
            FeatureTreeNode node = (FeatureTreeNode)treePath.getLastPathComponent();
            ret = node.getUserObject();
        }
        return ret;
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        boolean ret = false;
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(),
                    mouseEvent.getY());
            if (path != null) {
                Object node = path.getLastPathComponent();
                if ((node != null) && (node instanceof FeatureTreeNode)) {
                    FeatureTreeNode treeNode = (FeatureTreeNode) node;
                    Object userObject = treeNode.getUserObject();
                    if (userObject instanceof List) {
                        ret = true;
                    } else if (userObject instanceof Qualifier) {
                        Qualifier q = (Qualifier) userObject;                      
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        Component editor = render.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, leaf);       
        return editor;
    }
}
