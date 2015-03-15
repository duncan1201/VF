/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OutPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class OutPanelListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final OutPanel src = (OutPanel) evt.getSource();
            final String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equalsIgnoreCase("p3output")) {
                src.populateUI();
            }
        }
    }
}
