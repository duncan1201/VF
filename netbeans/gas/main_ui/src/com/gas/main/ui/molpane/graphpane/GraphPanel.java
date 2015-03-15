/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.IVisibleLocProvider;
import com.gas.common.ui.caret.CaretMoveEvent;
import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.caret.ICaretParentListener;
import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.linePlot.LinePlotCompMap;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.misc.PosIndicator;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.*;
import com.gas.domain.core.gc.api.GCResult;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.brickComp.BrickComp;
import com.gas.domain.ui.brickComp.BrickCompMap;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.TopBracket;
import com.gas.domain.ui.editor.TopBracketList;
import com.gas.domain.ui.shape.Arrow;
import com.gas.domain.ui.shape.IShapeList;
import com.gas.domain.ui.shape.IShapeListMap;
import com.gas.domain.ui.triangle.Triangle;
import com.gas.domain.ui.triangle.TriangleMap;
import com.gas.main.ui.actions.print.PrintAction;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.action.CopyBasesToClipboardAction;
import com.gas.main.ui.molpane.action.PasteBasesAction;
import com.gas.main.ui.molpane.action.SelectAllAction;
import com.gas.main.ui.molpane.graphpane.ftrtrack.FtrTrack;
import com.gas.main.ui.molpane.graphpane.ftrtrack.FtrTrackMap;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author dunqiang
 */
public class GraphPanel extends JPanel implements IVisibleLocProvider, Scrollable, IReclaimable, ICaretParent {

    private WeakReference<AnnotatedSeq> asRef;
    private Arrow selectedArrow;
    private Feture selectedFeture;
    private Loc2D selection;
    protected IShapeListMap featureArrowMap = new IShapeListMap();
    protected Map<String, JComponent> trackMap = new LinkedHashMap<String, JComponent>();
    protected BrickCompMap brickCompsMap = new BrickCompMap();
    private LinePlotCompMap linePlotCompMap = new LinePlotCompMap();
    protected FtrTrackMap ftrTracksMap = new FtrTrackMap();
    private TriangleMap trianglesMap = new TriangleMap();
    private Boolean doubleStranded;
    private GCResult gcResult;
    private WeakReference<ResPanel> resPanelRef;
    // for internal use    
    private WeakReference<JLayeredPane> layeredPaneRef;
    private GraphPanelLayout layout;
    private WeakReference<GraphPane> graphPaneRef;
    private WeakReference<MolPane> molPaneRef;
    protected static final int DEFAULT_LAYER = JLayeredPane.DEFAULT_LAYER;
    protected static final int PLOT_LAYER = JLayeredPane.DEFAULT_LAYER + 1;
    protected static final int TRIANGLE_LAYER = JLayeredPane.DEFAULT_LAYER + 2;
    protected static final int OVERLAY_LAYER = JLayeredPane.DEFAULT_LAYER + 3;
    protected static final int CARET_LAYER = JLayeredPane.DEFAULT_LAYER + 4;
    private boolean initUIDone = false;
    private boolean isEditingAllowed = Pref.CommonPtyPrefs.getInstance().getEditable();
    private WeakReference<JCaret> caretRef;
    private WeakReference<PosIndicator> movingIndicatorRef;
    private boolean paintVisibleOnly = true;

    public GraphPanel() {
        super();
        layout = new GraphPanelLayout();
        setLayout(layout);
        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane);
        layeredPaneRef = new WeakReference<JLayeredPane>(layeredPane);
        JCaret caret = new JCaret();
        caretRef = new WeakReference<JCaret>(caret);
        layeredPane.add(caret);
        layeredPane.setLayer(caret, CARET_LAYER);
        caret.install(this);
        caret.setSize(getCaretWidth(), getCaretHeight());
        caret.setState(isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);

        PosIndicator movingIndicator = new PosIndicator();
        movingIndicator.setSize(1, 0);
        movingIndicator.setLocation(GraphPane.getPadding().left, 0);
        movingIndicatorRef = new WeakReference<PosIndicator>(movingIndicator);
        layeredPane.add(movingIndicator);
        layeredPane.setLayer(movingIndicator, CARET_LAYER);

        hookupListeners();
        hookupActions();
    }

    public TopBracketList getSelectedTopBrackets() {
        TopBracketList topBrackets = getResPanel().getSelectedTopBrackets();
        return topBrackets;
    }

    public TopBracket getTopBracketByPtOnScreen(Point pt) {
        return getResPanel().getTopBracketByPtOnScreen(pt);
    }

    private void hookupActions() {
        final InputMap inputMap = getInputMap();
        // ctrl + C
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), CopyBasesToClipboardAction.class.getName());
        getActionMap().put(CopyBasesToClipboardAction.class.getName(), new CopyBasesToClipboardAction(this));

        // ctrl + A
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), SelectAllAction.class.getName());
        getActionMap().put(SelectAllAction.class.getName(), new SelectAllAction(this));

        // ctrl + C
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), PasteBasesAction.class.getName());
        getActionMap().put(PasteBasesAction.class.getName(), new PasteBasesAction(this));
        
        // ctrl + P
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), PrintAction.class.getName());
        getActionMap().put(PrintAction.class.getName(), new PrintAction());
    }

    @Override
    public boolean isPaintVisibleOnly() {
        return paintVisibleOnly;
    }

    public void setPaintVisibleOnly(boolean paintVisibleOnly) {
        boolean old = this.paintVisibleOnly;
        this.paintVisibleOnly = paintVisibleOnly;
        firePropertyChange("paintVisibleOnly", old, this.paintVisibleOnly);
    }

    public void setGCResult(GCResult gcResult) {
        GCResult old = this.gcResult;
        this.gcResult = gcResult;
        firePropertyChange("gcResult", old, gcResult);
    }

    protected PosIndicator getMovingIndicator() {
        return movingIndicatorRef.get();
    }

    protected void positionMovingIndicator(final Point ptScreen) {
        BrickComp brickComp = getBrickCompsMap().getPrimaryBrickComp();
        if (brickComp != null) {
            Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
            Integer x = brickComp.getCaretX(pt.x);
            if (x != null) {
                getMovingIndicator().setLocation(x, 0);
            }
        }
    }

    protected LinePlotCompMap getLinePlotCompMap() {
        return linePlotCompMap;
    }

    void reinitLinePlotCompMap() {
        if (getAs().getGcResult() == null) {
            getLinePlotCompMap().clear();
        } else {
            if (!gcResult.isEmpty()) {
                getLinePlotCompMap().getGC().addPlot("GC", getAs().getGcResult().getPaddedContents(), Color.BLUE, true);
            }
        }
    }

    @Override
    public Loc getTotalLoc() {
        Loc ret = new Loc();
        ret.setStart(1);
        ret.setEnd(getAs().getLength());
        ret.setTotalPos(getAs().getLength());
        return ret;
    }

    @Override
    public void cleanup() {
        featureArrowMap.clear();
        trackMap.clear();
        brickCompsMap.clear();
        ftrTracksMap.clear();
        trianglesMap.clear();
        linePlotCompMap.clear();
    }

    protected void initUI() {
        getResPanel();
        getFeatureArrowMap();
        getFtrTracksMap();
        initUIDone = true;
    }

    public boolean isInitUIDone() {
        return initUIDone;
    }

    public ResPanel getResPanel() {
        if (resPanelRef == null || resPanelRef.get() == null) {
            ResPanel resPanel = new ResPanel();
            resPanelRef = new WeakReference<ResPanel>(resPanel);
        }
        return resPanelRef.get();
    }

    @Override
    public Loc calculateVisibleLoc() {
        int width = getWidth();
        Rectangle visibleRect = getVisibleRect();
        Integer totalPos = getAs().getLength();
        int startPos = UIUtil.toScreenWidth(totalPos, width, visibleRect.x) - 3;
        int endPos = UIUtil.toScreenWidth(totalPos, width, visibleRect.x + visibleRect.width) + 3;
        startPos = Math.max(startPos, 1);
        endPos = Math.min(endPos, totalPos);
        return new Loc(startPos, endPos);
    }

    @Override
    public void caretMoved(int direction, boolean isShiftDown, boolean success) {
        Point ret = getCaret().getLocation();
        final JScrollPane scrollPane = getGraphPane().getScrollPane();
        final JScrollBar scrollBar = scrollPane.getHorizontalScrollBar();
        int min = scrollBar.getMinimum();
        int max = scrollBar.getMaximum();
        if (direction == SwingConstants.EAST) {
            if (success) {
                // adjusting the scroll bar
                Rectangle visibleRect = getVisibleRect();
                if (ret.x + 20 > visibleRect.x + visibleRect.width) {
                    int v = scrollBar.getValue();
                    int vNew = v + ret.x + 20 - (visibleRect.x + visibleRect.width);
                    vNew = Math.min(max, vNew);
                    scrollBar.setValue(vNew);
                }
                if (isShiftDown) {
                    updateSeqSelection(direction);
                }
            }
        } else if (direction == SwingConstants.WEST) {
            if (success) {
                // adjusting the scroll bar
                Rectangle visibleRect = getVisibleRect();
                if (ret.x - 20 < visibleRect.x) {
                    int v = scrollBar.getValue();
                    int vNew = v - (visibleRect.x - (ret.x - 20));
                    vNew = Math.max(min, vNew);
                    scrollBar.setValue(vNew);
                }
                if (isShiftDown) {
                    updateSeqSelection(direction);
                }
            }
        }
        if (success && !isShiftDown) {
            getGraphPane().setSelections(new LocList());
        }
    }

    private void updateSeqSelection(int direction) {
        LocList locList = getGraphPane().getSelections();
        Point caretPos = getCaretPos();
        if (!locList.isEmpty()) {
            Loc _selection = locList.get(0);

            boolean incrementEnd = false;
            boolean decrementEnd = false;
            boolean decrementStart = false;
            boolean incrementStart = false;
            if (direction == SwingConstants.EAST) {
                if (_selection.getStart().equals(_selection.getEnd())) {
                    if (caretPos.getX() == _selection.getStart().intValue() + 1) {
                        getGraphPane().setSelections(new LocList());
                    } else {
                        incrementEnd = true;
                    }
                } else if (caretPos.getX() > _selection.getMax()) {
                    incrementEnd = true;
                } else {//if (caretPos.getX() >= _selection.getMin()) {
                    incrementStart = true;
                }
            } else if (direction == SwingConstants.WEST) {
                if (_selection.getStart().equals(_selection.getEnd())) {
                    if (caretPos.getX() == _selection.getStart().intValue()) {
                        getGraphPane().setSelections(new LocList());
                    } else {
                        decrementStart = true;
                    }
                } else if (caretPos.getX() >= _selection.getMin()) {
                    decrementEnd = true;
                } else if (caretPos.getX() < _selection.getMin()) {
                    decrementStart = true;
                }
            }

            if (incrementEnd) {
                if (_selection.getMax() < getAs().getLength()) {
                    Loc n = new Loc(_selection.getMin(), _selection.getMax() + 1);
                    getGraphPane().setSelections(new LocList(n));
                }
            } else if (incrementStart) {
                if (_selection.getMin() < getAs().getLength()) {
                    Loc n = new Loc(_selection.getMin() + 1, _selection.getMax());
                    getGraphPane().setSelections(new LocList(n));
                }
            } else if (decrementStart) {
                if (_selection.getMin() > 1) {
                    Loc n = new Loc(_selection.getMin() - 1, _selection.getMax());
                    getGraphPane().setSelections(new LocList(n));
                }
            } else if (decrementEnd) {
                if (_selection.getMax() > 1) {
                    Loc n = new Loc(_selection.getMin(), _selection.getMax() - 1);
                    getGraphPane().setSelections(new LocList(n));
                }
            }
        } else {
            if (direction == SwingConstants.EAST) {
                Loc n = new Loc(caretPos.x - 1, caretPos.x - 1);
                getGraphPane().setSelections(new LocList(n));
            } else if (direction == SwingConstants.WEST) {
                Loc n = new Loc(caretPos.x, caretPos.x);
                getGraphPane().setSelections(new LocList(n));
            }
        }
    }

    public void setRmap(RMap rmap) {
        reinitTrianglesMap();
        getResPanel().refresh();
        revalidate();
    }

    protected Map<String, Triangle> reinitTrianglesMap() {
        UIUtil.removeComponentsInLayer(getLayeredPane(), GraphPanel.TRIANGLE_LAYER);
        trianglesMap.clear();
        return getTrianglesMap();
    }

    protected void reinitBrickTracksMap() {
        UIUtil.removeComponentsInLayer(getLayeredPane(), JLayeredPane.DEFAULT_LAYER, BrickComp.class);
        brickCompsMap.clear();
        getBrickCompsMap();
    }

    public TriangleMap getTrianglesMap() {
        if (trianglesMap.isEmpty() && getAs() != null && getAs().getRmap() != null) {
            trianglesMap.create(getAs().getRmap());
        }
        return trianglesMap;
    }

    public boolean isDoubleStranded() {
        return doubleStranded;
    }

    public void setDoubleStranded(Boolean doubleStranded) {
        Boolean old = this.doubleStranded;
        this.doubleStranded = doubleStranded;
        firePropertyChange("doubleStranded", old, this.doubleStranded);
    }

    public void removeInLayers(int... layers) {
        for (int layer : layers) {
            UIUtil.removeComponentsInLayer(getLayeredPane(), layer);
        }
    }

    public void refresh() {
        removeInLayers(GraphPanel.DEFAULT_LAYER, GraphPanel.OVERLAY_LAYER, GraphPanel.TRIANGLE_LAYER, GraphPanel.PLOT_LAYER);
        getLinePlotCompMap().clear();
        resPanelRef.get().refresh();
        featureArrowMap.clear();
        trianglesMap.clear();
        trackMap.clear();
        brickCompsMap.clear();
        ftrTracksMap.clear();

        layout.setStart(1);
        layout.setEnd(getAs().getLength());
        revalidate();
    }

    private void hookupListeners() {
        addComponentListener(new GraphPanelListeners.ResizeListener());
        addPropertyChangeListener(new GraphPanelListeners.PtyListener());
        layout.addPropertyChangeListener(new GraphPanelListeners.LayoutPtyListener());

        addPropertyChangeListener(new GraphPanelListeners.StatusLineUpdater());
    }

    protected Arrow getSelectedArrow() {
        return selectedArrow;
    }

    public void setSelectedArrow(Arrow arrow) {
        Arrow old = this.selectedArrow;
        this.selectedArrow = arrow;
        firePropertyChange("selectedArrow", old, this.selectedArrow);
    }

    public void setSelectedFeture(Feture feture) {
        Feture old = this.selectedFeture;
        this.selectedFeture = feture;
        firePropertyChange("selectedFeture", old, this.selectedFeture);
    }

    protected Feture getSelectedFeture() {
        return this.selectedFeture;
    }

    public void addSelection(Loc loc, boolean singleSelect) {

        Iterator<FtrTrack> tracksItr = ftrTracksMap.values().iterator();
        while (tracksItr.hasNext()) {
            FtrTrack track = tracksItr.next();
            track.setSelection(loc);
        }
    }

    public JLayeredPane getLayeredPane() {
        return layeredPaneRef.get();
    }

    public Integer getFullWidth() {
        Integer ret = null;
        BrickComp simpleBrickPanel = getBrickCompsMap().getPrimaryBrickComp();
        if (simpleBrickPanel != null) {
            ret = simpleBrickPanel.getDesiredWidth();
        }
        return ret;
    }

    public Map<String, JComponent> getTrackMap() {
        return trackMap;
    }

    public void setAs(AnnotatedSeq as) {
        AnnotatedSeq old = this.getAs();
        asRef = new WeakReference<AnnotatedSeq>(as);
        firePropertyChange("as", old, this.getAs());
    }

    public AnnotatedSeq getAs() {
        if (asRef != null) {
            return asRef.get();
        } else {
            return null;
        }
    }

    private MolPane getMolPane() {
        if (molPaneRef == null || molPaneRef.get() == null) {
            MolPane molPane = UIUtil.getParent(this, MolPane.class);
            molPaneRef = new WeakReference<MolPane>(molPane);
        }
        return molPaneRef.get();
    }

    private GraphPane getGraphPane() {
        if (graphPaneRef == null || graphPaneRef.get() == null) {
            GraphPane graphPane = UIUtil.getParent(this, GraphPane.class);
            graphPaneRef = new WeakReference<GraphPane>(graphPane);
        }
        return graphPaneRef.get();
    }

    public List<String> getBrickTrackNames() {
        return getBrickCompsMap().getBrickCompNames();
    }

    public BrickCompMap getBrickCompsMap() {

        if (brickCompsMap.isEmpty() && getAs() != null) {
            Insets insets = GraphPane.getPadding();

            IColorProvider colorProvider = getGraphPane().getPrimarySeqColorProvider();

            IColorProvider translationColorProvider = getGraphPane().getTranslationColorProvider();

            brickCompsMap.create(getAs(), insets, colorProvider, translationColorProvider);
        }
        return brickCompsMap;
    }

    public FtrTrack getFtrTrack(String name) {
        return ftrTracksMap.get(name.toUpperCase(Locale.ENGLISH));
    }

    public FtrTrackMap getFtrTracksMap() {
        if (ftrTracksMap.isEmpty()) {
            Map<String, IShapeList> arrowMap = getFeatureArrowMap();
            ftrTracksMap.create(arrowMap);
        }
        return ftrTracksMap;
    }

    public IShapeListMap getFeatureArrowMap() {
        if (featureArrowMap.isEmpty()) {
            List<Feture> fetures = AsHelper.getAllFetures(getAs(), true);
            featureArrowMap.create(fetures);
        }
        return featureArrowMap;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension ret = new Dimension();
        return ret;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int ret = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            BrickComp panel = brickCompsMap.get(BrickComp.SEQ);
            Insets insets = panel.getInsets();
            Rectangle panelVisibleRect = panel.getVisibleRect();

            float unitIncrement = panel.getUnitWidth();

            float quotient = (panelVisibleRect.x - insets.left) / unitIncrement;
            double lowerLimit = Math.floor(quotient) + 0.25;
            double upperLimit = Math.floor(quotient) + 0.75;
            boolean fuzzy;
            if (quotient > lowerLimit && quotient < upperLimit) {
                fuzzy = true;
            } else {
                fuzzy = false;
            }
            double reminder = 0;
            if (fuzzy) {
                reminder = Math.round((quotient - Math.floor(quotient)) * unitIncrement);
            }
            if (direction < 0) { // left
                if (fuzzy) {
                    ret = (int) reminder;
                } else {
                    float prevX = Math.round(quotient - 1) * unitIncrement + insets.left;
                    ret = Math.round(visibleRect.x - prevX);
                }
            } else { // right
                if (fuzzy) {
                    ret = (int) Math.round(unitIncrement - reminder);
                } else {
                    float nextX = Math.round(quotient + 1) * unitIncrement + insets.left;
                    ret = Math.round(nextX - visibleRect.x);
                }
            }
        } else { // vertical
            ret = 5;
        }
        return ret;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return getScrollableUnitIncrement(visibleRect, orientation, direction) * 5;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public Point getCaretLocation(Point ptScreen) {
        Point ret = null;

        BrickComp brickComp = getBrickCompsMap().getPrimaryBrickComp();
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        if (brickComp != null && ptScreen != null) {
            ret = new Point();
            ret.x = brickComp.getCaretX(pt.x);
            ret.y = brickComp.getLocation().y;
        }
        return ret;
    }

    @Override
    public final Integer getCaretWidth() {
        return 2;
    }

    @Override
    public final Integer getCaretHeight() {
        Integer ret;
        BrickCompMap brickCompMap = getBrickCompsMap();
        if (!brickCompMap.isEmpty()) {
            ret = Math.round(brickCompMap.getPrimaryBrickComp().getDesiredHeight() * 1.45f);
        } else {
            ret = 0;
        }
        return ret;
    }

    @Override
    public void fireCaretMoveEvent(CaretMoveEvent event) {
    }

    @Override
    public void addListener(ICaretParentListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Point loc, boolean forward) {
        Integer newCaretPos;
        if (forward) {
            newCaretPos = loc.x;
            getAs().delete(loc.x, loc.x);
        } else {
            newCaretPos = loc.x - 1;
            getAs().delete(loc.x - 1, loc.x - 1);
        }
        getCaret().setPos(new Point(newCaretPos, 1));

        getMolPane().refresh();
        setCanSave();
    }

    public Integer getCaretPos(int x) {
        Integer ret = null;
        BrickComp brickComp = getBrickCompsMap().getPrimaryBrickComp();
        Point pt = getCaret().getLocation();
        if (brickComp != null && pt != null) {
            ret = brickComp.getCaretPos(x);
        }
        return ret;
    }

    @Override
    public Point getCaretPos() {
        Point ret = null;
        BrickComp brickComp = getBrickCompsMap().getPrimaryBrickComp();
        Point pt = getCaret().getLocation();
        if (brickComp != null && pt != null) {
            Integer x = brickComp.getCaretPos(pt.x);
            if (x != null) {
                ret = new Point();
                ret.y = 1;
                ret.x = x;
            }
        }
        return ret;
    }

    @Override
    public void insert(Point pos, Character c) {
        insert(pos, c.toString());
    }

    @Override
    public void insert(Point pos, String c) {
        AnnotatedSeq as = getAs();
        as.insertSeq(pos.x, c.toString().toUpperCase(Locale.ENGLISH));
        getCaret().setPos(new Point(pos.x + c.length(), 1));
        getMolPane().refresh();
        setCanSave();
    }

    private void setCanSave() {
        getAs().setLastModifiedDate(new Date());
        AbstractSavableEditor editor = UIUtil.getParent(this, AbstractSavableEditor.class);
        if (editor != null) {
            editor.setCanSave();
        }
    }

    public void setSelection(Loc2D selection) {
        Loc2D old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }

    @Override
    public Loc2D getSelection() {
        return selection;
    }

    @Override
    public JCaret getCaret() {
        return caretRef.get();
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public final boolean isEditingAllowed() {
        return isEditingAllowed;
    }

    public void setEditingAllowed(boolean allowed) {
        boolean old = this.isEditingAllowed;
        this.isEditingAllowed = allowed;
        firePropertyChange("isEditingAllowed", old, this.isEditingAllowed);
    }

    @Override
    public void replace(Loc2D selection, String c) {
        AnnotatedSeq as = getAs();
        int minX = selection.getMinX();
        int maxX = selection.getMaxX();
        as.replace(minX, maxX, c);

        getCaret().setPos(new Point(minX + 1, 1));
        getMolPane().refresh();
        getGraphPane().setSelections(new LocList());

        setCanSave();
    }

    @Override
    public Integer getTotalPos() {
        AnnotatedSeq as = getAs();
        return as.getLength();
    }

    @Override
    public Point getCaretMaxPos() {
        Point ret = null;
        AnnotatedSeq as = getAs();
        if (as != null) {
            ret = new Point();
            ret.x = as.getLength();
            ret.y = 1;
        }
        return ret;
    }

    @Override
    public Point getCaretLocationByPos(Point pos) {
        Point ret = null;
        BrickComp brickComp = getBrickCompsMap().getPrimaryBrickComp();
        if (brickComp != null) {
            ret = brickComp.getCaretLoc(pos.x);
            if (ret != null) {
                ret = SwingUtilities.convertPoint(brickComp, ret, this);
            }
        }
        return ret;
    }

    @Override
    public Point getCaretPos(Point ptScreen) {
        Point ret = null;
        BrickComp brickComp = getBrickCompsMap().getPrimaryBrickComp();
        if (brickComp != null) {
            Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
            Integer caretX = brickComp.getCaretPos(pt.x);
            if (caretX != null) {
                ret = new Point(caretX, 1);
            }
        }
        return ret;
    }

    @Override
    public Double getCaretAngleByPos(Integer pos) {
        return null;
    }

    @Override
    public Integer getCaretRadius() {
        return null;
    }

    @Override
    public boolean isInputValid(String str) {
        if(str == null){
            return false;
        }
        boolean ret;
        boolean isNucleotide = getAs().isNucleotide();
        if (isNucleotide) {
            ret = BioUtil.areDNAs(str);
        } else {
            ret = BioUtil.areProteins(str);
        }
        return ret;
    }

    @Override
    public boolean isInputValid(KeyEvent e) {
        Character keyChar = e.getKeyChar();
        return isInputValid(keyChar.toString());
    }
}