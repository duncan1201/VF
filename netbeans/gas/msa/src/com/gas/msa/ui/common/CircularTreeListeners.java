/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.common.ui.light.Ellipse2DX;
import com.gas.common.ui.util.UIUtil;
import com.gas.msa.ui.tree.TreePane;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author dq
 */
class CircularTreeListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            CircularTree src = (CircularTree) evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("rotation")) {
                src.repaint();
            } else if (pName.equals("radialNodeLabel")
                    || pName.equals("radialEdgeLabel")
                    || pName.equals("lineWidth")
                    || pName.equals("nodeShape")
                    || pName.equals("nodeLabelVisible")
                    || pName.equals("transform")
                    || pName.equals("selected")
                    || pName.equals("sigDigits")
                    || pName.equals("fontSize")                    
                    || pName.equals("selectedLengthAttribute")
                    || pName.equals("selectedNameAttribute")
                    || pName.equals("edgeLabelVisible")) {
                src.repaint();
            } else if (pName.equals("root")) {
                TreeNode.Node root = (TreeNode.Node) v;
                String[] lengthAttNames = root.getFirstLeaf().getTreeNodeLength().getAttributeNames();
                src.setLengthAttributeNames(lengthAttNames);
                src.repaint();
            }
        }
    }

    static class Aptr extends MouseInputAdapter {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            CircularTree tree = (CircularTree) e.getSource();
            int r = e.getWheelRotation() * 3;
            float change = (float) ((r) * Math.PI * 2 / 360.0f);
            tree.setRotation(tree.getRotation() + change);
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            boolean leftMouseBtn = SwingUtilities.isLeftMouseButton(e);
            
            if(leftMouseBtn){
                handleLeftMouseBtn(e);
            }
        }
        
        private void handleLeftMouseBtn(MouseEvent e){
            CircularTree tree = (CircularTree) e.getSource();
            Point pt = e.getPoint();
            TreePane treePane = UIUtil.getParent(tree, TreePane.class);
            Ellipse2DX.Float selected = tree.getSelectableNodes().getElementAt(pt.x, pt.y);            
            if(selected != null){
                TreeNode.Node selectedTreeNode = (TreeNode.Node)selected.getData();
                treePane.setSelected(selectedTreeNode);
                //rectTree.getRoot().setSelected(false, true);
                //selectedTreeNode.setSelected(true, true);
                //rectTree.repaint();
            }else{
                treePane.setSelected(null);
                //rectTree.getRoot().setSelected(false, true);
                //rectTree.repaint();
            }            
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            CircularTree tree = (CircularTree) e.getSource();
            Point pt = e.getPoint();
            Ellipse2DX.Float selected = tree.getSelectableNodes().getElementAt(pt.x, pt.y);
            if(selected != null){                
                tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }else{
                tree.setCursor(Cursor.getDefaultCursor());
            }
        }        
    }
}
