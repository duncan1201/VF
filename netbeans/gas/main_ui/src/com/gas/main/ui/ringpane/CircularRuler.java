/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class CircularRuler extends JComponent {

    private final static int MIN_PIXEL_DISTANCE = 50;
    private final static List<Integer> MARK_DISTANCES = new ArrayList<Integer>();
    private Integer bottomInset = 1;
    private Integer tickHeight = 2;
    private Integer tickGap = 1;
    private float offset;
    private transient RingGraphPanel ringPanel;
    private CircularRulerLabelList selectedLabels = new CircularRulerLabelList();
    private CircularRulerLabelList labels = new CircularRulerLabelList();

    static {
        for (int i = 0; i < 999; i++) {
            if (i < 100) {
                if (i % 10 == 0) {
                    MARK_DISTANCES.add(i);
                }
            } else if (i < 1000) {
                if (i % 50 == 0) {
                    MARK_DISTANCES.add(i);
                }
            }
        }
    }
    private Loc range;
    private Loc selection;

    public CircularRuler() {
        addPropertyChangeListener(new CircularRulerListeners.PtyChangeListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (range == null) {
            return;
        }
        if (ringPanel == null) {
            ringPanel = UIUtil.getParent(this, RingGraphPanel.class);
        }
        int length = ringPanel.getAs().getLength();
        final Loc visibleLoc = ringPanel.calculateVisibleLoc();
        if (visibleLoc == null) {
            return;
        }
        labels.clear();
        selectedLabels.clear();
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform old = g2d.getTransform(); // backup

        Dimension size = getSize();
        int sLength = Math.min(size.height, size.width);
        Dimension square = new Dimension(sLength, sLength);
        Point.Double center = new Point.Double(square.width * 0.5, square.height * 0.5);
        FontMetrics fm = FontUtil.getFontMetrics(this);
        
        Integer markDistance = getMarkDistance();
        if (markDistance == null) {
            return;
        }

        final double increment = 2 * Math.PI / range.width();

        Double startPos = Math.floor(visibleLoc.getStart() * 1.0 / markDistance) * markDistance;        
        startPos = Math.max(startPos, markDistance);
        final Double endPos = Math.ceil(visibleLoc.getEnd() * 1.0 / markDistance) * markDistance;
        for (Integer pos = startPos.intValue(); LocUtil.contains(startPos.intValue(), endPos.intValue(), pos);) {

            if (pos + 3 > length) {
                pos = length;
            }
            
            CircularRulerLabel label = createLabel(pos, center, increment, fm);
            if(pos == length){
                labels.remove(label.getBounds());
            }
            labels.add(label);

            if (visibleLoc.getStart() > visibleLoc.getEnd()) {
                if (pos >= length) {
                    pos = markDistance;
                } else {
                    pos += markDistance;
                }
            } else {
                pos += markDistance;
            }
            g2d.setTransform(old); // restore                        
        }
        
        // draw the last label
        if(visibleLoc.contains(range.getEnd())){
            CircularRulerLabel lastLabel = createLabel(range.getEnd(), center, increment, fm);
            labels.remove(lastLabel.getBounds());
            labels.add(lastLabel);
        }

        // draw the selection
        if (selection != null && selection.isOverlapped(startPos.intValue(), endPos.intValue())) {                        
            g2d.setColor(Color.BLUE);
            //paintPos(g2d, selection.getStart(), center, increment, fm);
            CircularRulerLabel label = createLabel(selection.getStart(), center, increment, fm);
            selectedLabels.add(label);
            //paintPos(g2d, selection.getEnd(), center, increment, fm);
            label = createLabel(selection.getEnd(), center, increment, fm);
            selectedLabels.add(label);
            selectedLabels.removeOverlappedLabels();
            selectedLabels.paint(g2d);
        }
        g2d.setColor(Color.BLACK);
        
        if(!selectedLabels.isEmpty()){
            Iterator<CircularRulerLabel> itr = selectedLabels.iterator();
            while(itr.hasNext()){
                CircularRulerLabel l = itr.next();                
                labels.remove(l.getBounds());
            }
        }
        labels.paint(g2d);
        
        g2d.setTransform(old); // restore
    }
    
    private CircularRulerLabel createLabel(Integer pos, Point.Double center, double increment, FontMetrics fm){
        final int strWidth = fm.stringWidth(pos.toString());
        
        CircularRulerLabel ret = new CircularRulerLabel();
        ret.setAnchor(center);
        ret.setTheta(increment * (pos - 0.5) + Math.toRadians(-offset));
        ret.setData(pos);
        
        ret.setX(-strWidth * 0.5f + (float)center.getX());
        ret.setY(0f);
        ret.setFont(getFont());
        return ret;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        float old = this.offset;
        this.offset = offset;
        firePropertyChange("offset", old, this.offset);
    }

    public Integer getBottomInset() {
        return bottomInset;
    }

    private Integer getMarkDistance() {
        Integer ret = null;
        if (range == null) {
            return ret;
        }
        Dimension size = getSize();
        int sLength = Math.min(size.height, size.width);
        Dimension square = new Dimension(sLength, sLength);
        int thickness = getRingThickness();
        float innerRadius = square.width * 0.5f - thickness;
        int perimeter = (int) Math.round(2 * Math.PI * innerRadius);

        for (int i = 0; i < MARK_DISTANCES.size(); i++) {
            int screenWidth = UIUtil.toScreenWidth(perimeter, range.width(), MARK_DISTANCES.get(i));
            if (screenWidth > MIN_PIXEL_DISTANCE) {
                ret = MARK_DISTANCES.get(i);
                break;
            }
        }
        return ret;
    }

    protected Integer getRingThickness() {
        Integer ret = null;
        FontMetrics fm = FontUtil.getFontMetrics(this);
        ret = fm.getHeight();
        ret += getTickHeight();
        ret += getTickGap();
        ret += getBottomInset();
        return ret;
    }

    private Integer getTickGap() {
        return tickGap;
    }

    private Integer getTickHeight() {
        return tickHeight;
    }

    public Loc getRange() {
        return range;
    }

    public void setRange(Loc range) {
        Loc old = this.range;
        this.range = range;
        firePropertyChange("range", old, this.range);
    }

    public void deleteByPos(int pos) {
        if (range.contains(pos)) {
            Loc newRange = new Loc(range.getStart(), range.getEnd() - 1);
            setRange(newRange);
        }
    }

    public Loc getSelection() {
        return selection;
    }

    public void setSelection(Loc selection) {
        Loc old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }
}
