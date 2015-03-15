/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ckpanel;

import com.gas.common.ui.IVisibleLocProvider;
import com.gas.domain.ui.brickComp.BrickComp;
import com.gas.common.ui.color.ChromaColorProvider;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.color.TraceColorProvider;
import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.tigr.Kromatogram;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.biojava.bio.seq.DNATools;

/**
 *
 * @author dq
 */
public class ChromatogramComp2 extends JComponent implements IVisibleLocProvider {

    static Logger log = Logger.getLogger(ChromatogramComp2.class.getName());
    
    //private Kromatogram k;
    //private String seq;
    protected Read read;
    private boolean visibleA = true;
    private boolean visibleT = true;
    private boolean visibleC = true;
    private boolean visibleG = true;
    private boolean offsetVisible = true;
    private boolean visibleQv = true;
    private IColorProvider colorProvider = new TraceColorProvider();
    private static Insets cInsets = new Insets(1, 0, 1, 0);
    protected CenterGraph centerGraph;
    protected BrickComp brickComp;
    private boolean graphVisible = true;
    protected static Color QV_FILL_COLOR = new Color(208, 227, 252);
    protected static Color QV_OUTLINE_COLOR = new Color(125, 162, 206);
    protected Font baseFont = FontUtil.getDefaultMSFont();
    protected VariantMapMdl.Read variants;

    public ChromatogramComp2() {
        setLayout(new Layout());

        brickComp = new BrickComp();
        brickComp.setFont(baseFont);
        brickComp.setBorder(BorderFactory.createEmptyBorder(cInsets.top, cInsets.left, cInsets.bottom, cInsets.right));

        centerGraph = new CenterGraph();
        centerGraph.setBorder(BorderFactory.createEmptyBorder(cInsets.top, cInsets.left, cInsets.bottom, cInsets.right));

        add(centerGraph);
        add(brickComp);

        centerGraph.setBorder(BorderFactory.createEmptyBorder(cInsets.top, cInsets.left, cInsets.bottom, cInsets.right));

        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new ChromatogramCompListeners2.PtyListener());
    }   

    public void setVariants(VariantMapMdl.Read variants) {
        VariantMapMdl.Read old = this.variants;
        this.variants = variants;
        firePropertyChange("variants", old, this.variants);
    }

    public void setScale(float scale) {
        brickComp.setScale(scale);
    }

    public Integer getScrollableUnitIncrement() {
        return brickComp.getScrollableUnitIncrement();
    }

    public BrickComp getBrickComp() {
        return brickComp;
    }

    public void setBaseFont(Font baseFont) {
        Font old = this.baseFont;
        this.baseFont = baseFont;
        firePropertyChange("baseFont", old, this.baseFont);
    }

    public Read getRead() {
        return read;
    }

    public void setRead(Read read) {
        Read old = this.read;
        this.read = read;
        firePropertyChange("read", old, this.read);
    }

    public boolean isGraphVisible() {
        return graphVisible;
    }

    public Integer getDesiredHeight() {
        int ret = 0;
        ret = brickComp.getDesiredHeight();
        if (isGraphVisible()) {
            ret += ret * 5;
        }
        return ret;
    }

    public Integer getDesiredWidth() {
        return brickComp.getDesiredWidth();
    }

    public void setGraphVisible(boolean graphVisible) {
        boolean old = this.graphVisible;
        this.graphVisible = graphVisible;
        firePropertyChange("graphVisible", old, this.graphVisible);
    }

    public void setInsertMode(boolean insertMode) {
    }

    public boolean isInsertMode() {
        throw new UnsupportedOperationException();
    }

    public IColorProvider getColorProvider() {
        return colorProvider;
    }

    public boolean isVisibleQv() {
        return visibleQv;
    }

    public void setVisibleQv(boolean visibleQv) {
        boolean old = this.visibleQv;
        this.visibleQv = visibleQv;
        firePropertyChange("visibleQv", old, this.visibleQv);
    }

    protected Boolean isTraceVisible(Character c) {
        return isTraceVisible(c.toString());
    }

    protected Boolean isTraceVisible(String s) {
        Boolean ret = null;
        if (s.equalsIgnoreCase("a") || s.equals(DNATools.a().getName())) {
            ret = isVisibleA();
        } else if (s.equalsIgnoreCase("t") || s.equals(DNATools.t().getName())) {
            ret = isVisibleT();
        } else if (s.equalsIgnoreCase("c") || s.equals(DNATools.c().getName())) {
            ret = isVisibleC();
        } else if (s.equalsIgnoreCase("g") || s.equals(DNATools.g().getName())) {
            ret = isVisibleG();
        } else {
            throw new IllegalArgumentException(String.format("%s not recognized", s));
        }
        return ret;
    }

    protected CenterGraph getCenterGraph() {
        return centerGraph;
    }

    public boolean isOffsetVisible() {
        return offsetVisible;
    }

    public void setOffsetVisible(boolean offsetVisible) {
        boolean old = this.offsetVisible;
        this.offsetVisible = offsetVisible;
        firePropertyChange("offsetVisible", old, this.offsetVisible);
    }

    public boolean isVisibleA() {
        return visibleA;
    }

    public void setVisibleA(boolean visibleA) {
        boolean old = this.visibleA;
        this.visibleA = visibleA;
        firePropertyChange("visibleA", old, this.visibleA);
    }

    public boolean isVisibleC() {
        return visibleC;
    }

    public void setVisibleC(boolean visibleC) {
        boolean old = this.visibleC;
        this.visibleC = visibleC;
        firePropertyChange("visibleC", old, this.visibleC);
    }

    public boolean isVisibleG() {
        return visibleG;
    }

    public void setVisibleG(boolean visibleG) {
        boolean old = this.visibleG;
        this.visibleG = visibleG;
        firePropertyChange("visibleG", old, this.visibleG);
    }

    public boolean isVisibleT() {
        return visibleT;
    }

    public void setVisibleT(boolean visibleT) {
        boolean old = this.visibleT;
        this.visibleT = visibleT;
        firePropertyChange("visibleT", old, this.visibleT);
    }

    public void zoom(float ratio) {
        Dimension size = getSize();
        size.width = MathUtil.round(size.width * ratio);
        setPreferredSize(size);
        revalidate();
        repaint();
    }

    @Override
    public Loc calculateVisibleLoc() {
        int width = getWidth();
        Rectangle rect = getVisibleRect();
        Loc ret = new Loc(1, 100, true);
        return ret;
    }

    @Override
    public Loc getTotalLoc() {
        Loc ret = new Loc(1, read.bases.size(), true);        
        return ret;
    }

    @Override
    public boolean isPaintVisibleOnly() {
        return false;
    }

    class Layout implements LayoutManager {       
        
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
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            Dimension size = parent.getSize();
            int sPreferredHeight = brickComp.getDesiredHeight();
            if (isGraphVisible()) {
                centerGraph.setBounds(0, 0, size.width, size.height - sPreferredHeight);
                brickComp.setBounds(0, size.height - sPreferredHeight, size.width, sPreferredHeight);
            } else {
                centerGraph.setBounds(0, 0, 0, 0);
                brickComp.setBounds(0, 0, size.width, size.height);
            }
        }
    }

    public static class Read {

        private String name;
        private Integer offset;
        //private String seq;
        private List<Character> bases = new ArrayList<Character>();
        private Integer seqLend;// -> start of quality-trimmed sequence (aligned read coordinates)
        private Integer seqRend;//-> end of quality-trimmed sequence (aligned read coordinates)        
        private Kromatogram kromatogram;

        public List<Character> getBases() {
            return bases;
        }

        public Integer getSeqLend() {
            return seqLend;
        }

        public void setSeqLend(Integer seqLend) {
            this.seqLend = seqLend;
        }

        public Integer getSeqRend() {
            return seqRend;
        }

        public void setSeqRend(Integer seqRend) {
            this.seqRend = seqRend;
        }

        public void setBases(String s) {
            setBases(StrUtil.toChars(s));
        }

        public void setBases(List<Character> bases) {
            this.bases = bases;
        }

        public String getName() {
            return name;
        }
        
        public String getNameNoExt(){
            String ret = null;
            if(name != null){
                int index = name.lastIndexOf('.');
                if(index > -1){
                    ret = name.substring(0, index);
                }else{
                    ret = name;
                }
            }
            return ret;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }
        /*
         * public String getSeq() { return seq; }
         *
         * public void setSeq(String seq) { this.seq = seq; }
         */

        public Kromatogram getKromatogram() {
            return kromatogram;
        }

        public void setKromatogram(Kromatogram kromatogram) {
            this.kromatogram = kromatogram;
        }
    }

    public static class Sorter implements Comparator<Read> {

        @Override
        public int compare(Read o1, Read o2) {
            int ret = 0;
            ret = o1.getOffset().compareTo(o2.getOffset());
            if (ret == 0) {
                Integer end1 = o1.getOffset() + o1.getBases().size();
                Integer end2 = o2.getOffset() + o2.getBases().size();
                ret = end1.compareTo(end2);
            }
            if (ret == 0) {
                ret = StrUtil.compare(o1.getBases(), o2.getBases());
            }
            return ret;
        }
    }
}
