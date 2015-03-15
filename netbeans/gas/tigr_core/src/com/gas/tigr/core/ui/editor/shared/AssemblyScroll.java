/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.ruler.IRulerParent;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.ckpanel.ChromatogramComp2;
import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class AssemblyScroll extends JScrollPane implements IRulerParent {
    
    final static FontMetrics FM = FontUtil.getDefaultFontMetrics();
    public final static Insets INSETS = new Insets(0, 0, 0, FM.charWidth('A') * 3);
    private ColumnHeaderView columnHeaderView;
    protected MainComp mainComp;
    private RowHeaderView rowHeaderView;
    private CornerUI cornerUI;
    private float scale = 2f;
    //protected VariantMapMdl variantMapMdl;
    //protected Loc selection;
    private Integer totalPos;

    public AssemblyScroll() {
        super();
        mainComp = new MainComp();
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getViewport().setView(mainComp);
                
        columnHeaderView = new ColumnHeaderView();

        rowHeaderView = new RowHeaderView();
        setRowHeaderView(rowHeaderView);
        UIUtil.setPreferredHeight(columnHeaderView, columnHeaderView.getDesiredHeight());
        setColumnHeaderView(columnHeaderView);

        cornerUI = new CornerUI();
        setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerUI);

        hookupListeners();
    }

    private void hookupListeners() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AssemblyScrollListeners.AwtListener(this), AWTEvent.MOUSE_EVENT_MASK);
        addPropertyChangeListener(new AssemblyScrollListeners.PtyListener());
        getHorizontalScrollBar().getModel().addChangeListener(new AssemblyScrollListeners.ScrollBarListener(new WeakReference<AssemblyScroll>(this)));
        addComponentListener(new AssemblyScrollListeners.CompAdpt());
    }

    public CornerUI getCornerUI() {
        return cornerUI;
    }
    
    public void setSelection(Loc selection) {      
        mainComp.setSelection(selection);
        if(selection != null){
            Integer centerPos = selection.center();        
            center(centerPos);
        }
    }

    protected void center(Integer pos) {
        if(pos == null){
            return;
        }
        BoundedRangeModel mdl = getHorizontalScrollBar().getModel();
        int max = mdl.getMaximum();
        int extent = mdl.getExtent();
        Float x = UIUtil.toScreen(totalPos, pos, max + 1, 0, SwingConstants.CENTER);

        int value = MathUtil.round(x - extent / 2.0f);
        value = Math.max(0, value);

        getHorizontalScrollBar().setValue(value);

    }

    public void setVariantMapMdl(VariantMapMdl variantMapMdl) {        
        mainComp.setVariantMapMdl(variantMapMdl);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        float old = this.scale;
        this.scale = scale;
        firePropertyChange("scale", old, this.scale);
    }

    public void setBaseFont(Font font) {
        columnHeaderView.setBaseFont(font);
        mainComp.setBaseFont(font);
    }

    public ColumnHeaderView getColumnHeaderView() {
        return (ColumnHeaderView) super.getColumnHeader().getView();
    }

    public RowHeaderView getRowHeaderView() {
        return (RowHeaderView) super.getRowHeader().getView();
    }

    public MainComp getMainComp() {
        return mainComp;
    }

    public void setConsensusSeq(String seq) {
        columnHeaderView.setSeq(seq);        
    }

    public void setQV(List<Integer> qualities) {
        columnHeaderView.setQv(qualities);
    }

    public void setReads(List<ChromatogramComp2.Read> reads, Integer totalPos) {
        setTotalPos(totalPos);
        mainComp.setReads(reads, totalPos);
        rowHeaderView.setReads(reads);
        Integer width = rowHeaderView.calculatePreferredWidth();
        Integer width2 = cornerUI.calculatePreferredWidth();
        if(width == null || width2 == null){
            System.out.println();
        }
        UIUtil.setPreferredWidth(rowHeaderView, Math.max(width, width2));
    }

    public void setTotalPos(Integer totalPos) {
        this.totalPos = totalPos;
    }

    protected ICaretParent getCaretParent(Point ptOnScreen) {
        ICaretParent ret = null;
        if (ret == null) {
            ret = columnHeaderView.getCaretParent(ptOnScreen);
        }
        return ret;
    }

    protected void setCaretEnablement(ICaretParent caretParent) {
        if (caretParent == null) {
            return;
        }
        columnHeaderView.setCaretEnablement(caretParent);
    }

    @Override
    public int[] getHorizontalBounds() {
        int[] ret = new int[2];        
        Rectangle bounds = UIUtil.getBoundsNoBorder(columnHeaderView);
        ret[0] = bounds.x;
        ret[1] = bounds.x + bounds.width - 1;
        return ret;
    }
}
