/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import com.gas.common.ui.util.FontUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author dq
 */
public class RulerLabel {

    private Integer pos;
    private Integer x;
    private Integer y;
    private Integer tickHeight;
    private Integer tickGap;
    private Font font;
    private Rectangle bounds;
    private Color color;
    private PropertyChangeSupport propertyChangeSupport;

    public RulerLabel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(new RulerLabelListeners.PtyListener());
    }

    protected void paint(Graphics2D g) {
        if (x == null || y == null || pos == null || tickHeight == null || tickGap == null || font == null) {
            return;
        }
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int ascent = fm.getAscent();
        String posStr = pos.toString();
        int strWidth = fm.stringWidth(posStr);
        g.setFont(font);
        g.setColor(color);
        g.drawString(posStr, x, y + ascent);
        BasicStroke stroke = (BasicStroke) g.getStroke();
        int tickX = Math.round(x + strWidth * 0.5f - stroke.getLineWidth());
        int tickY = y + ascent;
        g.drawLine(tickX, tickY, tickX, tickY + tickHeight);
    }

    public void setPos(Integer pos) {
        Integer old = this.pos;
        this.pos = pos;
        propertyChangeSupport.firePropertyChange("pos", old, this.pos);
    }

    public Integer getPos() {
        return pos;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        Integer old = this.x;
        this.x = x;
        propertyChangeSupport.firePropertyChange("x", old, this.x);
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        Integer old = this.y;
        this.y = y;
        propertyChangeSupport.firePropertyChange("y", old, this.y);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getTickHeight() {
        return tickHeight;
    }

    public void setTickHeight(Integer tickHeight) {
        Integer old = this.tickHeight;
        this.tickHeight = tickHeight;
        propertyChangeSupport.firePropertyChange("tickHeight", old, this.tickHeight);
    }

    public Integer getTickGap() {
        return tickGap;
    }

    public void setTickGap(Integer tickGap) {
        Integer old = this.tickGap;
        this.tickGap = tickGap;
        propertyChangeSupport.firePropertyChange("tickGap", old, this.tickGap);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        Font old = this.font;
        this.font = font;
        propertyChangeSupport.firePropertyChange("font", old, this.font);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public boolean contains(Point p) {
        boolean ret = false;
        if (bounds != null) {
            ret = bounds.contains(p);
        }
        return ret;
    }

    public boolean intersects(Rectangle rect) {
        boolean ret = false;
        if (bounds != null && rect != null) {
            ret = bounds.intersects(rect);
        }
        return ret;
    }

    protected Rectangle createBounds() {
        Rectangle ret = null;
        if (x != null && y != null && pos != null && font != null && tickGap != null && tickHeight != null) {
            final FontMetrics fm = FontUtil.getFontMetrics(font);
            final int fontHeight = fm.getHeight();
            ret = new Rectangle();
            ret.x = x;
            ret.y = y;
            ret.width = fm.stringWidth(pos.toString());
            ret.height = fontHeight + tickGap + tickHeight;
        }
        return ret;
    }
}
