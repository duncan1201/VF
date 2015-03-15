/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.FontUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author dq
 */
public class LinearRuler implements IPaintable {

    private Integer start;
    private Integer end;
    private Font font;
    private LinearRulerLabelList labelList = new LinearRulerLabelList();
    private LinearRulerLabelList selectedLabels = new LinearRulerLabelList();
    private Rectangle2D.Double rect;
    private Loc selection;
    private PropertyChangeSupport propertyChangeSupport;

    public LinearRuler() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public Rectangle2D.Double getRect() {
        return rect;
    }

    @Override
    public void setRect(Rectangle2D.Double rect) {
        this.rect = rect;
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect) {
        paint(g, rect, start, end);
    }

    @Override
    public void paint(Graphics2D g, int startPos, int endPos) {
        paint(g, this.rect, startPos, endPos);
    }

    @Override
    public void paint(Graphics2D g, final Rectangle2D.Double rect, final int startPos, final int endPos) {
        if (rect == null || start == null || end == null || font == null) {
            return;
        }
        Font oldFont = g.getFont();

        selectedLabels.clear();
        labelList.clear();
        this.rect = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        int posDivisor = calculatePosDivisor(rect, fm);

        Integer pos = startPos;
        while (pos < endPos) {
            if (pos % posDivisor == 0 && pos != 0) {

                LinearRulerLabel label = createLabel(pos, fm);
                labelList.add(label);

                pos += posDivisor;
            } else {
                pos++;
            }
        }

        if (endPos == end) {
            LinearRulerLabel label = createLabel(endPos, fm);
            labelList.removeByRect(label.getBounds());
            labelList.add(label);
        }

        if (selection != null) {
            LinearRulerLabel label = createLabel(selection.getStart(), fm);
            selectedLabels.add(label);
            label = createLabel(selection.getEnd(), fm);
            selectedLabels.add(label);
        }
        g.setColor(Color.BLACK);
        selectedLabels.removeIntersection();
        labelList.removeIntersection(selectedLabels);
        labelList.paint(g);
        g.setColor(Color.BLUE);
        selectedLabels.paint(g);

        g.setFont(oldFont);
    }

    private LinearRulerLabel createLabel(Integer pos, final FontMetrics fm) {
        int strWidth = fm.stringWidth(pos.toString());
        Double x = rect.getX() + (pos - 0.5) * rect.getWidth() / (end - start + 1) - strWidth * 0.5f;
        LinearRulerLabel label = new LinearRulerLabel();
        label.setX(x.floatValue());
        label.setY((float) rect.getY());
        label.setFont(font);
        label.setPos(pos);
        label.setTickHeight(getTickHeight());
        return label;
    }

    public void setSelection(Loc selection) {
        Loc old = this.selection;
        this.selection = selection;
        propertyChangeSupport.firePropertyChange("selection", old, this.selection);
    }

    public Integer calculateDesiredHeight() {
        Integer ret = null;
        if (font != null) {
            FontMetrics fm = FontUtil.getFontMetrics(font);
            ret = getTickHeight() + fm.getHeight();
        }
        return ret;
    }

    private int getTickHeight() {
        return 2;
    }

    private int calculatePosDivisor(Rectangle2D rect, FontMetrics fm) {
        int strWidth = fm.stringWidth("A");
        int minPixelGap = strWidth * 18 + 10 - (strWidth * 5) % 10;
        int ret = MathUtil.round(1.0f * (end - start + 1) * minPixelGap / rect.getWidth());
        ret = ret + 10 - ret % 10;
        return ret;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}
