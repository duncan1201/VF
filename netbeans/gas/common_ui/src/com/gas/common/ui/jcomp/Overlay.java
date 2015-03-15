/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.jcomp.IOverlayParent;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author dunqiang
 */
public class Overlay extends JComponent implements IReclaimable {

    public final static float ALPHA = 0.3f;
    public final static float WHITENESS = 0.85f;
    protected float alpha = ALPHA;
    protected float whiteness = WHITENESS;
    private boolean westBorder;
    private boolean eastBorder;
    private boolean northBorder;
    private boolean southBorder;
    protected Integer cursorType;
    protected Color fillColor = new Color(whiteness, whiteness, whiteness, alpha);
    private Color borderColor = ColorCnst.CARMINE;
    protected boolean draggable;
    protected MouseInputAdapter mouseInputAdapter;
    private JComponent overlayParent;
    protected Dimension parentSize;
    private boolean busy;

    public Overlay() {
        this(ALPHA, WHITENESS);
    }

    public Overlay(float _alpha, float _whiteness) {
        super();

        setOpaque(false);

        hookupListeners();

        setAlpha(_alpha);
        setWhiteness(_whiteness);
        setFocusable(false);
    }

    private void hookupListeners() {
        mouseInputAdapter = new MouseInputAdapter() {
            int curX;
            Point loc;
            Dimension size;

            @Override
            public void mouseDragged(MouseEvent e) {
                final int x = e.getXOnScreen();
                int diff = x - curX;

                loc = getLocation(loc);
                size = getSize(size);
                loc.x += diff;
                if (loc.x >= 0 && loc.x + size.width < parentSize.width) {
                    curX = x;
                }
                loc.x = Math.max(loc.x, 0); // cannot be less than 0
                loc.x = Math.min(loc.x, parentSize.width - size.width);


                setLocation(loc);

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (cursorType != null) {
                    setCursor(Cursor.getPredefinedCursor(cursorType));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Overlay src = (Overlay) e.getSource();
                Rectangle bounds = getBounds();
                repaint();
                src.firePropertyChange("bounds", null, bounds);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                hookupParent();
                curX = e.getXOnScreen();
            }
        };
        addPropertyChangeListener(new OverlayListeners.PtyChangeListener());
    }

    @Override
    public void cleanup() {
        mouseInputAdapter = null;
        overlayParent = null;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    protected void hookupParent() {
        if (overlayParent == null) {
            overlayParent = (JComponent) UIUtil.getParent(this, IOverlayParent.class);
            overlayParent.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    parentSize = overlayParent.getSize();
                }
            });
            if (parentSize == null) {
                parentSize = overlayParent.getSize();
            }
        }
    }

    public void setDraggable(boolean draggable) {
        boolean old = this.draggable;
        this.draggable = draggable;
        firePropertyChange("draggable", old, this.draggable);
    }

    public void setBorderColor(Color borderColor) {
        Color old = this.borderColor;
        this.borderColor = borderColor;
        firePropertyChange("borderColor", old, this.borderColor);
    }

    public void setNorthBorder(boolean northBorder) {
        boolean old = this.northBorder;
        this.northBorder = northBorder;
        firePropertyChange("northBorder", old, this.northBorder);
    }

    public void setSouthBorder(boolean southBorder) {
        boolean old = this.southBorder;
        this.southBorder = southBorder;
        firePropertyChange("southBorder", old, this.southBorder);
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setCursorType(Integer cursorType) {
        this.cursorType = cursorType;
    }

    public boolean isEastBorder() {
        return eastBorder;
    }

    public void setEastBorder(boolean eastBorder) {
        boolean old = this.eastBorder;
        this.eastBorder = eastBorder;
        firePropertyChange("eastBorder", old, this.eastBorder);
    }

    public boolean isWestBorder() {
        return westBorder;
    }

    public void setWestBorder(boolean westBorder) {
        boolean old = this.westBorder;
        this.westBorder = westBorder;
        firePropertyChange("westBorder", old, this.westBorder);
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

    public boolean isNorthBorder() {
        return northBorder;
    }

    public boolean isSouthBorder() {
        return southBorder;
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(fillColor);
        g2d.fillRect(0, 0, getSize().width, getSize().height);
        g2d.setPaint(borderColor);

        Rectangle bounds = getBounds();
        Dimension size = getSize();

        if (isEastBorder()) {
            g2d.drawLine(size.width - 1, 0, size.width - 1, size.height);
        }
        if (isWestBorder()) {
            g2d.drawLine(0, 0, 0, size.height);
        }
        if (isNorthBorder()) {
            g2d.drawLine(0, 0, size.width - 1, 0);
        }
        if (isSouthBorder()) {
            g2d.drawLine(0, size.height - 1, size.width - 1, size.height - 1);
        }
    }
}
