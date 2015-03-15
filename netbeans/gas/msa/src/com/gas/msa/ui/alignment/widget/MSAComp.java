/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.linePlot.LinePlot;
import com.gas.common.ui.misc.PosIndicator;
import com.gas.common.ui.caret.CaretMoveEvent;
import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.caret.ICaretParentListener;
import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.light.*;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import com.gas.common.ui.util.Pref;
import com.gas.msa.ui.MSAEditor;
import com.gas.msa.ui.alignment.AlignPane;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class MSAComp extends JComponent implements ICaretParent {

    private JCaret caret;
    private boolean editingAllowed = Pref.CommonPtyPrefs.getInstance().getEditable();
    private PosIndicator posIndicator;
    private PosIndicator movingIndicator;
    private LinePlot linePlot;
    protected BaseListList alignmentList = new BaseListList();
    private MSA msa;
    private IColorProvider colorProvider;
    private WeakReference<MSAScroll> msaScrollRef;
    private Font textFont = FontUtil.getDefaultMSFont();
    private final float WIDTH_RATIO = 2;
    private int zoom;
    protected RectOverlei overlei = new RectOverlei();
    protected Loc spotLight;
    protected Loc2D selection;
    private CNST.PAINT paintState;
    JPopupMenu popupMenu;
    private boolean paintVisibleOnly = true;

    public MSAComp() {
        setOpaque(false);
        final Insets insets = AlignPane.getPadding();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));

        caret = new JCaret();
        caret.install(this);
        caret.setSize(getCaretWidth(), getCaretHeight());
        caret.setState(isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
        caret.setPos(new Point(1, 1));
        UIUtil.setX(caret, insets.left);
        posIndicator = new PosIndicator();
        UIUtil.setWidth(posIndicator, 1);
        movingIndicator = new PosIndicator();
        UIUtil.setWidth(movingIndicator, 1);

        requestFocusInWindow();

        hookupListeners();
    }

    /**
     * @param row: 1-based, starting from the aligned seq
     */
    public String getRowName(Integer row) {
        return this.alignmentList.getName(row);
    }

    public String getRowName(Point pt) {
        return this.alignmentList.getName(pt);
    }

    public void setPaintVisibleOnly(boolean paintVisibleOnly) {
        boolean old = this.paintVisibleOnly;
        this.paintVisibleOnly = paintVisibleOnly;
        firePropertyChange("paintVisibleOnly", old, this.paintVisibleOnly);
    }

    @Override
    public Loc2D getSelection() {
        return selection;
    }

    public void setPaintState(CNST.PAINT paintState) {
        CNST.PAINT old = this.paintState;
        this.paintState = paintState;
        firePropertyChange("paintState", old, this.paintState);
    }

    public Rectangle2D getAlignmentListRect() {
        return alignmentList.getRect();
    }

    /**
     * @return 1-based, starting from the alignment
     */
    Integer getRowNo(String rowName) {
        return alignmentList.getRowNo(rowName);
    }

    void selectByColumn(Integer startPos, Integer endPos) {
        if (startPos != null && endPos != null) {
            Point m = getCaretMaxPos();
            setSelection(new Loc2D(startPos, 1, endPos, m.x));
        } else {
            setSelection(null);
        }
    }

    protected void setSelection(Loc2D selection) {
        Loc2D old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }

    protected PosIndicator getPosIndicator() {
        return posIndicator;
    }

    protected PosIndicator getMovingIndicator() {
        return movingIndicator;
    }

    public void setZoom(int z) {
        int old = this.zoom;
        this.zoom = z;
        firePropertyChange("zoom", old, this.zoom);
    }

    private void hookupListeners() {
        getCaret().addPropertyChangeListener(new MSACompListeners.CaretPtyListener());
        addPropertyChangeListener(new MSACompListeners.PtyListener());
    }

    private void layoutUIObjects() {
        final int baseHeight = getBaseHeight();
        final Dimension size = getSize();
        if (size.width == 0 || size.height == 0) {
            return;
        }
        final Insets insets = getInsets();

        int _y = 0;

        for (int i = 0; i < alignmentList.size(); i++) {
            BaseList t = alignmentList.get(i);
            Rectangle2D.Double drawingRect = new Rectangle2D.Double(insets.left, _y, size.width - insets.left - insets.right, baseHeight);
            t.setRect(drawingRect);
            _y = _y + baseHeight;
        }

        overlei.setRect(new Rectangle2D.Double(insets.left, 0, size.width - insets.left - insets.right, size.height));

    }

    @Override
    public void paintComponent(Graphics g) {
        boolean go = go();
        if (!go) {
            return;
        }
        setPaintState(CNST.PAINT.STARTED);
        Graphics2D g2d = (Graphics2D) g;
        final Dimension size = getSize();
        final Insets insets = getInsets();
        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);
        final Rectangle visibleRect = getVisibleRect();

        int totalPos = alignmentList.get(0).size();
        double startPos;
        if (paintVisibleOnly) {
            startPos = Math.floor(totalPos * (visibleRect.getX() - insets.left) / (size.width - insets.left - insets.right));
            startPos = Math.max(1, startPos);
        } else {
            startPos = 1;
        }
        double endPos;
        if (paintVisibleOnly) {
            endPos = Math.ceil(totalPos * (visibleRect.getX() - insets.left + visibleRect.getWidth()) / (size.width - insets.left - insets.right));
            endPos = Math.min(totalPos, endPos);
        } else {
            endPos = msa.getLength();
        }
        _paint(g2d, (int) startPos, (int) endPos);
        setPaintState(CNST.PAINT.ENDING);
    }

    protected void setSpotLight(Loc spotLight) {
        Loc old = this.spotLight;
        this.spotLight = spotLight;
        firePropertyChange("spotLight", old, this.spotLight);
    }

    protected void moveCaretToFirstRow(int posX) {
        caret.setPos(new Point(posX, 1));
        caret.setState(isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
        requestFocusInWindow();
    }

    private void _paint(Graphics2D g2d, int startPos, int endPos) {
        g2d.setFont(textFont);

        layoutUIObjects();

        for (int i = 0; i < alignmentList.size(); i++) {
            BaseList t = alignmentList.get(i);
            t.paint(g2d, startPos, endPos);
        }
        if (!alignmentList.isEmpty()) {
            overlei.setTotalPos(alignmentList.get(0).size());
            overlei.paint(g2d, startPos, endPos);
        }
    }

    private int getBaseHeight() {
        final FontMetrics fm = FontUtil.getFontMetrics(textFont);
        int baseHeight = Math.round(fm.getHeight() * 1.25f);
        return baseHeight;
    }

    public IColorProvider getColorProvider() {
        return colorProvider;
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    private boolean go() {
        boolean ret = true;
        Dimension size = getSize();
        if (msa == null) {
            ret = false;
        } else if (size.width <= 0 || size.height <= 0) {
            ret = false;
        }
        return ret;
    }

    public void setMsa(MSA msa) {
        MSA old = this.msa;
        this.msa = msa;
        firePropertyChange("msa", old, this.msa);
    }

    public int calculateNormalWidth() {
        int ret = msa.getConsensus().length() * calculateNormalUnitWidth();
        return ret;
    }

    int calculateNormalUnitWidth() {
        final FontMetrics fm = FontUtil.getFontMetrics(textFont);
        final int strWidth = fm.charWidth('A');
        int ret = Math.round(strWidth * WIDTH_RATIO);
        return ret;
    }

    float calculateUnitWidth() {

        return calculateNormalUnitWidth() * zoom / 100f;
    }

    protected Integer calculatePreferredWidth() {
        if (msa == null) {
            return 0;
        }
        int ret;

        JViewport viewport = UIUtil.getParent(this, JViewport.class);
        int viewportWidth = viewport.getWidth();

        Insets insets = getInsets();

        int normalWidth = calculateNormalWidth();
        if (viewportWidth == 0) {

            ret = Math.round(normalWidth * zoom / 100f);
            ret += insets.left + insets.right;
        } else if (normalWidth >= viewportWidth) {
            ret = Math.max(Math.round(normalWidth * zoom / 100f + insets.left + insets.right), viewportWidth);
        } else {
            //ret = Math.round(normalWidth * zoom / 100f);
            //ret += insets.left + insets.right;
            ret = viewportWidth;
        }
        return ret;
    }

    @Override
    public void caretMoved(int direction, boolean isShiftDown, boolean success) {
        Point ret = getCaret().getLocation();
        if (direction == SwingConstants.EAST) {
            if (success) {
                // adjusting the scroll bar
                Rectangle visibleRect = getVisibleRect();
                JScrollBar scrollBar = getMsaScroll().getHorizontalScrollBar();
                if (ret.x + 20 > visibleRect.x + visibleRect.width) {
                    int v = scrollBar.getValue();
                    int vNew = v + ret.x + 20 - (visibleRect.x + visibleRect.width);
                    scrollBar.setValue(vNew);
                } else if (ret.x - 20 < visibleRect.x) {
                    scrollBar.setValue(visibleRect.x - (ret.x - 20));
                }
                if (isShiftDown) {
                    updateSelection(direction, success);
                }
            }
        } else if (direction == SwingConstants.WEST) {
            if (success) {
                // adjusting the scroll bar
                Rectangle visibleRect = getVisibleRect();
                JScrollBar scrollBar = getMsaScroll().getHorizontalScrollBar();
                if (ret.x - 20 < visibleRect.x) {
                    int v = scrollBar.getValue();
                    int vNew = v - (visibleRect.x - (ret.x - 20));
                    scrollBar.setValue(vNew);
                }
                if (isShiftDown) {
                    updateSelection(direction, success);
                }
            }
        } else if (direction == SwingConstants.NORTH) {
            if (!success) {
                MSAScroll msaScroll = getMsaScroll();
                getCaret().setState(JCaret.STATE.OFF);
                int _x = getCaret().getPos().x;
                Loc2D loc2d = getSelection();
                Integer startX = loc2d != null && isShiftDown ? loc2d.getMinX() : null;
                Integer endX = loc2d != null && isShiftDown ? loc2d.getMaxX() : null;
                msaScroll.getColumnHeaderUI().moveCaretToFirstRow(_x, startX, endX);
            }
            if (isShiftDown) {
                updateSelection(direction, success);
            }
        } else if (direction == SwingConstants.SOUTH) {
            if (success && isShiftDown) {
                updateSelection(direction, success);
            }
        }

        if (success && !isShiftDown) {
            // clear the selection is the caret moves
            if (!isShiftDown) {
                if (ret != null) {
                    getMsaScroll().setSelection(null);
                }
            } else {
            }
        }
    }

    private void updateSelection(int direction, boolean success) {
        boolean incrementMinX = false;
        boolean incrementMaxX = false;
        boolean incrementMinY = false;
        boolean incrementMaxY = false;
        boolean clearSelection = false;
        boolean selectCur = false;
        boolean selectPrev = false;
        boolean decrementMinX = false;
        boolean decrementMaxX = false;
        boolean decrementMaxY = false;
        boolean decrementMinY = false;
        boolean selectNoChange = false;

        Loc2D loc2d = getSelection();
        Point caretPos = getCaretPos();

        if (direction == SwingConstants.EAST) {
            if (loc2d != null) {
                if (caretPos.x == loc2d.getMaxX() + 2) {
                    incrementMaxX = true;
                } else {
                    if (loc2d.getMinX() < loc2d.getMaxX()) {
                        incrementMinX = true;
                    } else {
                        clearSelection = true;
                    }
                }
            } else {
                selectPrev = true;
            }
        } else if (direction == SwingConstants.WEST) {
            if (loc2d != null) {
                if (caretPos.x == loc2d.getMinX() - 1) {
                    decrementMinX = true;
                } else {
                    if (loc2d.getMinX() == loc2d.getMaxX()) {
                        clearSelection = true;
                    } else {
                        decrementMaxX = true;
                    }
                }
            } else {
                selectCur = true;
            }
        } else if (direction == SwingConstants.NORTH) {
            if (loc2d != null) {
                if (!success) {
                    if (loc2d.getMinY() == loc2d.getMaxY() && loc2d.getMinY() == 1) {
                        clearSelection = true;
                    } else {
                        selectNoChange = true;
                    }
                } else if (loc2d.getMinY() == loc2d.getMaxY()) {
                    decrementMinY = true;
                } else if (caretPos.y == loc2d.getMaxY() - 1) {
                    decrementMaxY = true;
                } else if (caretPos.y == loc2d.getMinY() - 1) {
                    decrementMinY = true;
                }
            }
        } else if (direction == SwingConstants.SOUTH) {
            if (loc2d != null) {
                if (caretPos.y == loc2d.getMinY() + 1) {
                    incrementMinY = true;
                } else {
                    incrementMaxY = true;
                }
            }
        }
        Loc2D newLoc2d = null;
        if (clearSelection) {
            // do nothing
        } else if (selectNoChange) {
            newLoc2d = new Loc2D(loc2d.x1, loc2d.y1, loc2d.x2, loc2d.y2);
        } else if (selectPrev) {
            newLoc2d = new Loc2D(caretPos.x - 1, caretPos.y, caretPos.x - 1, caretPos.y);
        } else if (selectCur) {
            newLoc2d = new Loc2D(caretPos.x, caretPos.y, caretPos.x, caretPos.y);
        } else if (incrementMaxX) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMaxX(loc2d.getMaxX() + 1);
        } else if (incrementMinX) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMinX(loc2d.getMinX() + 1);
        } else if (decrementMinX) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMinX(loc2d.getMinX() - 1);
        } else if (decrementMaxX) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMaxX(loc2d.getMaxX() - 1);
        } else if (incrementMaxY) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMaxY(loc2d.getMaxY() + 1);
        } else if (incrementMinY) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMinY(loc2d.getMinY() + 1);
        } else if (decrementMaxY) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMaxY(loc2d.getMaxY() - 1);
        } else if (decrementMinY) {
            newLoc2d = new Loc2D(loc2d);
            newLoc2d.setMinY(loc2d.getMinY() - 1);
        }
        setSelection(newLoc2d);
    }

    MSAPopup getPopupMenu() {
        MSAPopup ret = new MSAPopup();
        return ret;
    }

    void createUIObjects() {
        alignmentList = createAlignmentTextList(msa.getSortedEntries());
        layoutUIObjects();
        Integer width = calculatePreferredWidth();
        if (width == null) {
            revalidate();
            return;
        }
        int height = calculatePreferredHeight();
        UIUtil.setPreferredWidth(this, width);
        UIUtil.setPreferredHeight(this, height);
        UIUtil.setWidth(this, width);
        UIUtil.setHeight(this, height);
    }

    protected ViewUI getViewUI() {
        MSAScroll msaScroll = getMsaScroll();
        if (msaScroll != null) {
            return msaScroll.getViewUI();
        } else {
            return null;
        }
    }

    private int calculatePreferredHeight() {
        int ret = 0;
        ret += msa.getEntriesCount() * getBaseHeight();
        return ret;
    }

    private BaseListList createAlignmentTextList(Collection<MSA.Entry> entries) {
        BaseListList ret = new BaseListList();
        Iterator<MSA.Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            MSA.Entry entry = itr.next();
            BaseList _list = new BaseList();
            _list.setName(entry.getName());
            _list.setColorProvider(colorProvider);
            _list.setTexts(entry.getData().toCharArray());
            ret.add(_list);
        }
        return ret;
    }

    @Override
    public Point getCaretLocation(Point ptScreen) {
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        Point ret = alignmentList.getCaretLocation(pt, 0);
        return ret;
    }

    @Override
    public Point getCaretLocationByPos(Point pos) {
        Point ret = alignmentList.getCaretLocation(pos);
        return ret;
    }

    @Override
    public Point getCaretPos(Point ptScreen) {
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        Point ret = alignmentList.getPos(pt);
        return ret;
    }

    @Override
    public Integer getTotalPos() {
        return msa.getLength();
    }

    @Override
    public Point getCaretMaxPos() {
        Point ret = null;
        if (alignmentList != null && !alignmentList.isEmpty()) {
            ret = new Point();
            ret.x = alignmentList.get(0).size();
            ret.y = alignmentList.size();
        }
        return ret;
    }

    private MSAScroll getMsaScroll() {
        if (msaScrollRef == null || msaScrollRef.get() == null) {
            MSAScroll s = UIUtil.getParent(this, MSAScroll.class);
            msaScrollRef = new WeakReference<MSAScroll>(s);
        }
        return msaScrollRef.get();
    }

    protected void remove(Loc2D loc) {
        remove(loc, false);
    }

    protected void remove(Loc2D loc, boolean maintainLength) {
        for (int row = loc.getMinY(); row <= loc.getMaxY(); row++) {
            BaseList tList = alignmentList.get(row - 1);
            tList.deleteRange(loc.getMinX(), loc.getMaxX(), maintainLength);
        }
    }

    @Override
    public final Integer getCaretWidth() {
        if (!caret.isInsertMode()) {
            return 2;
        } else {
            return calculateNormalUnitWidth();
        }
    }

    @Override
    public final Integer getCaretHeight() {
        return getBaseHeight();
    }

    @Override
    public void fireCaretMoveEvent(CaretMoveEvent event) {
    }

    @Override
    public void addListener(ICaretParentListener listener) {
    }

    protected boolean validateInput(char c) {
        return true;
    }

    @Override
    public void delete(Point pos, boolean forward) {
        Point caretPos = getCaretPos();
        if (!forward) {
            caretPos.x--;
        }
        msa.delete(new Loc2D(caretPos.x, caretPos.y + 1, caretPos.x, caretPos.y + 1));

        getMsaScroll().refreshUI();

        // adjusting the caret position
        if (!forward) {
            getCaret().setPos(new Point(caretPos.x, caretPos.y));
        }

        setCanSave();
    }

    @Override
    public Point getCaretPos() {
        Point ret = null;
        Point caretLoc = getCaret().getCenter();
        ret = alignmentList.getPos(caretLoc);
        return ret;
    }

    /*
     * @ret 1-based
     */
    public Integer getRowNo(int yScreen) {
        Integer ret;
        int y = yScreen - getLocationOnScreen().y;
        ret = alignmentList.getRowNo(y);
        return ret;
    }

    @Override
    public void insert(Point caretPos, Character c) {
        insert(caretPos, c.toString());
    }

    @Override
    public void insert(Point caretPos, String c) {
        msa.insert(new Point(caretPos.x, caretPos.y + c.length()), c);
        getMsaScroll().refreshUI();

        setCanSave();

        getCaret().setPos(new Point(caretPos.x + c.length(), caretPos.y));
    }

    @Override
    public JCaret getCaret() {
        return caret;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public final boolean isEditingAllowed() {
        return editingAllowed;
    }

    public void setEditingAllowed(boolean editingAllowed) {
        boolean old = this.editingAllowed;
        this.editingAllowed = editingAllowed;
        firePropertyChange("editingAllowed", old, this.editingAllowed);
    }

    @Override
    public void replace(Loc2D caretParentSelection, String c) {
        caretParentSelection.translate(0, 1);
        msa.replace(caretParentSelection, c);
        getMsaScroll().refreshUI();

        final int delta = c.length() - caretParentSelection.width();
        if (delta != 0) {
            Point pt = getCaret().getPos();
            getCaret().setPos(new Point(pt.x + delta, pt.y));
        }
        setCanSave();

    }

    private void setCanSave() {
        MSAEditor editor = UIUtil.getParent(this, MSAEditor.class);
        if (editor != null) {
            editor.setCanSave();
        }
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
        boolean ret;

        boolean dna = msa.isDNA();
        if (dna) {
            ret = BioUtil.areNonambiguousDNAs(str);
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
}
