/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class LinearRulerLabelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            LinearRulerLabel src = (LinearRulerLabel) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("x") || name.equals("y")
                    || name.equals("pos") || name.equals("font")
                    || name.equals("tickHeight")) {
                Rectangle b = src.createBounds();
                if (b != null) {
                    src.setBounds(b);
                }
            }
        }
    }
}
