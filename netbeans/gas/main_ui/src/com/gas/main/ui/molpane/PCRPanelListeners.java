/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
class PCRPanelListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PCRPanel src = (PCRPanel)evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(name.equals("selectedIndex")){
                src.leftComboRef.get().setSelectedIndex((Integer)v);
                src.rightComboRef.get().setSelectedIndex((Integer)v);
            }
        }
    }
    
    static class NameFieldListener implements DocumentListener {

        WeakReference<PCRPanel> pcrPanelRef;
        
        NameFieldListener(PCRPanel pcrPanel){
            pcrPanelRef = new WeakReference<PCRPanel>(pcrPanel);
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            validateInput(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateInput(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateInput(e);
        }
        
        private void validateInput(DocumentEvent e){            
            pcrPanelRef.get().validateInput();
        }
    }
}
