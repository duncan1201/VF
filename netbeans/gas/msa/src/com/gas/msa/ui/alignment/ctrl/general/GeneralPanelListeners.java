/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.general;

import com.gas.domain.core.msa.MSA;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class GeneralPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object v = evt.getNewValue();
            String pName = evt.getPropertyName();
            GeneralPanel src = (GeneralPanel) evt.getSource();
            if (pName.equals("msa")) {
                MSA msa = (MSA) v;
                src.initComponentsByType(msa.isDnaByGuess());

                src.getConsensusPanel().populateUI(msa);

                src.getPropertiesPanel().populateUI(msa);
            }
        }
    }
}
