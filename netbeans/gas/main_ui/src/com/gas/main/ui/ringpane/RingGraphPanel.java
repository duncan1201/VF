/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.IVisibleLocProvider;
import com.gas.common.ui.caret.CaretMoveEvent;
import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.caret.ICaretParentListener;
import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.jcomp.CircularOverlay;
import com.gas.common.ui.linePlot.CirLinePlot;
import com.gas.common.ui.linePlot.CirLinePlotsComp;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.gc.api.GCResult;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.main.ui.actions.print.PrintAction;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.action.CopyBasesToClipboardAction;
import com.gas.main.ui.molpane.action.PasteBasesAction;
import com.gas.main.ui.molpane.action.SelectAllAction;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class RingGraphPanel extends JPanel implements IVisibleLocProvider, IReclaimable, ICaretParent {

    public static final int CARET_LAYER = JLayeredPane.PALETTE_LAYER;
    public static final int CURVED_BRACKETS_LAYER = JLayeredPane.DEFAULT_LAYER + 1;
    public static final int CENTER_LABEL_LAYER = JLayeredPane.DEFAULT_LAYER + 2;
    public static final int RING_LAYER = JLayeredPane.DEFAULT_LAYER + 3;
    public static final int PLOT_LAYER = JLayeredPane.DEFAULT_LAYER + 4;
    public static final int CONNECTOR_LAYER = JLayeredPane.DEFAULT_LAYER + 5;
    public static final int LABEL_LAYER = JLayeredPane.DEFAULT_LAYER + 6;
    public static final int OVERLAY_LAYER = JLayeredPane.DEFAULT_LAYER + 7;
    private WeakReference<AnnotatedSeq> asRef;
    private RingGraphPanelListeners.AWTListener awtListener;
    private WeakReference<BrickRing> brickRingRef;
    private BrickRingList translatedBrickRings = new BrickRingList();
    private LabelList labelList = new LabelList();
    private ConnectorsComp connectorsComp;
    private ConnectorList connectors = new ConnectorList();
    private Label nameLabel;
    private Label lengthLabel;
    private WeakReference<CircularRuler> circularRulerRef;
    private WeakReference<JCaret> caretRef;
    private RingListMapComp ringListMapComp;
    private LabelListComp labelListComp;
    private SortedRingListMap sortedRingMap = new SortedRingListMap();
    private WeakReference<CirLinePlotsComp> cirLinePlotsCompRef;
    private WeakReference<CircularOverlay> overlayRef;
    private Loc selectedLoc;
    private Feture selectedFeture;
    private Boolean doubleStranded;
    private WeakReference<MolPane> molPaneRef;
    private WeakReference<JLayeredPane> layeredPaneRef;
    private Float offset = 0f;
    private Float rulerFontSize;
    private Float baseFontSize;
    private RingGraphPanelLayout layout = null;
    private CurvedBracketList curvedBrackets = new CurvedBracketList();
    private Loc visibleLoc;
    private GCResult gcResult;
    private boolean editingAllowed = Pref.CommonPtyPrefs.getInstance().getEditable();
    private IColorProvider translationColorProvider;
    private boolean initUIDone = false;
    private WeakReference<RingPane> ringPaneRef;
    private boolean paintVisibleOnly = true;

    public RingGraphPanel() {
        setBackground(new Color(248, 248, 248));
        layout = new RingGraphPanelLayout();
        setLayout(layout);
        Insets insets = getDesiredInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPaneRef = new WeakReference<JLayeredPane>(layeredPane);
        add(layeredPane);
        getCaret();
        hookupListeners();
        hookupActions();
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
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), PasteBasesAction.class.getName());
        getActionMap().put(PasteBasesAction.class.getName(), new PasteBasesAction(this));
        
        // ctrl + P
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), PrintAction.class.getName());
        getActionMap().put(PrintAction.class.getName(), new PrintAction());
    }

    public void setGCResult(GCResult gcResult) {
        GCResult old = this.gcResult;
        this.gcResult = gcResult;
        firePropertyChange("gcResult", old, this.gcResult);
    }

    @Override
    public Double getCaretAngleByPos(Integer pos) {
        return brickRingRef.get().getCaretAngleByPos(pos);
    }

    @Override
    public void cleanup() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(awtListener);
        translatedBrickRings.clear();
        sortedRingMap.clear();

        curvedBrackets.clear();

    }

    protected void initUI() {
        initUIDone = false;
        brickRingRef = null;
        circularRulerRef = null;
        translatedBrickRings.clear();
        sortedRingMap.clear();
        labelList.clear();
        connectors.clear();
        curvedBrackets.clear();
        nameLabel = null;
        lengthLabel = null;
        ringListMapComp = null;
        connectorsComp = null;

        getLayeredPane().removeAll();

        getBrickRing();
        getCircularRuler();
        getCurvedBrackets();
        getTranslatedBrickRings();
        getLabelList();
        getSortedRingMap();
        getConnectors();
        getNameLabel();
        getLengthLabel();
        getLayeredPane().add(getCaret());
        getLayeredPane().setLayer(getCaret(), CARET_LAYER);
        getRingListMapComp();
        getConnectorsComp();
        initUIDone = true;
    }

    public void setSeedColor(String featureName, Color c) {
        getRingListMapComp().setSeedColor(featureName, c);
        getLabelListComp().setSeedColor(featureName, c);
        getConnectorsComp().setSeedColor(featureName, c);
    }

    private RingPane getRingPane() {
        if (ringPaneRef == null || ringPaneRef.get() == null) {
            RingPane ringPane = UIUtil.getParent(this, RingPane.class);
            ringPaneRef = new WeakReference<RingPane>(ringPane);
        }
        return ringPaneRef.get();
    }

    protected RingListMapComp getRingListMapComp() {
        if (ringListMapComp == null) {
            ringListMapComp = new RingListMapComp();
            getLayeredPane().add(ringListMapComp);
            getLayeredPane().setLayer(ringListMapComp, JLayeredPane.DEFAULT_LAYER);
        }
        return ringListMapComp;
    }

    protected void reinitCirLinePlotsComp() {
        CirLinePlot c = getCirLinePlotsComp().getGC();
        if (c == null) {
            c = new CirLinePlot();
            getCirLinePlotsComp().setGC(c);
        }
        if (getAs().getGcResult() == null) {
            getCirLinePlotsComp().clear();
        } else {
            c.addPlot(CirLinePlotsComp.GC, getAs().getGcResult().getPaddedContents(false), Color.BLUE);
        }
    }

    protected CirLinePlotsComp getCirLinePlotsComp() {
        if (cirLinePlotsCompRef == null) {
            CirLinePlotsComp cirLinePlotsComp = new CirLinePlotsComp();
            getLayeredPane().add(cirLinePlotsComp);
            getLayeredPane().setLayer(cirLinePlotsComp, RingGraphPanel.PLOT_LAYER);
            cirLinePlotsCompRef = new WeakReference<CirLinePlotsComp>(cirLinePlotsComp);
        }
        return cirLinePlotsCompRef.get();
    }

    protected LabelListComp getLabelListComp() {
        if (labelListComp == null) {
            labelListComp = new LabelListComp();
            getLayeredPane().add(labelListComp);
            getLayeredPane().setLayer(labelListComp, LABEL_LAYER);
        }
        return labelListComp;
    }

    protected void updateSelection(Point ptScreen) {
        Ring ring = getRingListMapComp().updateSelection(ptScreen);
        CurvedBracket bracket = updateSiteSelection(ptScreen);
        Label label = updateLabelSelection(ptScreen);

        Loc selected = null;
        if (ring != null) {
            Feture feture = (Feture) ring.getData();
            selected = feture.getLucation().toLoc();
            selected.setTotalPos(getAs().getLength());

            Label selectedLabel = labelListComp.getLabel(ring);
            labelListComp.setSelected(selectedLabel);
            Connector selectedConn = getConnectorsComp().getConnector(selectedLabel);
            getConnectorsComp().setSelected(selectedConn);
            getMolPane().getSidePanel().getAnnotationPanel().setSelected(feture);
        } else if (bracket != null) {
            RMap.Entry entry = (RMap.Entry) bracket.getData();
            entry.getStart();
            entry.getEnd();
            selected = new Loc(entry.getStart(), entry.getEnd());
            selected.setTotalPos(getAs().getLength());

            getConnectorsComp().updateSelection(null);
        } else if (label != null) {
            Ring ringL = (Ring) label.getData();

            Feture feture = (Feture) ringL.getData();
            getRingListMapComp().updateSelection(feture);
            selected = feture.getLucation().toLoc();
            selected.setTotalPos(getAs().getLength());

            getConnectorsComp().updateSelection(label);
            getMolPane().getSidePanel().getAnnotationPanel().setSelected(feture);
        }

        if (labelListComp.getSelected() == null) {
            getConnectorsComp().updateSelection(null);
            //getMolPane().setSelectedFeture(null);
            getMolPane().getSidePanel().getAnnotationPanel().updatingUI = true;
            getMolPane().getSidePanel().getAnnotationPanel().getTree().clearSelection();
            getMolPane().getSidePanel().getAnnotationPanel().updatingUI = false;
        }

        setSelectedLoc(selected);

    }

    private CurvedBracket updateSiteSelection(Point ptScreen) {
        CurvedBracket ret = null;
        CurvedBracketList cList = getCurvedBrackets();
        Point pt = new Point(ptScreen);
        SwingUtilities.convertPointFromScreen(pt, this);
        ret = cList.updateSelection(pt);
        if (ret != null) {
            getLayeredPane().moveToFront(ret);
        }
        return ret;
    }

    private Label updateLabelSelection(Point ptScreen) {
        Label ret = getLabelListComp().updateSelection(ptScreen);
        return ret;
    }

    public boolean isInitUIDone() {
        return initUIDone;
    }

    protected void reinitCurvedBrackets() {
        UIUtil.removeComponentsInLayer(getLayeredPane(), CURVED_BRACKETS_LAYER);
        curvedBrackets.clear();
        getCurvedBrackets();
    }

    public Label getLengthLabel() {
        if (lengthLabel == null) {
            lengthLabel = new Label();
            lengthLabel.setOpaque(false);
            getLayeredPane().add(lengthLabel);
            getLayeredPane().setLayer(lengthLabel, CENTER_LABEL_LAYER);

        }
        return lengthLabel;
    }

    public void setLengthLabel(Label lengthLabel) {
        this.lengthLabel = lengthLabel;
    }

    public Label getNameLabel() {
        if (nameLabel == null) {
            nameLabel = new Label();
            nameLabel.setOpaque(false);
            getLayeredPane().add(nameLabel);
            getLayeredPane().setLayer(nameLabel, CENTER_LABEL_LAYER);
        }
        return nameLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public void selectCurvedBrackets(RMap.EntryList entries) {
        Iterator<CurvedBracket> itr = getCurvedBrackets().iterator();
        while (itr.hasNext()) {
            CurvedBracket b = itr.next();
            RMap.Entry entry = entries.getEntry(b.getTxt(), b.getStartPos(), b.getEndPos());
            if (entry != null) {
                b.setSelected(true);
                getLayeredPane().moveToFront(b);
            } else {
                b.setSelected(false);
            }
        }
    }

    public void selectCurvedBracket(RMap.Entry entry) {
        Iterator<CurvedBracket> itr = getCurvedBrackets().iterator();
        while (itr.hasNext()) {
            CurvedBracket b = itr.next();
            if (b.getTxt().equalsIgnoreCase(entry.getName()) && b.getStartPos().equals(entry.getStart()) && b.getEndPos().equals(entry.getEnd())) {
                b.setSelected(true);
                getLayeredPane().moveToFront(b);
            } else {
                b.setSelected(false);
            }
        }
    }

    public LabelList getLabelList() {
        if (labelList.isEmpty()) {
            labelList.addAll(createLabels());
        }
        return labelList;
    }

    protected ConnectorsComp getConnectorsComp() {
        if (connectorsComp == null) {
            connectorsComp = new ConnectorsComp();
            getLayeredPane().add(connectorsComp);
            getLayeredPane().setLayer(connectorsComp, CONNECTOR_LAYER);
        }
        return connectorsComp;
    }

    protected ConnectorList getConnectors() {
        if (connectors.isEmpty()) {
            connectors = createConnectors();
        }
        return connectors;
    }

    private ConnectorList createConnectors() {
        ConnectorList ret = new ConnectorList();
        LabelList labels = getLabelList();
        Iterator<Label> itr = labels.iterator();
        while (itr.hasNext()) {
            Label label = itr.next();
            Color color = label.getTextColor();
            Connector connector = new Connector();
            connector.setForeground(color);
            connector.setData(label);
            ret.add(connector);
        }
        return ret;
    }

    protected LabelList createLabels() {
        LabelList ret = new LabelList();
        synchronized (sortedRingMap) {
            Iterator<String> itr = getSortedRingMap().keySet().iterator();
            while (itr.hasNext()) {
                final String key = itr.next();
                Iterator<Ring> ringItr = getSortedRingMap().get(key).iterator();
                while (ringItr.hasNext()) {
                    Ring ring = ringItr.next();
                    final Color seedColor = ring.getSeedColor();
                    Feture feture = (Feture) ring.getData();
                    String displayName = feture.getDisplayName();
                    Label label = new Label();
                    label.setOpaque(true);
                    label.setData(ring);
                    label.setText(displayName);
                    //label.setBorderColor(seedColor);
                    label.setTextColor(seedColor);
                    ret.add(label);
                }
            }
        }
        return ret;
    }

    protected CurvedBracketList createCurvedBrackets() {
        CurvedBracketList ret = new CurvedBracketList();
        if (getAs() != null && getAs().getRmap() != null) {
            Iterator<RMap.Entry> itr = getAs().getRmap().getEntriesIterator();
            while (itr.hasNext()) {
                RMap.Entry entry = itr.next();

                CurvedBracket bracket = new CurvedBracket();
                bracket.setStartPos(entry.getStart());
                bracket.setEndPos(entry.getEnd());
                bracket.setTotalPos(getAs().getLength());
                bracket.setTxt(entry.getName());
                bracket.setData(entry);
                bracket.setStartOffset(offset);

                ret.add(bracket);
                getLayeredPane().add(bracket);
                getLayeredPane().setLayer(bracket, CURVED_BRACKETS_LAYER);

            }
            ret = CurvedBracket.compact(ret);
        }
        return ret;
    }

    public CurvedBracketList getCurvedBrackets() {
        if (curvedBrackets.isEmpty() && getAs() != null && getAs().getRmap() != null) {
            curvedBrackets = createCurvedBrackets();
        }
        return curvedBrackets;
    }

    public IColorProvider getTranslationColorProvider() {
        if (translationColorProvider == null) {
            String name = Pref.ColorProviderPrefs.getInstance().getColorProviderName(Pref.ColorProviderPrefs.KEY.TRANSLATION);
            translationColorProvider = ColorProviderFetcher.getColorProvider(name);
        }
        return translationColorProvider;
    }

    public void setRmap(RMap rmap) {
        reinitCurvedBrackets();
        revalidate();
    }

    public void setTranslationColorProvider(IColorProvider translationColorProvider) {
        IColorProvider old = getTranslationColorProvider();
        this.translationColorProvider = translationColorProvider;
        firePropertyChange("translationColorProvider", old, this.translationColorProvider);
    }

    public Float getRulerFontSize() {
        return rulerFontSize;
    }

    public void setRulerFontSize(Float rulerFontSize) {
        Float old = this.rulerFontSize;
        this.rulerFontSize = rulerFontSize;
        firePropertyChange("rulerFontSize", old, this.rulerFontSize);
    }

    public Float getBaseFontSize() {
        if (baseFontSize == null) {
            baseFontSize = Pref.CommonPtyPrefs.getInstance().getBaseFontSize();
        }
        return baseFontSize;
    }

    public void setBaseFontSize(Float baseFontSize) {
        Float old = this.baseFontSize;
        this.baseFontSize = baseFontSize;
        firePropertyChange("baseFontSize", old, this.baseFontSize);
    }

    public void setVisibleLoc(Loc visibleLoc) {
        Loc old = this.visibleLoc;
        this.visibleLoc = visibleLoc;
        firePropertyChange("visibleLoc", old, this.visibleLoc);
    }

    @Override
    public Loc calculateVisibleLoc() {
        Loc ret = null;
        final Rectangle visibleRect = getVisibleRect();
        if (getAs() == null || visibleRect.getWidth() <= 0 || visibleRect.getHeight() <= 0) {
            return ret;
        }
        Integer length = getAs().getLength();

        final Rectangle drawingRect = layout.getDrawingRect(this);
        if (visibleRect.width < drawingRect.width) {
            double rad = Math.asin(visibleRect.width * 1.0 / drawingRect.width);
            double radOffset = Math.toRadians(offset) - rad;
            int start = (int) Math.round(radOffset / (2 * Math.PI) * length);
            int extent = (int) Math.ceil(length * rad / Math.PI);
            int end = start + extent;
            if (start < 1) {
                start = length + start;
            }
            if (end > length) {
                end = end - length;
            }

            ret = new Loc(start, end);
        } else {
            ret = new Loc(1, length);
        }
        ret.setTotalPos(getAs().getLength());
        return ret;
    }

    @Override
    public void caretMoved(int direction, boolean isShiftDown, boolean success) {
        final Rectangle visibleRect = getVisibleRect();
        final double centerX = visibleRect.getCenterX();
        final Line2D line = getCaret().createLine();
        final Rectangle lineBounds = line.getBounds();
        Integer amount = null;
        if (direction == SwingConstants.EAST) {
            if (lineBounds.getCenterX() > centerX) {
                if (lineBounds.getMaxY() + 30 > visibleRect.getMaxY()) {
                    amount = (int) Math.round((lineBounds.getMaxY() + 30 - visibleRect.getMaxY()) / 10);
                } else if (lineBounds.getMaxX() + 30 > visibleRect.getMaxX()) {
                    amount = (int) Math.round((lineBounds.getMaxX() + 30 - visibleRect.getMaxX()) / 10);
                }
                if (amount != null) {
                    amount = Math.abs(amount);
                }
            }
            if (isShiftDown) {
                updateSelection(direction);
            }
        } else if (direction == SwingConstants.WEST) {
            if (lineBounds.getCenterX() < centerX) {
                if (lineBounds.getMaxY() + 30 > visibleRect.getMaxY()) {
                    amount = (int) Math.round((lineBounds.getMaxY() + 30 - visibleRect.getMaxY()) / 10);
                } else if (lineBounds.getMinX() - 30 < visibleRect.getMinX()) {
                    amount = (int) Math.round((lineBounds.getMinX() - 30 - visibleRect.getMinX()) / 10);
                }
                if (amount != null) {
                    amount = -Math.abs(amount);
                }
            }
            if (isShiftDown) {
                updateSelection(direction);
            }
        }
        if (amount != null) {
            getRingPane().scroll(amount);
        }
        if (success && !isShiftDown) {
            getRingPane().setSelections(new LocList());
        }
    }

    private void updateSelection(int direction) {
        Point caretPos = getCaretPos();
        LocList locList = getRingPane().getSelections();
        if (locList.isEmpty()) {
            if (direction == SwingConstants.EAST) {
                int newPos = caretPos.x - 1;
                if (newPos == 0) {
                    newPos = getAs().getLength();
                }
                Loc loc = new Loc(newPos, newPos);
                getRingPane().setSelections(new LocList(loc));
            } else if (direction == SwingConstants.WEST) {
                Loc loc = new Loc(caretPos.x, caretPos.x);
                getRingPane().setSelections(new LocList(loc));
            }
        } else {
            boolean incrementEnd = false;
            boolean incrementStart = false;
            boolean decrementEnd = false;
            boolean decrementStart = false;
            Loc loc = locList.get(0);
            int startPrev = loc.getStart() - 1;
            if (startPrev < 1) {
                startPrev = getAs().getLength();
            }

            if (direction == SwingConstants.EAST) {
                if (loc.getStart().equals(loc.getEnd())) {
                    if (caretPos.x == 1) {
                        incrementEnd = true;
                    } else if (caretPos.x > loc.getStart() + 1) {
                        incrementEnd = true;
                    } else {
                        getRingPane().setSelections(new LocList());
                    }
                } else {
                    int endNext = loc.getEnd() + 2;
                    if (endNext > getAs().getLength()) {
                        endNext = endNext % getAs().getLength();
                    }

                    if (caretPos.x == endNext) {

                        incrementEnd = true;
                    } else {
                        incrementStart = true;
                    }
                }
            } else if (direction == SwingConstants.WEST) {
                if (loc.getStart().equals(loc.getEnd())) {
                    if (caretPos.x < loc.getStart()) {
                        decrementStart = true;
                    } else {
                        getRingPane().setSelections(new LocList());
                    }
                } else if (caretPos.x == startPrev) {
                    decrementStart = true;
                } else {
                    decrementEnd = true;
                }
            }

            if (incrementEnd) {
                int newEnd = loc.getEnd() + 1;
                if (newEnd > getAs().getLength()) {
                    newEnd = 1;
                }
                Loc newLoc = new Loc(loc.getStart(), newEnd, getAs().getLength());
                getRingPane().setSelections(new LocList(newLoc));
            } else if (incrementStart) {
                int newStart = loc.getStart() + 1;
                if (newStart > getAs().getLength()) {
                    newStart = 1;
                }
                Loc newLoc = new Loc(newStart, loc.getEnd(), getAs().getLength());
                getRingPane().setSelections(new LocList(newLoc));
            } else if (decrementStart) {
                int newStart = loc.getStart() - 1;
                if (newStart < 1) {
                    newStart = getAs().getLength();
                }
                Loc newLoc = new Loc(newStart, loc.getEnd(), getAs().getLength());
                getRingPane().setSelections(new LocList(newLoc));
            } else if (decrementEnd) {
                int newEnd = loc.getEnd() - 1;
                if (newEnd < 1) {
                    newEnd = getAs().getLength();
                }
                Loc newLoc = new Loc(loc.getStart(), newEnd, getAs().getLength());
                getRingPane().setSelections(new LocList(newLoc));
            }
        }
    }

    private void hookupListeners() {
        Toolkit.getDefaultToolkit().addAWTEventListener(getAWTListener(), AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK
                | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        addComponentListener(new RingGraphPanelListeners.CompListener());
        addPropertyChangeListener(new RingGraphPanelListeners.PtyChangeListener());
        addPropertyChangeListener(new RingGraphPanelListeners.StatusLineUpdater());
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(Float offset) {
        Float old = this.offset;
        this.offset = offset;
        firePropertyChange("offset", old, this.offset);
    }

    public Dimension getFullSize() {
        Dimension ret = null;
        BrickRing brickRing = getBrickRing();
        if (brickRing != null) {
            Dimension bSize = brickRing.getFullSize();
            Insets insets = getDesiredInsets();
            ret = new Dimension(insets.left + insets.right + bSize.width, insets.top + insets.bottom + bSize.height);
        }
        return ret;
    }

    protected float getUnitIncrement() {
        float ret = 0;
        int length = getAs().getLength();
        ret = length / 360.0f;
        return ret;
    }

    public CircularRuler getCircularRuler() {
        if (circularRulerRef == null || circularRulerRef.get() == null) {
            CircularRuler circularRuler = new CircularRuler();
            circularRulerRef = new WeakReference<CircularRuler>(circularRuler);
            Font font = FontUtil.getDefaultSansSerifFont();
            circularRuler.setFont(font);
            getLayeredPane().add(circularRuler);
            getLayeredPane().setLayer(circularRuler, JLayeredPane.DEFAULT_LAYER);
        }
        return circularRulerRef.get();
    }

    public synchronized BrickRing getBrickRing() {
        if ((brickRingRef == null || brickRingRef.get() == null) && getAs() != null) {
            BrickRing brickRing = new BrickRing();
            brickRingRef = new WeakReference<BrickRing>(brickRing);
            brickRing.setBases(getAs().getSiquence().getData());
            brickRing.setStartPos(1);
            brickRing.setEndPos(getAs().getLength());

            getLayeredPane().add(brickRing);
            getLayeredPane().setLayer(brickRing, JLayeredPane.DEFAULT_LAYER);

        }
        if (brickRingRef != null && brickRingRef.get() != null) {
            return brickRingRef.get();
        } else {
            return null;
        }
    }

    public BrickRingList getTranslatedBrickRings() {
        if (translatedBrickRings.isEmpty() && getAs() != null) {
            String colorProviderName = Pref.ColorProviderPrefs.getInstance().getColorProviderName(Pref.ColorProviderPrefs.KEY.TRANSLATION);
            IColorProvider colorProvider = ColorProviderFetcher.getColorProvider(colorProviderName);
            List<TranslationResult> sorted = new ArrayList<TranslationResult>(getAs().getTranslationResults());
            Collections.sort(sorted, new TranslationResult.FrameComparator(false));
            Iterator<TranslationResult> itr = sorted.iterator();
            while (itr.hasNext()) {
                TranslationResult r = itr.next();
                BrickRing ring = new BrickRing();
                ring.setBases(r.getData());
                ring.setStartPos(r.getStartPos());
                ring.setEndPos(r.getEndPos());
                ring.setTotalPos(getAs().getLength());
                ring.setData(r);
                ring.setDoubleStranded(false);
                ring.setColorProvider(colorProvider);
                ring.setRotateOffset(-offset);

                translatedBrickRings.add(ring);
                getLayeredPane().add(ring);
                getLayeredPane().setLayer(ring, JLayeredPane.DEFAULT_LAYER);
            }
        }
        return translatedBrickRings;
    }

    protected BrickRingList getTranslatedBrickRings(boolean forward) {
        BrickRingList ret = new BrickRingList();
        Iterator<BrickRing> itr = getTranslatedBrickRings().iterator();
        while (itr.hasNext()) {
            BrickRing b = itr.next();
            TranslationResult tr = (TranslationResult) b.getData();
            if ((forward && tr.getFrame() > 0) || (!forward && tr.getFrame() < 0)) {
                ret.add(b);
            }
        }
        return ret;
    }

    protected void reinitTranslatedBrickRings() {
        Iterator<BrickRing> itr = translatedBrickRings.iterator();
        while (itr.hasNext()) {
            BrickRing ring = itr.next();
            getLayeredPane().remove(ring);
        }
        translatedBrickRings.clear();
        getTranslatedBrickRings();
    }

    public Boolean isDoubleStranded() {
        return doubleStranded;
    }

    public void setDoubleStranded(Boolean doubleStranded) {
        Boolean old = this.doubleStranded;
        this.doubleStranded = doubleStranded;
        firePropertyChange("doubleStranded", old, this.doubleStranded);
    }

    protected Rectangle getBoundsOnScreen() {
        Rectangle ret = null;
        Point loc = UIUtil.getLocOnScreen(this);
        if (loc != null) {
            ret = getBounds();
            ret.setLocation(loc);
        }
        return ret;
    }

    public void setSelectedLoc(Loc loc) {
        Loc old = this.selectedLoc;
        this.selectedLoc = loc;
        firePropertyChange("selectedLoc", old, this.selectedLoc);
    }

    public void setSelectedFeture(Feture feture) {
        Feture old = this.selectedFeture;
        this.selectedFeture = feture;
        firePropertyChange("selectedFeture", old, this.selectedFeture);
    }

    public Loc getSelectedLoc() {
        return selectedLoc;
    }

    protected CircularOverlay getOverlay() {
        if (overlayRef == null || overlayRef.get() == null) {
            CircularOverlay overlay = new CircularOverlay();
            overlayRef = new WeakReference<CircularOverlay>(overlay);
            getLayeredPane().add(overlay);
            getLayeredPane().setLayer(overlay, OVERLAY_LAYER);
        }
        return overlayRef.get();
    }

    public JLayeredPane getLayeredPane() {
        return layeredPaneRef.get();
    }

    protected Insets getDesiredInsets() {
        FontMetrics fm = FontUtil.getFontMetrics(this);
        final int height = fm.getHeight();
        Insets ret = new Insets(height * 2, height * 3, height * 2, height * 3);
        return ret;
    }

    protected RingGraphPanelListeners.AWTListener getAWTListener() {
        if (awtListener == null) {
            awtListener = new RingGraphPanelListeners.AWTListener(this);
        }
        return awtListener;
    }

    public AnnotatedSeq getAs() {
        if (asRef != null) {
            return asRef.get();
        } else {
            return null;
        }
    }

    public void setAs(AnnotatedSeq as) {
        AnnotatedSeq old = getAs();
        asRef = new WeakReference<AnnotatedSeq>(as);
        firePropertyChange("as", old, getAs());
    }

    public void setTranslationResults(Set<TranslationResult> translationResults) {
        reinitTranslatedBrickRings();
        revalidate();
    }

    private RingListMap createRingListMap() {
        RingListMap ret = new RingListMap();
        if (getAs() != null) {
            ret.createRings(getAs());
        }
        return ret;
    }

    public SortedRingListMap getSortedRingMap() {
        if (sortedRingMap.isEmpty()) {
            RingListMap _ringListMap = createRingListMap();
            sortedRingMap.create(_ringListMap);
        }
        return sortedRingMap;
    }

    @Override
    public Point getCaretLocation(Point ptScreen) {
        Point2D ret2d = null;
        if (getBrickRing() != null) {
            ret2d = getBrickRing().getCaretLocation(ptScreen);
        }
        Point ret = null;
        if (ret2d != null) {
            ret = new Point();
            ret.setLocation(ret2d.getX(), ret2d.getY());
            ret = SwingUtilities.convertPoint(getBrickRing(), ret, this);
        }
        return ret;
    }

    @Override
    public Integer getCaretWidth() {
        return 2;
    }

    @Override
    public Integer getCaretHeight() {
        return 2 * getBrickRing().getFullRingThickness();
    }

    @Override
    public void fireCaretMoveEvent(CaretMoveEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(ICaretParentListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Point loc, boolean forward) {
        int startPos;
        int endPos;
        if (selectedLoc == null) {
            if (forward) {
                startPos = endPos = loc.x;
            } else {
                startPos = endPos = loc.x - 1;
                if (startPos == 0) {
                    startPos = endPos = getAs().getLength();
                }
            }
        } else {
            startPos = selectedLoc.getStart();
            endPos = selectedLoc.getEnd();
        }

        if (startPos <= 0) {
            return;
        }

        getAs().removeSeq(startPos, endPos);

        refresh(startPos);
        getMolPane().getSidePanel().refresh();
        getRingPane().getMinimap().refresh();
        setCanSave();
    }

    @Override
    public Integer getTotalPos() {
        return getAs().getLength();
    }

    MolPane getMolPane() {
        if (molPaneRef == null || molPaneRef.get() == null) {
            MolPane molPane = UIUtil.getParent(this, MolPane.class);
            molPaneRef = new WeakReference<MolPane>(molPane);
        }
        return molPaneRef.get();
    }

    protected void refresh(Integer caretPos) {
        // update the curvedBrackets
        reinitCurvedBrackets();

        // update the GC result        
        getCirLinePlotsComp().clear();


        // update the brick ring(main sequence)
        getBrickRing().setEndPos(getAs().getLength());
        getBrickRing().setBases(getAs().getSiquence().getData());

        // update the translation result
        reinitTranslatedBrickRings();

        // update the ruler
        getCircularRuler().setRange(new Loc(1, getAs().getLength()));
        getCircularRuler().setSelection(null);
        // clear the rings, let the RingGraphPanelLayout to handle the rest
        getSortedRingMap().clear();

        // clear the label list, let the RingGraphPanelLayout to handle the rest 
        getLabelList().clear();

        // clear the connectors, let the RingGraphPanelLayout to handle the rest 
        getConnectors().clear();

        if (caretPos != null) {
            getCaret().setPos(new Point(caretPos, 1));
        }
        if (selectedLoc != null) {
            //setSelectedLoc(null);
        }
        revalidate();
    }

    @Override
    public Point getCaretPos() {
        Point ret = null;
        Double angdeg = getCaret().getAngdeg();
        Integer caretPos = getBrickRing().getCaretPos(angdeg);
        if (caretPos != null) {
            ret = new Point();
            ret.x = caretPos;
            ret.y = 1;
        }
        return ret;
    }

    @Override
    public Point getCaretPos(Point ptScreen) {
        Point ret = null;
        Point p = UIUtil.convertPointFromScreen(ptScreen, this);
        final Dimension size = getSize();
        final Point2D origin = new Point.Double(size.width * 0.5, size.height * 0.5);
        Double angdeg = MathUtil.getAngleInDegrees(p, origin);
        Integer x = getBrickRing().getCaretPos(angdeg);
        if (x != null) {
            ret = new Point(x, 1);
        }
        return ret;
    }

    @Override
    public void insert(Point pos, Character c) {
        insert(pos, c.toString());
    }

    @Override
    public void insert(Point pos, String c) {
        boolean valid = validateInput(c);
        if (!valid) {
            return;
        }
        getAs().insertSeq(pos.x, c.toString());
        Integer caretPos = pos.x + 1;
        caretPos = Math.min(caretPos, getAs().getLength());
        getMolPane().getSidePanel().refresh();
        refresh(caretPos);
        getRingPane().getMinimap().refresh();
        setCanSave();
    }

    private void setCanSave() {
        getAs().setLastModifiedDate(new Date());
        AbstractSavableEditor editor = UIUtil.getParent(this, AbstractSavableEditor.class);
        if (editor != null) {
            editor.setCanSave();
        }
    }

    private boolean validateInput(String c) {
        boolean ret;
        boolean isDNA = getAs().isDNA();
        if (isDNA) {
            ret = BioUtil.areNonambiguousDNAs(c);
        } else {
            ret = BioUtil.areProteins(c);
        }
        return ret;
    }

    @Override
    public Loc2D getSelection() {
        Loc2D ret = null;
        if (selectedLoc != null) {
            ret = new Loc2D();
            ret.x1 = selectedLoc.getStart();
            ret.x2 = selectedLoc.getEnd();
            ret.y1 = ret.y2 = 1;
        }
        return ret;
    }

    /*
     * Caret can be created only once
     */
    @Override
    public JCaret getCaret() {
        if (caretRef == null || caretRef.get() == null) {
            JCaret caret = new JCaret();
            caretRef = new WeakReference<JCaret>(caret);
            caret.setSize(90, 90);
            caret.install(this);
            caret.setState(isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
            getLayeredPane().add(caret);
            getLayeredPane().setLayer(caret, CARET_LAYER);
        }
        return caretRef.get();
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public boolean isEditingAllowed() {
        return editingAllowed;
    }

    public void setEditingAllowed(boolean allowed) {
        boolean old = this.editingAllowed;
        this.editingAllowed = allowed;
        firePropertyChange("editingAllowed", old, this.editingAllowed);
    }

    @Override
    public void replace(Loc2D selection, String c) {
        c = c.trim();
        int totalPos = getAs().getLength();
        if (totalPos <= selection.width()) {
            return;
        }
        getAs().replace(selection.getX1(), selection.getX2(), c);
        int caretPos = selection.getX1() + c.length();
        getMolPane().getSidePanel().refresh();
        refresh(caretPos);
        getRingPane().getMinimap().refresh();
        setCanSave();
    }

    @Override
    public Point getCaretLocationByPos(Point pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public Integer getCaretRadius() {
        return Math.round(getBrickRing().getSize().width * 0.5f);
    }

    protected static class FetureMetaData {

        public String name;
        public int totalPos;
        public int featureCount;

        public Float getAvgFeatureLength() {
            Float ret = 1.0f * totalPos / featureCount;
            return ret;
        }
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
        Character c = e.getKeyChar();
        return isInputValid(c.toString());
    }

    @Override
    public Loc getTotalLoc() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPaintVisibleOnly() {
        return paintVisibleOnly;
    }
}