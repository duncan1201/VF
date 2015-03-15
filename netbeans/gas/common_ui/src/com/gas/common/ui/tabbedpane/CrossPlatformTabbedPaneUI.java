/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author dq
 */
public class CrossPlatformTabbedPaneUI extends BasicTabbedPaneUI {

    private Insets _tabAreaInsets;
    private Insets _contentBorderInsets;
    private Color _selectedColor;
    private Color _shadow;
    private Color _darkShadow;

    public void setFocus(Color focus) {
        this.focus = focus;
    }

    public void setShadow(Color c) {
        _shadow = c;
    }

    public void setDarkShadow(Color c) {
        _darkShadow = c;
    }

    public void setSelectedColor(Color color) {
        this._selectedColor = color;
    }

    public Color getShadow() {
        return shadow;
    }

    public void setTabInsets(Insets tabInsets) {
        this.tabInsets = tabInsets;
    }

    public void setContentBorderInsets(Insets insets) {
        _contentBorderInsets = insets;
    }

    @Override
    protected Insets getContentBorderInsets(int tabPlacement) {
        return _contentBorderInsets;
    }

    public void setSelectedTabPadInsets(Insets selectedTabPadInsets) {
        this.selectedTabPadInsets = selectedTabPadInsets;
    }

    public void setTabAreaInsets(Insets tabAreaInsets) {
        this._tabAreaInsets = tabAreaInsets;
    }

    @Override
    protected Insets getTabAreaInsets(int tabPlacement) {
        return _tabAreaInsets;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement,
            int tabIndex,
            int x, int y, int w, int h,
            boolean isSelected) {
        Color c = !isSelected || _selectedColor == null
                ? tabPane.getBackgroundAt(tabIndex) : _selectedColor;
        g.setColor(c);
        switch (tabPlacement) {
            case LEFT:
                g.fillRect(x + 1, y + 1, w - 1, h - 3);
                break;
            case RIGHT:
                g.fillRect(x, y + 1, w - 2, h - 3);
                break;
            case BOTTOM:
                g.fillRect(x + 1, y, w - 3, h - 1);
                break;
            case TOP:
            default:
                g.fillRect(x + 1, y + 1, w - 3, h - 1);
        }
    }

    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
            int selectedIndex,
            int x, int y, int w, int h) {
        Rectangle selRect = selectedIndex < 0 ? null
                : getTabBounds(selectedIndex, calcRect);

        g.setColor(_shadow);

        // Draw unbroken line if tabs are not on RIGHT, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        //
        if (tabPlacement != RIGHT || selectedIndex < 0
                || (selRect.x - 1 > w)
                || (selRect.y < y || selRect.y > y + h)) {
            g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 3);
            g.setColor(_darkShadow);
            g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
        } else {
            // Break line to show visual connection to selected tab
            g.drawLine(x + w - 2, y + 1, x + w - 2, selRect.y - 1);
            g.setColor(_darkShadow);
            g.drawLine(x + w - 1, y, x + w - 1, selRect.y - 1);

            if (selRect.y + selRect.height < y + h - 2) {
                g.setColor(_shadow);
                g.drawLine(x + w - 2, selRect.y + selRect.height,
                        x + w - 2, y + h - 2);
                g.setColor(_darkShadow);
                g.drawLine(x + w - 1, selRect.y + selRect.height,
                        x + w - 1, y + h - 2);
            }
        }
    }
}
