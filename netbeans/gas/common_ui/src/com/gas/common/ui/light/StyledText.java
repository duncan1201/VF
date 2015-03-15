/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class StyledText {

    private TextLayout textLayout;
    private String text;
    private Double bits;
    private Rectangle2D rect;

    public StyledText() {
    }

    public Rectangle2D getRect() {
        return rect;
    }

    public Double getBits() {
        return bits;
    }

    public void setBits(Double bits) {
        this.bits = bits;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void paint(Graphics2D g, Double x_, Double y_, Double width_, Double height_, Color fgColor, boolean paintLetter) {
        if (x_ == null || y_ == null || width_ == null || height_ == null || text == null || text.isEmpty()) {
            return;
        }
        this.rect = new Rectangle2D.Double(x_, y_, width_, height_);

        if (paintLetter) {
            textLayout = createTextLayout();

            Shape shape = textLayout.getOutline(null);
            Rectangle2D _rect = shape.getBounds2D();
            double sx = width_ / _rect.getWidth();
            double sy = height_ / _rect.getHeight();
            AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
            Shape scaledShape = at.createTransformedShape(shape);
            Rectangle2D scaledRect = scaledShape.getBounds2D();

            double tx = this.rect.getX() - scaledRect.getX();
            double ty = this.rect.getY() + scaledRect.getHeight();
            AffineTransform at2 = AffineTransform.getTranslateInstance(tx, ty);
            Shape tShape = at2.createTransformedShape(scaledShape);

            g.setColor(fgColor);
            g.fill(tShape);
        } else {
            g.setColor(fgColor);
            g.fill(new Rectangle2D.Double(x_, y_, width_, height_));
        }
    }

    private TextLayout createTextLayout() {

        TextLayout ret = new TextLayout(text, new Font("Helvetica", Font.PLAIN, 16), new FontRenderContext(null, true, false));
        return ret;
    }

    public int getTextLength() {
        return text == null ? 0 : text.length();
    }

    static class BitSorter implements Comparator<StyledText> {

        @Override
        public int compare(StyledText o1, StyledText o2) {
            int ret = o1.getBits().compareTo(o2.getBits());
            if (ret == 0) {
                ret = o1.getText().compareTo(o2.getText()) * -1;
            }
            return ret;
        }
    }
}
