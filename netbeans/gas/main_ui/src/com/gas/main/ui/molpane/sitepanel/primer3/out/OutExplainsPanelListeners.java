/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OutExplainsPanel;
import com.gas.common.ui.util.StrUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 *
 * @author dq
 */
class OutExplainsPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        final String regexConsider = "considered [0-9]+";
        final String regexOk = "ok [0-9]+";

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            OutExplainsPanel src = (OutExplainsPanel) evt.getSource();
            String name = evt.getPropertyName();
            if(name.equals("p3output")){                
                src.refresh();
            }
        }
    }
}
