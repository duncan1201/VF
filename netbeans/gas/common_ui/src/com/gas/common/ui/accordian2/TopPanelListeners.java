/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class TopPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            TopPanel src = (TopPanel) evt.getSource();
            Object v = evt.getNewValue();
            if (name.equals("barName")) {
                String barName = (String) v;
                UIUtil.removeComponentsByClass(src, BarPanel.class);

                OutlookPane pane = UIUtil.getParent(src, OutlookPane.class);
                IOutlookPanel panel = pane.contentsMap.get(barName);
                src.scrollPaneRef.get().setViewportView((Component) panel);
                if (pane.contentBgColor != null) {
                    UIUtil.setBackground(src.scrollPaneRef.get(), pane.contentBgColor, JPanel.class);
                }
                ((JComponent) panel).revalidate();
                ((JComponent) panel).repaint();
                panel.expanded();

                Icon icon = pane.iconsMap.get(barName);
                if (icon == null) {
                    icon = OutlookPane.defaultExpandedIcon;
                }
                BarPanel barPanel = new BarPanel(barName, icon, true, true);
                src.add(barPanel, BorderLayout.NORTH);
                barPanel.revalidate();


            }
        }
    }
}
