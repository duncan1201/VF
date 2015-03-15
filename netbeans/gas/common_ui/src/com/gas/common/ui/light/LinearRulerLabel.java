/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.util.FontUtil;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author dq
 */
public class LinearRulerLabel {

    private Float x;
    private Float y;
    private Integer pos;
    private Font font;
    private Integer tickHeight;
    private Rectangle bounds;
    private PropertyChangeSupport propertyChangeSupport;

    public LinearRulerLabel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(new LinearRulerLabelListeners.PtyListener());
    }

    public void paint(Graphics2D g) {
        if (pos == null || x == null || y == null || font == null || tickHeight == null) {
            return;
        }
        final String posStr = pos.toString();
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int fontHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        final int strWidth = fm.stringWidth(posStr);
        float _x = x;
        float _y = y + ascent;
        g.setFont(font);
        g.drawString(posStr, _x, _y);
        int xMiddle = Math.round(x + strWidth * 0.5f);
        int y1 = Math.round(y + fontHeight);
        int y2 = y1 + tickHeight;
        g.drawLine(xMiddle, y1, xMiddle, y2);
    }

    public void setX(Float x) {
        Float old = this.x;
        this.x = x;
        propertyChangeSupport.firePropertyChange("x", old, this.x);
    }

    public void setY(Float y) {
        Float old = this.y;
        this.y = y;
        propertyChangeSupport.firePropertyChange("y", old, this.y);
    }

    public void setPos(Integer pos) {
        Integer old = this.pos;
        this.pos = pos;
        propertyChangeSupport.firePropertyChange("pos", old, this.pos);
    }

    public void setFont(Font font) {
        Font old = this.font;
        this.font = font;
        propertyChangeSupport.firePropertyChange("font", old, this.font);
    }

    public void setTickHeight(Integer tickHeight) {
        Integer old = this.tickHeight;
        this.tickHeight = tickHeight;
        propertyChangeSupport.firePropertyChange("tickHeight", old, this.tickHeight);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Boolean intersects(Rectangle _bounds) {
        Boolean ret = null;
        if (_bounds != null && getBounds() != null) {
            ret = _bounds.intersects(getBounds());
        }
        return ret;
    }

    public Boolean intersects(LinearRulerLabel label) {
        return intersects(label.getBounds());
    }

    protected Rectangle createBounds() {
        Rectangle ret = null;
        if (x != null && y != null && pos != null && font != null && tickHeight != null) {
            ret = new Rectangle();
            ret.x = Math.round(x);
            ret.y = Math.round(y);
            FontMetrics fm = FontUtil.getFontMetrics(font);
            final int fontHeight = fm.getHeight();
            ret.width = fm.stringWidth(pos.toString());
            ret.height = fontHeight + tickHeight;
        }
        return ret;
    }
}
