/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class Line2DXList extends ArrayList<Line2DX.Float> {

    public Point2D getCenter() {
        Double minX = getMinX();
        Double maxX = getMaxX();
        Double minY = getMinY();
        Double maxY = getMaxY();
        Point2D ret = null;
        if (minX != null && maxX != null && minY != null && maxY != null) {
            ret = new Point2D.Double((minX + maxX) * 0.5, (minY + maxY) * 0.5);
        }
        return ret;
    }

    public Double getMinX() {
        Double ret = null;
        for (int i = 0; i < size(); i++) {
            Line2D line = get(i);
            if (ret == null || ret > Math.min(line.getX1(), line.getX2())) {
                ret = Math.min(line.getX1(), line.getX2());
            }
        }
        return ret;
    }

    public Double getMaxX() {
        Double ret = null;
        for (int i = 0; i < size(); i++) {
            Line2D line = get(i);
            if (ret == null || ret < Math.max(line.getX1(), line.getX2())) {
                ret = Math.max(line.getX1(), line.getX2());
            }
        }
        return ret;
    }

    public Double getMinY() {
        Double ret = null;
        for (int i = 0; i < size(); i++) {
            Line2D line = get(i);
            if (ret == null || ret > Math.min(line.getY1(), line.getY2())) {
                ret = Math.min(line.getY1(), line.getY2());
            }
        }
        return ret;
    }

    public Double getMaxY() {
        Double ret = null;
        for (int i = 0; i < size(); i++) {
            Line2D line = get(i);
            if (ret == null || ret < Math.max(line.getY1(), line.getY2())) {
                ret = Math.max(line.getY1(), line.getY2());
            }
        }
        return ret;
    }

    public Double getWidth() {
        Double ret = null;
        Double minX = getMinX();
        Double maxX = getMaxX();
        if (minX != null && maxX != null) {
            ret = maxX - minX;
        }
        return ret;
    }

    public Double getHeight() {
        Double ret = null;
        Double minY = getMinY();
        Double maxY = getMaxY();
        if (minY != null && maxY != null) {
            ret = maxY - minY;
        }
        return ret;
    }

    public void translate(double x, double y) {
        for (int i = 0; i < size(); i++) {
            Line2DX.Float line = get(i);
            line.setLine(line.getX1() + x, line.getY1() + y, line.getX2() + x, line.getY2() + y);
        }
    }

    public void scale(float scaleX, float scaleY) {
        for (int i = 0; i < size(); i++) {
            Line2DX.Float line = get(i);
            line.setLine(line.getX1() * scaleX, line.getY1() * scaleY, line.getX2() * scaleX, line.getY2() * scaleY);
        }
    }

    public Point2D center() {
        double minX = getMinX();
        double maxX = getMaxX();
        double minY = getMinY();
        double maxY = getMaxY();
        return new Point2D.Double((minX + maxX) * 0.5, (minY + maxY) * 0.5);
    }

    public void paint(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            Line2DX.Float line2d = get(i);
            line2d.paint(g);
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Iterator<Line2DX.Float> itr = iterator();
        while (itr.hasNext()) {
            ret.append(itr.next());
            if (itr.hasNext()) {
                ret.append('\n');
            }
        }
        return ret.toString();
    }
}
