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
class ConnectorListeners {
    static class PtyChangeListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Connector conn = (Connector)evt.getSource();
            final String pName = evt.getPropertyName();
            if(pName.equals("selected")){
                conn.repaint();
            }
        }
    
    }
}
