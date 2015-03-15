/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.msa.ui.MSAEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 *
 * @author dq
 */
class NodePanelListeners {
    
    static class AttNameComboListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox)e.getSource();
            String item = (String)src.getSelectedItem();
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            treePane.setSelectedNodeAtt(item);
        }
    }
    
    static class ShapeComboListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox)e.getSource();
            String shapeStr = (String)src.getSelectedItem();
            MSAPref.getInstance().setNodeShape(shapeStr);
        }
    }
    
    static class VisibleNodeListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox)e.getSource();
            
            final NodePanel nodePanel = UIUtil.getParent(src, NodePanel.class);
            nodePanel.getShapeCombo().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            if(e.getStateChange() == ItemEvent.DESELECTED){                
                MSAPref.getInstance().setNodeShape("none");                
            }else{
                String shapeStr = (String)nodePanel.getShapeCombo().getSelectedItem();               
                MSAPref.getInstance().setNodeShape(shapeStr);
            }
        }
    }
    
    static class VisibleListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox)e.getSource();
            NodePanel nodePanel = UIUtil.getParent(src, NodePanel.class);
            nodePanel.getAttNamesCombo().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            treePane.setNodeLabelVisible(e.getStateChange() == ItemEvent.SELECTED);
            
            MSAEditor editor = UIUtil.getParent(src, MSAEditor.class);           
            editor.getMsa().getMsaSetting().setNodeLabelDisplay(e.getStateChange() == ItemEvent.SELECTED);
        }
    }
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            NodePanel nodePanel = (NodePanel)evt.getSource();
            if(pName.equals("nameAttributeNames")){
                String[] names = (String[])v;
                nodePanel.getAttNamesCombo().setModel(new DefaultComboBoxModel(names));
            }else if(pName.equals("selectedNameAttribute")){                
                nodePanel.getAttNamesCombo().setSelectedItem(v);
            }
        }    
    }
}
