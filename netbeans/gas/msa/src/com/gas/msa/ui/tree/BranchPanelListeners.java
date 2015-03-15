/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.msa.ui.MSAEditor;
import com.gas.msa.ui.alignment.widget.MSAScroll;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class BranchPanelListeners {
    
    static class LineWidthListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner)e.getSource();
            Integer lineWidth = (Integer)src.getValue();
            
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            treePane.setLineWidth(lineWidth);
            
            MSAEditor editor = UIUtil.getParent(treePane, MSAEditor.class);
            editor.getMsa().getMsaSetting().setLineWidth(lineWidth);
        }
    }    
    
    static class SigDigitListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner)e.getSource();
            Integer sigDigits = (Integer)src.getValue();
            
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            treePane.setSigDigits(sigDigits);
            
            MSAEditor editor = UIUtil.getParent(treePane, MSAEditor.class);
            editor.getMsa().getMsaSetting().setSigDigits(sigDigits);           
        }
    }
    
    static class AttNameComboListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox)e.getSource();
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            String selectedAtt = (String)src.getSelectedItem();
            treePane.setSelectedEdgeAtt(selectedAtt);
        }
    }
    
    static class VisibleListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox)e.getSource();
            BranchPanel branchPanel = UIUtil.getParent(src, BranchPanel.class);
            boolean enabled = e.getStateChange() == ItemEvent.SELECTED;
            
            branchPanel.getAttNamesCombo().setEnabled(enabled);
            branchPanel.getLineWidthSpinner().setEnabled(enabled);
            branchPanel.getSigDigitSpinner().setEnabled(enabled);
            
            TreePane treePane = UIUtil.getParent(branchPanel, TreePane.class);            
            treePane.setEdgeLabelVisible(enabled);
            MSAEditor editor = UIUtil.getParent(treePane, MSAEditor.class);
            editor.getMsa().getMsaSetting().setTreeEdgeLabelDisplay(enabled);
        }
    }
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            BranchPanel src = (BranchPanel)evt.getSource();
            
            if(pName.equals("attNames")){
                String[] attNames = src.getAttNames();
                src.getAttNamesCombo().setModel(new DefaultComboBoxModel(attNames));
            }else if(pName.equals("selectedAttName")){
                String selected = src.getSelectedAttName();
                src.getAttNamesCombo().setSelectedItem(selected);
            }
        }
    }
}
