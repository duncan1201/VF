/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.FontUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author dq
 */
public class Base {

    private Float x;
    private Float y;
    private Float width;
    private Float height;
    private Character text;
    private Color color;
    private Color bgColor;
    private boolean strikeout;

    public Base() {
    }

    public Base(Character text) {
        this.text = text;
    }

    public void paint(Graphics2D g, double x_, double y_, double width_, double height_, Color bgColor, Color fgColor) {
        if (text == null) {
            return;
        }
        x = (float) x_;
        y = (float) y_;
        width = (float) width_;
        height = (float) height_;
        this.bgColor = bgColor;
        this.color = fgColor;


        Font font = g.getFont();
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int fontHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        int strWidth = fm.charWidth(text);
        float _x = (float) (x_ + (width_ - strWidth) * 0.5f);
        float _y = (float) (y_ + ascent + (height_ - fontHeight) * 0.5f);

        Color oldColor = null;

        if (this.bgColor != null) {
            g.setColor(this.bgColor);
            g.fillRect(MathUtil.round(x), MathUtil.round(y), MathUtil.round(width), MathUtil.round(height));
        }
        if (color != null) {
            oldColor = g.getColor();
            g.setColor(color);
        } else {
            g.setColor(Color.BLACK);
        }

        if (width_ > fm.charWidth('T')) {
            g.drawString(text.toString(), _x, _y);
        }
        if (strikeout) {
            BasicStroke oldStroke = (BasicStroke)g.getStroke();
            g.setStroke(new BasicStroke(oldStroke.getLineWidth() + 1));
            //g.drawLine(Math.round(_x), Math.round((float) y_ + ascent * 0.5f), Math.round(_x + strWidth), Math.round((float) y_ + ascent * 0.5f));
            g.drawLine(MathUtil.round(x_), Math.round((float) y_ + ascent * 0.5f + 1), MathUtil.round(x_ + width_), Math.round((float) y_ + ascent * 0.5f + 1));
            g.setStroke(oldStroke);
        }
        if (oldColor != null) {
            g.setColor(color);
        }
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Character getText() {
        return text;
    }

    public void setText(Character c) {
        this.text = c;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
