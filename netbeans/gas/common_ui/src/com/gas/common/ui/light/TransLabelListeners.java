/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class TransLabelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            TransLabel src = (TransLabel) e.getSource();
            String pName = e.getPropertyName();
            if (pName.equals("anchor")
                    || pName.equals("theta")
                    || pName.equals("x")
                    || pName.equals("y")
                    || pName.equals("width")
                    || pName.equals("height")) {
                src.calculateBounds();
            }
        }
    }
}
