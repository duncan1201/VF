/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.RectangleComparator;
import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.jcomp.Overlay;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.ckpanel.ChromatogramComp2;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class MainComp extends JComponent implements Scrollable {

    static Logger log = Logger.getLogger(MainComp.class.getName());
    
    private AssemblyScroll assemblySPane;
    List<ChromatogramComp2> comp2s = new ArrayList<ChromatogramComp2>();
    List<ChromatogramComp2.Read> reads = new ArrayList<ChromatogramComp2.Read>();
    private Integer totalPos;
    private CNST.LAYOUT layoutState = null;
    Font baseFont;
    VariantMapMdl variantMapMdl;
    JLayeredPane layeredPane;
    private Overlay westOverlay;
    private Overlay eastOverlay;
    private Loc selection;

    public MainComp() {
        Insets insets = AssemblyScroll.INSETS;
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        LayoutManager layout = new Layout();
        setLayout(layout);

        westOverlay = new Overlay(0.65f, 0.45f);
        eastOverlay = new Overlay(0.65f, 0.45f);

        layeredPane = new JLayeredPane();
        layeredPane.add(westOverlay);
        layeredPane.setLayer(westOverlay, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(eastOverlay);
        layeredPane.setLayer(eastOverlay, JLayeredPane.PALETTE_LAYER);
        add(layeredPane);

        addPropertyChangeListener(new MainCompListeners.PtyListener());        

        addComponentListener(new MainCompListeners.CompListener());

        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke("RIGHT"), "positiveUnitIncrement");
        im.put(KeyStroke.getKeyStroke("LEFT"), "negativeUnitIncrement");
    }
    
    protected List<Rectangle> getRects(){
        List<Rectangle> ret = new ArrayList<Rectangle>();
        for(int i = 0; i < comp2s.size(); i++){
            ChromatogramComp2 comp2 = comp2s.get(i);
            Rectangle rect = comp2.getBounds();
            ret.add(rect);
        }
        Collections.sort(ret, new RectangleComparator(SwingConstants.VERTICAL));
        return ret;
    }

    public void setSelection(Loc selection) {
        Loc old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }

    public Loc getSelection() {
        return selection;
    }

    public void setLayoutState(CNST.LAYOUT layoutState) {
        CNST.LAYOUT old = this.layoutState;
        this.layoutState = layoutState;
        firePropertyChange("layoutState", old, this.layoutState);
    }

    public CNST.LAYOUT getLayoutState() {
        return layoutState;
    }
    
    public void setVariantMapMdl(VariantMapMdl variantMapMdl) {
        VariantMapMdl old = this.variantMapMdl;
        this.variantMapMdl = variantMapMdl;
        firePropertyChange("variantMapMdl", old, this.variantMapMdl);
    }
   

    public Font getBaseFont() {
        return baseFont;
    }

    public void setBaseFont(Font baseFont) {
        Font old = this.baseFont;
        this.baseFont = baseFont;
        firePropertyChange("baseFont", old, this.baseFont);
    }

    public void setReads(List<ChromatogramComp2.Read> reads, Integer totalPos) {
        List<ChromatogramComp2.Read> old = this.reads;
        this.reads = reads;
        this.totalPos = totalPos;
        firePropertyChange("reads", old, this.reads);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int ret = 0;
        if (!comp2s.isEmpty()) {
            int unit = comp2s.get(0).getScrollableUnitIncrement();
            int remainder = visibleRect.x % unit;

            if (orientation == SwingConstants.HORIZONTAL) {
                if (direction < 0) { //up/left
                    if (remainder > 0) {
                        ret = remainder;
                    } else {
                        ret = unit;
                    }
                } else { // down/right
                    ret = unit - remainder;
                }
            } else if (orientation == SwingConstants.VERTICAL) {
                boolean visible = getAssemblySPane().getVerticalScrollBar().isVisible();
                if (!visible) {
                    return 0;
                }
                if (direction < 0) { // up/left
                    ret = 4;
                } else {  // down/right
                    ret = 4;
                }
            }
        }
        return ret;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        int ret = 0;
        if (!comp2s.isEmpty()) {
            int unit = comp2s.get(0).getScrollableUnitIncrement();
            if (orientation == SwingConstants.HORIZONTAL) {
                ret = unit * 5;
            } else if (orientation == SwingConstants.VERTICAL) {
                ret = unit * 10;
            }
        }
        return ret;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    class Layout implements LayoutManager {

        private boolean adjustingPreferredLayoutSize;
        private int y;
        private int desiredWidth;

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
            ret.width = desiredWidth;
            return ret;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            Dimension ret = new Dimension();
            return ret;
        }

        void layoutOverlay(Container parent) {
            MainComp mainPanel = (MainComp) parent;
            Loc selection = mainPanel.getSelection();
            if (selection == null) {
                westOverlay.setSize(0, 0);
                eastOverlay.setSize(0, 0);
                return;
            }
            Dimension size = mainPanel.getSize();
            Insets insets = mainPanel.getInsets();
            int totalDisplayWidth = size.width - insets.left - insets.right;


            Integer eastXEnd = UIUtil.toScreen(totalPos, selection.getStart() - 1, totalDisplayWidth, insets.left, SwingConstants.RIGHT, Integer.class);
            int westWidth = MathUtil.round(eastXEnd + 1);

            if (westWidth >= 0) {
                westOverlay.setBounds(0, 0, westWidth, size.height);
            }

            Integer eastXStart = UIUtil.toScreen(totalPos, selection.getEnd() + 1, totalDisplayWidth, insets.left, SwingConstants.LEFT, Integer.class);
            int eastWidth = MathUtil.round(size.width - eastXStart + 1);

            if (eastWidth >= 0 && eastXStart >= 0) {
                eastOverlay.setBounds(eastXStart, 0, eastWidth, size.height);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            if (totalPos == null) {
                return;
            }
            setLayoutState(CNST.LAYOUT.STARTED);
            Dimension size = parent.getSize();            
            Insets insets = getInsets();            
            int totalDisplayWidth = size.width - insets.left - insets.right;            
            if (!adjustingPreferredLayoutSize) {
                layoutOverlay(parent);
            }
            y = 0;
            final int valueScrollBar = getAssemblySPane().getHorizontalScrollBar().getValue();
            for (int i = 0; i < comp2s.size(); i++) {
                ChromatogramComp2 p = comp2s.get(i);
                int height = p.getDesiredHeight();
                ChromatogramComp2.Read read = p.getRead();                
                float x = UIUtil.toScreen(totalPos, read.getOffset(), totalDisplayWidth, insets.left, SwingConstants.RIGHT);
                int xInt = MathUtil.round(x);
                p.setScale(getAssemblySPane().getScale());
                int width = p.getDesiredWidth();

                // must add the valueScrollBar
                p.setBounds(xInt + valueScrollBar, y, width, height);                
                if (xInt < 0) {
                    
                }
                y += height;
            }
            desiredWidth = getDesiredWidth();
            
            if (!adjustingPreferredLayoutSize) {                
                UIUtil.setPreferredWidth(parent, desiredWidth);
                UIUtil.setPreferredHeight(parent, y);
                getAssemblySPane().getRowHeaderView().revalidate();
                setLayoutState(CNST.LAYOUT.ENDING);
            }            
        }
        
        private int getDesiredWidth(){
            int ret = 0;
            float scale = getAssemblySPane().getScale();
            Insets insets = AssemblyScroll.INSETS;          
            int charWidth = FontUtil.getMSFontCharWidth(baseFont);
            ret = MathUtil.round(totalPos * charWidth * scale) + insets.left + insets.right;
            return ret;
        }
    }

    private AssemblyScroll getAssemblySPane() {
        if (assemblySPane == null) {
            assemblySPane = UIUtil.getParent(this, AssemblyScroll.class);
        }
        return assemblySPane;
    }
}