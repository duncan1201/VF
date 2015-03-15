/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *
 * @author dq
 */
public class BorderAA extends LineBorder {

    public BorderAA(Color color, int thickness) {
        super(color, thickness);
    }

    public BorderAA(Color color) {
        super(color);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        if ((this.thickness > 0) && (g instanceof Graphics2D)) {
            IconButton btn = (IconButton) c;
            Color parentColor = btn.getParent().getBackground();
            boolean chosen = btn.isChosen();

            Graphics2D g2d = (Graphics2D) g;

            Color oldColor = g2d.getColor();
            if (chosen) {
                g2d.setColor(this.lineColor);
                g2d.drawLine(0, 0, width, 0);//top
                g2d.drawLine(width - this.thickness, 0, width - this.thickness, height);//right
                g2d.drawLine(width - this.thickness, height - this.thickness, 0, height - this.thickness);//bottom
            }
            if (chosen) {
                g2d.setColor(parentColor);
                g2d.drawLine(0, height - this.thickness, 0, 0);//left                
            }

            g2d.setColor(oldColor);
        }
    }
}
