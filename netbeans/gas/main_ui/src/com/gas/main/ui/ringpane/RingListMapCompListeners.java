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
class RingListMapCompListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RingListMapComp src = (RingListMapComp)evt.getSource();
            final String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(pName.equals("ringMap")){
                final Float startOffset = src.getStartOffset();
                if(startOffset != null){
                    src.getRingMap().setStartOffset(startOffset);
                }
                src.repaint();
            }
        }
    
    }
}
