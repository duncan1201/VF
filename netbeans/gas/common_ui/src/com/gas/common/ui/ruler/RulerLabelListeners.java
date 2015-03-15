/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class RulerLabelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            RulerLabel src = (RulerLabel) evt.getSource();
            if (name.equals("x") || name.equals("y")
                    || name.equals("font") || name.equals("pos")
                    || name.equals("tickHeight") || name.equals("tickGap")) {
                Rectangle b = src.createBounds();
                if (b != null) {
                    src.setBounds(b);
                }
            }
        }
    }
}
