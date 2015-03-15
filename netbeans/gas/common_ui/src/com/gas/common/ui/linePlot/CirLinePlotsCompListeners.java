/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.linePlot;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author dq
 */
class CirLinePlotsCompListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            CirLinePlotsComp src = (CirLinePlotsComp) evt.getSource();
            final String name = evt.getPropertyName();
            if (name.equals("startOffset")) {
                Map<String, CirLinePlot> plots = src.getPlots();
                Iterator<String> itr = plots.keySet().iterator();
                while (itr.hasNext()) {
                    String key = itr.next();
                    plots.get(key).setStartOffset(src.getStartOffset());
                }
                src.repaint();
            }
        }
    }
}
