/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.InputEventUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.domain.ui.brickComp.BrickComp;
import com.gas.common.ui.ruler.Ruler;
import com.gas.common.ui.ruler.RulerLoc;
import com.gas.common.ui.ruler.RulerLocList;
import com.gas.common.ui.tooltip.ToolTipMgr;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.TopBracket;
import com.gas.domain.ui.editor.TopBracketList;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.domain.ui.shape.Arrow;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.IShapeList;
import com.gas.domain.ui.shape.IShapeListMap;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.MolPanePopup;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import javax.persistence.metamodel.SetAttribute;
import javax.swing.BoundedRangeModel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
public class GraphPaneListeners {

    public static void resetGraphPanelPreferredSize(GraphPane graphPane, float zoom) {
        GraphPanel graphPanel = graphPane.getGraphPanel();

        Integer fullWidth = graphPanel.getFullWidth();
        if (fullWidth != null) {
            int newPreferredWidth = Math.max(graphPane.getScrollPane().getViewport().getSize().width, MathUtil.round(fullWidth * zoom));
            UIUtil.setPreferredWidth(graphPanel, newPreferredWidth);

            Loc cLoc = graphPane.getCenterLoc();
            if (cLoc != null) {
                graphPane.center(cLoc);
            }
            graphPanel.revalidate();
            graphPane.getRuler().repaint();
        }
    }

    static class PtyListener implements PropertyChangeListener {

        PtyListener(GraphPane graphPane) {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            GraphPane src = (GraphPane) evt.getSource();
            Object nV = evt.getNewValue();
            if (name.equals("as")) {
                AnnotatedSeq as = (AnnotatedSeq) nV;

                Font textFont = FontUtil.getDefaultSansSerifFont().deriveFont(src.getRulerFontSize());
                src.getRuler().setTextFont(textFont);
                UIUtil.setPreferredHeight(src.getRuler(), src.getRuler().getDesiredHeight());

                src.getMinimap().setAs(as);
                src.reinitTrackVisibilityMap();
                src.reinitLabelVisibilityMap();
                src.getGraphPanel().setAs(as);
                src.getRowHeaderView().reset();                
                RulerLocList locList = new RulerLocList(new RulerLoc(1, as.getLength(), 0));
                if (!as.getParentLocSet().isEmpty()) {
                    Iterator<ParentLoc> itr = as.getParentLocSet().iterator();
                    while (itr.hasNext()) {
                        ParentLoc parentLoc = itr.next();
                        RulerLoc rulerLoc = parentLoc.toRulerLoc();
                        locList.add(rulerLoc);
                    }
                }
                if(src.getColumnHeaderView() != null){
                    src.getColumnHeaderView().setLocList(locList);                
                    UIUtil.setPreferredHeight(src.getColumnHeaderView(), src.getColumnHeaderView().getDesiredHeight());                
                }
            } else if (name.equals("selectedFeture")) {
                Feture feture = src.getSelectedFeture();
                Lucation luc = feture.getLucation();
                int min = luc.getStart();
                int max = luc.getEnd();
                LocList locList = new LocList();
                Loc loc = new Loc(min, max);
                locList.add(new Loc(min, max));
                src.setSelections(locList);
                src.getGraphPanel().setSelectedFeture(feture);
                if (src.getAs().isCircular()) {
                    loc.setTotalPos(src.getAs().getLength());
                }
                src.setCenterLoc(loc);
            } else if (name.equals("selectedArrow")) {
                Arrow arrow = (Arrow) nV;
                src.getGraphPanel().setSelectedArrow(arrow);
            } else if (name.equals("selections")) {
                LocList locList = (LocList) nV;

                final Ruler ruler = src.getColumnHeaderView();
                if (!locList.isEmpty()) {
                    Loc loc = locList.get(0);
                    if(ruler != null){
                        ruler.setSelection(loc);
                    }
                    src.getGraphPanel().setSelection(new Loc2D(loc.getStart(), 1, loc.getEnd(), 1));
                } else {
                    if(ruler != null){
                        ruler.setSelection(null);
                    }
                    src.getGraphPanel().setSelection(null);
                }
            } else if (name.equals("centerLoc")) {
                src.center(src.getCenterLoc());
            } else if (name.equals("minimapShown")) {
                if (src.isMinimapShown()) {
                    src.add(src.getMinimap(), BorderLayout.NORTH);
                } else {
                    src.remove(src.getMinimap());
                }
                src.revalidate();
            } else if (name.equals("baseNumberShown")) {
                Ruler ruler = src.getRuler();

                Boolean baseNumber = (Boolean) evt.getNewValue();
                if (!baseNumber) {
                    src.getScrollPane().getColumnHeader().remove(ruler);
                } else {
                    src.getScrollPane().getColumnHeader().setView(ruler);
                }
                src.getScrollPane().invalidate();
                src.getScrollPane().validate();
            } else if (name.equals("rulerFontSize")) {
                Float newSize = (Float) evt.getNewValue();
                Ruler ruler = src.getRuler();
                UIUtil.setPreferredHeight(ruler, ruler.getDesiredHeight());
                ruler.repaint();
                Font newTextFont = ruler.getTextFont().deriveFont(newSize);
                ruler.setTextFont(newTextFont);
                src.getScrollPane().getColumnHeader().invalidate();
                src.getScrollPane().validate();
            } else if (name.equals("baseSize")) {
                Float newSize = (Float) evt.getNewValue();
                GraphPanel graphPanel = src.getGraphPanel();
                Iterator<BrickComp> itr = graphPanel.getBrickCompsMap().values().iterator();
                while (itr.hasNext()) {
                    BrickComp sbp = itr.next();
                    Font newFont = sbp.getFont().deriveFont(newSize);
                    sbp.setFont(newFont);
                    sbp.resetDesiredHeight();
                    sbp.resetDesiredWidth();
                    sbp.invalidate();
                }
                resetGraphPanelPreferredSize(src, src.getAs().getAsPref().getZoom());
            } else if (name.equals("annotationLabelSize")) {
                Float newSize = (Float) evt.getNewValue();
                GraphPanel graphPanel = src.getGraphPanel();
                Iterator<IShapeList> itr = graphPanel.getFeatureArrowMap().values().iterator();
                while (itr.hasNext()) {
                    Iterator<IShape> arrowItr = itr.next().iterator();
                    while (arrowItr.hasNext()) {
                        IShape arrow = arrowItr.next();
                        Font newFont = arrow.getTextFont().deriveFont(newSize);
                        arrow.setTextFont(newFont);
                    }
                }
                graphPanel.revalidate();
            } else if (name.equals("translationColorProvider")) {
                GraphPanel graphPanel = src.getGraphPanel();
                Iterator<BrickComp> itr = graphPanel.getBrickCompsMap().getTranslationBrickComps().iterator();
                while (itr.hasNext()) {
                    BrickComp sbp = itr.next();
                    sbp.setColorProvider(src.getTranslationColorProvider());
                }
            } else if (name.equals("primarySeqColorProvider")) {
                GraphPanel graphPanel = src.getGraphPanel();
                BrickComp sbp = graphPanel.getBrickCompsMap().getPrimaryBrickComp();
                if (sbp != null) {
                    sbp.setColorProvider(src.getPrimarySeqColorProvider());
                }
            }
        }
    }

    static class ColorPreferenceListener implements PropertyChangeListener {

        private GraphPane graphPane;

        public ColorPreferenceListener(GraphPane graphPane) {
            this.graphPane = graphPane;
        }
        GraphPanel graphPanel;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            Color color = (Color) evt.getNewValue();
            graphPane.getGraphPanel().getFtrTrack(propertyName).setArrowSeedColor(color);
        }
    }

    static class LabelVisiblePrefListener implements PropertyChangeListener {

        private GraphPane graphPane;

        public LabelVisiblePrefListener(GraphPane graphPane) {
            this.graphPane = graphPane;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            Boolean visible = (Boolean) evt.getNewValue();
            graphPane.setLableVisible(propertyName, visible);
        }
    }

    static class MinimapListener implements PropertyChangeListener {

        private GraphPane graphPane;

        public MinimapListener(GraphPane graphPane) {
            this.graphPane = graphPane;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            if (pName.equals("overlayStart") && !graphPane.scrollValueAdjusting) {
                Float newValue = (Float) evt.getNewValue();
                BoundedRangeModel model = graphPane.getScrollPane().getHorizontalScrollBar().getModel();

                graphPane.getScrollPane().getHorizontalScrollBar().setValue(MathUtil.round(model.getMaximum() * newValue));
            }
        }
    }

    static class ScrollPaneHorizontalRangeModelListener implements ChangeListener {

        private GraphPane graphPane;

        public ScrollPaneHorizontalRangeModelListener(GraphPane graphPane) {
            this.graphPane = graphPane;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            BoundedRangeModel model = (BoundedRangeModel) e.getSource();
            boolean adjusting = model.getValueIsAdjusting();
            //if (adjusting) return;
            int value = model.getValue();
            int max = model.getMaximum();
            int extent = model.getExtent();

            graphPane.scrollValueAdjusting = true;
            if (!graphPane.getMinimap().isBusy()) {
                graphPane.getMinimap().setOverlayStart(value * 1.0f / max);
                graphPane.getMinimap().setOverlayWidth(extent * 1.0f / max);
            }
            graphPane.scrollValueAdjusting = false;
        }
    }

    static class AWTMouseListener implements AWTEventListener {

        private MolPanePopup popupMenu;
        private WeakReference<MolPane> molPaneRef;
        // the following fields are cross-clicks
        private GraphPane graphPane;
        private Point ptPressedOnScreen = null;
        //private Point dragEnd = null;
        private Point locOnScreen = null;
        private boolean dragging = false;
        private boolean moving = false;
        private boolean dragCompleted = false;
        // the following fields are per event dispatch                         
        private boolean singleLeftPressed = false;
        private boolean singleLeft = false;
        boolean shiftLeftSelect = false;
        boolean rightClick = false;

        enum STATE {

            singleLeftDown, singleLeft, continuousLeftSelect, rightClick
        };
        // control click modifiers
        final int CTRL_LEFT_CLICK = InputEventUtil.getCtrlLeftBtnDownMask();
        /*
         * Windows: ctrl + left click Mac: Command + left click
         */
        boolean multiLeftSelect = false;
        /*
         * Shift + left click
         */
        Boolean isWithinViewport = null;
        private MouseEvent me = null;

        public AWTMouseListener(GraphPane graphPane) {
            this.graphPane = graphPane;
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if(!graphPane.isShowing()){
                return;
            }
            Object src = event.getSource();
            boolean isDescendantOfGraphPane = UIUtil.isDescendantOfClass((Component) src, GraphPane.class);

            if (!isDescendantOfGraphPane) {
                return;
            }

            analyzeEvent(event);
            if (isWithinViewport == null || !isWithinViewport) {
                return;
            }

            Rectangle toolTipBoundsOnScreen = ToolTipMgr.sharedInstance().getBoundsOnScreen();
            boolean popupDescendant = UIUtil.isDescendantOfClass((Component) src, JPopupMenu.class);
            if (popupDescendant) {
                return;
            }
            boolean onToolTip = UIUtil.contains(toolTipBoundsOnScreen, locOnScreen);
            if (onToolTip) {
                return;
            }
            if (singleLeftPressed) {
                TopBracket selectedTopBracket = graphPane.getGraphPanel().getTopBracketByPtOnScreen(locOnScreen);

                if (src instanceof Arrow) {
                    Arrow arrow = (Arrow) src;
                    graphPane.setSelection(arrow, arrow.getLoc());
                    getMolPane().clearSelectedSites();
                } else if (selectedTopBracket != null) {
                    getMolPane().setSelectedSite(selectedTopBracket);
                    getMolPane().setSelection(new Loc(selectedTopBracket.getStartPos(), selectedTopBracket.getEndPos()));
                    graphPane.setSelectedArrow(null);
                } else if (!onToolTip && isDescendantOfGraphPane) {
                    graphPane.setSelections(new LocList());
                    graphPane.setSelectedArrow(null);
                    getMolPane().clearSelectedSites();
                }

            } else if (dragging) {
                handleDragging(me);
            } else if (multiLeftSelect) {
                if (src instanceof Arrow) {
                    Arrow arrow = (Arrow) src;
                    graphPane.setSelection(arrow, arrow.getLoc());
                }
            } else if (shiftLeftSelect) {
            } else if (singleLeftPressed && !popupDescendant && !onToolTip) {
                graphPane.setSelections(new LocList());
            } else if (rightClick) {
                handleRightClick(me);
            } else if (dragCompleted) {
                handleDragComplete(me);
            } else if (moving) {
                graphPane.getGraphPanel().positionMovingIndicator(locOnScreen);
            }
        }

        private void handleDragging(MouseEvent me) {
            updateSelection(me);
            //graphPane.getGraphPanel().positionMovingIndicator(locOnScreen);            
        }

        private void reset() {
            singleLeftPressed = false;
            singleLeft = false;
            shiftLeftSelect = false;
            rightClick = false;
        }

        private void handleDragComplete(MouseEvent me) {
            updateSelection(me);

            dragCompleted = false;
            ptPressedOnScreen = null;
            //dragEnd = null;
        }

        private void updateSelection(MouseEvent me) {
            if (ptPressedOnScreen == null) {
                return;
            }
            Point dragEnd = me.getLocationOnScreen();
            GraphPanel graphPanel = graphPane.getGraphPanel();
            BrickComp brickComp = graphPanel.getBrickCompsMap().getPrimaryBrickComp();
            Point _dragStart = UIUtil.convertPointFromScreen(ptPressedOnScreen, brickComp);

            SwingUtilities.convertPointFromScreen(dragEnd, brickComp);

            _dragStart.x = Math.max(_dragStart.x, 0);
            dragEnd.x = Math.max(dragEnd.x, 0);

            final int caretPosStart = graphPanel.getCaretPos(_dragStart.x);
            final int caretPosEnd = graphPanel.getCaretPos(dragEnd.x);
            final int startPos = Math.min(caretPosStart, caretPosEnd);
            Integer endPos;
            if (caretPosStart == caretPosEnd) {
                endPos = startPos;
            } else {
                endPos = Math.max(caretPosStart, caretPosEnd) - 1;
            }
            endPos = Math.min(endPos, graphPane.getTotalPos());

            final Loc loc = new Loc(startPos, endPos);
            graphPane.setSelections(new LocList(loc));
        }

        private void analyzeEvent(AWTEvent event) {
            reset();
            if (event instanceof MouseEvent) {

                me = (MouseEvent) event;
                int modifier = me.getModifiersEx();

                isWithinViewport = UIUtil.isWithin(graphPane.getScrollPane().getViewport(), me.getLocationOnScreen());

                if (isWithinViewport == null || !isWithinViewport) {
                    return;
                }

                if (modifier == CTRL_LEFT_CLICK) {
                    multiLeftSelect = true;
                } else if (modifier == InputEvent.BUTTON1_DOWN_MASK) {
                    if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                        singleLeftPressed = true;
                    }
                    singleLeft = true;
                } else if (modifier == InputEvent.BUTTON1_MASK) {
                    singleLeft = true;
                } else if (modifier == InputEventUtil.getShiftLeftClickMask()) {
                    //logger.info("continuousSelect click...");
                    shiftLeftSelect = true;
                } else if (SwingUtilities.isRightMouseButton(me) && me.getID() == MouseEvent.MOUSE_RELEASED) {
                    //logger.info("SwingUtilities.isRightMouseButton()");
                    rightClick = true;
                }

                locOnScreen = me.getLocationOnScreen();
                if (me.getID() == MouseEvent.MOUSE_PRESSED) {
                    ptPressedOnScreen = me.getLocationOnScreen();
                } else if (me.getID() == MouseEvent.MOUSE_ENTERED) {
                } else if (me.getID() == MouseEvent.MOUSE_RELEASED) {
                    ptPressedOnScreen = null;
                    if (dragging) {
                        dragCompleted = true;
                        dragging = false;
                    }
                } else if (me.getID() == MouseEvent.MOUSE_DRAGGED && singleLeft) {
                    dragging = true;
                }

                moving = me.getID() == MouseEvent.MOUSE_MOVED;
            }
        }

        void handleRightClick(MouseEvent me) {
            popupMenu = getPopupMenu(me);
            updatePopupMenuEnablement(me);
            MolPane molPane = getMolPane();
            Point p = me.getLocationOnScreen();

            SwingUtilities.convertPointFromScreen(p, molPane);
            UIUtil.showPopupMenu(popupMenu, molPane, p.x, p.y);
        }

        private MolPane getMolPane() {
            if (molPaneRef == null || molPaneRef.get() == null) {
                MolPane molPane = UIUtil.getParent(graphPane, MolPane.class);
                molPaneRef = new WeakReference<MolPane>(molPane);
            }
            return molPaneRef.get();
        }

        MolPanePopup getPopupMenu(MouseEvent me) {
            if (popupMenu == null) {
                popupMenu = new MolPanePopup(getMolPane());
            }
            return popupMenu;
        }

        private void updatePopupMenuEnablement(MouseEvent me) {
            RMap.EntryList selectedEntries = getMolPane().getSelectedSites();
            Object src = me.getSource();
            Arrow arrow = null;
            boolean isArrow = src instanceof Arrow;
            if (isArrow) {
                arrow = (Arrow) src;
            }

            Feture feture = null;
            if (isArrow) {
                feture = (Feture) arrow.getData();
            }
            popupMenu.setRen(selectedEntries);

            popupMenu.setOligo(getMolPane().getAs().isOligo());
            popupMenu.setFeture(feture);
            popupMenu.updateEnablementBasedOnSelection();
            final StringList names = ExplorerTC.getInstance().getSelectedFolder().getElementNames();
            popupMenu.setFolderElementNames(names);
        }
    }
}
