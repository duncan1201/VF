/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author dunqiang
 */
public class BackgroundPainter implements Painter<JComponent> {

    private Color backgroundColor;

    public BackgroundPainter() {
    }

    public BackgroundPainter(Color color) {
        backgroundColor = color;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void paint(Graphics2D g, JComponent object, int width, int height) {
        if (backgroundColor == null) {
            backgroundColor = object.getParent().getBackground();
        }
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
    }
}