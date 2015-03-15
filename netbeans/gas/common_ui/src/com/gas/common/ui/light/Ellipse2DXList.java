/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class Ellipse2DXList extends ArrayList<Ellipse2DX.Float> {

    public Ellipse2DXList() {
    }

    public void translate(double x, double y) {
        for (int i = 0; i < size(); i++) {
            Ellipse2DX.Float e = get(i);
            e.setFrame(e.getX() + x, e.getY() + y, e.getWidth(), e.getHeight());
        }
    }

    public Ellipse2DX.Float getElementAt(int x, int y) {
        Ellipse2DX.Float ret = null;
        for (Ellipse2DX.Float f : this) {
            if (f.contains(x, y)) {
                ret = f;
            }
        }
        return ret;
    }

    public List<Ellipse2DX.Float> getElementsAt(int x, int y) {
        List<Ellipse2DX.Float> ret = new ArrayList<Ellipse2DX.Float>();
        for (Ellipse2DX.Float f : this) {
            if (f.contains(x, y)) {
                ret.add(f);
            }
        }
        return ret;
    }

    public void scale(double x, double y) {
        for (int i = 0; i < size(); i++) {
            Ellipse2D e = get(i);
            e.setFrameFromCenter(e.getCenterX(), e.getCenterY(), e.getCenterX() + e.getWidth() * 0.5 * x, e.getCenterY() + e.getHeight() * 0.5 * y);
        }
    }

    public void translateCenter(double x, double y) {
        for (int i = 0; i < size(); i++) {
            Ellipse2D e = get(i);
            double centerX = e.getCenterX() * x;
            double centerY = e.getCenterY() * y;
            e.setFrameFromCenter(centerX, centerY, centerX + e.getWidth() * 0.5, centerY + e.getHeight() * 0.5);
        }
    }

    public void paint(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            Ellipse2DX.Float e = get(i);
            e.paint(g);
        }
    }
}
