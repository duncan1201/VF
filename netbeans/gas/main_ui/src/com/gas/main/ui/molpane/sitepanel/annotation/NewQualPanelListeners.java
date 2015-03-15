/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.core.StringComparator;
import com.gas.domain.core.as.FetureKey;
import com.gas.domain.core.as.api.IFetureKeyService;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class NewQualPanelListeners {

    static class FeatureTypeOnlyListener implements ItemListener{

        NewQualPanel panel;
        private IFetureKeyService service = Lookup.getDefault().lookup(IFetureKeyService.class);
        
        FeatureTypeOnlyListener(NewQualPanel panel){
            this.panel = panel;
        }       

        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            boolean selected = ItemEvent.SELECTED == state;
            if(selected){                
                FetureKey fetureKey = service.getFullByName(this.panel.fk);
                Set<String> items = fetureKey.getQualifiers();
                List<String> tmp = new ArrayList<String>(items);
                Collections.sort(tmp, new StringComparator());
                panel.qualifierComboBox.setModel(new DefaultComboBoxModel(tmp.toArray(new String[tmp.size()])));
            }else{
                List<String> all = service.getAllQualifiers();
                Collections.sort(all, new StringComparator());
                panel.qualifierComboBox.setModel(new DefaultComboBoxModel(all.toArray(new String[all.size()])));
            }
        }
    }
    
    static class ValueListener implements DocumentListener {

        private NewQualPanel panel;

        ValueListener(NewQualPanel panelRef) {
            this.panel = panelRef;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            processEvent(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            processEvent(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            processEvent(e);            
        }

        private void processEvent(DocumentEvent e) {
            Document doc = e.getDocument();
            try {
                String text = doc.getText(0, doc.getLength());
                panel.setValue(text);
                panel.validateInput();
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }            
        }
    }
}
