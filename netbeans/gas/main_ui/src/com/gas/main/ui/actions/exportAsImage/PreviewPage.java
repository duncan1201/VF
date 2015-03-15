/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

import com.gas.common.ui.util.FontUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

/**
 *
 * @author dq
 */
public class PreviewPage extends JComponent {

    private BufferedImage image;
    private Float scale;

    public PreviewPage() {
        hookupListeners();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        if (scale != null && image != null) {
            FontMetrics fm = FontUtil.getDefaultFontMetrics();
            int heightFont = fm.getHeight();
            float width = scale * image.getWidth() + heightFont * 3;
            float height = scale * image.getHeight() + heightFont * 3;
            ret.setSize(width, height);
        }
        return ret;
    }

    private void hookupListeners() {
        addPropertyChangeListener(new PreviewPageListeners.PtyListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        final Dimension size = getSize();
        if (this.scale == null || this.image == null || size.width <= 0 || size.height <= 0) {
            return;
        }
        try {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, size.width, size.height);
            Image imageScaled = image.getScaledInstance(Math.round(image.getWidth() * scale), Math.round(image.getHeight() * scale), Image.SCALE_DEFAULT);

            int x = Math.round((size.width - imageScaled.getWidth(this)) * 0.5f);
            int y = Math.round((size.height - imageScaled.getHeight(this)) * 0.5f);
            g.drawImage(imageScaled, x, y, this);

            BevelBorder border = new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.DARK_GRAY);
            border.paintBorder(this, g, x - 2, y - 2, imageScaled.getWidth(this) + 4, imageScaled.getHeight(this) + 4);
        } catch(ArrayIndexOutOfBoundsException aioobe){
            String msg = "Preview is not available because the image size is too large. However, it still can be exported.";
            final FontMetrics fm = g.getFontMetrics();
            final int ascent = fm.getAscent();
            final int heightFont = fm.getHeight();
            final int widthStr = fm.stringWidth(msg);
            int x = Math.round((size.width - widthStr) * 0.5f);
            int y = ascent + Math.round((size.height - heightFont) * 0.5f);
            g.setColor(Color.BLACK);
            g.drawString(msg, 20, y);
        }
    }

    public void setImage(BufferedImage image) {
        BufferedImage old = this.image;
        this.image = image;
        firePropertyChange("image", old, this.image);
    }

    public void setScale(Float scale) {
        Float old = this.scale;
        this.scale = scale;
        firePropertyChange("scale", old, this.scale);
    }
}
