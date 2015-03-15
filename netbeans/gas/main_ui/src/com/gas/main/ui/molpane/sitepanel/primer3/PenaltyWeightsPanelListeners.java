/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class PenaltyWeightsPanelListeners {

    protected static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            PenaltyWeightsPanel src = (PenaltyWeightsPanel) evt.getSource();
            if (name.equals("userInput")) {
                src.populateUI();
            }
        }
    }
}
