/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author dq
 */
class CornerUIListeners {
    
    static class MouseAdpt extends MouseAdapter{
        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger()){
                triggerPopup(e);
            }
        }
        
        private void triggerPopup(MouseEvent e){
        
        }
    }
}
