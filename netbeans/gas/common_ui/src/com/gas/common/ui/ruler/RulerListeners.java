/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;

/**
 *
 * @author dq
 */
class RulerListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Ruler src = (Ruler) evt.getSource();
            Object v = evt.getNewValue();
            final String name = evt.getPropertyName();
            if (name.equals("selection") || name.equals("indicator") || name.equals("locList")) {
                if (name.equals("locList")) {
                    Collections.sort((RulerLocList) v, new RulerLoc.Sorter());
                }
                src.repaint();
            }
        }
    }
}
