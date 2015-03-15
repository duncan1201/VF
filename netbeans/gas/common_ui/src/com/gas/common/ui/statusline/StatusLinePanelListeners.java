/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.statusline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class StatusLinePanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final StatusLinePanel src = (StatusLinePanel) evt.getSource();
            final String name = evt.getPropertyName();
            if (name.equals("caretPos") || name.equals("start")
                    || name.equals("end") || name.equals("selectLength")
                    || name.equals("caretSeqName") || name.equals("startSeqName")
                    || name.equals("endSeqName")) {
                if (src.start != null && src.end != null && src.selecLength != null) {
                    if (src.startSeqName != null && src.endSeqName != null) {
                        if (src.startSeqName.equals(src.endSeqName)) {
                            src.setText(String.format("%d bp - %d bp (%s %d bp)", src.start, src.end, src.startSeqName, src.selecLength));
                        } else {
                            src.setText(String.format("%d bp - %d bp (%s-%s %d bp)", src.start, src.end, src.startSeqName, src.endSeqName, src.selecLength));
                        }
                    } else {
                        src.setText(String.format("%d bp - %d bp (%d bp)", src.start, src.end, src.selecLength));
                    }

                } else if (src.start != null && src.end != null) {
                    src.setText(String.format("%d bp - %d bp", src.start, src.end));
                } else if (src.caretPos != null || src.caretSeqName != null) {
                    src.setText(String.format("%s%dbp", src.caretSeqName == null ? "" : src.caretSeqName + ":", src.caretPos));
                } else if (src.caretPos == null && src.start == null && src.end == null) {
                    src.setText("");
                }
            }
        }
    }
}
