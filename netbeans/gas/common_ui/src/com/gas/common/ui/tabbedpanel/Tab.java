/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpanel;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class Tab extends JComponent {

    String id;
    String text;
    Image image;
    boolean selected;
    int tabPlacement;

    Tab(String text, Image image, int tabPlacement) {
        this.text = text;
        this.image = image;
        this.tabPlacement = tabPlacement;

        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new TabListeners.PtyListener());
        addMouseListener(new TabListeners.MouseAdpt());
    }

    void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        if (text == null || text.isEmpty() || size.width <= 0 || size.height <= 0) {
            return;
        }
        
        TabArea tabArea = UIUtil.getParent(this, TabArea.class);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (selected) {
            Font selectedFont = tabArea.getSelectedFont();
            g2d.setFont(selectedFont);
            g2d.setPaint(tabArea.selectedColor);
        } else {
            Font selectedFont = tabArea.getUnselectedFont();
            g2d.setFont(selectedFont);
            g2d.setPaint(tabArea.color);
        }
        g2d.fillRect(0, 0, size.width, size.height);

        g2d.setColor(Color.BLACK);
        final FontMetrics fm = g2d.getFontMetrics();
        final int ascent = fm.getAscent();
        if (tabPlacement == SwingConstants.BOTTOM) {
            int x = Math.round((size.width - fm.stringWidth(text)) * 0.5f);
            int y = ascent + Math.round((size.height - fm.getHeight()) * 0.5f);
            g2d.drawString(text, x, y);
            GeneralPath path = createBorder();
            g2d.draw(path);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private GeneralPath createBorder() {
        final Dimension size = getSize();
        GeneralPath ret = new GeneralPath();
        if (tabPlacement == SwingConstants.BOTTOM) {
            ret.moveTo(0, 0);
            ret.lineTo(0, size.height - 1);
            ret.lineTo(size.width - 1, size.height - 1);
            ret.lineTo(size.width - 1, 0);
        }
        return ret;
    }

    @Override
    public Dimension getSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        Insets insets = getInsets();
        TabArea tabArea = UIUtil.getParent(this, TabArea.class);
        if (tabArea == null || tabArea.getSelectedFont() == null) {
            return ret;
        }
        FontMetrics fm = FontUtil.getFontMetrics(tabArea.getSelectedFont());
        if (tabPlacement == SwingConstants.BOTTOM) {

            int height = fm.getHeight() + insets.top + insets.bottom;
            ret.height = height;
            if (text != null) {
                ret.width = fm.stringWidth(text) + insets.left + insets.right;
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }
}
