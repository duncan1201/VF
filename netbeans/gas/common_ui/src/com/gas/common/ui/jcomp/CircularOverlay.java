/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;

/**
 *
 * @author dunqiang
 */
public class CircularOverlay extends JComponent {

    public final static float ALPHA = 0.35f;
    public final static float WHITENESS = 0.75f;
    private float alpha = ALPHA;
    private float whiteness = WHITENESS;
    private Color fillColor = new Color(whiteness, whiteness, whiteness, alpha);
    private Color transparentColor = new Color(1.0f, 1.0f, 1.0f, 0);
    private Color borderColor = new Color(whiteness * 0.5f, whiteness * 0.5f, whiteness * 0.5f, alpha);
    private float startAngle;
    private float extent;
    private float rotateOffset;

    public CircularOverlay() {
        this(ALPHA, WHITENESS);
    }

    public float getRotateOffset() {
        return rotateOffset;
    }

    public void setRotateOffset(float rotateOffset) {
        float old = this.rotateOffset;
        this.rotateOffset = rotateOffset;
        firePropertyChange("rotateOffset", old, this.rotateOffset);
    }

    public float getExtent() {
        return extent;
    }

    public void setExtent(float extent) {
        if (extent < 0 || extent > 360) {
            throw new IllegalArgumentException(String.format("0 < Extent < 360: %f", extent));
        }
        float old = this.extent;
        this.extent = extent;
        firePropertyChange("extent", old, this.extent);
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        if (startAngle < 0 || startAngle > 360) {
            throw new IllegalArgumentException(String.format("0 < startAngle < 360: %f", startAngle));
        }
        float old = this.startAngle;
        this.startAngle = startAngle;
        firePropertyChange("startAngle", old, this.startAngle);
    }

    public CircularOverlay(float _alpha, float _whiteness) {
        super();

        setOpaque(false);

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String pName = evt.getPropertyName();
                if (pName.equals("whiteness") || pName.equals("alpha")) {
                    fillColor = new Color(whiteness, whiteness, whiteness, alpha);
                    repaint();
                } else if (pName.equals("borderColor")) {
                    repaint();
                } else if (pName.equals("startAngle") || pName.equals("extent")) {
                    repaint();
                } else if (pName.equals("rotateOffset")) {
                    repaint();
                }
            }
        });

        setAlpha(_alpha);
        setWhiteness(_whiteness);
        setFocusable(false);
    }

    public void setBorderColor(Color borderColor) {
        Color old = this.borderColor;
        this.borderColor = borderColor;
        firePropertyChange("borderColor", old, this.borderColor);
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getWhiteness() {
        return whiteness;
    }

    public final void setWhiteness(float whiteness) {
        if (whiteness > 1 || whiteness < 0) {
            throw new IllegalArgumentException("cannot have whiteness > 1 or whiteness < 0");
        }
        float old = this.whiteness;
        this.whiteness = whiteness;
        firePropertyChange("whiteness", old, this.whiteness);
    }

    public final void setAlpha(float alpha) {
        if (alpha > 1 || alpha < 0) {
            throw new IllegalArgumentException("cannot have alpha > 1 or alpha < 0");
        }
        float old = this.alpha;
        this.alpha = alpha;
        firePropertyChange("alpha", old, this.alpha);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(fillColor);
        Dimension size = getSize();
        if (extent == 0) {
            g2d.setColor(transparentColor);
            g2d.fillRect(0, 0, size.width, size.height);
            return;
        }

        GeneralPath path = createPath();
        g2d.fill(path);

        g2d.setPaint(borderColor);

        g2d.draw(path);
    }

    private GeneralPath createPath() {
        GeneralPath ret = new GeneralPath();
        Rectangle bounds = getBounds();
        bounds.x = bounds.y = 0;
        Arc2D.Float arc = new Arc2D.Float(bounds, rotateOffset - startAngle + 90, 360 - extent, Arc2D.PIE);
        ret.append(arc, false);
        return ret;
    }
}
