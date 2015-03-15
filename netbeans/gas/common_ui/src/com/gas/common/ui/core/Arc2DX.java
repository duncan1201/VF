/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.RectHelper;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author dq
 */
public class Arc2DX {

    public static class Float extends Arc2D.Float {

        private Color color;

        public Float() {
            super();
        }

        public Float(Rectangle2D ellipseBounds, float start, float extent, int type) {
            super(ellipseBounds, start, extent, type);
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void paint(Graphics2D g) {
            Color oldColor = g.getColor();
            if (color != null) {
                g.setColor(color);
            }
            g.draw(this);
            g.setColor(oldColor);
        }

        public void cbd(double angInDegree) {
            boolean contains = containsAngle(angInDegree);

        }

        public void abc(Point point) {
            Point origin = RectHelper.getCenter(getBounds());
            double angleInDegree = MathUtil.getAngleInDegrees(point, origin);

        }

        public double getMiddleAngle() {
            return getMiddleAngle(false);
        }

        public double getMiddleAngle(boolean normalize) {
            double ret = getAngleStart() + getAngleExtent() / 2;
            if (normalize) {
                ret = MathUtil.normalizeDegree(ret);
            }
            return ret;
        }
    }
}
