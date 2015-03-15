/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author dq
 */
class OverlayListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Overlay overlay = (Overlay) evt.getSource();
            String pName = evt.getPropertyName();
            if (pName.equals("whiteness") || pName.equals("alpha")) {
                overlay.fillColor = new Color(overlay.whiteness, overlay.whiteness, overlay.whiteness, overlay.alpha);
                overlay.repaint();
            } else if (pName.equals("westBorder") || pName.equals("eastBorder")
                    || pName.equals("southBorder") || pName.equals("northBorder")
                    || pName.equals("borderColor")) {
                overlay.repaint();
            } else if (pName.equals("draggable")) {
                if (overlay.draggable) {
                    overlay.addMouseListener(overlay.mouseInputAdapter);
                    overlay.addMouseMotionListener(overlay.mouseInputAdapter);
                } else {
                    overlay.removeMouseListener(overlay.mouseInputAdapter);
                    overlay.removeMouseMotionListener(overlay.mouseInputAdapter);
                }
            }
        }
    }
}
