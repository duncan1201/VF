/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.msa.ui.common.ITree;
import com.gas.msa.ui.common.TreeNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class TreePaneListeners {

    static class PrefPtyListener implements PropertyChangeListener {

        private TreePane treePane;

        PrefPtyListener(TreePane treePane) {
            this.treePane = treePane;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("treeNodeLabelDisplay")) {
                Boolean visible = (Boolean) v;
                treePane.setNodeLabelVisible(visible);
            } else if(pName.equals("lineWidth")){
                Integer width = (Integer)v;
                treePane.setLineWidth(width);
            } else if(pName.equals("sigDigits")){
                Integer sigDigits = (Integer)v;
                treePane.setSigDigits(sigDigits);
            } else if(pName.equals("fontSize")){
                Float fontSize = (Float)v;
                treePane.setFontSize(fontSize);
            } else if(pName.equals("nodeShape")){
                String shape = (String)v;
                ITree.SHAPE nodeShape = ITree.SHAPE.get(shape);
                treePane.setNodeShape(nodeShape);
            }
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            TreePane src = (TreePane) evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(pName.equals("selectedNodeAtt")){
                String name = (String)v;
                
                src.getRectTree().setSelectedNameAttribute(name);
                src.getRadialTree().setSelectedNameAttribute(name);
                src.getCircularTree().setSelectedNameAttribute(name);
            } else if(pName.equals("selectedEdgeAtt")){
                String name = (String)v;
                
                src.getRectTree().setSelectedLengthAttribute(name);
                src.getRadialTree().setSelectedLengthAttribute(name);
                src.getCircularTree().setSelectedLengthAttribute(name);
            } else if(pName.equals("sigDigits")){
                Integer sigDigits = (Integer)v;
                
                src.getRectTree().setSigDigits(sigDigits);
                src.getRadialTree().setSigDigits(sigDigits);
                src.getCircularTree().setSigDigits(sigDigits);
            } else if(pName.equals("fontSize")){
                Float fontSize = (Float)v;
                
                src.getRectTree().setFontSize(fontSize);
                src.getRadialTree().setFontSize(fontSize);
                src.getCircularTree().setFontSize(fontSize);                
            } else if(pName.equals("nodeShape")){
                ITree.SHAPE nodeShape = (ITree.SHAPE)v;
                
                src.getRectTree().setNodeShape(nodeShape);
                src.getRadialTree().setNodeShape(nodeShape);
                src.getCircularTree().setNodeShape(nodeShape);
            } else if(pName.equals("selected")){
                TreeNode.Node selected = (TreeNode.Node)v;
                
                src.getRectTree().setSelected(selected);
                src.getRadialTree().setSelected(selected);
                src.getCircularTree().setSelected(selected);
            }
        }
    }
}
