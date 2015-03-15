/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.minimap;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.jcomp.IOverlayParent;
import com.gas.common.ui.jcomp.Overlay;
import com.gas.domain.ui.shape.IShape.TEXT_LOC;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.AsPref;
import com.gas.domain.ui.pref.ColorPref;
import com.gas.domain.ui.shape.Arrow;
import com.gas.domain.ui.shape.ArrowComparators;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.IShapeList;
import com.gas.domain.ui.shape.layout.DefaultLayoutHelper;
import com.gas.domain.ui.shape.layout.ILayoutHelper;
import com.gas.domain.ui.shape.layout.LayoutHelperFinder;
import com.gas.main.ui.molpane.graphpane.GraphPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class Minimap extends JPanel implements IOverlayParent, IReclaimable {

    private AnnotatedSeq as;
    private WeakReference<JLayeredPane> layeredPaneRef;
    private Map<String, IShapeList> arrowsMap = new TreeMap<String, IShapeList>(new Feture.FtrNameComparator());
    private Float overlayStart;
    private Float overlayWidth;
    private Overlay overlay;
    private Overlay westOverlay;
    private Overlay eastOverlay;
    protected boolean busy;

    public Minimap() {
        super();
        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder());

        setLayout(new Layout());
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPaneRef = new WeakReference<JLayeredPane>(layeredPane);
        add(layeredPane);

        eastOverlay = new Overlay();
        eastOverlay.setWhiteness(0.2f);
        westOverlay = new Overlay();
        westOverlay.setWhiteness(0.2f);

        layeredPane.add(eastOverlay);
        layeredPane.setLayer(eastOverlay, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(westOverlay);
        layeredPane.setLayer(westOverlay, JLayeredPane.PALETTE_LAYER);

        overlay = new Overlay();
        overlay.setWhiteness(1.0f);
        overlay.setAlpha(0);
        overlay.setDraggable(true);

        overlay.setEastBorder(true);
        overlay.setWestBorder(true);
        overlay.setCursorType(Cursor.E_RESIZE_CURSOR);



        layeredPane.add(overlay);
        layeredPane.setLayer(overlay, JLayeredPane.PALETTE_LAYER);


        hookupListeners();
    }

    @Override
    public void cleanup() {
        overlay = null;
        westOverlay = null;
        eastOverlay = null;
        arrowsMap.clear();
    }

    public JLayeredPane getLayeredPane() {
        return layeredPaneRef.get();
    }

    public AnnotatedSeq getAs() {
        return as;
    }

    private void hookupListeners() {
        overlay.addPropertyChangeListener(new MinimapListeners.OverlayPtyChangeListener());
        addPropertyChangeListener(new MinimapListeners.PtyChangeListener());

        addComponentListener(new MinimapListeners.CompAdptr());
    }

    public void updateOverlaysBounds() {
        if (getOverlayStart() != null && getOverlayWidth() != null && getOverlayStart() >= 0) {
            Dimension size = getSize();
            final int x = MathUtil.round(getOverlayStart() * size.width);
            int width = MathUtil.round(getOverlayWidth() * size.width);
            getOverlay().setBusy(true);
            getOverlay().setBounds(x, 0, width, size.height);
            getOverlay().setBusy(false);
            updateGrayOverlays();
        }
    }

    public void refresh() {
        arrowsMap.clear();
        UIUtil.removeComponentsInLayer(getLayeredPane(), JLayeredPane.DEFAULT_LAYER);
        arrowsMap = createArrowsMap();
        revalidate();
    }

    public Overlay getOverlay() {
        return overlay;
    }

    protected void updateGrayOverlays() {
        Dimension size = Minimap.this.getSize();
        if (overlayStart + overlayWidth > 1) {
            eastOverlay.setSize(0, 0);
            int x = Math.round(size.width * (overlayWidth + overlayStart - 1));
            int width = Math.round(size.width * (1 - overlayWidth));
            westOverlay.setBounds(x, 0, width, size.height);
        } else {
            final int x = MathUtil.round(overlayStart * size.width);
            int width = MathUtil.round(overlayWidth * size.width);
            int eastX = x + width;
            int eastWidth = size.width - eastX;
            eastOverlay.setBounds(eastX, 0, eastWidth, size.height);
            int westWidth = x + 1;
            if (westWidth > 0) {
                westOverlay.setBounds(0, 0, westWidth, size.height);
            }
        }
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setOverlayStart(Float overlayStart) {
        if (overlayStart > 1) {
            throw new IllegalArgumentException(String.format("OverlayStart should >= 0 and <= 1: %f", overlayStart));
        }
        Float old = this.overlayStart;
        this.overlayStart = overlayStart;
        firePropertyChange("overlayStart", old, this.overlayStart);
    }

    public void setOverlayWidth(Float overlayWidth) {
        if (overlayWidth > 1) {
            throw new IllegalArgumentException(String.format("OverlayWidth should >=0 and <=1: %f", overlayWidth));
        }
        Float old = this.overlayWidth;
        this.overlayWidth = overlayWidth;
        firePropertyChange("overlayWidth", old, this.overlayWidth);
    }

    public void setArrowsMap(Map<String, IShapeList> arrowsMap) {
        this.arrowsMap = arrowsMap;
    }
    
    public Map<String, IShapeList> getArrowsMap(){
        if(arrowsMap.isEmpty()){            
            arrowsMap = createArrowsMap();
        }
        return this.arrowsMap;
    }
    
    protected Map<String, IShapeList> createArrowsMap() {
        Map<String, IShapeList> ret = new TreeMap<String, IShapeList>(new Feture.FtrNameComparator()); 
        
        if(as == null){
            return ret;
        }
        AsPref asPref = as.getAsPref();
        Map<String, Boolean> visibleMap = asPref.getTrackData();        
        if(visibleMap == null || visibleMap.isEmpty()){
            return ret;
        }
        
        Map<String, List<Feture>> feturesMap = AsHelper.getFetureMap(getAs(), true);
        
     
            Iterator<String> itr = feturesMap.keySet().iterator();
            while (itr.hasNext()) {
                String str = itr.next();
                if(!visibleMap.containsKey(str.toUpperCase(Locale.ENGLISH)) || !visibleMap.get(str.toUpperCase(Locale.ENGLISH))){
                    continue;
                }
                Iterator<Feture> fItr = feturesMap.get(str).iterator();
                while (fItr.hasNext()) {
                    Feture feture = fItr.next();
                    Arrow arrow = createArrow(feture);
                    if (!ret.containsKey(str)) {
                        ret.put(str, new IShapeList());
                    }
                    getLayeredPane().add(arrow);
                    ret.get(str).add(arrow);
                }
            }
            itr = ret.keySet().iterator();
            while (itr.hasNext()) {
                String str = itr.next();
                IShapeList arrowList = ret.get(str);
                Collections.sort(arrowList, new ArrowComparators.StartPosComparator());
                ArrowComparators.adjacentConservedCompact(arrowList);
                ret.put(str, arrowList);
            }
        
    return ret;
    }

    public Float getOverlayStart() {
        return overlayStart;
    }

    public Float getOverlayWidth() {
        return overlayWidth;
    }

    private Arrow createArrow(Feture feture) {
        Arrow arrow = new Arrow();
        String key = feture.getKey();
        Color color = ColorPref.getInstance().getColor(key);
        arrow.setSeedColor(color);
        arrow.setDisplayText("");
        //arrow.setData(feture);

        Font font = FontUtil.getDefaultMSFont().deriveFont(2.0f);
        arrow.setTextFont(font);
        arrow.setVerticalPaddingRatio(0.2f);
        Lucation lucation = feture.getLucation();

        arrow.setLuc(lucation);
        arrow.setForward(lucation.getStrand());
        arrow.setTextLoc(TEXT_LOC.NONE);

        arrow.setHoverEnabled(false);
        return arrow;
    }

    public void setAs(AnnotatedSeq as) {
        AnnotatedSeq old = this.as;
        this.as = as;
        firePropertyChange("as", old, this.as);
    }

    private static class Layout implements LayoutManager {

        private boolean adjustingPreferredSize = false;
        private int y = 0;

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension ret = new Dimension();
            adjustingPreferredSize = true;
            layoutContainer(parent);
            adjustingPreferredSize = false;
            ret.height = y;
            return ret;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            Dimension ret = new Dimension();
            return ret;
        }

        @Override
        public void layoutContainer(Container parent) {
            ILayoutHelper layoutHelper = LayoutHelperFinder.findDefault();;
            
            Rectangle drawingRect = UIUtil.getBoundsNoBorder(parent);
            if (drawingRect.width < 0) {
                return;
            }
            Minimap minimap = (Minimap)parent;
            y = 0;
            Integer height = null;
            final int vGap = 0;
            int lastX = drawingRect.x;
            Iterator<String> itr = minimap.getArrowsMap().keySet().iterator();
            while (itr.hasNext()) {
                String str = itr.next();
                Iterator<IShape> aItr = minimap.getArrowsMap().get(str).iterator();
                while (aItr.hasNext()) {
                    IShape arrow = aItr.next();
                    
                    if (height == null) {
                        height = layoutHelper.getDesiredHeight(arrow);
                    }
                    int totalPos = minimap.getAs().getLength();
                    layoutHelper = LayoutHelperFinder.findDefault();
                    Integer width = layoutHelper.getDesiredWidth(arrow, arrow.getTextLoc(), drawingRect, totalPos);
                    Integer start = layoutHelper.getDesiredStartPixel(arrow, arrow.getTextLoc(), drawingRect, totalPos);
                    
                    if(start == null || width == null){
                        arrow.setBounds(0, 0, 0, 0);
                        continue;
                    }
                    
                    if (start < lastX || start + width < lastX) {
                        y = y + height + vGap;
                    }
                    lastX = start + width - 1;

                    if (!adjustingPreferredSize) {
                        arrow.setBounds(start, y, width, height);
                        minimap.getLayeredPane().setLayer((Component)arrow, JLayeredPane.DEFAULT_LAYER);
                    }
                }
            }
            if (height != null) {
                y += height;
            }
            if (!adjustingPreferredSize) {
                UIUtil.setPreferredHeight(parent, y);
            }
        }
    }
}
