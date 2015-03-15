/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.caret;

import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.util.UIUtil;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;

/**
 *
 * @author dq
 */
class JCaretListeners {

    static class K1Listener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            ICaretParent caretParent = (ICaretParent) e.getSource();
            if (caretParent.getCaret() != null) {
                if (caretParent.getCaret().getState() == JCaret.STATE.OFF || caretParent == null || !((Component) caretParent).isShowing()) {
                    return;
                }
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    caretParent.getCaret().handleKeyPressed(e);
                }
            }
        }
    }

    static class AWTListener implements AWTEventListener {

        private JCaret caret;

        public AWTListener(JCaret caret) {
            this.caret = caret;
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if (caret == null || caret == null || caret.getCaretParent() == null || !caret.isEnabled() || !((Component) caret.getCaretParent()).isShowing()) {
                return;
            }
            Object src = event.getSource();

            boolean dialogDescendant = UIUtil.isDescendantOfClass((Component) src, JDialog.class);
            if (dialogDescendant) {
                return;
            }
            boolean popupDescendant = UIUtil.isDescendantOfClass((Component) src, JPopupMenu.class);
            if (popupDescendant) {
                return;
            }

            if (event instanceof MouseEvent) {
                if (event.getID() == MouseEvent.MOUSE_RELEASED) {
                    boolean isWithinBoundary = caret.isWithinBoundary((MouseEvent) event);

                    if (isWithinBoundary) {
                        caret.handleMouseEvent((MouseEvent) event);
                    } else {
                    }
                }
            }
        }
    }

    protected static class PtyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            JCaret jcaret = (JCaret) evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("state")) {
                if (jcaret.getState() == JCaret.STATE.ON) {
                    jcaret.getFlasher().stop();
                    jcaret.repaint();
                } else if (jcaret.getState() == JCaret.STATE.BLINK) {
                    jcaret.getFlasher().start();
                } else {
                    jcaret.getFlasher().stop();
                    jcaret.repaint();
                }
            } else if (name.equals("insertMode")) {
                int width = jcaret.getCaretParent().getCaretWidth();
                UIUtil.setWidth(jcaret, width);
            } else if (name.equals("location")) {
                jcaret.repaint();
                //jcaret.setPos(jcaret.getCaretParent().getCaretPos());
            } else if (name.equals("pos")) {
                if (v != null) {
                    final Point pos = (Point) v;
                    final ICaretParent caretParent = jcaret.getCaretParent();
                    final Dimension size = ((Component) caretParent).getSize();
                    if (!caretParent.isCircular()) {
                        Point location = jcaret.getCaretParent().getCaretLocationByPos(pos);
                        if (location != null) {
                            jcaret.setLocation(location);
                        }
                    } else {
                        Double angle = caretParent.getCaretAngleByPos(pos.x);
                        if (angle != null) {
                            jcaret.setBounds(0, 0, size.width, size.height);
                            jcaret.setAngdeg(angle);
                        }
                    }
                    jcaret.repaint();
                    StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setCaretPos(pos.x);
                }
            }
        }
    }
}
