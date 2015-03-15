/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class Text {

    private String str;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private Font font;
    private Color bgColor;
    private Color fgColor;
    private boolean selected;

    public Color getFgColor() {
        return fgColor;
    }

    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Rectangle getRect() {
        Rectangle ret = null;
        if (x != null && y != null && width != null && height != null) {
            ret = new Rectangle(x, y, width, height);
        }
        return ret;
    }

    public String getStr() {
        return str;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    
    public void paint(Graphics2D g) {
        paint(g, SwingConstants.CENTER);
    }

    public void paint(Graphics2D g, int horizontalAlignment) {
        final Insets insets = UIUtil.getDefaultInsets();
        if (str == null || font == null || str.isEmpty() || x == null || y == null || width == null || height == null) {
            return;
        }
        if (bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(x, y, width, height);
        }
        g.setColor(Color.BLACK);
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int widthStr = fm.stringWidth(str);
        final int heightFont = fm.getHeight();
        final int ascent = fm.getAscent();
        int _x ;
        if(horizontalAlignment == SwingConstants.CENTER){
            _x = Math.round(x + (width - widthStr) * 0.5f);
        }else if(horizontalAlignment == SwingConstants.LEFT){
            _x = Math.round(x + insets.left);
        }else if(horizontalAlignment == SwingConstants.RIGHT){
            _x = Math.round(x + width - insets.left - widthStr);
        }else{
            throw new IllegalArgumentException();
        }
        int _y = Math.round(y + ascent + (height - heightFont) * 0.5f);
        if (fgColor != null) {
            g.setColor(fgColor);
        }
        g.drawString(str, _x, _y);
    }
}
