/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.IVisibleLocProvider;
import com.gas.domain.ui.brickComp.BrickComp;
import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.histogram.Histogram;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.ruler.Ruler;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ColumnHeaderView extends JPanel implements IVisibleLocProvider {

    private AssemblyScroll assemblySPane;
    BrickComp concensus;
    Ruler ruler;
    Histogram qualityHistogram;
    String seq;
    List<Integer> qv = new ArrayList<Integer>();
    Font baseFont = FontUtil.getDefaultMSFont();
    CNST.LAYOUT layoutState = null;

    public ColumnHeaderView() {
        Insets insets = AssemblyScroll.INSETS;
        setBorder(BorderFactory.createEmptyBorder(0, insets.left ,0 ,insets.right));
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        LayoutManager layoutMgr = new Layout();
        setLayout(layoutMgr);

        final Insets insets = AssemblyScroll.INSETS;
        
        ruler = new Ruler();
        ruler.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        Font textFont = FontUtil.getDefaultSansSerifFont().deriveFont(FontUtil.getDefaultFontSize());
        ruler.setTextFont(textFont);
        add(ruler);

        concensus = new BrickComp();
        concensus.setFont(baseFont);        
        concensus.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        add(concensus);

        qualityHistogram = new Histogram();
        qualityHistogram.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        add(qualityHistogram);
    }

    public CNST.LAYOUT getLayoutState() {
        return layoutState;
    }

    public void setLayoutState(CNST.LAYOUT layoutState) {
        CNST.LAYOUT old = this.layoutState;
        this.layoutState = layoutState;
        firePropertyChange("layoutState", old, this.layoutState);
    }

    public BrickComp getConcensus() {
        return concensus;
    }

    public Histogram getQualityHistogram() {
        return qualityHistogram;
    }
    
    private void hookupListeners() {
        addPropertyChangeListener(new ColumnHeaderViewListeners.PtyListener());
    }

    public ICaretParent getCaretParent(Point ptOnScreen) {
        ICaretParent ret = null;
        return ret;
    }

    public void setBaseFont(Font baseFont) {
        Font old = this.baseFont;
        this.baseFont = baseFont;
        firePropertyChange("baseFont", old, this.baseFont);
    }

    public void setQv(List<Integer> qv) {
        List<Integer> old = this.qv;
        this.qv = qv;
        firePropertyChange("qv", old, this.qv);
    }

    public void setSeq(String seq) {
        String old = this.seq;
        this.seq = seq;
        firePropertyChange("seq", old, this.seq);
    }

    public int getDesiredHeight() {
        int ret = concensus.getDesiredHeight();
        ret += ruler.getDesiredHeight();
        ret += qualityHistogram.getDesiredHeight();
        return ret;
    }

    public int getDesiredWidth() {
        int ret = 0;
        if (seq != null) {
            float scale = getAssemblySPane().getScale();
            Font font = concensus.getFont();
            Insets insets = concensus.getInsets();
            int cWidth = FontUtil.getMSFontCharWidth(font);
            ret = MathUtil.round(cWidth * seq.length() * scale) + insets.left + insets.right;
        }
        return ret;
    }

    public boolean setCaretEnablement(ICaretParent caretParent) {
        boolean ret = false;
        return ret;
    }

    @Override
    public Loc calculateVisibleLoc() {
        int width = getWidth();
        Rectangle visibleRect = getVisibleRect();
        Integer totalPos = concensus.getBrickCount();
        int startPos = UIUtil.toScreenWidth(totalPos, width, visibleRect.x) - 3;
        int endPos = UIUtil.toScreenWidth(totalPos, width, visibleRect.x + visibleRect.width) + 3;
        startPos = Math.max(startPos, 1);
        endPos = Math.min(endPos, totalPos);
        return new Loc(startPos, endPos);
    }

    @Override
    public Loc getTotalLoc() {
        Loc ret = null;
        if (seq != null) {
            ret = new Loc(1, seq.length(), true);
        }
        return ret;
    }

    @Override
    public boolean isPaintVisibleOnly() {
        return false;
    }

    private class Layout implements LayoutManager {

        private int y;
        private int width;
        private boolean adjustingPreferredLayoutSize;

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension ret = new Dimension();
            adjustingPreferredLayoutSize = true;
            layoutContainer(parent);
            adjustingPreferredLayoutSize = false;
            ret.height = y;
            ret.width = width;
            return ret;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            Dimension ret = new Dimension();
            return ret;
        }

        @Override
        public void layoutContainer(Container parent) {
            setLayoutState(CNST.LAYOUT.STARTED);
            Dimension size = parent.getSize();
            y = 0;

            int dHeight = ruler.getDesiredHeight();
            ruler.setBounds(0, y, size.width, dHeight);

            y = y + dHeight;
            dHeight = concensus.getDesiredHeight();
            concensus.setBounds(0, y, size.width, dHeight);

            y = y + dHeight;
            dHeight = qualityHistogram.getDesiredHeight();
            if (!adjustingPreferredLayoutSize) {
                qualityHistogram.setBounds(0, y, size.width, dHeight);
            }
            setLayoutState(CNST.LAYOUT.ENDING);
        }
    }

    private AssemblyScroll getAssemblySPane() {
        if (assemblySPane == null) {
            assemblySPane = UIUtil.getParent(this, AssemblyScroll.class);
        }
        return assemblySPane;
    }
}