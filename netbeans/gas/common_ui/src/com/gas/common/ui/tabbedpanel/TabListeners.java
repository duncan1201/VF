/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpanel;

import com.gas.common.ui.util.UIUtil;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class TabListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Tab src = (Tab) evt.getSource();
            final String name = evt.getPropertyName();
            if (name.equals("selected")) {
                src.repaint();
            }
        }
    }

    static class MouseAdpt extends MouseAdapter {

        MouseAdpt() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Tab src = (Tab) e.getSource();
            final TabbedPanel tabbedPanel = UIUtil.getParent(src, TabbedPanel.class);
            final TabContainer tabContainer = tabbedPanel.tabAreaRef.get().tabContainer;
            Tab selected = tabContainer.getSelected();
            if (selected != src) {
                tabContainer.setSelected(src);
                tabbedPanel.fireChangeEvents();
            }
        }
    }
}
