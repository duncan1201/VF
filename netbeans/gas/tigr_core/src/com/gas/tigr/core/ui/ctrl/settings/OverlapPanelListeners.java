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
public class OverlapPanelListeners {
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            OverlapPanel src = (OverlapPanel)evt.getSource();
            String name = evt.getPropertyName();
            if(name.equals("tigrSettings")){          
                TIGRSettings settings = src.tigrSettings;
                src.maxErr32Spinner.setValue(settings.getMaxError32());
                src.maximumEndSpinner.setValue(settings.getMaximumEnd());
                src.minimumLengthSpinner.setValue(settings.getMinimumLength());
                src.minimumPercentSpinner.setValue(settings.getMinPercent());
            }
        }
    }
}
