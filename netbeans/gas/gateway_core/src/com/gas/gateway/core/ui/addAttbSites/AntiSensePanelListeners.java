/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.PrimerAdapter;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author dq
 */
class AntiSensePanelListeners {

    static class FuseListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComponent src = (JComponent)e.getSource();
            boolean selected = e.getStateChange() == ItemEvent.SELECTED;
            AntiSensePanel antiSensePanel = UIUtil.getParent(src, AntiSensePanel.class);
            antiSensePanel.spacerField.setEnabled(selected);
            
            AddAttbSitesPanel addAttbSitesPanel = UIUtil.getParent(antiSensePanel, AddAttbSitesPanel.class);
            PrimerAdapter adapter = addAttbSitesPanel.getRightPrimerAdapter();
            if (selected) {            
                adapter.setPrefix(addAttbSitesPanel.getAntisensePanel().spacerField.getText());
            }
            addAttbSitesPanel.updatePreview();            
        }
    }
    
    static class SpacerListener implements DocumentListener{

        JTextField textfield;
        
        SpacerListener(JTextField textfield) {
            this.textfield = textfield;
        }       
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            process(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {      
            process(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            process(e);
        }
        
        private void process(DocumentEvent e){                       
            AddAttbSitesPanel addAttSitesPanel = UIUtil.getParent(textfield, AddAttbSitesPanel.class);            
            final String text = textfield.getText();
            PrimerAdapter primerAdapter = addAttSitesPanel.getRightPrimerAdapter();
            StringList forbidden = new StringList(primerAdapter.getForbidden());
            if(BioUtil.areNonambiguousDNAs(text)  && !forbidden.contains(text)){                
                primerAdapter.setPrefix(text.toUpperCase(Locale.ENGLISH));
                addAttSitesPanel.updatePreview();                               
            }else{
                final String prefix = primerAdapter.getPrefix();
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        textfield.setText(prefix);
                    }
                });
            }            
        }
    }
    
    static class PtyChangeLisener implements PropertyChangeListener{

        private AntiSensePanel panel;
        
        public PtyChangeLisener(AntiSensePanel ref){
            this.panel = ref;
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final String pName = evt.getPropertyName();
            Object newValue = evt.getNewValue();
            if(pName.equals("stopCodonIncluded")){                
                panel.stopCodonBtn.setSelected((Boolean)newValue);
            }
        }
    }
    
    static class StopCodonListener implements ItemListener {

        private WeakReference<AddAttbSitesPanel> parentRef ;
        private AntiSensePanel panel;
        
        public StopCodonListener(AntiSensePanel ref){
            this.panel = ref;
        }
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            panel.stopCodonIncluded = e.getStateChange() == ItemEvent.SELECTED;
            
            parentRef = getParentRef(e);
            if(parentRef == null)
                return;

            parentRef.get().updatePreview();
        }
        
        private WeakReference<AddAttbSitesPanel> getParentRef(ItemEvent e){
            if(parentRef == null){
                Object src = e.getSource();
                AddAttbSitesPanel panel = UIUtil.getParent((Component)src, AddAttbSitesPanel.class);
                if(panel != null){
                    parentRef = new WeakReference<AddAttbSitesPanel>(panel);
                }
            }
            return parentRef;
        }
    
    }
}
