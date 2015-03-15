/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.core.Arc2DX;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class CircularBrickListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object v = evt.getNewValue();
            String pName = evt.getPropertyName();
            CircularBrick src = (CircularBrick) evt.getSource();
            if (pName.equals("innerArc") || pName.equals("outerArc")) {
                createPath(src);
            }
        }

        void createPath(CircularBrick c) {
            if (c.getInnerArc() != null && c.getOuterArc() != null) {
                GeneralPath p = new GeneralPath();
                p.append(c.getOuterArc(), false);
                p.append(c.getInnerArc(), true);
                p.closePath();

                c.setPath(p);
            }
        }
    }
}
