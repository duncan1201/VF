/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class DelConfirmPanelListeners {
    static class BoxListener implements ItemListener{

        private WeakReference<DelConfirmPanel> ref;
        
        BoxListener(DelConfirmPanel delConfirmPanel){
            this.ref = new WeakReference<DelConfirmPanel>(delConfirmPanel);
        }
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            this.ref.get().validateInput();
        }
    }
}
