/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author dq
 */
class RequestTrialPanelListeners {
    static class EmailListener implements DocumentListener{

        private RequestTrialPanel p;
        
        EmailListener(RequestTrialPanel p){
            this.p = p;
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            p.validateInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            p.validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            p.validateInput();
        }
    }
}
