/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OutTreeRenderer.OligoPanel;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

/**
 *
 * @author dq
 */
public class OutTreeEditor extends AbstractCellEditor implements TreeCellEditor{

    OutTreeRenderer renderer;
    OutTree tree;
    
    OutTreeEditor(OutTree tree){
        this.tree = tree;
        renderer = new OutTreeRenderer();
    }
    
    /**
     * Returns true.
     * @param e  an event object
     * @return true
     */
    @Override
    public boolean isCellEditable(EventObject e) { 
        OutTree src = (OutTree)e.getSource();
        TreePath path = src.getSelectionPath();
        if(path == null){
            return false;
        }
        OligoTreeNode node = (OligoTreeNode)path.getLastPathComponent();
        if(node.isOligo() || node.isSeq()){
            return true;
        }else{
            return false; 
        }
    }     
    
    @Override
    public Object getCellEditorValue() {
        TreePath selectionPath = tree.getSelectionPath();
        if(selectionPath == null){
            return null;
        }
        OligoTreeNode node = (OligoTreeNode)selectionPath.getLastPathComponent();
        return node.getUserObject();
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        Component ret = renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, leaf);
        return ret;
    }
    
}
