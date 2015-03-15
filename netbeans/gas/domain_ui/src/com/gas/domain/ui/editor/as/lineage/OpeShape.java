/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author dq
 */
class OpeShape {

    String name;
    String date;
    int x;
    int y;
    int width;
    int height;

    public void paint(Graphics2D g) {        
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final FontMetrics fm = g.getFontMetrics();
        final int fontHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        final int nameStrWidth = fm.stringWidth(name);
        g.setColor(Color.DARK_GRAY);
        Font font = g.getFont().deriveFont(Font.ITALIC);
        g.setFont(font);
        g.drawString(name, x + (width - nameStrWidth) * 0.5f, y + ascent + (height * 0.5f - fontHeight) * 0.5f);
        if (date != null) {
            int dateStrWidth = fm.stringWidth(date);
            g.drawString(date, x + (width - dateStrWidth) * 0.5f, y + height * 0.5f + ascent + (height * 0.5f - fontHeight) * 0.5f);
        }
        int delta = Math.round(fm.charWidth('A') * 0.6f);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(x, Math.round(y + height * 0.5f), x + delta, Math.round(y + height * 0.5f) - delta);
        g.drawLine(x, Math.round(y + height * 0.5f), x + width, Math.round(y + height * 0.5f));
        g.drawLine(x, Math.round(y + height * 0.5f), x + delta, Math.round(y + height * 0.5f) + delta);
    }
}
