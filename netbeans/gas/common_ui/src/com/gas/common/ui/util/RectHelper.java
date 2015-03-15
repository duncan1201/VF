/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import com.gas.common.ui.core.RectList;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author dq
 */
public class RectHelper {

    public static Rectangle getNewRect2(Rectangle bounds, final float angdeg, float radius, double incrementDeg, Rectangle proposed, RectList rectList) {
        Point2D anchor = new Point2D.Double(bounds.getWidth() * 0.5, bounds.getHeight() * 0.5);
        Rectangle ret = new Rectangle(proposed);
        boolean intersect = rectList.intersects(ret.getX(), ret.getY(), ret.getWidth(), ret.getHeight());
        boolean contains = true;
        boolean oneRound = false;
        double delta = incrementDeg;
        while (intersect || !contains) {
            AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(delta), anchor.getX(), anchor.getY());
            ret = UIUtil.transform(at, ret, true);
            delta += incrementDeg;
            intersect = rectList.intersects(ret);
            contains = bounds.contains(ret);
            oneRound = delta > 360;

            if (oneRound) {
                ret = null;
                break;
            } else if (!intersect && contains) {
                return ret;
            }
        }

        if (oneRound) {
            ret = null;
            return ret;
        } else if (!intersect && contains) {
            return ret;
        } else {
            return null;
        }

    }

    public static Point getCenter(Rectangle2D rect) {
        final Point ret = new Point();
        ret.setLocation(rect.getCenterX(), rect.getCenterY());
        return ret;
    }

    public static boolean intersects(Rectangle rect, double x, double y, double w, double h) {
        int tw = rect.width;
        int th = rect.height;
        if (w <= 0 || h <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = rect.x;
        int ty = rect.y;

        w += x;
        h += y;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((w < x || w > tx)
                && (h < y || h > ty)
                && (tw < tx || tw > x)
                && (th < ty || th > y));
    }

    public static boolean contains(Rectangle rect, Line2D line) {
        boolean ret = false;
        Point2D p1 = line.getP1();
        Point2D p2 = line.getP2();
        ret = rect.contains(p1) && rect.contains(p2);
        return ret;
    }

    public static Line2D.Float getTopLine(Rectangle rect) {
        Point loc = rect.getLocation();
        Dimension size = rect.getSize();
        Line2D.Float ret = new Line2D.Float(loc.x, loc.y, loc.x + size.width, loc.y);
        return ret;
    }

    public static Line2D.Float getRightLine(Rectangle rect) {
        Point loc = rect.getLocation();
        Dimension size = rect.getSize();
        Line2D.Float ret = new Line2D.Float(loc.x + size.width, loc.y, loc.x + size.width, loc.y + size.height);
        return ret;
    }

    public static Line2D.Float getLeftLine(Rectangle rect) {
        Point loc = rect.getLocation();
        Dimension size = rect.getSize();
        Line2D.Float ret = new Line2D.Float(loc.x, loc.y, loc.x, loc.y + size.height);
        return ret;
    }

    public static Line2D.Float getBottomLine(Rectangle rect) {
        Point loc = rect.getLocation();
        Dimension size = rect.getSize();
        Line2D.Float ret = new Line2D.Float(loc.x, loc.y + size.height, loc.x + size.width, loc.y + size.height);
        return ret;
    }
}
