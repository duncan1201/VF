/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.light.Text;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class MSAScroll extends JScrollPane {

    ViewUI viewUI;
    RowHeaderUI rowHeaderUI;
    ColumnHeaderUI columnHeaderUI;
    CornerUI cornerUI;
    WeakReference<MSA> msaRef;
    private IColorProvider colorProvider;
    private Loc2D selection;

    public MSAScroll() {
        viewUI = new ViewUI();
        rowHeaderUI = new RowHeaderUI();
        columnHeaderUI = new ColumnHeaderUI();
        cornerUI = new CornerUI();

        setRowHeaderView(rowHeaderUI);
        setColumnHeaderView(columnHeaderUI);
        setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerUI);

        getViewport().setView(viewUI);

        UIUtil.disableScrolling(this);

        hookupListeners();
    }
    
    /**
     * @param name: entry name or "consensus"
     */
    void setSelectedRow(String name){
        if(name.equalsIgnoreCase("consensus")){
            setSelection(new Loc2D(1, 1, msaRef.get().getLength(), 1));
            getCornerUI().selectedConsensus(true);
        }else{
            Integer rowNo = viewUI.getMsaComp().getRowNo(name) + 1;
            setSelection(new Loc2D(1, rowNo, msaRef.get().getLength(), rowNo));
            rowHeaderUI.setSelectedRow(name);
            getCornerUI().selectedConsensus(false);
        }
    }

    /*
     * @param selection: 1-based two dimensional position. y coordicate is from top to bottom.  
     * the width and height are always > 0 because doesn't need to know the direction.
     */
    protected void setSelection(Loc2D selection) {
        Loc2D old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }

    public void refreshUI() {

        getViewUI().getMsaComp().createUIObjects();
        getViewUI().getMsaComp().repaint();

        getColumnHeaderUI().getColumnHeaderComp().recalculateRefreshConsensusUI();
        getColumnHeaderUI().getColumnHeaderComp().createUIPlotObjects();
        getColumnHeaderUI().getColumnHeaderComp().initPreferredHeight();
        getColumnHeaderUI().getColumnHeaderComp().repaint();
    }

    protected Loc2D getSelection() {
        return selection;
    }

    private void hookupListeners() {
        addPropertyChangeListener(new MSAScrollListeners.PtyListener());
        MSAScrollListeners.CaretPtyListener caretPtyListener = new MSAScrollListeners.CaretPtyListener(new WeakReference<MSAScroll>(this));
        viewUI.getMsaComp().getCaret().addPropertyChangeListener(caretPtyListener);
        columnHeaderUI.getCaret().addPropertyChangeListener(caretPtyListener);
        viewUI.getMsaComp().addPropertyChangeListener(new MSAScrollListeners.MsaCompPtyListener(new WeakReference<MSAScroll>(this)));

        MSAScrollListeners.MouseAptr mouseAptr = new MSAScrollListeners.MouseAptr();
        addMouseListener(mouseAptr);
        addMouseMotionListener(mouseAptr);
        getColumnHeaderUI().addComponentListener(new MSAScrollListeners.ColumnHeaderCompAptr(this));
        getViewUI().addComponentListener(new MSAScrollListeners.ViewUICompAptr(this));

    }

    /**
     *@return entry name or "consensus"
     */
    String getRowName(Point pt) {
        String ret = null;
        Rectangle viewUIRect = UIUtil.convertRect(viewUI, viewUI.getVisibleRect(), this);
        Rectangle cornerUIRect = UIUtil.convertRect(cornerUI, cornerUI.getVisibleRect(), this);
        Rectangle columnHeaderUIRect = UIUtil.convertRect(columnHeaderUI, columnHeaderUI.getVisibleRect(), this);
        Rectangle rowHeaderUIRect = UIUtil.convertRect(rowHeaderUI, rowHeaderUI.getVisibleRect(), this);
        
        if (viewUIRect.contains(pt)) {
            Point ptConverted = SwingUtilities.convertPoint(this, pt, viewUI.getMsaComp());
            ret = viewUI.getMsaComp().getRowName(ptConverted);
        } else if (cornerUIRect.contains(pt) || columnHeaderUIRect.contains(pt)) {
            ret = "consensus";
        } else if (rowHeaderUIRect.contains(pt)) {
            Point ptConverted = SwingUtilities.convertPoint(this, pt, rowHeaderUI);
            Text text = rowHeaderUI.textList.getText(ptConverted);
            ret = text.getStr();
        }
        return ret;
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    public void setMsa(MSA msa) {
        MSA old = getMsa();
        this.msaRef = new WeakReference<MSA>(msa);
        firePropertyChange("msa", old, getMsa());
    }

    public MSA getMsa() {
        if (msaRef != null) {
            return msaRef.get();
        } else {
            return null;
        }
    }

    public ViewUI getViewUI() {
        return viewUI;
    }

    public ColumnHeaderUI getColumnHeaderUI() {
        return columnHeaderUI;
    }

    public CornerUI getCornerUI() {
        return cornerUI;
    }

    public RowHeaderUI getRowHeaderUI() {
        return rowHeaderUI;
    }
}
