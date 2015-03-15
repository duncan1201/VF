/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.triangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
public class TriangleListeners {

    protected static class PtyChangeListener implements PropertyChangeListener {

        private Triangle triangle;

        public PtyChangeListener(Triangle triangle) {
            this.triangle = triangle;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            if (pName.equals("selected") || pName.equals("down")) {
                triangle.repaint();
            }
        }
    }
}
