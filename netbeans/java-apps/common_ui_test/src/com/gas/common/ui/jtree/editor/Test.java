/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jtree.editor;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class Test {

    public Test() {
        JFrame frame = new JFrame("Test Cell Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTree tree = new JTree();
        frame.getContentPane().add(tree);
        tree.setCellRenderer(new DTreeCellRenderer());
// tree.setCellEditor(new DefaultTreeCellEditor(tree, (DefaultTreeCellRenderer)tree.getCellRenderer()));
        tree.setCellEditor(new DTreeCellEditor((DTreeCellRenderer) tree.getCellRenderer(), tree));
        tree.setEditable(true);
        tree.setInvokesStopCellEditing(true);
        frame.pack();
        frame.setSize(new Dimension(400, 500));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new Test();
            }
        });
    }
}