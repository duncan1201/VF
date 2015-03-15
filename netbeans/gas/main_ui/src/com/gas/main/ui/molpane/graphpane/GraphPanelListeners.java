/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.MathUtil;
import com.gas.domain.ui.brickComp.BrickComp;
import com.gas.common.ui.ruler.Ruler;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.gc.api.GCResult;
import com.gas.domain.core.ren.RMap;
import com.gas.common.ui.util.Pref;
import com.gas.domain.ui.shape.Arrow;
import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.statusline.StatusLinePanel;
import com.gas.common.ui.util.LocUtil;
import com.gas.main.ui.molpane.MolPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class GraphPanelListeners {

    static class ResizeListener extends ComponentAdapter {

        private GraphPane graphPane;
        private Dimension oldSize;
        private Dimension size = new Dimension();

        public ResizeListener() {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            GraphPanel src = (GraphPanel) e.getSource();
            if (graphPane == null) {
                graphPane = UIUtil.getParent(src, GraphPane.class);
            }
            this.oldSize = this.size;
            src.getSize(this.size);
            src.getLayeredPane().setBounds(0, 0, size.width, size.height);
            if (this.size != null && this.size.width > 0) {
                Ruler ruler = graphPane.getColumnHeaderView();
                if(ruler != null){                  
                    UIUtil.setPreferredWidth(ruler, size.width);
                    ruler.revalidate();
                    ruler.repaint();                    
                }
            }
            UIUtil.setHeight(src.getMovingIndicator(), this.size.height);
            if (this.oldSize.height != this.size.height) {
                graphPane.getRowHeaderView().revalidate();
            }
        }
    }

    static class LayoutPtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final String name = evt.getPropertyName();
            GraphPanelLayout src = (GraphPanelLayout) evt.getSource();
            Object nV = evt.getNewValue();
            if (name.equals("state")) {
                CNST.LAYOUT state = (CNST.LAYOUT) nV;
                if (state == CNST.LAYOUT.ENDING) {
                    RowHeaderView rowHeaderView = src.getGraphPane().getRowHeaderView();
                    UIUtil.doLayout(rowHeaderView);
                    JCaret caret = src.getGraphPanel().getCaret();
                    if (caret.getPos() != null) {
                        Point pt = src.getGraphPanel().getCaretLocationByPos(caret.getPos());
                        if (pt != null) {
                            caret.setLocation(pt);
                        }
                    } else {
                        caret.setPos(new Point(1, 1));
                    }
                }
            }
        }
    }

    static class StatusLineUpdater implements PropertyChangeListener {

        WeakReference<MolPane> molPaneRef;
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            GraphPanel src = (GraphPanel)evt.getSource();
            final String name = evt.getPropertyName();

            StatusLinePanel statusLinePanel = StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement();
            Object v = evt.getNewValue();
            if (name.equals("selection")) {
                final Loc2D s = (Loc2D) v;
                if (s != null) {
                    statusLinePanel.setStart(s.x1);
                    statusLinePanel.setEnd(s.x2);
                    AnnotatedSeq as = getMolPane(src).getAs();
                    if(as.isCircular()){
                        int width = LocUtil.width(s.x1, s.x2, as.getLength()).intValue();
                        statusLinePanel.setSelectLength(width);
                    }else{
                        statusLinePanel.setSelectLength(s.x2 - s.x1 + 1);
                    }
                } else {
                    statusLinePanel.clearSelection();
                }
            }

        }

        private MolPane getMolPane(GraphPanel panel) {
            if(molPaneRef == null || molPaneRef.get() == null){
                MolPane molPane = UIUtil.getParent(panel, MolPane.class);
                molPaneRef = new WeakReference<MolPane>(molPane);
            }
            return molPaneRef.get();
        }
    }

    static class PtyListener implements PropertyChangeListener {

        private GraphPane graphPane;
        private GraphPanelLayout layout = null;

        public PtyListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            GraphPanel src = (GraphPanel) evt.getSource();
            String name = evt.getPropertyName();
            Object nV = evt.getNewValue();
            if (layout == null) {
                layout = (GraphPanelLayout) src.getLayout();
            }

            if (graphPane == null) {
                graphPane = UIUtil.getParent(src, GraphPane.class);
            }
            if (name.equals("as")) {
                final AnnotatedSeq as = src.getAs();
                float zoom = as.getAsPref().getZoom();
                src.removeInLayers(GraphPanel.DEFAULT_LAYER, GraphPanel.OVERLAY_LAYER, GraphPanel.TRIANGLE_LAYER);
                src.featureArrowMap.clear();
                src.trackMap.clear();
                src.brickCompsMap.clear();
                src.ftrTracksMap.clear();
                
                Integer fullWidth = src.getFullWidth();

                int newPreferredWidth = Math.max(src.getVisibleRect().width, MathUtil.round(fullWidth * zoom));
                UIUtil.setPreferredWidth(src, newPreferredWidth);

                layout.setStart(1);
                layout.setEnd(src.getAs().getLength());
                
                if (src.getAs().getRmap() != null) {
                    src.getResPanel().refresh();
                }
                UIUtil.setHeight(src.getCaret(), src.getCaretHeight());

                BrickComp brickComp = src.getBrickCompsMap().getPrimaryBrickComp();
                src.getCaret().setLocation(GraphPane.getPadding().left, brickComp.getLocation().y);

                if (as.getGcResult() != null) {
                    src.setGCResult(as.getGcResult());
                }

                src.initUI();
                src.revalidate();
            } else if (name.equals("selectedArrow")) {
                Arrow selectedArrow = (Arrow) nV;
                src.getFtrTracksMap().setSelectedArrow(selectedArrow);
                if (selectedArrow != null) {
                    Loc loc = selectedArrow.getLoc();
                    src.setSelection(new Loc2D(loc));
                }
            } else if (name.equals("selectedFeture")) {
                src.getFtrTracksMap().setSelectedFeture(src.getSelectedFeture());
            } else if (name.equals("selection")) {
                Loc2D loc2d = (Loc2D) nV;
                layout.layoutOverlays(src);
                BrickComp brickComp = src.getBrickCompsMap().getPrimaryBrickComp();
                brickComp.setSelection2D(loc2d);
                if(loc2d == null){
                    src.setSelectedFeture(null);
                }
            } else if (name.equals("doubleStranded")) {
                BrickComp sbp = src.getBrickCompsMap().getPrimaryBrickComp();
                if (sbp != null) {
                    Boolean doubleLine = (Boolean) evt.getNewValue();
                    sbp.setDoubleLine(doubleLine);
                    UIUtil.setHeight(src.getCaret(), src.getCaretHeight());
                    src.revalidate();
                }
            } else if (name.equals("isEditingAllowed")) {
                src.getCaret().setState(src.isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
            } else if (name.equals("gcResult")) {
                if (nV == null) {
                    src.getLinePlotCompMap().getGC().clear();
                } else {
                    GCResult gcResult = (GCResult) nV;
                    if (!gcResult.isEmpty()) {
                        src.getLinePlotCompMap().getGC().addPlot("GC", gcResult.getPaddedContents(), Color.BLUE, true);
                    }
                }
                src.revalidate();
            }
        }
    }
}