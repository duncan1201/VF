/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
public class RowHeaderUIListeners {
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RowHeaderUI src = (RowHeaderUI)evt.getSource();
            String name = evt.getPropertyName();
            if(name.equals("max")){
                src.repaint();
            }
        }
    }
}
