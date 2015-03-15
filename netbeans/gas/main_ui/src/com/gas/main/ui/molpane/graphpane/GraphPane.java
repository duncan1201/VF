/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.common.ui.ruler.Ruler;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.misc.Filler;
import com.gas.common.ui.ruler.IRulerParent;
import com.gas.common.ui.ruler.RulerLoc;
import com.gas.common.ui.ruler.RulerLocList;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.ui.pref.ColorPref;
import com.gas.common.ui.util.Pref;
import com.gas.domain.ui.brickComp.BrickComp;
import com.gas.domain.ui.shape.Arrow;
import com.gas.main.ui.minimap.Minimap;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.lang.ref.WeakReference;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author dunqiang
 */
public class GraphPane extends JPanel implements IReclaimable, IRulerParent {

    private WeakReference<Ruler> rulerRef;
    private Float rulerFontSize;
    private WeakReference<JScrollPane> scrollPaneRef;
    private Minimap minimap;
    private WeakReference<AnnotatedSeq> asRef;
    private WeakReference<GraphPanel> graphPanelRef;
    private Feture selectedFeture;
    private Map<String, Boolean> trackVisibilityMap = new HashMap<String, Boolean>();
    private final Map<String, Boolean> labelVisibilityMap = new HashMap<String, Boolean>();
    private static Insets padding;
    private Loc centerLoc;
    private LocList selections = new LocList();
    private Arrow selectedArrow;
    protected boolean scrollValueAdjusting;
    private Boolean minimapShown;
    private Boolean baseNumberShown;
    private Float baseSize;
    private Float annotationLabelSize;
    private Set<TranslationResult> translationResults = new HashSet<TranslationResult>();
    private IColorProvider translationColorProvider;
    private IColorProvider primarySeqColorProvider;
    GraphPaneListeners.ColorPreferenceListener colorPrefListener;
    GraphPaneListeners.AWTMouseListener awtMouseListener;
    GraphPaneListeners.LabelVisiblePrefListener labelVisibleListener;

    public GraphPane() {
        LayoutManager layout = new BorderLayout(0, 2);
        setLayout(layout);
        setOpaque(true);
        setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane();
        scrollPaneRef = new WeakReference<JScrollPane>(scrollPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new Filler(Color.WHITE));
        GraphPanel graphPanel = new GraphPanel();
        graphPanelRef = new WeakReference<GraphPanel>(graphPanel);
        scrollPane.setViewportView(graphPanel);
        UIUtil.disableScrolling(scrollPane);
        minimap = new Minimap();

        add(minimap, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);


        final Ruler ruler = getRuler();
        Insets insets = GraphPane.getPadding();
        ruler.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));

        getScrollPane().setColumnHeaderView(ruler);

        final RowHeaderView rowHeader = new RowHeaderView();
        getScrollPane().setRowHeaderView(rowHeader);
        rowHeader.setPreferredWidth(80);

        hookupListeners();
    }

    @Override
    public int[] getHorizontalBounds() {
        BrickComp brickComp = getGraphPanel().getBrickCompsMap().getPrimaryBrickComp();
        Rectangle bounds = UIUtil.getBoundsNoBorder(brickComp);
        //Rectangle bounds = brickComp.getBounds();
        int[] ret = new int[2];
        ret[0] = bounds.x;
        ret[1] = bounds.x + bounds.width - 1;
        return ret;
    }

    public IColorProvider getPrimarySeqColorProvider() {
        return primarySeqColorProvider;
    }

    public void setPrimarySeqColorProvider(IColorProvider primarySeqColorProvider) {
        IColorProvider old = this.primarySeqColorProvider;
        this.primarySeqColorProvider = primarySeqColorProvider;
        firePropertyChange("primarySeqColorProvider", old, this.primarySeqColorProvider);
    }

    public IColorProvider getTranslationColorProvider() {
        return translationColorProvider;
    }

    public void setTranslationColorProvider(IColorProvider translationColorProvider) {
        IColorProvider old = this.translationColorProvider;
        this.translationColorProvider = translationColorProvider;
        firePropertyChange("translationColorProvider", old, this.translationColorProvider);
    }

    public Float getAnnotationLabelSize() {
        if (annotationLabelSize == null) {
            annotationLabelSize = Pref.CommonPtyPrefs.getInstance().getAnnotationLabelSize();
        }
        return annotationLabelSize;
    }

    public void setAnnotationLabelSize(Float annotationLabelSize) {
        Float old = getAnnotationLabelSize();
        this.annotationLabelSize = annotationLabelSize;
        firePropertyChange("annotationLabelSize", old, this.annotationLabelSize);
    }

    public Float getRulerFontSize() {
        if (rulerFontSize == null) {
            rulerFontSize = Pref.CommonPtyPrefs.getInstance().getRulerFontSize();
        }
        return rulerFontSize;
    }

    public void setRulerFontSize(Float rulerFontSize) {
        Float old = this.rulerFontSize;
        this.rulerFontSize = rulerFontSize;
        firePropertyChange("rulerFontSize", old, this.rulerFontSize);
    }

    public boolean isBaseNumberShown() {
        return baseNumberShown;
    }

    public void setBaseNumberShown(Boolean baseNumberShown) {
        Boolean old = this.baseNumberShown;
        this.baseNumberShown = baseNumberShown;
        firePropertyChange("baseNumberShown", old, this.baseNumberShown);
    }

    public Boolean isMinimapShown() {
        return minimapShown;
    }

    public void setMinimapShown(Boolean minimapShown) {
        Boolean old = this.minimapShown;
        this.minimapShown = minimapShown;
        firePropertyChange("minimapShown", old, this.minimapShown);
    }

    public void setTranslationResults(Set<TranslationResult> translationResults) {
        GraphPanel graphPanel = getGraphPanel();

        graphPanel.reinitBrickTracksMap();

        reinitLabelVisibilityMap();
        reinitTrackVisibilityMap();

        graphPanel.invalidate();
        graphPanel.validate();
    }

    public void refresh() {
        if (getAs() == null) {
            return;
        }
        reinitTrackVisibilityMap();
        reinitLabelVisibilityMap();
        getMinimap().refresh();
        getGraphPanel().refresh();
        RulerLocList locList = new RulerLocList(new RulerLoc(1, getAs().getLength()));
        locList.addAll(getAs().getParentLocSet().toRulerLocList());
        getColumnHeaderView().setLocList(locList);

        revalidate();
    }

    public Minimap getMinimap() {
        return minimap;
    }

    public final JScrollPane getScrollPane() {
        return scrollPaneRef.get();
    }

    @Override
    public void cleanup() {
        ColorPref.getInstance().removePropertyChangeListener(colorPrefListener);
        Pref.LabelVisiblePref.getInstance().getPropertyChangeSupport().removePropertyChangeListener(labelVisibleListener);
        Toolkit.getDefaultToolkit().removeAWTEventListener(awtMouseListener);
    }

    private void hookupListeners() {
        getScrollPane().getHorizontalScrollBar().getModel().addChangeListener(new GraphPaneListeners.ScrollPaneHorizontalRangeModelListener(this));

        minimap.addPropertyChangeListener(new GraphPaneListeners.MinimapListener(this));

        colorPrefListener = new GraphPaneListeners.ColorPreferenceListener(this);
        ColorPref.getInstance().addPropertyChangeListener(colorPrefListener);

        labelVisibleListener = new GraphPaneListeners.LabelVisiblePrefListener(this);
        Pref.LabelVisiblePref.getInstance().getPropertyChangeSupport().addPropertyChangeListener(labelVisibleListener);

        awtMouseListener = new GraphPaneListeners.AWTMouseListener(this);
        Toolkit.getDefaultToolkit().addAWTEventListener(awtMouseListener, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

        addPropertyChangeListener(new GraphPaneListeners.PtyListener(this));

    }

    protected void setSelection(Arrow arrow, Loc loc) {
        setSelectedArrow(arrow);

        centerLoc = loc;

        setSelections(new LocList(loc));
    }

    public void setSelections(LocList locList) {
        LocList old = this.selections;
        this.selections = locList;
        firePropertyChange("selections", old, this.selections);
    }

    public void setSelectedFeture(Feture feture) {
        Feture old = this.selectedFeture;
        this.selectedFeture = feture;
        firePropertyChange("selectedFeture", old, this.selectedFeture);
    }

    public Feture getSelectedFeture() {
        return selectedFeture;
    }

    public Float getBaseSize() {
        if (baseSize == null) {
            baseSize = Pref.CommonPtyPrefs.getInstance().getBaseFontSize();
        }
        return baseSize;
    }

    public void setBaseSize(Float baseSize) {
        Float old = getBaseSize();
        this.baseSize = baseSize;
        firePropertyChange("baseSize", old, this.baseSize);
    }

    public LocList getSelections() {
        return selections;
    }

    public Arrow getSelectedArrow() {
        return selectedArrow;
    }

    public void setSelectedArrow(Arrow arrow) {
        Arrow old = this.selectedArrow;
        this.selectedArrow = arrow;
        firePropertyChange("selectedArrow", old, this.selectedArrow);
    }

    public void center(Loc loc) {
        JScrollPane pane = getScrollPane();
        JViewport viewport = pane.getViewport();

        Integer totalPos = getTotalPos();
        if (totalPos == null) {
            return;
        }

        Dimension size = pane.getViewport().getSize();

        int preferredWidth = getGraphPanel().getPreferredSize().width;
        int totalWidth2 = preferredWidth - getPadding().left - getPadding().right;

        if (totalWidth2 < 0) {
            return;
        }

        int startPixel = UIUtil.toScreenWidth(totalWidth2, totalPos, loc.getStart());
        int endPixel = UIUtil.toScreenWidth(totalWidth2, totalPos, loc.getEnd());
        int pixel = UIUtil.toScreenWidth(totalWidth2, totalPos, loc.center());
        pixel += getPadding().left;

        int toStart = pixel - size.width / 2;
        toStart = Math.max(0, toStart);

        if (toStart >= 0) {
            Point point = viewport.getViewPosition();
            int from = point.x;
            Loc viewPortLoc = new Loc(from, from + viewport.getSize().width);
            point.x = toStart;

            if (viewPortLoc.isOverlapped(new Loc(startPixel, endPixel))) {
                viewport.setViewPosition(point);
            } else {
                viewport.setViewPosition(point);
            }
        }
    }

    protected Integer getTotalPos() {
        Integer ret = null;
        if (getAs() != null) {
            ret = getAs().getLength();
        }
        return ret;
    }

    Ruler getRuler() {
        if (rulerRef == null || rulerRef.get() == null) {
            Ruler ruler = new Ruler();
            rulerRef = new WeakReference<Ruler>(ruler);
        }
        return rulerRef.get();
    }

    public static Insets getPadding() {
        if (padding == null) {
            final FontMetrics fm = FontUtil.getDefaultFontMetrics();
            final int charWidth = fm.charWidth('W');
            padding = new Insets(0, Math.round(charWidth * 0.8f), 0, Math.round(charWidth * 1.7f));
        }
        return padding;
    }

    public Ruler getColumnHeaderView() {
        Ruler ret = (Ruler) getScrollPane().getColumnHeader().getView();
        return ret;
    }

    private Float calculateZoom() {
        GraphPanel panel = UIUtil.getChild(this, GraphPanel.class);

        int fullWidth = panel.getFullWidth();
        int visibleWidth = panel.getSize().width;
        float zoom = visibleWidth * 1.0f / fullWidth;
        return zoom;
    }

    public float zoomIn() {
        GraphPanel panel = UIUtil.getChild(this, GraphPanel.class);

        Float zoom = null;
        int totalPos = getTotalPos();
        int posWidth;
        if (selectedArrow != null) {
            Lucation luc = selectedArrow.getLuc();
            posWidth = luc.width();
            int viewportWidth = getScrollPane().getViewport().getSize().width;
            int expectedPanelWidth = MathUtil.round(viewportWidth * 0.5 * totalPos / posWidth);
            int fullWidth = panel.getFullWidth();
            zoom = 1.0f * expectedPanelWidth / fullWidth;
        } else {
            zoom = calculateZoom();
            zoom = zoom * 2;
        }
        return zoom;
    }

    protected Loc getCenterLoc() {
        return centerLoc;
    }

    public void setCenterLoc(Loc loc) {
        Loc old = this.centerLoc;
        this.centerLoc = loc;
        firePropertyChange("centerLoc", old, this.centerLoc);
    }

    public float fitZoom() {
        float ret = 0;

        return ret;
    }

    public RowHeaderView getRowHeaderView() {
        RowHeaderView ret = (RowHeaderView) getScrollPane().getRowHeader().getView();
        return ret;
    }

    public AnnotatedSeq getAs() {
        if (asRef == null) {
            return null;
        } else {
            return asRef.get();
        }
    }

    public void setAs(AnnotatedSeq as) {
        asRef = new WeakReference<AnnotatedSeq>(as);
        firePropertyChange("as", null, getAs());
    }

    public GraphPanel getGraphPanel() {
        return graphPanelRef.get();
    }

    public Map<String, Boolean> getTrackVisibilityMap() {
        return trackVisibilityMap;
    }

    public void setTrackVisible(String name, boolean visible) {
        if (getAs() == null) {
            return;
        }
        if (trackVisibilityMap.containsKey(name.toUpperCase(Locale.ENGLISH))) {
            trackVisibilityMap.put(name.toUpperCase(Locale.ENGLISH), visible);
        } else {
            throw new IllegalArgumentException("\"" + name.toUpperCase(Locale.ENGLISH) + "\"" + " not found!");
        }
        getMinimap().refresh();
        getGraphPanel().revalidate();

    }

    public void setLableVisible(String name, boolean visible) {
        if (getAs() == null) {
            return;
        }
        if (labelVisibilityMap.containsKey(name.toUpperCase(Locale.ENGLISH))) {
            labelVisibilityMap.put(name.toUpperCase(Locale.ENGLISH), visible);
        } else {
            throw new IllegalArgumentException("\"" + name + "\"" + " not found!");
        }

        getGraphPanel().getFtrTrack(name.toUpperCase(Locale.ENGLISH)).invalidate();
        getGraphPanel().revalidate();

    }

    public Map<String, Boolean> getLabelVisibilityMap() {
        if (labelVisibilityMap.isEmpty()) {
            reinitLabelVisibilityMap();
        }
        return labelVisibilityMap;
    }

    protected void reinitLabelVisibilityMap() {
        labelVisibilityMap.clear();
        Iterator<Feture> fetureItr = AsHelper.getAllFetures(getAs(), true).iterator();
        while (fetureItr.hasNext()) {
            Feture feture = fetureItr.next();
            String key = feture.getKey().toUpperCase(Locale.ENGLISH);
            boolean visible = Pref.LabelVisiblePref.getInstance().isVisible(key);
            labelVisibilityMap.put(key.toUpperCase(Locale.ENGLISH), visible);
        }
    }

    protected void reinitTrackVisibilityMap() {
        trackVisibilityMap.clear();
        Iterator<Feture> fetureItr = AsHelper.getAllFetures(getAs(), true).iterator();
        while (fetureItr.hasNext()) {
            Feture feture = fetureItr.next();
            String key = feture.getKey();
            boolean visible = getAs().getAsPref().isTrackVisible(key.toUpperCase(Locale.ENGLISH));
            trackVisibilityMap.put(key.toUpperCase(Locale.ENGLISH), visible);
        }
        Iterator<String> itr = getGraphPanel().getBrickTrackNames().iterator();
        while (itr.hasNext()) {
            trackVisibilityMap.put(itr.next().toUpperCase(Locale.ENGLISH), Boolean.TRUE);
        }
    }
}
