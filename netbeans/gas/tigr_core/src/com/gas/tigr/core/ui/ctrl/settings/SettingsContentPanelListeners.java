/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.settings;

import com.gas.domain.core.tigr.TIGRSettings;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class SettingsContentPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            SettingsContentPanel src = (SettingsContentPanel) evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("tigrSettings")) {
                src.overlapPanel.setTigrSettings((TIGRSettings) v);
                src.miscPanel.setSettings((TIGRSettings) v);
            }
        }
    }
}
