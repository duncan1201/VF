/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.jcomp.CircularOverlay;
import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.statusline.StatusLinePanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.MolPanePopup;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
class RingGraphPanelListeners {

    static class StatusLineUpdater implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            StatusLinePanel statusLinePanel = StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement();
            if (name.equals("selectedLoc")) {
                Loc loc = (Loc) v;
                if (loc != null) {
                    statusLinePanel.setStart(loc.getStart());
                    statusLinePanel.setEnd(loc.getEnd());
                    if (loc.getTotalPos() != null) {
                        statusLinePanel.setSelectLength(loc.width());
                    }
                } else {
                    statusLinePanel.clearSelection();
                }
            }
        }
    }

    static class CompListener extends ComponentAdapter {

        CompListener() {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            RingGraphPanel ringPanel = (RingGraphPanel) e.getSource();
            Dimension size = ringPanel.getSize();
            ringPanel.getLayeredPane().setSize(size);
            Loc loc = ringPanel.calculateVisibleLoc();
            if (loc == null) {
                return;
            }
            ringPanel.setVisibleLoc(loc);
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        private RingPane ringPane;
        private IASEditor editor;

        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RingGraphPanel ringGraphPanel = (RingGraphPanel) evt.getSource();
            if (editor == null) {
                editor = UIUtil.getParent(ringGraphPanel, IASEditor.class);
            }
            if (ringPane == null) {
                ringPane = UIUtil.getParent(ringGraphPanel, RingPane.class);
            }
            Object oV = evt.getOldValue();
            Object nV = evt.getNewValue();
            String pName = evt.getPropertyName();
            if (pName.equals("selectedLoc")) {
                Loc loc = ringGraphPanel.getSelectedLoc();
                CircularOverlay ol = ringGraphPanel.getOverlay();
                if (loc != null) {
                    int totalPos = ringPane.getAs().getLength();
                    float startAngle = UIUtil.toDegree(loc.getStart(), totalPos, SwingConstants.LEFT, Float.class);
                    float extent = UIUtil.toScreenWidthFloat(360, totalPos, loc.width());

                    ol.setStartAngle(startAngle);
                    ol.setExtent(extent);
                } else {
                    ol.setExtent(0);
                }
                ringGraphPanel.getBrickRing().setSelection(loc);
                ringGraphPanel.getCircularRuler().setSelection(loc);
            } else if (pName.equals("doubleStranded")) {
                Boolean doubleStranded = ringGraphPanel.isDoubleStranded();
                ringGraphPanel.getBrickRing().setDoubleStranded(doubleStranded);
                ringGraphPanel.revalidate();
            } else if (pName.equals("offset")) {

                final float offset = ringGraphPanel.getOffset();
                // most time-consuming tasks first
                ringGraphPanel.getBrickRing().setRotateOffset(-offset);
                Iterator<BrickRing> bItr = ringGraphPanel.getTranslatedBrickRings().iterator();
                while (bItr.hasNext()) {
                    BrickRing bRing = bItr.next();
                    bRing.setRotateOffset(-offset);
                }

                // then, ruler
                ringGraphPanel.getCircularRuler().setOffset(offset);

                // then, plots
                ringGraphPanel.getCirLinePlotsComp().setStartOffet(offset);

                // then rings
                ringGraphPanel.getRingListMapComp().setStartOffset(-offset);

                // then brackets
                ringGraphPanel.getCurvedBrackets().setStartOffset(offset);

                // reposition the caret
                final Point caretPos = ringGraphPanel.getCaret().getPos();
                if (caretPos != null) {
                    ringGraphPanel.getCaret().setPos(new Point(caretPos.x, 1), true);
                }
                ringGraphPanel.getOverlay().setRotateOffset(offset);
                ringGraphPanel.revalidate();// relayout the labels

            } else if (pName.equals("visibleLoc")) {
                Loc visibleLoc = ringPane.getRingGraphPanel().calculateVisibleLoc();
                if (visibleLoc == null) {
                    return;
                }
                int value = ringPane.getScrollBar().getValue();
                int max = ringPane.getScrollBar().getMaximum();
                float increment = value * 1.0f / max * 360;
                ringPane.getRingGraphPanel().setOffset(increment);
                final AnnotatedSeq as = ringPane.getRingGraphPanel().getAs();
                float overlayStart = 1.0f * visibleLoc.getStart() / as.getLength();
                float overlayWidth;

                overlayWidth = 1.0f * visibleLoc.width() / as.getLength();

                boolean minimapAdjusting = ringPane.getMinimap().isBusy();
                if (!minimapAdjusting && !RingPaneListeners.MinimapPtyListener.busy) {
                    ringPane.getMinimap().setBusy(true);
                    ringPane.getMinimap().setOverlayStart(overlayStart);
                    ringPane.getMinimap().setOverlayWidth(overlayWidth);
                    ringPane.getMinimap().setBusy(false);
                }
            } else if (pName.equals("rulerFontSize")) {
                CircularRuler ruler = ringGraphPanel.getCircularRuler();
                Font newFont = ruler.getFont().deriveFont(ringGraphPanel.getRulerFontSize());
                ruler.setFont(newFont);
                ringGraphPanel.revalidate();
            } else if (pName.equals("baseFontSize")) {
                Float newSize = ringGraphPanel.getBaseFontSize();
                Font newFont = ringGraphPanel.getBrickRing().getFont().deriveFont(newSize);
                ringGraphPanel.getBrickRing().setFont(newFont);
                ringGraphPanel.revalidate();
            } else if (pName.equals("translationResults")) {
                ringGraphPanel.reinitTranslatedBrickRings();
                ringGraphPanel.revalidate();
            } else if (pName.equals("translationColorProvider")) {
                IColorProvider provider = ringGraphPanel.getTranslationColorProvider();
                Iterator<BrickRing> itr = ringGraphPanel.getTranslatedBrickRings().iterator();
                while (itr.hasNext()) {
                    BrickRing ring = itr.next();
                    ring.setColorProvider(provider);
                }
            } else if (pName.equals("rmap")) {
                ringGraphPanel.reinitCurvedBrackets();
                ringGraphPanel.revalidate();
            } else if (pName.equals("as")) {
                final AnnotatedSeq as = ringGraphPanel.getAs();
                if (as.getLength() == 0) {
                    return;
                }

                ringGraphPanel.initUI();

                ringGraphPanel.getCircularRuler().setRange(new Loc(1, as.getLength()));
                ringGraphPanel.getNameLabel().setText(as.getName());
                ringGraphPanel.getLengthLabel().setText(String.format("%dbp", as.getLength()));

                float zoom = as.getAsPref().getZoom();
                Dimension size = ringGraphPanel.getFullSize();
                Dimension newSize = new Dimension(Math.round(size.width * zoom), Math.round(size.height * zoom));
                ringGraphPanel.setPreferredSize(newSize);
                ringGraphPanel.revalidate();
            } else if (pName.equals("editingAllowed")) {
                ringGraphPanel.getCaret().setState((Boolean) nV ? JCaret.STATE.BLINK : JCaret.STATE.ON);
            } else if (pName.equals("selectedFeture")) {
                final Feture f = (Feture) nV;
                Label label = ringGraphPanel.getLabelListComp().updateSelection(f);
                ringGraphPanel.getConnectorsComp().updateSelection(label);
                ringGraphPanel.getRingListMapComp().updateSelection(f);
                if (f != null) {
                    Lucation lucation = f.getLucation();
                    Loc loc = lucation.toLoc();
                    loc.setTotalPos(ringGraphPanel.getAs().getLength());
                    ringGraphPanel.setSelectedLoc(loc);
                } else {
                    ringGraphPanel.setSelectedLoc(null);
                }
            } else if (pName.equals("gcResult")) {
                ringGraphPanel.revalidate();
            }
        }
    }

    static class AWTListener implements AWTEventListener {

        private RingGraphPanel ringGraphPanel;
        private Point pressedLocOnScreen;
        private Point draggedLocOnScreen;
        private DegreeFloatList draggedDegs = new DegreeFloatList();
        private WeakReference<RingPane> ringPaneRef;
        private WeakReference<MolPane> molPaneRef;
        private MolPanePopup popupMenu;

        AWTListener(RingGraphPanel ringGraphPanel) {
            this.ringGraphPanel = ringGraphPanel;
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if(!ringGraphPanel.isShowing()){
                return;
            }
            Object src = event.getSource();
            if (ringPaneRef == null) {
                RingPane ringPane = UIUtil.getParent(ringGraphPanel, RingPane.class);
                if (ringPane != null) {
                    ringPaneRef = new WeakReference<RingPane>(ringPane);
                }
            }
            if (ringPaneRef == null || ringPaneRef.get() == null || !ringPaneRef.get().isShowing() || ringGraphPanel.getBrickRing() == null) {
                return;
            }

            if (event instanceof MouseEvent) {

                Point locOnScreen = ((MouseEvent)event).getLocationOnScreen();
                if(event.getID() == MouseEvent.MOUSE_WHEEL){
                    locOnScreen = MouseInfo.getPointerInfo().getLocation();
                }                

                Rectangle viewportBoundsOnScreen = UIUtil.getBoundsOnScreen(ringPaneRef.get().getScrollPane().getViewport());

                if (viewportBoundsOnScreen == null) {
                    return;
                } else if (!viewportBoundsOnScreen.contains(locOnScreen)) {
                    return;
                } else {                    
                    boolean descendantOfDialog = UIUtil.isDescendantOfClass((Component) src, JDialog.class);
                    if (descendantOfDialog) {                        
                        return;
                    }
                    boolean popupDescendant = UIUtil.isDescendantOfClass((Component) src, JPopupMenu.class);
                    if (popupDescendant) {
                        return;
                    }
                }
                handleMouseEvent((MouseEvent) event);
            }
        }

        private void handleMouseEvent(MouseEvent me) {
            int id = me.getID();
            if (id == MouseEvent.MOUSE_PRESSED) {
                handleMousePressed(me);
            } else if (id == MouseEvent.MOUSE_DRAGGED) {
                handleMouseDragged(me);
            } else if (id == MouseEvent.MOUSE_RELEASED) {
                handleMouseReleased(me);
            } else if (id == MouseEvent.MOUSE_WHEEL) {
                handleMouseWheel((MouseWheelEvent) me);
            } else if(id == MouseEvent.MOUSE_MOVED){
                handleMouseMoved(me);
            }
        }
        
        private void handleMouseMoved(MouseEvent me){
            RingPane ringPane = ringPaneRef.get();
            Point locOnScreen = me.getLocationOnScreen();
            ringPane.getRingGraphPanel().getRingListMapComp().updateMouseIn(locOnScreen);            
        }
        
        private void handleMouseWheel(MouseWheelEvent e) {
            RingPane ringPane = ringPaneRef.get();
            JScrollBar bar = ringPane.getScrollBar();
            int value = bar.getModel().getValue();
            int max = bar.getModel().getMaximum();
            int min = bar.getModel().getMinimum();
            int extent = bar.getModel().getExtent();
            int rotation = e.getWheelRotation();
            float zoom = ringPane.getAs().getAsPref().getZoom();
            int delta = Math.round(1 / zoom);
            if (rotation > 0) { // down
                if (value + delta + extent < max) {
                    bar.setValue(value + delta);
                } else {
                    bar.setValue(min);
                }
            } else { // up
                if (value - delta > min) {
                    bar.setValue(value - delta);
                } else {
                    bar.setValue(max - extent);
                }
            }
        }

        private MolPane getMolPane() {
            if (molPaneRef == null || molPaneRef.get() == null) {
                MolPane molPane = UIUtil.getParent(ringGraphPanel, MolPane.class);
                molPaneRef = new WeakReference<MolPane>(molPane);
            }
            return molPaneRef.get();
        }

        private void handleMouseReleased(MouseEvent me) {
            pressedLocOnScreen = null;
            draggedLocOnScreen = null;
            draggedDegs.clear();
        }

        private void handleMouseDragged(MouseEvent me) {
            draggedLocOnScreen = me.getLocationOnScreen();

            Rectangle bounds = ringGraphPanel.getBoundsOnScreen();
            final Dimension size = ringGraphPanel.getSize();
            if (bounds != null && pressedLocOnScreen != null && draggedLocOnScreen != null) {
                Point start = new Point((int) Math.round(bounds.getCenterX()), (int) Math.round(bounds.getY()));
                MathUtil.getAngleInDegrees(start, start);

                Point pressedLoc = new Point(pressedLocOnScreen);
                SwingUtilities.convertPointFromScreen(pressedLoc, ringGraphPanel);

                Point draggedLoc = new Point(draggedLocOnScreen);
                SwingUtilities.convertPointFromScreen(draggedLoc, ringGraphPanel);

                final Point2D.Double center = new Point2D.Double(size.width * 0.5, size.height * 0.5);

                Double pressedDeg = MathUtil.getAngleInDegrees(new Point2D.Double(pressedLoc.x, pressedLoc.y), center);
                Double draggedDeg = MathUtil.getAngleInDegrees(new Point2D.Double(draggedLoc.x, draggedLoc.y), center);

                draggedDegs.addIfDiffDirection(MathUtil.normalizeDegree(90 - draggedDeg.floatValue()).floatValue());
                Integer pressedPos = ringGraphPanel.getBrickRing().getCaretPos(pressedDeg);
                Integer draggedPos = ringGraphPanel.getBrickRing().getCaretPos(draggedDeg);

                boolean clockwise = draggedDegs.isClockwise();
                final int totalPos = ringPaneRef.get().getAs().getLength();
                Loc loc = new Loc();
                if (clockwise) {
                    if (pressedPos.equals(draggedPos)) {
                        loc.setStart(pressedPos);
                        loc.setEnd(draggedPos);
                    } else {
                        loc.setStart(pressedPos);
                        if (draggedPos.intValue() == 1) {
                            loc.setEnd(totalPos);
                        } else {
                            loc.setEnd(draggedPos - 1);
                        }
                    }
                } else {
                    if (pressedPos.equals(draggedPos)) {
                        loc.setStart(pressedPos);
                        loc.setEnd(draggedPos);
                    } else {
                        loc.setStart(draggedPos);
                        if (pressedPos.intValue() == 1) {
                            loc.setEnd(totalPos);
                        } else {
                            loc.setEnd(pressedPos - 1);
                        }
                    }
                }
                loc.setTotalPos(ringGraphPanel.getAs().getLength());
                System.out.println(clockwise);
                System.out.println(loc);
                ringGraphPanel.setSelectedLoc(loc);
            }
        }

        private void handleMousePressed(MouseEvent me) {
            final boolean isLeftBtn = SwingUtilities.isLeftMouseButton(me);
            final boolean isRightBtn = SwingUtilities.isRightMouseButton(me);

            if (isLeftBtn) {
                handleLeftMousePressed(me);
            } else if (isRightBtn) {
                handleRightMousePressed(me);
            }

        }

        private void handleRightMousePressed(MouseEvent me) {
            if (popupMenu == null) {
                popupMenu = new MolPanePopup(getMolPane());
            }
            updatePopup(me);
            Point p = me.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(p, ringPaneRef.get());
            UIUtil.showPopupMenu(popupMenu, ringPaneRef.get(), p.x, p.y);
        }

        private void updatePopup(MouseEvent me) {
            Feture feture = null;

            Ring ring = ringGraphPanel.getRingListMapComp().updateSelection(me.getLocationOnScreen());
            Label label = ringGraphPanel.getLabelListComp().updateSelection(me.getLocationOnScreen());
            if (ring != null) {
                feture = (Feture) ring.getData();
            } else if (label != null) {
                Ring r = (Ring) label.getData();
                feture = (Feture) r.getData();
            }
            popupMenu.setFeture(feture);
            popupMenu.setRen(ringGraphPanel.getCurvedBrackets().getSelectedEntries());
            popupMenu.updateEnablementBasedOnSelection();
        }

        private void handleLeftMousePressed(MouseEvent me) {
            pressedLocOnScreen = me.getLocationOnScreen();
            ringGraphPanel.updateSelection(pressedLocOnScreen);
        }
    }
}
