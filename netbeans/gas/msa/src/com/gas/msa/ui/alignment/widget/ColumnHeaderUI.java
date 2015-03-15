/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.misc.PosIndicator;
import com.gas.common.ui.caret.CaretMoveEvent;
import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.caret.ICaretParentListener;
import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.light.BaseList;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import com.gas.common.ui.util.Pref;
import com.gas.msa.ui.MSAEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.Locale;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoableEditSupport;

/**
 *
 * @author dq
 */
public class ColumnHeaderUI extends JPanel implements ICaretParent {

    private WeakReference<MSAScroll> msaScrollRef;
    private WeakReference<ColumnHeaderComp> columnHeaderCompRef;
    private WeakReference<MSA> msaRef;
    private IColorProvider colorProvider;
    private WeakReference<JCaret> caretRef;
    private boolean editingAllowed = Pref.CommonPtyPrefs.getInstance().getEditable();
    private PosIndicator posIndicator;
    private PosIndicator movingIndicator;
    private UndoableEditSupport editSupport;

    public ColumnHeaderUI() {
        initComponents();
        hookupListeners();

        editSupport = new UndoableEditSupport(this);
    }

    private void initComponents() {
        setBackground(new Color(250, 250, 250));
        setLayout(new ColumnHeaderUILayout());
        //setLayout(new BorderLayout());

        ColumnHeaderComp columnHeaderComp = new ColumnHeaderComp();
        columnHeaderCompRef = new WeakReference<ColumnHeaderComp>(columnHeaderComp);

        JCaret caret = new JCaret();
        caretRef = new WeakReference<JCaret>(caret);
        caret.install(this);
        caret.setSize(getCaretWidth(), getCaretHeight());
        caret.setState(JCaret.STATE.OFF);
        caret.setPos(new Point(1, 1));

        posIndicator = new PosIndicator();
        movingIndicator = new PosIndicator();

        UIUtil.setWidth(posIndicator, 1);
        UIUtil.setWidth(movingIndicator, 1);

        add(posIndicator);
        add(movingIndicator);
        add(caret);
        add(columnHeaderComp);
    }

    @Override
    public Integer getTotalPos() {
        return getMsa().getLength();
    }

    public void setPaintVisibleOnly(boolean paintVisibleOnly) {
        columnHeaderCompRef.get().setPaintVisibleOnly(paintVisibleOnly);
    }

    protected PosIndicator getPosIndicator() {
        return posIndicator;
    }

    public ColumnHeaderComp getColumnHeaderComp() {
        if (columnHeaderCompRef == null) {
            return null;
        } else {
            return columnHeaderCompRef.get();
        }
    }

    protected void setPosIndicatorLoc(int x) {
        posIndicator.setLocation(x, getColumnHeaderComp().getRulerHeight() + ColumnHeaderComp.GAP);
    }

    protected void setMovingIndicatorLoc(int x) {
        movingIndicator.setLocation(x, getColumnHeaderComp().getRulerHeight() + ColumnHeaderComp.GAP);
    }

    protected PosIndicator getMovingIndicator() {
        return movingIndicator;
    }

    private void hookupListeners() {
        getCaret().addPropertyChangeListener(new ColumnHeaderUIListeners.CaretPtyListener());
        addPropertyChangeListener(new ColumnHeaderUIListeners.PtyListener());
        addComponentListener(new ColumnHeaderUIListeners.CompAdptr());
        getColumnHeaderComp().addPropertyChangeListener(new ColumnHeaderUIListeners.CompPtyListener(new WeakReference<ColumnHeaderUI>(this)));
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    public void setMsa(MSA msa) {
        MSA old = getMsa();
        msaRef = new WeakReference<MSA>(msa);
        firePropertyChange("msa", old, getMsa());
    }

    private MSA getMsa() {
        if (msaRef == null) {
            return null;
        } else {
            return msaRef.get();
        }
    }

    /*
     * @ret the caret coordinate(x,y) relative to the caret parent
     */
    @Override
    public Point getCaretLocation(Point pScreen) {
        BaseList consensusList = getColumnHeaderComp().getConsensusList();
        SwingUtilities.convertPointFromScreen(pScreen, this);
        Integer caretX = consensusList.getCaretX(pScreen.x);
        Point ret = null;
        if (caretX != null) {
            ret = new Point(caretX, MathUtil.round(consensusList.getRect().getY()));
        }
        return ret;
    }

    @Override
    public Point getCaretLocationByPos(Point pos) {
        BaseList consensusList = getColumnHeaderComp().getConsensusList();
        Point ret = consensusList.getCaretLoc(pos.x);
        return ret;
    }

    @Override
    public Point getCaretMaxPos() {
        Point ret = null;
        BaseList consensusList = getColumnHeaderComp().getConsensusList();
        if (consensusList != null && !consensusList.isEmpty()) {
            ret = new Point();
            ret.x = consensusList.size();
            ret.y = 1;
        }
        return ret;
    }

    protected void moveCaretToFirstRow(int xPos, Integer startPos, Integer endPos) {
        BaseList consensusList = getColumnHeaderComp().getConsensusList();
        int _y = MathUtil.round(consensusList.getRect().getY());
        getCaret().setPos(new Point(xPos, 1));
        getCaret().setState(isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
        if (startPos != null && endPos != null) {
            setSelection(new Loc2D(startPos, 1, endPos, 1));
        }
        requestFocusInWindow();
    }

    protected void highlight(int startPos, int endPos) {
    }

    private MSAScroll getMsaScroll() {
        if (msaScrollRef == null || msaScrollRef.get() == null) {
            MSAScroll scroll = UIUtil.getParent(this, MSAScroll.class);
            msaScrollRef = new WeakReference<MSAScroll>(scroll);
        }
        return msaScrollRef.get();
    }

    @Override
    public final Integer getCaretWidth() {
        if (getCaret().isInsertMode()) {
            int ret = Math.round(1.0f * getSize().width / getColumnHeaderComp().getConsensusList().size());
            return ret;
        } else {
            return 2;
        }
    }

    @Override
    public final Integer getCaretHeight() {
        return getColumnHeaderComp().calculateConsensusHeight();
    }

    @Override
    public void fireCaretMoveEvent(CaretMoveEvent event) {
    }

    @Override
    public void addListener(ICaretParentListener listener) {
    }

    @Override
    public void caretMoved(int direction, boolean isShiftDown, boolean success) {
        Point ret = getCaret().getLocation();
        if (direction == SwingConstants.EAST) {
            if (success) {
                // adjust the scroll bar        
                Rectangle visibleRect = getVisibleRect();
                if (ret.getX() + 20 > visibleRect.getX() + visibleRect.getWidth()) {
                    JScrollBar scrollBar = getMsaScroll().getHorizontalScrollBar();
                    int v = scrollBar.getValue();
                    int newV = MathUtil.round(v + ret.getX() + 20 - (visibleRect.getX() + visibleRect.getWidth()));
                    if (newV < scrollBar.getMaximum()) {
                        scrollBar.setValue(newV);
                    }
                }
            }
        } else if (direction == SwingConstants.WEST) {
            if (success) {
                // adjust the scroll bar
                Rectangle visibleRect = getVisibleRect();
                if (ret.getX() - 20 < visibleRect.getX()) {
                    JScrollBar scrollBar = getMsaScroll().getHorizontalScrollBar();
                    int v = scrollBar.getValue();
                    int newV = MathUtil.round(v - (visibleRect.getX() - (ret.getX() - 20)));
                    newV = Math.max(newV, scrollBar.getMinimum());
                    if (newV >= scrollBar.getMinimum()) {
                        scrollBar.setValue(newV);
                    }
                }
            }
        } else if (direction == SwingConstants.NORTH) {
            // do nothing
        } else if (direction == SwingConstants.SOUTH) {
            getCaret().setState(JCaret.STATE.OFF);
            getMsaScroll().getViewUI().getMsaComp().moveCaretToFirstRow(getCaret().getPos().x);
        }

        if (success) {
            // clear the selection is the caret moves
            if (ret != null && !isShiftDown) {
                getMsaScroll().setSelection(null);
            }
        }

        if (isShiftDown) {
            updateSeqSelection(direction, success);
        }
    }

    private void updateSeqSelection(int direction, boolean success) {
        boolean noChange = false;
        boolean clearSelect = false;
        boolean selectPrev = false;
        boolean selectCur = false;
        boolean incrementMaxX = false;
        boolean incrementMinX = false;
        boolean decrementMinX = false;
        boolean decrementMaxX = false;
        Loc2D loc = getSelection();
        Point caretPos = getCaretPos();
        if (direction == SwingConstants.EAST) {
            if (!success) {
                noChange = true;
            } else if (loc == null) {
                selectPrev = true;
            } else if (caretPos.x == loc.getMaxX() + 2) {
                incrementMaxX = true;
            } else if (caretPos.x == loc.getMinX() + 1) {
                if (loc.getMinX() == loc.getMaxX()) {
                    clearSelect = true;
                } else {
                    incrementMinX = true;
                }
            }
        } else if (direction == SwingConstants.WEST) {
            if (!success) {
                noChange = true;
            } else if (loc == null) {
                selectCur = true;
            } else if (caretPos.x == loc.getMinX() - 1) {
                decrementMinX = true;
            } else if (caretPos.x == loc.getMaxX()) {
                if (loc.getMinX() == loc.getMaxX()) {
                    clearSelect = true;
                } else {
                    decrementMaxX = true;
                }
            }
        } else if (direction == SwingConstants.SOUTH) {
            Loc2D msaSelection = getMsaScroll().getViewUI().getMsaComp().getSelection();
            if (msaSelection == null) {
                noChange = true;
            } else {
                clearSelect = true;
            }
        } else if (direction == SwingConstants.NORTH) {
            noChange = true;
        }

        Loc2D newLoc = null;
        if (clearSelect) {
            // do nothing
        } else if (noChange) {
            if (loc != null) {
                newLoc = new Loc2D(loc);
            }
        } else if (selectCur) {
            newLoc = new Loc2D(caretPos.x, 1, caretPos.x, 1);
        } else if (selectPrev) {
            newLoc = new Loc2D(caretPos.x - 1, 1, caretPos.x - 1, 1);
        } else if (incrementMinX) {
            newLoc = new Loc2D(loc);
            newLoc.setMinX(loc.getMinX() + 1);
        } else if (incrementMaxX) {
            newLoc = new Loc2D(loc);
            newLoc.setMaxX(loc.getMaxX() + 1);
        } else if (decrementMaxX) {
            newLoc = new Loc2D(loc);
            newLoc.setMaxX(loc.getMaxX() - 1);
        } else if (decrementMinX) {
            newLoc = new Loc2D(loc);
            newLoc.setMinX(loc.getMinX() - 1);
        }
        setSelection(newLoc);
        if ((direction == SwingConstants.WEST || direction == SwingConstants.EAST) && success) {
            Integer startX = newLoc != null ? newLoc.getMinX() : null;
            Integer endX = newLoc != null ? newLoc.getMaxX() : null;
            getMsaScroll().getViewUI().getMsaComp().selectByColumn(startX, endX);
        }
    }

    private void setSelection(Loc2D loc2d) {
        this.columnHeaderCompRef.get().setSelection(loc2d);
    }

    protected boolean validateInput(String c) {
        boolean ret = true;
        if (getMsa().isDnaByGuess()) {
            ret = BioUtil.areNonambiguousDNAs(c) || BioUtil.isGaps(c);
        } else {
            ret = BioUtil.areProteins(c) || BioUtil.isGaps(c);
        }
        return ret;
    }

    @Override
    public Loc2D getSelection() {
        return getColumnHeaderComp().getSelection();
    }

    @Override
    public void delete(final Point pos, boolean forward) {
        if (!forward) {
            pos.x = pos.x - 1;
        }
        getMsa().delete(new Loc2D(pos.x, 1, pos.x, getMsa().getEntriesCount() + 1));
        getMsaScroll().refreshUI();

        if (!forward) {
            getCaret().setPos(new Point(pos.x, 1));
        }

        setCanSave();
    }

    private void setCanSave() {
        MSAEditor editor = UIUtil.getParent(this, MSAEditor.class);
        if (editor != null) {
            editor.setCanSave();
        }
    }

    public Integer getInsertPos(int xScreen) {
        BaseList consensusList = getColumnHeaderComp().getConsensusList();
        int caretX = consensusList.getCaretX(xScreen - getLocationOnScreen().x);
        Integer ret = getColumnHeaderComp().getConsensusList().getCaretPos(caretX);
        return ret;
    }

    @Override
    public Point getCaretPos() {
        Point ret = null;
        Point caretLoc = getCaret().getLocation();
        Integer caretPos = getColumnHeaderComp().getConsensusList().getCaretPos(caretLoc.x);
        if (caretPos != null) {
            ret = new Point(caretPos, 1);
        }
        return ret;
    }

    @Override
    public Point getCaretPos(Point ptScreen) {
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        Integer caretX = getColumnHeaderComp().getConsensusList().getCaretPos(pt.x);
        if (caretX != null) {
            return new Point(caretX, 1);
        } else {
            return null;
        }
    }

    @Override
    public void insert(Point pos, Character c) {
        insert(pos, c.toString());
    }

    @Override
    public void insert(Point pos, String c) {
        c = c.toUpperCase(Locale.ENGLISH);
        c = c.replaceAll("U", "T");
        c = c.replaceAll("u", "t");
        boolean valid = validateInput(c);
        if (!valid) {
            return;
        }

        getMsa().insert(pos, c.toString());
        getMsaScroll().refreshUI();

        // adjust the caret location      
        getCaret().setPos(new Point(pos.x + 1, 1));

        setCanSave();
    }

    @Override
    public JCaret getCaret() {
        if (caretRef != null) {
            return caretRef.get();
        } else {
            return null;
        }
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public boolean isEditingAllowed() {
        return editingAllowed;
    }

    public void setEditingAllowed(boolean editingAllowed) {
        boolean old = this.editingAllowed;
        this.editingAllowed = editingAllowed;
        firePropertyChange("editingAllowed", old, this.editingAllowed);
    }

    @Override
    public void replace(Loc2D selection, String c) {
        getMsa().replace(selection, c.toUpperCase(Locale.ENGLISH));

        // update the UI
        getMsaScroll().refreshUI();

        // clear the selection
        getMsaScroll().setSelection(null);

        // adjust the caret position        
        getCaret().setPos(new Point(selection.getMinX(), 1));

        setCanSave();
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
        boolean dna = msaRef.get().isDNA();
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
