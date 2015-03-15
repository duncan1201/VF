/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author dq
 */
public class Line2DX {

    /**
     * A line segment specified with float coordinates.
     *
     * @since 1.2
     */
    public static class Float extends Line2D.Float {

        private Color color;
        private Object data;
        private double angleRad;

        public Float() {
        }

        public Float(float x1, float y1, float x2, float y2) {
            super(x1, y1, x2, y2);
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void setAngleRad(double angleRad) {
            this.angleRad = angleRad;
        }

        public double getAngleRad() {
            return this.angleRad;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public void paint(Graphics2D g2d) {
            Color oldColor = g2d.getColor();
            if (color == null) {
                g2d.setColor(Color.BLACK);
            } else {
                g2d.setColor(color);
            }

            g2d.draw(this);
            g2d.setColor(oldColor);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            if (getP1() != null && getP2() != null) {
                ret.append(getP1());
                ret.append(',');
                ret.append(getP2());
                ret.append(',');
                ret.append(angleRad);
            }
            return ret.toString();
        }
    }
}
