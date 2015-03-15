/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.layout;

import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.Box;

/**
 *
 * @author dq
 */
public class CollapsibleLayout implements LayoutManager {

    @Override
    public void layoutContainer(Container parent) {

        Dimension parentSize = parent.getSize();

        int expandedCount = 0;
        int height = 0;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component comp = parent.getComponent(i);
            if (comp instanceof CollapsibleTitlePanel) {
                CollapsibleTitlePanel cPanel = (CollapsibleTitlePanel) comp;
                if (!cPanel.isCollapsed()) {
                    expandedCount++;
                } else {
                    height += cPanel.getPreferredSize().height;
                }
            } else { // remove the filler if any
                parent.remove(comp);
                i--;
            }

        }

        int extraHeight = parentSize.height - height;
        if (expandedCount > 0) {
            int unitExtraHeight = extraHeight / expandedCount;
            for (int i = 0; i < parent.getComponentCount(); i++) {
                Component comp = parent.getComponent(i);
                if (comp instanceof CollapsibleTitlePanel) {
                    CollapsibleTitlePanel cPanel = (CollapsibleTitlePanel) comp;
                    if (!cPanel.isCollapsed()) {
                        Dimension size = cPanel.getPreferredSize();
                        cPanel.setPreferredSize(new Dimension(size.width, unitExtraHeight));
                    }
                }
            }
            Component filler = Box.createRigidArea(new Dimension(1, extraHeight % expandedCount));
            parent.add(filler);
        } else { // all collapsed
            Component filler = Box.createRigidArea(new Dimension(1, extraHeight));
            parent.add(filler);
        }


        int y = 0;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component comp = parent.getComponent(i);
            Dimension size = comp.getPreferredSize();
            comp.setBounds(0, y, parentSize.width, size.height);
            y += size.height;
        }
        parent.setPreferredSize(new Dimension(parentSize.width, y));
    }

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
}
