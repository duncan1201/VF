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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author dq
 */
class RectTreeListeners {

    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RectTree src = (RectTree)evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(pName.equals("edgeLabelVisible") 
                    || pName.equals("nodeShape") 
                    || pName.equals("lineWidth")
                    || pName.equals("sigDigits")
                    || pName.equals("fontSize")
                    || pName.equals("nodeShape")
                    || pName.equals("selected")
                    || pName.equals("nodeLabelVisible")       
                    || pName.equals("selectedNameAttribute")       
                    || pName.equals("selectedLengthAttribute")                           
                    || pName.equals("transform")){
                src.repaint();                
            }else if(pName.equals("root")){
                TreeNode.Node rootNode = (TreeNode.Node)v;
                String[] lengthAttributeNames = rootNode.getFirstLeaf().getTreeNodeLength().getAttributeNames();
                src.setLengthAttributeNames(lengthAttributeNames);
                src.repaint();
            }
        }
    
    }
    
    static class Adpt extends MouseInputAdapter {

        public Adpt() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            boolean leftMouseBtn = SwingUtilities.isLeftMouseButton(e);
            
            if(leftMouseBtn){
                handleLeftMouseBtn(e);
            }
        }
        
        private void handleLeftMouseBtn(MouseEvent e){
            RectTree rectTree = (RectTree) e.getSource();
            Point pt = e.getPoint();
            TreePane treePane = UIUtil.getParent(rectTree, TreePane.class);
            Ellipse2DX.Float selected = rectTree.getSelectableNodes().getElementAt(pt.x, pt.y);            
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
            RectTree rectTree = (RectTree) e.getSource();
            Point pt = e.getPoint();
            Ellipse2DX.Float selected = rectTree.getSelectableNodes().getElementAt(pt.x, pt.y);
            if(selected != null){                
                rectTree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }else{
                rectTree.setCursor(Cursor.getDefaultCursor());
            }
        }
    }
}
