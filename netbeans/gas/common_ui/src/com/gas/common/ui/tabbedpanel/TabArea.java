/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpanel;

import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class TabArea extends JPanel {

    int tabPlacement;
    TabBorder tabBorder;
    TabContainer tabContainer;
    JPanel contentPane;
    Paint selectedColor = new Color(168, 189, 208);
    Paint color = ColorCnst.BABY_BLUE;
    Insets insetsTab = new Insets(3, 3, 3, 3);
    Font selectedFont = FontUtil.getDefaultSansSerifFont();
    Font unselectedFont = FontUtil.getDefaultSansSerifFont();
    int gapTab = 3;

    /**
     * @param tabPlacement possible values: SwingConstants.LEFT/RIGHT/TOP/BOTTOM
     */
    public TabArea(int tabPlacement) {
        this.tabPlacement = tabPlacement;
        initComponents();
    }

    void addTab(String title, Image image, int index, String id) {
        Tab tab = new Tab(title, image, tabPlacement);
        tab.id = id;
        final Insets insets = getInsetsTab();
        tab.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        tabContainer.addTab(tab, index);
        tabContainer.setSelected(tab);
    }

    Font getSelectedFont() {
        return selectedFont;
    }

    Font getUnselectedFont() {
        return unselectedFont;
    }

    Insets getInsetsTab() {
        if (insetsTab == null) {
            insetsTab = createInsetsTab();
        }
        return insetsTab;
    }

    int getGapTab() {
        return gapTab;
    }

    private Insets createInsetsTab() {
        return new Insets(3, 3, 3, 3);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        Dimension sizeTabPanel = tabContainer.getPreferredSize();
        Dimension sizeTabBorder = tabBorder.getPreferredSize();
        if (tabPlacement == SwingConstants.BOTTOM || tabPlacement == SwingConstants.TOP) {
            ret.height = sizeTabPanel.height + sizeTabBorder.height;
            ret.width = 1;
        }
        return ret;
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        contentPane = new JPanel(new GridBagLayout());
        if (tabPlacement == SwingConstants.BOTTOM) {
            GridBagConstraints c = null;
            int gridy = 0;

            c = new GridBagConstraints();
            c.gridy = gridy++;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.gridwidth = 2;
            tabBorder = new TabBorder(tabPlacement);
            add(tabBorder, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = gridy;
            c.anchor = GridBagConstraints.NORTH;
            tabContainer = new TabContainer(tabPlacement);
            add(tabContainer, c);

            c = new GridBagConstraints();
            c.gridy = gridy;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            add(contentPane, c);

        } else if (tabPlacement == SwingConstants.RIGHT) {
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
