/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.linePlot.LinePlotCompMap;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author dq
 */
public class VerticalRuler {

    private Rectangle2D rect;
    private Integer startPos;
    private Integer endPos;
    private int[] positions;
    private int[] tickPos;
    private String label;
    private Font posFont = FontUtil.getDefaultSansSerifFont().deriveFont(FontUtil.getSmallFontSize());
    private Font labelFont = FontUtil.getDefaultSansSerifFont();
    private Color rulerColor = Color.GRAY;
    Color labelColor = Color.BLACK;
    private Color bgColor;
    private boolean selected;
    private int tickLength;
    private PropertyChangeSupport propertyChangeSupport;

    public VerticalRuler() {
        FontMetrics fm = FontUtil.getFontMetrics(labelFont);
        tickLength = fm.charWidth('a');
        propertyChangeSupport = new PropertyChangeSupport(this);
        hookupListeners();
    }

    private void hookupListeners() {
        propertyChangeSupport.addPropertyChangeListener(new VerticalRulerListeners.PtyListener());
    }

    public Rectangle2D getRect() {
        return rect;
    }

    public void setRect(Rectangle2D rect) {
        this.rect = rect;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        propertyChangeSupport.firePropertyChange("selected", old, this.selected);
    }

    public void paint(Graphics2D g, int startPos, int endPos) {
        paint(g, this.rect, startPos, endPos);
    }

    public void paint(Graphics2D g, Rectangle2D rect) {
        paint(g, rect, 1, 1);
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void paint(Graphics2D g, final Rectangle2D rect, final int startPos, final int endPos) {
        if (bgColor != null) {
            g.setColor(bgColor);
            g.fill(rect);
        }

        AffineTransform old = g.getTransform();
        if (positions != null && tickPos != null && this.startPos != null && this.endPos != null && rect != null) {
            g.setColor(rulerColor);
            BasicStroke stroke = (BasicStroke) g.getStroke();
            this.rect = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            final double unitHeight = rect.getHeight() / (this.endPos - this.startPos);
            final double xTick = rect.getX() + rect.getWidth() - tickLength;
            final double x2Tick = rect.getX() + rect.getWidth() - 1;
            for (int tick : tickPos) {
                double _y = rect.getY() + unitHeight * (this.endPos - tick) - stroke.getLineWidth();
                _y = Math.max(1, _y);
                g.drawLine(MathUtil.round(xTick), MathUtil.round(_y), MathUtil.round(x2Tick), MathUtil.round(_y));
            }
            final int yTickMin = rect.getY() == 0 ? 1 : MathUtil.round(rect.getY());
            g.drawLine(MathUtil.round(x2Tick), yTickMin, MathUtil.round(x2Tick), MathUtil.round(rect.getY() + rect.getHeight()));

            g.setFont(posFont);
            FontMetrics fmPos = g.getFontMetrics();
            final int fmPosHeight = fmPos.getHeight();
            final int fmPosAscent = fmPos.getAscent();
            final int fmPosDescent = fmPos.getDescent();
            final int fmPosWidth = fmPos.charWidth('8');

            for (int i = 0; i < positions.length; i++) {
                Integer pos = positions[i];
                int strWidth = fmPos.stringWidth(pos.toString());
                double _x = rect.getX() + rect.getWidth() - tickLength - strWidth;
                double _y;
                if (i == 0) {
                    _y = rect.getY() + rect.getHeight() - fmPosDescent;
                } else if (i == positions.length - 1) {
                    _y = rect.getY() + fmPosAscent;
                } else {
                    _y = rect.getY() + unitHeight * (this.endPos - pos) + fmPosAscent * 0.5;
                }
                g.drawString(pos.toString(), (float) _x, (float) _y);
                g.setTransform(old);
            }
        }


        if (label != null && !label.isEmpty()) {
            final Insets insets = UIUtil.getDefaultInsets();
            g.setColor(labelColor);
            g.setFont(labelFont);
            FontMetrics fm = g.getFontMetrics();
            int ascent = fm.getAscent();
            double y = ascent + ((float) rect.getHeight() - ascent) / 2.0f + rect.getY();
            g.drawString(label, insets.left, (float) y);
        }
        g.setTransform(old);
    }

    public int calculatePreferredWidth() {
        Insets insets = UIUtil.getDefaultInsets();
        int ret = 0;
        FontMetrics fmPos = FontUtil.getFontMetrics(posFont);
        FontMetrics fmLabel = FontUtil.getFontMetrics(labelFont);

        ret = fmLabel.stringWidth(label) + insets.left + insets.right;
        return ret;
    }

    public void setEndPos(Integer endPos) {
        this.endPos = endPos;
    }

    public void setPositions(int... positions) {
        this.positions = positions;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public void setTicksPos(int[] tickPos) {
        this.tickPos = tickPos;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static VerticalRuler createPredefined(String name) {
        VerticalRuler ret = null;
        if (name.equals(LinePlotCompMap.GC)) {
            ret = new VerticalRuler();
            ret.setLabel("GC");
            ret.setStartPos(0);
            ret.setEndPos(100);
            ret.setTicksPos(new int[]{0, 50, 100});
            ret.setPositions(new int[]{0, 50, 100});
        }
        return ret;
    }
}
