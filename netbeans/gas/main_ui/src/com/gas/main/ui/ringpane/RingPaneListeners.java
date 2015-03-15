/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.color.IColorProvider;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.main.ui.minimap.Minimap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class RingPaneListeners {

    static class ColorPreferenceListener implements PropertyChangeListener {

        private RingPane ringPane;
        
        ColorPreferenceListener(RingPane ringPane){
            this.ringPane = ringPane;
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            Color color = (Color) evt.getNewValue();
            ringPane.getRingGraphPanel().setSeedColor(propertyName, color);
            
        }
    }
    
    static class ScrollBarListener implements AdjustmentListener {

        private RingPane ringPane;

        ScrollBarListener(RingPane ringPane) {
            this.ringPane = ringPane;
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            boolean adjusting = e.getValueIsAdjusting();
            if (!adjusting || e.getAdjustmentType() == AdjustmentEvent.BLOCK_INCREMENT
                    || e.getAdjustmentType() == AdjustmentEvent.BLOCK_DECREMENT) {

                Loc visibleLoc = ringPane.getRingGraphPanel().calculateVisibleLoc();
                if (visibleLoc == null) {
                    return;
                }
                final AnnotatedSeq as = ringPane.getAs();
                ringPane.getRingGraphPanel().setVisibleLoc(visibleLoc);

                int value = e.getValue();

                int totalLength = as.getLength();
                if (totalLength != ringPane.getScrollBar().getMaximum()) {
                    ringPane.getScrollBar().setMaximum(as.getLength());
                }
                final float increment = value * 1.0f / totalLength * 360;
                ringPane.getRingGraphPanel().setOffset(increment);
                float overlayStart = 1.0f * visibleLoc.getStart() / as.getLength();
                float overlayWidth;

                overlayWidth = 1.0f * visibleLoc.width() / as.getLength();


                if (!ringPane.getMinimap().isBusy() && !MinimapPtyListener.busy) {
                    ringPane.getMinimap().setBusy(true);
                    ringPane.getMinimap().setOverlayStart(overlayStart);
                    ringPane.getMinimap().setOverlayWidth(overlayWidth);
                    ringPane.getMinimap().setBusy(false);
                }
            }

        }
    }

    static class MinimapPtyListener implements PropertyChangeListener {

        private RingPane ringPane;
        protected static boolean busy;

        MinimapPtyListener(RingPane ringPane) {
            this.ringPane = ringPane;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            if (pName.equals("overlayStart")) {
                boolean _adjusting = ringPane.getMinimap().isBusy();
                if (!_adjusting) {
                    MinimapPtyListener.busy = true;
                    Float overlayStart = (Float) evt.getNewValue();
                    int max = ringPane.getScrollBar().getMaximum();
                    ringPane.getScrollBar().setValueIsAdjusting(true);
                    ringPane.getScrollBar().setValue(Math.round(max * overlayStart));
                    ringPane.getScrollBar().setValueIsAdjusting(false);
                    MinimapPtyListener.busy = false;
                }
            }
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RingPane ringPane = (RingPane) evt.getSource();
            AnnotatedSeq as = ringPane.getAs();
            final String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("as")) {

                ringPane.ringGraphPanelRef.get().setAs(as);
                ringPane.minimap.setAs(as);
                ringPane.getScrollBar().setMaximum(as.getLength());
                if(!as.getTranslationResults().isEmpty()){
                    ringPane.setTranslationResults(as.getTranslationResults());
                }                
            } else if (name.equals("minimapShown")) {
                Minimap minimap = ringPane.getMinimap();
                if (ringPane.getMinimapShown()) {
                    ringPane.add(minimap, BorderLayout.NORTH);
                } else {
                    ringPane.remove(minimap);
                }
                ringPane.revalidate();
                ringPane.getRingGraphPanel().revalidate();
            } else if (name.equals("translationResults")) {
                ringPane.getRingGraphPanel().reinitTranslatedBrickRings();
                ringPane.getRingGraphPanel().revalidate();                       
            } else if (name.equals("translationColorProvider")) {
                IColorProvider r = ringPane.getTranslationColorProvider();
                ringPane.getRingGraphPanel().setTranslationColorProvider(r);
            } else if (name.equals("centerPos")) {
                Integer pos = ringPane.getCenterPos();
                if (pos != null) {
                    ringPane.getScrollBar().setValue(pos - 1);
                }
            } else if (name.equals("UIUpdateFlag")) {
                if (ringPane.getAs() == null) {
                    return;
                }
                ringPane.getRingGraphPanel().refresh(null);
            } else if (name.equals("annotationLabelSize")) {
                Float size = (Float) v;
                ringPane.getRingGraphPanel().getLabelListComp().setFontSize(size);
                ringPane.getRingGraphPanel().revalidate();
            }
        }
    }

    static class ScrollPaneResizeListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            JScrollPane src = (JScrollPane) e.getSource();
            RingGraphPanel view = (RingGraphPanel) src.getViewport().getView();
            if (view != null && view.getBrickRing() != null) {
                view.getBrickRing().repaint();
            }
        }
    }

    static class ScrollPaneHBarModelListener implements ChangeListener {

        private RingPane ringPane;
        private JScrollPane scrollPane;

        ScrollPaneHBarModelListener(RingPane ringPane) {
            this.ringPane = ringPane;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            RingGraphPanel ringPanel = ringPane.getRingGraphPanel();
            Dimension preferredSize = ringPanel.getPreferredSize();
            if (scrollPane == null) {
                scrollPane = ringPane.getScrollPane();
            }
            Dimension viewportSize = scrollPane.getViewport().getSize();
            int value = Math.round((preferredSize.width - viewportSize.width) * 0.5f);
            int max = scrollPane.getHorizontalScrollBar().getMaximum();

            scrollPane.getHorizontalScrollBar().setValue(value);
            scrollPane.getVerticalScrollBar().setValue(0);
        }
    }
}
