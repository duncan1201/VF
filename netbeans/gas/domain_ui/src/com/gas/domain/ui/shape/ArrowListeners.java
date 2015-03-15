/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.domain.ui.shape.IShape.TEXT_LOC;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
public class ArrowListeners {

    public static class ArrowMouseAdapter extends MouseAdapter {

        public ArrowMouseAdapter() {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Arrow arrow = (Arrow) e.getSource();
            arrow.mouseHover = true;
            if (!arrow.selected) {
                arrow.repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Arrow arrow = (Arrow) e.getSource();
            arrow.mouseHover = false;
            if (!arrow.selected) {
                arrow.repaint();
            }
        }
    }

    public static class PtyChangeListener implements PropertyChangeListener {

        public PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Arrow arrow = (Arrow) evt.getSource();
            final String name = evt.getPropertyName();
            if (name.equals("textLoc")) {
                TEXT_LOC old = (TEXT_LOC) evt.getOldValue();
                TEXT_LOC _new = (TEXT_LOC) evt.getNewValue();
                if (old == TEXT_LOC.NONE && _new == TEXT_LOC.INSIDE) {
                    arrow.repaint();
                } else if (old == TEXT_LOC.INSIDE && _new == TEXT_LOC.NONE) {
                    arrow.repaint();
                }
            } else if (name.equals("seedColor") || name.equals("selected")) {
                arrow.repaint();
            } else if (name.equals("data")) {
                arrow.setRichToolTip(ToolTipCreatorHelper.create(arrow), true, false);                
            } else if (name.equals("hoverEnabled")) {
                arrow.mouseHover = false;
                arrow.removeMouseListener(arrow.arrowMouseAdapter);
                arrow.repaint();
            } else if (name.equals("textFont")) {
                arrow.repaint();
            }
        }
    }
}
