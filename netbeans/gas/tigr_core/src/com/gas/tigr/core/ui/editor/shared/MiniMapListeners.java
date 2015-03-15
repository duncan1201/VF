/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.editor.TigrPtPanel;
import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JScrollBar;

/**
 *
 * @author dq
 */
class MiniMapListeners {

    static class AwtListener implements AWTEventListener {

        MiniMap minimap;
        TigrPtPanel tigrPtPanel;
        MainComp mainPanel;

        public AwtListener(MiniMap minimap) {
            this.minimap = minimap;
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if (!minimap.isShowing()) {
                return;
            }
            MouseEvent me = (MouseEvent) event;
            if (me.getID() == MouseEvent.MOUSE_PRESSED) {
                Point locOnScreen = me.getLocationOnScreen();
                Boolean contains = UIUtil.isWithin(minimap, locOnScreen);
                if (contains != null && contains) {
                    init();
                    mainPanel.requestFocusInWindow();
                }
            }
        }

        void init() {
            if (tigrPtPanel == null) {
                tigrPtPanel = UIUtil.getParent(minimap, TigrPtPanel.class);
                mainPanel = tigrPtPanel.getAssemblyPanel().getAssemblySPane().getMainComp();
            }
        }
    }

    static class PtyListener implements PropertyChangeListener {

        TigrPtPanel tigrPtPanel;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            MiniMap src = (MiniMap) evt.getSource();
            String pName = evt.getPropertyName();
            if (tigrPtPanel == null) {
                tigrPtPanel = UIUtil.getParent(src, TigrPtPanel.class);
                if (tigrPtPanel == null) {
                    return;
                }
            }
            if (pName.equals("overlayStart") || pName.equals("overlayWidth")) {
                if (src.overlayStart != null && src.overlayWidth != null) {
                    src.updateOverlayPositions();
                }
                if (pName.equals("overlayStart")) {
                    Float start = (Float) evt.getNewValue();
                    JScrollBar scrollBar = tigrPtPanel.getAssemblyPanel().getAssemblySPane().getHorizontalScrollBar();
                    int max = scrollBar.getMaximum();
                    scrollBar.setValue(MathUtil.round(max * start));
                }
            }
        }
    }

    static class CompAdpt extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            MiniMap miniMap = (MiniMap) e.getSource();
            Rectangle rect = miniMap.getBounds();
            miniMap.layeredPane.setBounds(rect);
            miniMap.cardPanel.setBounds(rect);
            miniMap.cardPanel.revalidate();
            miniMap.updateOverlayPositions();
        }
    }
}
