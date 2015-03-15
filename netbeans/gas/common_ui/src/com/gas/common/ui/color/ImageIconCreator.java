/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author dq
 */
public class ImageIconCreator {

    private static final int GAP = 4;
    private static final Character[] AAs = {'A', 'G', 'S', 'L'};
    private static final Character[] NUCLETIDEs = {'A', 'T', 'C', 'G'};

    public static ImageIcon createImageIcon(IColorProvider provider) {
        ImageIcon ret = new ImageIcon(createImage(provider));
        return ret;
    }

    private static Image createImage(IColorProvider provider) {
        Font font = FontUtil.getDefaultMSFont(FontUtil.getDefaultFontSize());
        FontMetrics fm = FontUtil.getFontMetrics(font);
        int charWidth = fm.charWidth('a');

        BufferedImage ret =
                new BufferedImage((charWidth + GAP) * 4, fm.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Character[] chars = null;
        if (provider.isForProtein()) {
            chars = AAs;
        } else {
            chars = NUCLETIDEs;
        }
        Graphics2D g2d = ret.createGraphics();
        for (int i = 0; i < chars.length; i++) {
            Color color = provider.getColor(chars[i]);
            Color bgColor;
            Color fgColor;
            if (provider.isBackgroundColor()) {
                bgColor = color;
                fgColor = ColorUtil.getForeground(color);
            } else {
                fgColor = color;
                bgColor = Color.WHITE;
            }
            g2d.setColor(bgColor);
            g2d.fillRect((charWidth + GAP) * i, 0, (charWidth + GAP), fm.getHeight());
            g2d.setColor(fgColor);
            g2d.drawString(chars[i].toString(), (charWidth + GAP) * i + GAP * 0.5f, fm.getAscent());
        }

        g2d.dispose();
        return ret;
    }
}
