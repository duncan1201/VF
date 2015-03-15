/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpanel;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class TabContainer extends JPanel {

    int tabPlacement;
    List<Tab> tabs = new ArrayList<Tab>();

    TabContainer(int tabPlacement) {
        this.tabPlacement = tabPlacement;
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(0, insets.left, insets.bottom, insets.right));
        setLayout(new Layout());

        hookupListeners();
    }

    private void hookupListeners() {
    }

    void addTab(Tab tab, int index) {
        FontMetrics fm = FontUtil.getDefaultFontMetrics();
        if (tabPlacement == SwingConstants.BOTTOM) {
            add(tab);
            if (index > -1) {
                tabs.add(index, tab);
            } else {
                tabs.add(tab);
            }
            revalidate();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        final TabArea tabbedPanel = UIUtil.getParent(this, TabArea.class);
        final Insets insets = getInsets();
        final int compCount = getComponentCount();
        if (tabPlacement == SwingConstants.BOTTOM) {
            int width = insets.left + insets.right;
            int height = 0;
            for (int i = 0; i < compCount; i++) {
                Component comp = getComponent(i);
                if (comp instanceof Tab) {
                    Tab tmp = (Tab) comp;
                    Dimension sizeTmp = tmp.getPreferredSize();
                    width += sizeTmp.width + tabbedPanel.getGapTab();
                    height = sizeTmp.height;
                }
            }
            height += insets.top + insets.bottom;
            ret.width = width;
            ret.height = height;
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    boolean setSelected(Tab tab) {
        boolean success = false;
        int compCount = getComponentCount();
        for (int i = 0; i < compCount; i++) {
            Component comp = getComponent(i);
            if (comp instanceof Tab) {
                success = true;
                Tab tmp = (Tab) comp;
                tmp.setSelected(tmp == tab);
            }
        }
        if (success) {
            TabArea tabArea = UIUtil.getParent(this, TabArea.class);
            tabArea.tabBorder.repaint();


            TabbedPanel tabbedPanel = UIUtil.getParent(this, TabbedPanel.class);
            CardLayout cardLayout = (CardLayout) tabbedPanel.contentPaneRef.get().getLayout();
            cardLayout.show(tabbedPanel.contentPaneRef.get(), tab.id);
        }
        return success;
    }

    Tab getSelected() {
        Tab ret = null;
        int compCount = getComponentCount();
        for (int i = 0; i < compCount; i++) {
            Component comp = getComponent(i);
            if (comp instanceof Tab) {
                Tab tab = (Tab) comp;
                if (tab.selected) {
                    ret = tab;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * @param text case insensitive
     */
    Tab getTabByText(String text) {
        Tab ret = null;
        for (Tab tab : tabs) {
            if (text.equalsIgnoreCase(tab.text)) {
                ret = tab;
                break;
            }
        }
        return ret;
    }
}

class Layout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension ret = new Dimension();
        return ret;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension ret = new Dimension();
        return ret;
    }

    @Override
    public void layoutContainer(Container parent) {
        TabContainer tabPanel = (TabContainer) parent;
        TabArea tabArea = UIUtil.getParent(tabPanel, TabArea.class);
        final Insets insets = tabPanel.getInsets();

        int x = insets.left;
        int y = 0;
        Iterator<Tab> itr = tabPanel.tabs.iterator();

        while (itr.hasNext()) {
            Tab tab = itr.next();
            Dimension size = tab.getPreferredSize();
            tab.setBounds(x, y, size.width, size.height);
            x += size.width + tabArea.getGapTab();
        }
    }
}
