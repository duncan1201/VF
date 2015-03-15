/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.triangle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class Triangle extends JComponent {

    private boolean selected;
    private Integer pos;
    private Boolean down = false;

    public Triangle() {
        addPropertyChangeListener(new TriangleListeners.PtyChangeListener(this));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GeneralPath path = createPath();
        if (selected) {
            g2d.setColor(Color.BLUE);
        } else {
            g2d.setColor(Color.BLACK);
        }
        g2d.fill(path);
    }

    private GeneralPath createPath() {
        Dimension size = getSize();
        if (size.width % 2 == 0) {
            throw new IllegalArgumentException("the width must be odd");
        }
        GeneralPath path = new GeneralPath();
        if (down) {
            path.moveTo(0, 0);
            path.lineTo(Math.round(size.width * 0.5f) - 1, size.height - 1);
            path.lineTo(size.width - 1, 0);
            path.closePath();
        } else {

            path.moveTo(0, size.height - 1);
            path.lineTo(size.width - 1, size.height - 1);
            path.lineTo(Math.round(size.width * 0.5f) - 1, 0);

            path.closePath();
        }
        return path;
    }

    /**
     * @return the pos where the triangle is between pos and pos + 1
     */
    public Integer getPos() {
        return pos;
    }

    /**
     * @param pos: the triangle is between pos and pos + 1
     */
    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Boolean isDown() {
        return down;
    }

    public void setDown(Boolean down) {
        this.down = down;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.pos != null ? this.pos.hashCode() : 0);
        hash = 97 * hash + (this.down != null ? this.down.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Triangle other = (Triangle) obj;
        if (this.pos != other.pos && (this.pos == null || !this.pos.equals(other.pos))) {
            return false;
        }
        if (this.down != other.down && (this.down == null || !this.down.equals(other.down))) {
            return false;
        }
        return true;
    }
}
