/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.ColorUtil;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author dq
 */
public class Histogram implements IPaintable {

    private FloatList dataList = new FloatList();
    private Rectangle2D.Double rect;
    private Color from;
    private Color to;
    private PropertyChangeSupport propertyChangeSupport;

    public Histogram() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public Rectangle2D.Double getRect() {
        return rect;
    }

    public void setHeight(double height) {
        rect.setRect(rect.getX(), rect.getY(), rect.getWidth(), height);
    }

    @Override
    public void setRect(Rectangle2D.Double rect) {
        Rectangle2D old = this.rect;
        this.rect = rect;
        propertyChangeSupport.firePropertyChange("rect", old, this.rect);
    }

    public void setFrom(Color from) {
        this.from = from;
    }

    public void setTo(Color to) {
        this.to = to;
    }

    public void remove(int pos) {
        dataList.remove(pos - 1);
    }

    public void set(int pos, float f) {
        dataList.set(pos - 1, f);
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect) {
        paint(g, rect, 1, dataList.size());
    }

    @Override
    public void paint(Graphics2D g, int startPos, int endPos) {
        paint(g, this.rect, startPos, endPos);
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect, int startPos, int endPos) {
        if (rect == null || rect.getWidth() == 0 || rect.getHeight() == 0) {
            return;
        }
        this.rect = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        int size = dataList.size();
        double unitSpace = 1.0f * rect.getWidth() / size;
        int x = (int) Math.round(rect.getX());
        int x2 = 0;
        int y = 0;
        int width = 0;
        int height = 0;
        for (int i = startPos - 1; i < dataList.size() && i < endPos; i++) {
            Float f = dataList.get(i);
            x2 = MathUtil.round((i + 1) * unitSpace + rect.getX());
            width = x2 - x;
            height = MathUtil.round(f * rect.getHeight());
            Color iColor = ColorUtil.interpolate(from, to, f);
            g.setColor(iColor);
            g.fillRect(x, MathUtil.round(rect.getY() + rect.getHeight() - height), width, height);
            x = x2;
        }
    }

    /**
     * @param floatList: each float must be in the range of 0-1 inclusively
     */
    public void setDataList(FloatList floatList) {
        this.dataList = floatList;
    }

    /**
     *
     */
    public void setDataList(int[] ints) {
        this.dataList = new FloatList(ints);
        Float max = this.dataList.getMax();
        this.dataList.divide(max);
    }
}
