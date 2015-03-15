/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jtree.editor;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;

/**
 *
 * @author dq
 */
public class DTreeCellEditor extends JPanel implements TreeCellEditor {

    DTreeCellRenderer _renderer;
    private JTree _tree;

    public DTreeCellEditor(DTreeCellRenderer rederer, JTree tree) {
        System.out.println("DTreeCellEditor.DTreeCellEditor()");
        _tree = tree;
        _renderer = rederer;
        field = new JTextField();
        checkBox = new JCheckBox();
        
        add(checkBox);
        add(field);
    }
    
    JTextField field;
    JCheckBox checkBox;

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
            boolean leaf, int row) {
        System.err.println("DTreeCellEditor.getTreeCellEditorComponent()");
        //tree.invalidate();
        //tree.validate();
        
        //Component c = _renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, leaf);
        //Component c = _renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
        //c.invalidate();
        //c.validate();
        setLayout(new GridLayout(2, 1));
        checkBox.setText("Check me");
        field.setText(value.toString());
        //setBorder(new EmptyBorder(5, 0, 5, 0));
        //setBackground(UIManager.getColor("Tree.background"));        
        return this;
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
// TODO Auto-generated method stub
        System.err.println("DTreeCellEditor.addCellEditorListener()");
    }

    @Override
    public void cancelCellEditing() {
// TODO Auto-generated method stub
        System.err.println("DTreeCellEditor.cancelCellEditing()");
    }

    @Override
    public Object getCellEditorValue() {
// TODO Auto-generated method stub
        System.err.println("DTreeCellEditor.getCellEditorValue()");
        return field.getText();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        System.err.println("DTreeCellEditor.isCellEditable()");
        return true;
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        System.err.println("DTreeCellEditor.removeCellEditorListener()");
// TODO Auto-generated method stub

    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
// TODO Auto-generated method stub
        System.err.println("DTreeCellEditor.shouldSelectCell()");
        _tree.validate();
        return true;
    }

    @Override
    public boolean stopCellEditing() {
// TODO Auto-generated method stub
        System.err.println("DTreeCellEditor.stopCellEditing()");
        return true;
    }
}
