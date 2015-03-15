/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.shape;

import com.gas.common.ui.util.MathUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 *
 * @author dunqiang
 */
public class Seperator extends JComponent {

    static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    static final float DEFAULT_LINE_WIDTH = 1.0f;
    private Color color = DEFAULT_COLOR;
    private float lineWidth = DEFAULT_LINE_WIDTH;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize();
        //g.fillRoundRect(0, 0, size.width, size.height, WIDTH, HEIGHT);


        g2d.setColor(color);
        BasicStroke stroke = new BasicStroke(lineWidth);
        g2d.setStroke(stroke);
        int lineWidthOffset = MathUtil.round(getLineWidth() / 2.0f, Integer.class);
        int y = MathUtil.round(size.height / 2.0, Integer.class) - lineWidthOffset;
        g2d.drawLine(0, y, size.width, y);
        //g2d.fillRect(0, 0, size.width, size.height);
    }
}
