/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.digest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author dq
 */
class DigestInputPanelListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        private DigestInputPanel digestPanel;

        PtyChangeListener(DigestInputPanel digestPanel) {
            this.digestPanel = digestPanel;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            if (pName.equals("useAllAtOnce")) {
                digestPanel.useAllAtOnceBtn.setSelected(digestPanel.useAllAtOnce);
            } else if (pName.equals("useSeparatly")) {
                digestPanel.useSeparatelyBtn.setSelected(digestPanel.useSeparatly);
            } else if(pName.equals("useAllSites")){
                digestPanel.useAllSitesBtn.setSelected(digestPanel.useAllSites);
                digestPanel.namesCombo.setEnabled(!digestPanel.useAllSites);
            } else if(pName.equals("useSelectedSite")){
                digestPanel.useSelectedSiteBtn.setSelected(digestPanel.useSelectedSite);
            } else if(pName.equals("selectedEnzyme")){
                String sEnzyme = digestPanel.getSelectedEnzyme();
                digestPanel.namesCombo.setSelectedItem(sEnzyme);                
            } else if(pName.equals("names")){
                String[] names = digestPanel.getNames();
                DefaultComboBoxModel model = new DefaultComboBoxModel(names);
                digestPanel.namesCombo.setModel(model);
            }
        }
    }
    
    static class ComboListener implements ActionListener{

        private DigestInputPanel digestPanel;
        
        ComboListener(DigestInputPanel digestPanel){
            this.digestPanel = digestPanel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String item = (String)digestPanel.namesCombo.getSelectedItem();
            digestPanel.selectedEnzyme = item;
        }
    
    }
    
    static class BtnListener implements ActionListener {

        private DigestInputPanel digestPanel;
        
        BtnListener(DigestInputPanel digestPanel){
            this.digestPanel = digestPanel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            digestPanel.useSelectedSite = digestPanel.useSelectedSiteBtn.isSelected();
            digestPanel.useAllAtOnce = digestPanel.useAllAtOnceBtn.isSelected();
            digestPanel.useSeparatly = digestPanel.useSeparatelyBtn.isSelected();
            digestPanel.useAllSites = digestPanel.useAllSitesBtn.isSelected();
            
            digestPanel.namesCombo.setEnabled(digestPanel.useSelectedSite);
        }
    }
}
