/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
class EntityShape {

    String name;
    String absolutePath;
    boolean active = true;
    int x;
    int y;
    int width;
    int height;
    final static Image imageActive = ImageHelper.createImage(ImageNames.DNA_active_16);
    final static Image imageInactive = ImageHelper.createImage(ImageNames.DNA_inactive_16);

    public void paint(Graphics2D g, ImageObserver observer) {
        final FontMetrics fm = g.getFontMetrics();
        final int charWidth = fm.charWidth('A');
        final int fontHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        final int strWidth = fm.stringWidth(name);

        if (active) {
            g.setColor(ColorCnst.LINK_COLOR);
        } else {
            g.setColor(Color.BLACK);
        }
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (absolutePath != null && !absolutePath.isEmpty()) {
            g.drawString(name, x + charWidth * 2 + 16, y + ascent + (height - fontHeight) * 0.5f);
            if (active) {
                g.drawImage(imageActive, x + charWidth, Math.round(y + (height - imageActive.getHeight(observer)) * 0.5f), observer);
            } else {
                g.drawImage(imageInactive, x + charWidth, Math.round(y + (height - imageInactive.getHeight(observer)) * 0.5f), observer);
            }
        } else {
            g.drawString(name, x + (width - strWidth) * 0.5f, y + ascent + (height - fontHeight) * 0.5f);
        }
    }

    public boolean contains(Point pt) {
        return UIUtil.contains(x, y, width, height, pt);
    }
}
