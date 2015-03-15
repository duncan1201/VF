/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.util.BioUtil;
import java.lang.ref.WeakReference;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**

 @author dq
 */
class NewLocPanelListeners {

    static class FromListener implements ChangeListener {

        private WeakReference<NewLocPanel> panelRef;

        FromListener(WeakReference<NewLocPanel> toSpinnerRef) {
            this.panelRef = toSpinnerRef;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            final JSpinner toSpinner = panelRef.get().getToSpinner();
            final JSpinner src = (JSpinner) e.getSource();
            final boolean circular = panelRef.get().isCircular();
            final int totalPos = panelRef.get().getTotalPos();
            Integer value = (Integer) src.getValue();
            if (!circular) {
                SpinnerNumberModel toModel = (SpinnerNumberModel) toSpinner.getModel();
                toModel.setMinimum(value);
            } else {
                if (value.equals(0) || value.equals(totalPos + 1)) {
                    int newValue = BioUtil.normalizeCircularPos(value, totalPos);
                    src.setValue(newValue);
                }
            }
            panelRef.get().validateInput();
        }
    }

    static class ToListener implements ChangeListener {

        private WeakReference<NewLocPanel> panelRef;

        ToListener(WeakReference<NewLocPanel> fromSpinnerRef) {
            this.panelRef = fromSpinnerRef;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            final JSpinner fromSpinner = panelRef.get().fromSpinner;
            final JSpinner src = (JSpinner) e.getSource();
            final boolean circular = panelRef.get().isCircular();
            final int totalPos = panelRef.get().getTotalPos();
            Integer value = (Integer) src.getValue();

            if (!circular) {
                SpinnerNumberModel fromModel = (SpinnerNumberModel) fromSpinner.getModel();
                fromModel.setMaximum((Comparable) value);
            } else {
                if (value.equals(0) || value.equals(totalPos + 1)) {
                    int newValue = BioUtil.normalizeCircularPos(value, totalPos);
                    src.setValue(newValue);
                }
            }
            panelRef.get().validateInput();
        }
    }
}
