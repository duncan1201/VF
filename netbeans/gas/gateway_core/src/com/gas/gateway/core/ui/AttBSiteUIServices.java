/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui;

import com.gas.common.ui.util.FontUtil;
import com.gas.gateway.core.service.api.AttSite;
import com.gas.gateway.core.ui.api.IAttBSiteUIServices;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IAttBSiteUIServices.class)
public class AttBSiteUIServices implements IAttBSiteUIServices {
    
    private Map<String, Color> bgColors = new HashMap<String, Color>();
    private Color fgColor = Color.WHITE;

    public AttBSiteUIServices() {
        init();
    }

    private void init() {
        if (bgColors.isEmpty()) {
            bgColors.put(AttSite.attB1.getName(), new Color(109, 94, 0));
            bgColors.put(AttSite.attB1r.getName(), new Color(109, 94, 0));
            bgColors.put(AttSite.attB2.getName(), new Color(0, 143, 212));
            bgColors.put(AttSite.attB2r.getName(), new Color(0, 143, 212));
            bgColors.put(AttSite.attB3.getName(), new Color(191, 49, 26));
            bgColors.put(AttSite.attB3r.getName(), new Color(191, 49, 26));
            bgColors.put(AttSite.attB4.getName(), new Color(233, 166, 31));
            bgColors.put(AttSite.attB4r.getName(), new Color(233, 166, 31));
            bgColors.put(AttSite.attB5.getName(), new Color(121, 29, 126));
            bgColors.put(AttSite.attB5r.getName(), new Color(121, 29, 126));
        }
    }

    @Override
    public Color getBackgroundColor(String name) {
        Color ret = bgColors.get(name);
        return ret;
    }

    @Override
    public Color getForegroundColor(AttSite site) {
        return fgColor;
    }

    @Override
    public Image createImage(AttSite left, AttSite right, boolean leftDir, boolean rightDir) {
        final Insets insets = new Insets(1, 1, 1, 1);
        final FontMetrics fm = FontUtil.getDefaultFontMetrics();
        Dimension imageSize = getImageSize(left, right, insets, fm);
        BufferedImage ret =
                new BufferedImage(imageSize.width, imageSize.height,
                BufferedImage.TYPE_INT_ARGB);
        drawOnImage(ret, left, right, insets, fm, leftDir, rightDir);
        return ret;

    }

    private void drawOnImage(BufferedImage image, AttSite left, AttSite right, Insets insets, FontMetrics fm, boolean leftDir, boolean rightDir) {       
        if (left == null || right == null) {
            return;
        }
        Graphics2D g = image.createGraphics();

        final Dimension size = new Dimension(image.getWidth(), image.getHeight());

        final int siteWidth = fm.stringWidth(right.getName()) + insets.left + insets.right + Math.round(size.height * 0.5f);

        // draw the left AttB site

        drawAttSite(left, g, new Rectangle(0, 0, siteWidth, image.getHeight()), insets, fm, leftDir);

        // draw the bar
        int x = siteWidth + 1;
        final float heightRatio = 0.35f;
        int y = Math.round(size.height * (1 - heightRatio) * 0.5f);
        int width = image.getWidth() - siteWidth - siteWidth;
        int height = Math.round(size.height * heightRatio);
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);

        // draw the right AttB site

        drawAttSite(right, g, new Rectangle(image.getWidth() - siteWidth, 0, siteWidth, image.getHeight()), insets, fm, rightDir);

        g.dispose();
    }

    private void drawAttSite(AttSite site, Graphics2D g, final Rectangle rect, Insets insets, FontMetrics fm, boolean dir) {
        
        final int ascent = fm.getAscent();
        // draw the AttB site
        int x = rect.x;
        int y = rect.y;
        Color bgColor = getBackgroundColor(site.getName());
        g.setColor(bgColor);
        GeneralPath path = createPath(rect, dir);
        g.fill(path);
        x += insets.left;
        y += ascent + insets.top;
        fgColor = getForegroundColor(site);
        g.setColor(fgColor);
        if (!dir) {
            x += rect.height * 0.5f;
        }
        g.drawString(site.getName(), x, y);
    }
    
    private GeneralPath createPath(Rectangle rect, boolean forward) {
        GeneralPath ret = new GeneralPath();
        double offset = rect.height * 0.5;
        if (forward) {
            ret.moveTo(rect.x, rect.y);
            ret.lineTo(rect.x + rect.width - offset, rect.y);
            ret.lineTo(rect.x + rect.width, rect.y + rect.height * 0.5);
            ret.lineTo(rect.x + rect.width - offset, rect.y + rect.height);
            ret.lineTo(rect.x, rect.y + rect.height);
            ret.closePath();
        } else {
            ret.moveTo(rect.x + rect.width, rect.y);
            ret.lineTo(rect.x + rect.width, rect.y + rect.height);
            ret.lineTo(rect.x + offset, rect.y + rect.height);
            ret.lineTo(rect.x, rect.y + rect.height * 0.5);
            ret.lineTo(rect.x + offset, rect.y);
            ret.closePath();
        }
        return ret;
    }

    private Dimension getImageSize(AttSite left, AttSite right, final Insets insets, final FontMetrics fm) {

        Dimension ret = new Dimension();
        int stringWidth = 0;
        if (fm == null || left == null || right == null) {
            return ret;
        }

        String name = left.getName();
        stringWidth += fm.stringWidth(name);
        stringWidth += insets.left + insets.right;

        name = right.getName();
        stringWidth += fm.stringWidth(name);
        stringWidth += insets.left + insets.right;

        final int width = Math.round(stringWidth * 1.5f);
        final int height = fm.getHeight() + insets.top + insets.bottom;
        ret.setSize(width, height);
        return ret;
    }

    @Override
    public Icon createIcon(AttSite left, AttSite right, boolean leftDir, boolean rightDir) {
        Image image = createImage(left, right, leftDir, rightDir);
        ImageIcon ret = new ImageIcon(image);
        return ret;
    }
}
