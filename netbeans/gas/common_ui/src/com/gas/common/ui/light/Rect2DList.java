/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class Rect2DList extends ArrayList<Rectangle2D> {

    public void scale(double x, double y) {
        for (int i = 0; i < size(); i++) {
            Rectangle2D r = get(i);
            r.setFrameFromCenter(r.getCenterX(), r.getCenterY(), r.getCenterX() + r.getWidth() * x * 0.5, r.getCenterY() + r.getHeight() * y * 0.5);
        }
    }

    public void translateCenter(double x, double y) {
        for (int i = 0; i < size(); i++) {
            Rectangle2D r = get(i);
            final double centerX = r.getCenterX() * x;
            final double centerY = r.getCenterY() * y;
            r.setFrameFromCenter(centerX, centerY, centerX + r.getWidth() * 0.5, centerY + r.getHeight() * 0.5);
        }
    }

    public void translate(double x, double y) {
        for (int i = 0; i < size(); i++) {
            Rectangle2D r = get(i);
            r.setRect(r.getX() + x, r.getY() + y, r.getWidth(), r.getHeight());
        }
    }

    public void paint(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            Rectangle2D r = get(i);
            g.draw(r);
        }
    }
}
