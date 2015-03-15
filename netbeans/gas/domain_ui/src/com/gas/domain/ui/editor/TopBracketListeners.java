/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

class TopBracketListeners {
 
    static class PtyChangeListener implements PropertyChangeListener{

        private TopBracket topBracket;
        
        PtyChangeListener(TopBracket topBracket){
            this.topBracket = topBracket;
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            if(pName.equals("selected")){
                topBracket.repaint();
            }
        }
    }
}
