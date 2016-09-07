/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.main.ui.molpane.graphpane.ftrtrack.FtrTrack;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.jcomp.Overlay;
import com.gas.common.ui.linePlot.LinePlotComp;
import com.gas.common.ui.linePlot.LinePlotCompMap;
import com.gas.common.ui.util.MathUtil;
import com.gas.domain.ui.brickComp.BrickComp;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.domain.ui.triangle.Triangle;
import com.gas.domain.ui.triangle.TriangleMap;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.AlternateColorFactory;
import com.gas.common.ui.util.Pref;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import static java.awt.Toolkit.getDefaultToolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author dunqiang
 */
public class GraphPanelLayout implements LayoutManager {

    private CNST.LAYOUT state;
    private Integer start;
    private Integer end;
    // for internal use
    private PropertyChangeSupport propertyChangeSupport;
    private GraphPane graphPane;
    private GraphPanel graphPanel;
    AlternateColorFactory colorFactory = new AlternateColorFactory(new Color(237, 243, 254), new Color(248, 248, 248));

    GraphPanelLayout() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    void setState(CNST.LAYOUT state) {
        CNST.LAYOUT old = this.state;
        this.state = state;
        propertyChangeSupport.firePropertyChange("state", old, this.state);
    }

    void setEnd(Integer end) {
        this.end = end;
    }

    void setStart(Integer start) {
        this.start = start;
    }

    Integer getEnd() {
        return end;
    }

    Integer getStart() {
        return start;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension ret = new Dimension();
        return ret;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension ret = new Dimension();
        return ret;
    }

    private boolean validate(Container parent) {
        boolean ret = true;
        if (start == null) {
            ret = false;
        } else if (end == null) {
            ret = false;
        } else if (parent.getSize().height < 0 || parent.getSize().width < 0) {
            ret = false;
        } else {
            GraphPanel graphPanel = (GraphPanel) parent;
            ret = graphPanel.isInitUIDone();
        }
        return ret;
    }

    private int _layoutFeatures(Container parent, int y) {
        GraphPanel graphPanel = (GraphPanel) parent;
        Insets insets = parent.getInsets();

        Map<String, JComponent> trackMap = graphPanel.getTrackMap();

        GraphPane graphPane = UIUtil.getParent((JComponent) parent, GraphPane.class);

        Map<String, Boolean> trackVisibleMap = graphPane.getTrackVisibilityMap();

        Rectangle drawingRect = UIUtil.getBoundsNoBorder(parent);

        Dimension size = drawingRect.getSize();

        Map<String, FtrTrack> ftrTrackMap = graphPanel.getFtrTracksMap();

        Iterator<String> itr = ftrTrackMap.keySet().iterator();

        while (itr.hasNext()) {
            String key = itr.next().toUpperCase(Locale.ENGLISH);
            final FtrTrack track = ftrTrackMap.get(key);

            Boolean visible = trackVisibleMap.get(key.toUpperCase(Locale.ENGLISH));

            if (visible) {
                Color color = colorFactory.getColor();

                track.setBackground(color);
                if (!graphPanel.getLayeredPane().isAncestorOf(track)) {
                    graphPanel.getLayeredPane().add(track);
                    graphPanel.getLayeredPane().setLayer(track, JLayeredPane.DEFAULT_LAYER);
                }
            }

            UIUtil.setPreferredWidth(track, size.width);

            if (visible) {
                Dimension pSize = track.getPreferredSize();
                if (pSize.height == 0 || !track.isValid()) {

                    track.setBounds(insets.left, y, size.width - insets.left - insets.right, pSize.height);
                    track.invalidate();
                    track.layoutSelf();
                    pSize = track.getPreferredSize();
                }
                track.setBounds(insets.left, y, size.width - insets.left - insets.right, pSize.height);

                y = y + pSize.height;
            } else {
                track.setBounds(0, 0, 0, 0);
            }

            trackMap.put(key, track);
        }

        return y;
    }

    private void updatePreferredSize(Container parent, int newPreferredHeight) {
        GraphPane graphPane = UIUtil.getParent((JComponent) parent, GraphPane.class);

        UIUtil.setPreferredHeight((JComponent) parent, newPreferredHeight);
        UIUtil.setPreferredHeight(graphPane.getRowHeaderView(), newPreferredHeight);
    }

    private int _layoutLinePlotCompMap(Container parent, int y) {
        y += UIUtil.getDefaultInsets().top;
        GraphPanel graphPanel = (GraphPanel) parent;
        final Dimension size = graphPanel.getSize();
        graphPanel.reinitLinePlotCompMap();
        LinePlotCompMap linePlotCompMap = graphPanel.getLinePlotCompMap();
        Iterator<String> itr = linePlotCompMap.keySet().iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            LinePlotComp linePlotComp = linePlotCompMap.get(str);
            if (!graphPanel.getLayeredPane().isAncestorOf(linePlotComp)) {
                graphPanel.getLayeredPane().add(linePlotComp);
                graphPanel.getLayeredPane().setLayer(linePlotComp, GraphPanel.PLOT_LAYER);
            }
            int height = 100;
            if (str.equals(LinePlotCompMap.GC)) {
                height = Pref.CommonPtyPrefs.getInstance().getGCHeight();
            }
            if (linePlotComp.isEmpty()) {
                linePlotComp.setSize(0, 0);
            } else {
                final Insets insets = GraphPane.getPadding();
                linePlotComp.setBackground(colorFactory.getColor());
                linePlotComp.setBounds(insets.left, y, size.width - insets.left - insets.right, height);
            }
            y += height;
        }

        return y;
    }

    private int _layoutBrickTracks(Container parent, int y) {
        GraphPanel graphPanel = (GraphPanel) parent;
        GraphPane graphPane = UIUtil.getParent(graphPanel, GraphPane.class);
        Map<String, JComponent> trackMap = graphPanel.getTrackMap();
        Map<String, BrickComp> bPanelMap = graphPanel.getBrickCompsMap();
        if (bPanelMap.isEmpty()) {
            return y;
        }

        int width = graphPanel.getSize().width;

        BrickComp seqPanel = graphPanel.getBrickCompsMap().getPrimaryBrickComp();
        int preferredHeight = seqPanel.getDesiredHeight();
        seqPanel.setBounds(0, y, width, preferredHeight);
        float unitWidth = seqPanel.getUnitWidth();
        int dnaCount = seqPanel.getBrickCount();

        Iterator<String> nameItr = bPanelMap.keySet().iterator();
        while (nameItr.hasNext()) {
            String name = nameItr.next();
            BrickComp brickComp = bPanelMap.get(name);

            if (name.equals(BrickComp.SEQ)) {
                UIUtil.setPreferredWidth(brickComp, width);
                preferredHeight = brickComp.getDesiredHeight();
                brickComp.setBounds(0, y, width, preferredHeight);
                y += preferredHeight;
            } else {
                String frameStr = name.split(" ")[1];
                int frame = Integer.parseInt(frameStr);
                int startOffset = 0;
                int endOffset = 0;

                startOffset = MathUtil.round((Math.abs(frame) - 1) * unitWidth);
                int dnaUntranslatedCount = (dnaCount - (Math.abs(frame) - 1)) % 3;
                endOffset = MathUtil.round(unitWidth * dnaUntranslatedCount);

                if (frame < 0) {
                    int tmp = startOffset;
                    startOffset = endOffset;
                    endOffset = tmp;
                }

                UIUtil.setPreferredWidth(brickComp, width - startOffset - endOffset);
                preferredHeight = brickComp.getDesiredHeight();
                brickComp.setBounds(startOffset + 1, y, width - startOffset - endOffset, preferredHeight);
                y += preferredHeight;
            }

            if (!name.equals(BrickComp.SEQ) && graphPane.getTranslationColorProvider() != null) {
                brickComp.setColorProvider(graphPane.getTranslationColorProvider());
            }
            if (!graphPanel.getLayeredPane().isAncestorOf(brickComp)) {
                graphPanel.getLayeredPane().add(brickComp);
                graphPanel.getLayeredPane().setLayer(brickComp, GraphPanel.DEFAULT_LAYER);
            }
            trackMap.put(name.toUpperCase(Locale.ENGLISH), brickComp);
        }
        return y;
    }

    @Override
    public void layoutContainer(Container parent) {
        if (!validate(parent)) {
            return;
        }
        int y = parent.getInsets().top;

        if (graphPanel == null) {
            graphPanel = (GraphPanel) parent;
        }

        if (graphPanel.getSize().width <= 0 || graphPanel.getAs() == null) {
            return;
        }
        if (graphPane == null || graphPane == null) {
            graphPane = UIUtil.getParent((JComponent) parent, GraphPane.class);
        }
        if(graphPane.getScrollPane().getViewport().getSize().width > graphPanel.getPreferredSize().width){
            UIUtil.setPreferredWidth(graphPanel, graphPane.getScrollPane().getViewport().getSize().width);
            graphPanel.revalidate();
            return;
        }
        
        setState(CNST.LAYOUT.STARTED);

        graphPanel.trackMap.clear();

        colorFactory.resetCounter();

        y = _layoutResPanel(parent, y);

        y = _layoutBrickTracks(parent, y);

        _layoutTriangles(parent);


        y = _layoutLinePlotCompMap(parent, y);

        y = _layoutFeatures(parent, y);

        layoutOverlays((GraphPanel) parent);

        updatePreferredSize(parent, y);

        setState(CNST.LAYOUT.ENDING);
    }

    protected GraphPanel getGraphPanel() {
        if (graphPanel != null) {
            return graphPanel;
        } else {
            return null;
        }
    }

    protected GraphPane getGraphPane() {
        if (graphPane != null) {
            return graphPane;
        } else {
            return null;
        }
    }

    private int _layoutResPanel(Container parent, int y) {
        GraphPanel graphPanel = (GraphPanel) parent;
        ResPanel resPanel = graphPanel.getResPanel();
        final Insets insets = GraphPane.getPadding();
        if (!graphPanel.getLayeredPane().isAncestorOf(resPanel)) {
            graphPanel.getLayeredPane().add(resPanel);
            graphPanel.getLayeredPane().setLayer(resPanel, JLayeredPane.DEFAULT_LAYER);
            resPanel.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        }

        Dimension size = graphPanel.getSize();

        if (size.width <= 0) {
            return y;
        }

        UIUtil.setPreferredWidth(resPanel, size.width);
        resPanel.invalidate();
        resPanel.validate();

        Dimension pSize = resPanel.getPreferredSize();
        resPanel.setBounds(0, y, size.width, pSize.height);

        y += pSize.height;
        return y;
    }

    protected void layoutOverlays(GraphPanel graphPanel) {
        BrickComp sbp = graphPanel.getBrickCompsMap().getPrimaryBrickComp();
        final int y = sbp.getBounds().y;

        UIUtil.removeComponentsInLayer(graphPanel.getLayeredPane(), GraphPanel.OVERLAY_LAYER);
        graphPanel.repaint();
        Loc2D loc2d = graphPanel.getSelection();
        if (loc2d == null) {
            return;
        }
        final int totalPos = graphPanel.getAs().getLength();
        Loc selection = loc2d.toLoc();
        selection.setTotalPos(totalPos);

        final Insets insets = GraphPane.getPadding();
        int screenWidth = graphPanel.getSize().width - insets.left - insets.right;
        Loc whole = new Loc(1, totalPos, totalPos);

        LocList subtraction = whole.subtract(selection);
        Collections.sort(subtraction);

        ListIterator<Loc> itr = subtraction.listIterator();
        while (itr.hasNext()) {
            Overlay ol = new Overlay();
            ol.setBorderColor(Color.GRAY);
            if (!itr.hasNext()) {
                ol.setWestBorder(true);
            } else if (!itr.hasPrevious()) {
                ol.setEastBorder(true);
            } else {
                ol.setEastBorder(true);
                ol.setWestBorder(true);
            }
            Loc loc = itr.next();
            int startPos = loc.getStart();
            int startX = UIUtil.toScreen(totalPos, startPos - 1, screenWidth, insets.left, SwingConstants.RIGHT, Integer.class);

            int endPos = loc.getEnd();
            int endX = UIUtil.toScreen(totalPos, endPos, screenWidth, insets.left, SwingConstants.RIGHT, Integer.class);

            ol.setBounds(startX, y, endX - startX + 1, graphPanel.getSize().height - y);
            graphPanel.getLayeredPane().add(ol);
            graphPanel.getLayeredPane().setLayer(ol, GraphPanel.OVERLAY_LAYER);
        }
    }

    private void _layoutTriangles(Container parent) {
        GraphPanel graphPanel = (GraphPanel) parent;
        BrickComp simpleBrickPanel = graphPanel.getBrickCompsMap().getPrimaryBrickComp();
        final Rectangle rect = simpleBrickPanel.getBounds();
        float unitWidth = simpleBrickPanel.getUnitWidth();
        Insets insets = simpleBrickPanel.getInsets();
        if (rect.width <= 0 || rect.height <= 0) {
            return;
        }

        float triangleSize = Math.round(FontUtil.getDefaultFontMetrics().charWidth('W') * 1.0f);
        if (triangleSize % 2 == 0) {
            triangleSize++;
        }
        TriangleMap triangleMap = graphPanel.getTrianglesMap();
        Iterator<Triangle> itr = triangleMap.values().iterator();
        while (itr.hasNext()) {
            Triangle triangle = itr.next();
            int pos = triangle.getPos();
            if (triangle.isDown()) {
                triangle.setBounds(rect.x + insets.left + Math.round(pos * unitWidth - triangleSize * 0.5f), Math.round(rect.y - triangleSize), Math.round(triangleSize), Math.round(triangleSize));
            } else {
                triangle.setBounds(rect.x + insets.left + Math.round(pos * unitWidth - triangleSize * 0.5f), rect.y + rect.height, Math.round(triangleSize), Math.round(triangleSize));
            }
            graphPanel.getLayeredPane().add(triangle);
            graphPanel.getLayeredPane().setLayer(triangle, GraphPanel.TRIANGLE_LAYER);
        }
    }
}
