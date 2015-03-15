/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpanel;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class TabBorder extends JComponent {

    int tabPlacement;

    TabBorder(int tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    @Override
    public void paintComponent(Graphics g) {
        TabArea tabArea = UIUtil.getParent(this, TabArea.class);
        if (tabArea == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        final Dimension size = getSize();

        g2d.setPaint(tabArea.selectedColor);
        g2d.fillRect(0, 0, size.width, size.height);

        g2d.setPaint(Color.BLACK);
        Tab selected = tabArea.tabContainer.getSelected();
        if (tabPlacement == SwingConstants.BOTTOM) {
            g2d.drawLine(0, 0, size.width, 0);
            final Rectangle rect = UIUtil.convertRect(tabArea.tabContainer, selected.getBounds(), this);
            g2d.drawLine(0, size.height - 1, rect.x, size.height - 1);
            g2d.drawLine(rect.x + rect.width - 1, size.height - 1, size.width - 1, size.height - 1);
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        final FontMetrics fm = FontUtil.getDefaultFontMetrics();
        int width = Math.round(fm.getHeight() * 0.30f);
        if (tabPlacement == SwingConstants.BOTTOM || tabPlacement == SwingConstants.TOP) {
            ret.height = width;
            ret.width = 1;
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }
}
