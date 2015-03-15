/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.minimap;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.jcomp.Overlay;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.ui.shape.IShapeList;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import javax.swing.JLayeredPane;

/**
 *
 * @author dq
 */
class MinimapListeners {

    static class CompAdptr extends ComponentAdapter {


        @Override
        public void componentResized(ComponentEvent e) {
            Minimap minimap = (Minimap)e.getSource();
            Rectangle rect = minimap.getBounds();
            minimap.getLayeredPane().setBounds(rect);
            minimap.updateOverlaysBounds();
        }
    }

    static class OverlayPtyChangeListener implements PropertyChangeListener {


        OverlayPtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Overlay overlay = (Overlay)evt.getSource();
            Minimap minimap = UIUtil.getParent(overlay, Minimap.class);
            String pName = evt.getPropertyName();
            Object newVal = evt.getNewValue();
            if (evt.getSource() == minimap.getOverlay()) {
                boolean valueAdjusting = minimap.getOverlay().isBusy();

                if (pName.equals("bounds")) {
                    if (!valueAdjusting) {
                        Rectangle b = (Rectangle) newVal;
                        
                        minimap.setOverlayStart(1.0f * b.x / minimap.getWidth());
                        minimap.setOverlayWidth(1.0f * b.width / minimap.getWidth());
                        minimap.updateGrayOverlays();
                      
                    }
                }

            }
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {


        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Minimap minimap = (Minimap)evt.getSource();
            String pName = evt.getPropertyName();
            if (pName.equals("as")) {
                
                minimap.getArrowsMap().clear();
                UIUtil.removeComponentsInLayer(minimap.getLayeredPane(), JLayeredPane.DEFAULT_LAYER);
                Map<String, IShapeList> _arrowsMap = minimap.createArrowsMap();
                minimap.setArrowsMap(_arrowsMap);
                minimap.revalidate();
            } else if (pName.equals("overlayStart")) {
                if (minimap.getOverlayWidth() != null) {
                    minimap.updateOverlaysBounds();
                }
            } else if (pName.equals("overlayWidth")) {
                if (minimap.getOverlayStart() != null && minimap.getOverlayWidth() != null) {
                    minimap.updateOverlaysBounds();
                }
            }
        }
    }
}
