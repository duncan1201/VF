/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class TransLabelList extends ArrayList<TransLabel> {

    public boolean contains(double x, double y) {
        boolean ret = false;
        for (int i = 0; i < size(); i++) {
            TransLabel r = get(i);
            if (r.contains(x, y)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public void translateByAdd(float x, float y) {
        for (int i = 0; i < size(); i++) {
            TransLabel label = get(i);
            label.translateByAdd(x, y);
        }
    }

    public void translateByMul(float x, float y) {
        for (int i = 0; i < size(); i++) {
            TransLabel label = get(i);
            label.translateByMul(x, y);
        }
    }

    public Integer getCenterX() {
        Integer ret = null;
        Integer minX = getMinX();
        Integer maxX = getMaxX();
        if (minX != null && maxX != null) {
            ret = Math.round((minX + maxX) * 0.5f);
        }
        return ret;
    }

    public Integer getCenterY() {
        Integer ret = null;
        Integer minY = getMinY();
        Integer maxY = getMaxY();
        if (minY != null && maxY != null) {
            ret = Math.round((minY + maxY) * 0.5f);
        }
        return ret;
    }

    public Point getCenter() {
        Point p = new Point();
        p.x = getCenterX();
        p.y = getCenterY();
        return p;
    }

    public Integer getMinX() {
        Integer ret = null;
        for (int i = 0; i < size(); i++) {
            TransLabel l = get(i);
            Integer minX = l.getMinX();
            if (minX != null) {
                if (ret == null || ret > minX) {
                    ret = minX;
                }
            }
        }
        return ret;
    }

    public Integer getWidth() {
        Integer ret = null;
        Integer maxX = getMaxX();
        Integer minX = getMinX();
        if (maxX != null && minX != null) {
            ret = maxX - minX;
        }
        return ret;
    }

    public Integer getHeight() {
        Integer ret = null;
        Integer maxY = getMaxY();
        Integer minY = getMinY();
        if (maxY != null && minY != null) {
            ret = maxY - minY;
        }
        return ret;
    }

    public Integer getMaxX() {
        Integer ret = null;
        for (int i = 0; i < size(); i++) {
            TransLabel l = get(i);
            Integer maxX = l.getMaxX();
            if (maxX != null) {
                if (ret == null || ret < maxX) {
                    ret = maxX;
                }
            }
        }
        return ret;
    }

    public Integer getMinY() {
        Integer ret = null;
        for (int i = 0; i < size(); i++) {
            TransLabel l = get(i);
            Integer minY = l.getMinY();
            if (minY != null) {
                if (ret == null || ret > minY) {
                    ret = minY;
                }
            }
        }
        return ret;
    }

    public Integer getMaxY() {
        Integer ret = null;
        for (int i = 0; i < size(); i++) {
            TransLabel l = get(i);
            Integer maxY = l.getMaxY();
            if (maxY != null) {
                if (ret == null || ret < maxY) {
                    ret = maxY;
                }
            }
        }
        return ret;
    }

    public void paint(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            TransLabel t = get(i);
            t.paint(g);
        }
    }

    @Override
    public void clear() {
        super.clear();
    }
}
