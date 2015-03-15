/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class RingListMapComp extends JComponent {

    private SortedRingListMap ringMap = new SortedRingListMap();
    private Float startOffset;

    public RingListMapComp() {
        addPropertyChangeListener(new RingListMapCompListeners.PtyListener());
    }

    public SortedRingListMap getRingMap() {
        return ringMap;
    }

    public void setStartOffset(float startOffset) {
        this.startOffset = startOffset;
        ringMap.setStartOffset(startOffset);
        repaint();
    }
    
    void setSeedColor(String k, Color c){
        ringMap.setSeedColor(k, c);
        repaint();
    }
    
    protected Float getStartOffset(){
        return this.startOffset;
    }

    public void setRingMap(SortedRingListMap ringMap) {
        SortedRingListMap old = this.ringMap;
        this.ringMap = ringMap;
        firePropertyChange("ringMap", old, this.ringMap);
    }

    public Dimension getDesiredDimension() {
        Dimension ret = new Dimension();

        LocList locList = new LocList();

        Iterator<String> itr = ringMap.keySet().iterator();
        int layerCount = 1;
        Integer ringWidth = null;
        while (itr.hasNext()) {
            String key = itr.next();
            List<Ring> rings = ringMap.get(key);
            Iterator<Ring> ringItr = rings.iterator();
            while (ringItr.hasNext()) {
                Ring ring = ringItr.next();
                if (ringWidth == null) {
                    ringWidth = ring.getRingThickness();
                }
                Feture feture = (Feture) ring.getData();
                Lucation luc = feture.getLucation();
                if (luc == null) {
                    System.out.print("");
                }
                Loc loc = new Loc(luc.getStart(), luc.getEnd());
                boolean intersect = locList.intersect(loc);
                if (intersect) {
                    layerCount++;
                    locList.clear();
                }

                locList.add(loc);
            }
        }

        if (!ringMap.isEmpty()) {
            ret.width = ret.height = (layerCount * 2 + 4) * (ringWidth + RingGraphPanelLayout.RING_GAP);
        }

        return ret;
    }
    
    /**
    @return the Ring at the location indicated by the parameter {@code ptScreen}
    */
    public Ring updateSelection(Point ptScreen){
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        return ringMap.updateSelection(pt);
    }
    
    public void updateMouseIn(Point ptScreen){
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        ringMap.updateMouseIn(pt);
    }
    
    public Ring updateSelection(Feture feture){
        return ringMap.updateSelection(feture);
    }
    
    public Ring getRing(Point ptScreen){
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        return ringMap.getRing(pt);
    }

    protected void layoutRings() {
        int offset = 0;
        LocList locList = new LocList();
        Dimension size = getSize();
        Rectangle square = new Rectangle(0, 0, size.width, size.height);
        Iterator<String> itrStr = ringMap.keySet().iterator();

        while (itrStr.hasNext()) {
            String key = itrStr.next();
            List<Ring> ringList = ringMap.get(key);
            Iterator<Ring> ringItr = ringList.iterator();
            while (ringItr.hasNext()) {
                Ring ring = ringItr.next();
                Feture feture = (Feture) ring.getData();
                Lucation luc = feture.getLucation();
                if (luc.isEmpty()) {
                    ring.setBounds(0, 0, 0, 0);
                    continue;
                }
                Loc loc = luc.toLoc();
                boolean intersect = locList.intersect(loc);
                if (intersect) {
                    offset += ring.getRingThickness() + RingGraphPanelLayout.RING_GAP;
                    locList.clear();
                }

                Rectangle rect = UIUtil.shrink(square, offset, offset);
                ring.setBounds(rect);
                locList.add(loc);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        ringMap.paint((Graphics2D)g);
    }

}
