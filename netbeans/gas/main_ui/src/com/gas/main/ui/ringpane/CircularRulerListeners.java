/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class CircularRulerListeners {
    public static class PtyChangeListener implements PropertyChangeListener{
        
        public PtyChangeListener( ){
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            CircularRuler src = (CircularRuler)evt.getSource();
            String pName = evt.getPropertyName();
            if(pName.equals("offset") || pName.equals("range")){
                src.repaint();
            }else if(pName.equals("selection")){
                src.repaint();
            }
        }
    }
}
