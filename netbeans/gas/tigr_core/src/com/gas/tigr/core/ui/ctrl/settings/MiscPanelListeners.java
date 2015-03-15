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
public class MiscPanelListeners {
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            MiscPanel src = (MiscPanel)evt.getSource();
            if(name.equals("settings")){
                TIGRSettings settings = src.settings;
                src.considerLowScores.setSelected(settings.getLowScores());
                src.includeSingletons.setSelected(settings.getGenerateSingletons());
                src.keepBadSeqs.setSelected(settings.getIncludeBadSeq());
                src.trimmedSeq.setSelected(settings.getTrimmedSeq());
            }
        }
    }
}
