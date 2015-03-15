/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.painter.LinearGradientPainter;
import com.gas.common.ui.painter.LinearGradientPainterFactory;
import com.gas.common.ui.util.FontUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dq
 */
public class ArrowImageCreator {

    private static Map<String, Image> images = new HashMap<String, Image>();

    public static Image createImage(Color seedColor, int width, int height) {
        return createImage(seedColor, new Dimension(width, height));
    }

    public static Image createImage(Color seedColor, Dimension size) {
        String key = createImageKey(seedColor, size);
        if (!images.containsKey(key)) {
            BufferedImage ret = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = ret.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Arrow arrow = new Arrow();
            arrow.setSize(size.width, size.height);
            arrow.setTextFont(FontUtil.getDefaultMSFont());
            arrow.setForward(Boolean.TRUE);
            arrow.setDisplayText("");

            Rectangle rect = new Rectangle(size);
            Stroke stroke = g2d.getStroke();
            CNST.CAP cap = PathCreator.getEndCAP(key, Boolean.TRUE, false, false);
            List<GeneralPath> paths = PathCreator.createSingleArrowPath(arrow, false, false, null, rect, (BasicStroke) stroke);
            LinearGradientPainter painter = LinearGradientPainterFactory.create(seedColor);
            painter.setGeneralPath(paths.get(0));
            painter.paint(g2d, null, size.width, size.height);
            images.put(key, ret);
        }
        return images.get(key);
    }

    private static String createImageKey(Color seedColor, Dimension size) {
        StringBuilder ret = new StringBuilder();
        ret.append(seedColor.getRGB());
        ret.append(size.width);
        ret.append(size.height);
        return ret.toString();
    }
}
