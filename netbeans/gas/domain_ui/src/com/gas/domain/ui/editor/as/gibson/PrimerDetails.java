/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.light.Text;
import com.gas.common.ui.light.TextList;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.PathUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.OverlapPrimer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
class PrimerDetails extends JComponent {

    OverlapPrimer overlapPrimer;
    Font fontMS = FontUtil.getDefaultMSFont();

    PrimerDetails() {
    }

    void setOverlapPrimer(OverlapPrimer overlapPrimer) {
        this.overlapPrimer = overlapPrimer;
        repaint();
    }
    
    @Override
    public Dimension getPreferredSize(){
        Dimension ret = super.getPreferredSize();
        FontMetrics fm = FontUtil.getFontMetrics(fontMS);
        int heightFont = fm.getHeight();
        ret.setSize(ret.getWidth(), heightFont * 3.5);
        return ret;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (overlapPrimer == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String seqAnnealing = overlapPrimer.getAnnealingSeq();
        String seqFlappyEnd = overlapPrimer.getFlappyEndSeq();

        String portion1st = null;
        String portion2nd = null;
        if (overlapPrimer.isForward()) {
            portion1st = seqFlappyEnd;
            portion2nd = seqAnnealing;
        } else {
            portion1st = StrUtil.reverse(seqAnnealing);
            portion2nd = StrUtil.reverse(seqFlappyEnd);
        }

        TextList textList = new TextList();
        
        g2d.setFont(fontMS);
        FontMetrics fm = g2d.getFontMetrics();
        int widthChar = fm.charWidth('A');
        int heightFont = fm.getHeight();
        int x = 0;
        int y = Math.round(heightFont * 1.3f);
        
        for (int i = 0; i < portion1st.length(); i++) {
            Character c = portion1st.charAt(i);
            Text text = new Text();
            x += widthChar;
            text.setX(x);
            text.setY(y);
            text.setWidth(widthChar);
            text.setHeight(heightFont);
            text.setStr(c.toString());
            textList.add(text);
        }

        TextList textList2 = new TextList();
        for (int i = 0; i < portion2nd.length(); i++) {
            Character c = portion2nd.charAt(i);
            Text text = new Text();
            x += widthChar;
            text.setX(x);
            text.setY(y);
            text.setWidth(widthChar);
            text.setHeight(heightFont);
            text.setStr(c.toString());
            textList2.add(text);
        }

        Dimension size = getSize();
        final int widthText = textList.getWidth() + textList2.getWidth();
        int deltaX = Math.round(size.width * 0.5f - widthText * 0.5f);
        textList.translate(deltaX, 0);
        textList2.translate(deltaX, 0);

        Rectangle portion1Bg = textList.calculateBounds();
        portion1Bg.x = 0;
        portion1Bg.width = Math.round(size.width * 0.5f);
        Rectangle portion2Bg = textList2.calculateBounds();
        portion2Bg.width += size.width - portion2Bg.getMaxX();

        GibsonAssemblyPanel panel = UIUtil.getParent(this, GibsonAssemblyPanel.class);
        List<Color> colors = panel.primersPreview.getColors(overlapPrimer);

        g2d.setColor(colors.get(0));
        g2d.fill(portion1Bg);

        textList.setBgColor(colors.get(0));
        textList.setFgColor(ColorUtil.getForeground(colors.get(0)));
        textList.setFont(fontMS);
        textList.paint(g2d);

        g2d.setColor(colors.get(1));
        g2d.fill(portion2Bg);

        textList2.setBgColor(colors.get(1));
        textList2.setFgColor(ColorUtil.getForeground(colors.get(1)));
        textList2.setFont(fontMS);
        textList2.paint(g2d);

        GeneralPath path = null;
        int minX = textList.getMinX();
        int minY = textList.getMinY();
        int maxY = textList.getMaxY();
        int heightArrow = Math.round(textList.getHeight() * 0.7f);
        if (overlapPrimer.isForward()) {
            path = createArrow(true, minX, minY - heightArrow, widthText, heightArrow);
        } else {
            path = createArrow(false, minX, maxY, widthText, heightArrow);
        }
        g2d.setColor(ColorCnst.DARTMOUTH_GREEN);
        g2d.fill(path);
    }

    private GeneralPath createArrow(boolean forward, int x, int y, int width, int height) {
        GeneralPath ret = new GeneralPath();

        ret.moveTo(x, y + height);
        Point2D curPt = PathUtil.relativelyLineTo(ret, width, 0);
        curPt = PathUtil.relativelyLineTo(ret, -height, -height);
        ret.lineTo(x, curPt.getY());
        ret.closePath();

        if (!forward) {
            ret = PathUtil.flipHorizontally(ret);
            ret = PathUtil.flipVertically(ret);
        }

        return ret;
    }
}
