/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class CircularRulerLabel {

    private Integer tickHeight = 2;
    private Integer tickGap = 1;
    private Float x;
    private Float y;
    private Object data;
    private Point2D anchor;
    private java.lang.Double theta;
    private Polygon bounds;
    private Font font;

    public void paint(Graphics2D g) {
        if (data == null || x == null || y == null || theta == null || anchor == null || font == null) {
            return;
        }
        AffineTransform old = g.getTransform();


        g.rotate(theta, anchor.getX(), anchor.getY());

        final FontMetrics fm = g.getFontMetrics();
        final int ascent = fm.getAscent();
        final int height = fm.getHeight();
        final int strWidth = fm.stringWidth(data.toString());
        g.drawString(data.toString(), x, y + ascent);
        g.drawLine(Math.round(x + strWidth * 0.5f), Math.round(y + height + getTickGap()), Math.round(x + strWidth * 0.5f), Math.round(y + height + getTickGap() + getTickHeight()));
        bounds = calculateBounds();
        
        g.setTransform(old);
    }

    public boolean contains(Point pt) {
        return contains(pt.x, pt.y);
    }

    public boolean contains(int x, int y) {
        boolean ret = getBounds().contains(x, y);
        return ret;
    }

    public boolean intersects(CircularRulerLabel l){
        Polygon polygon = l.getBounds();
        return intersects(polygon);
    }
    
    public boolean intersects(Polygon polygon) {
        if(polygon == null){
            return false;
        }
        boolean ret = getBounds().getBounds().intersects(polygon.getBounds());
        return ret;
    }

    public Polygon getBounds() {
        if (bounds == null) {
            bounds = calculateBounds();
        }
        return bounds;
    }

    private Polygon calculateBounds() {
        if (font == null) {
            return null;
        }
        FontMetrics fm = FontUtil.getFontMetrics(font);
        AffineTransform at = AffineTransform.getRotateInstance(theta, anchor.getX(), anchor.getY());
        final int strWidth = fm.stringWidth(data.toString());
        final int height = fm.getHeight();
        Polygon polygon = new Polygon();
        polygon.addPoint(Math.round(x), Math.round(y));
        polygon.addPoint(Math.round(x) + strWidth, Math.round(y));
        polygon.addPoint(Math.round(x) + strWidth, Math.round(y) + height + getTickHeight() + getTickGap());
        polygon.addPoint(Math.round(x), Math.round(y) + height + getTickHeight() + getTickGap());

        Polygon ret = UIUtil.transform(at, polygon);
        return ret;
    }

    public Point2D getAnchor() {
        return anchor;
    }

    public void setAnchor(Point2D anchor) {
        this.anchor = anchor;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Double getTheta() {
        return theta;
    }

    public void setTheta(Double theta) {
        this.theta = theta;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Integer getTickHeight() {
        return tickHeight;
    }

    public void setTickHeight(Integer tickHeight) {
        this.tickHeight = tickHeight;
    }

    public Integer getTickGap() {
        return tickGap;
    }

    public void setTickGap(Integer tickGap) {
        this.tickGap = tickGap;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    
    protected static class PosComparator implements Comparator<CircularRulerLabel>{

        @Override
        public int compare(CircularRulerLabel o1, CircularRulerLabel o2) {
            int ret = 0;
            Integer p1 = (Integer)o1.getData();
            Integer p2 = (Integer)o2.getData();
            if(p1 != null && p2 != null){
                ret = p1.compareTo(p2);
            }
            return ret;
        }
    }
}
