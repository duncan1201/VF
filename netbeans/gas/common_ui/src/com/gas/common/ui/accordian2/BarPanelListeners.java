/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class BarPanelListeners {

    static class MouseAdpt extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            BarPanel p = (BarPanel) e.getSource();
            final String name = p.getText();
            boolean expanded = p.isExpanded();
            if (!expanded) {
                BottomPanel bp = UIUtil.getParent(p, BottomPanel.class);
                if (bp != null) {
                    bp.setSelected(name);
                    bp.setExpanded(name);
                }
                OutlookPane pane = UIUtil.getParent(p, OutlookPane.class);
                pane.show(name);
            } else {
                OutlookPane pane = UIUtil.getParent(p, OutlookPane.class);
                pane.showNext(name);
            }
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            BarPanel src = (BarPanel) evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("selected")) {
                Boolean selected = (Boolean) v;
                if (selected) {
                    src.label.setText(String.format("<html><b>%s</b></html>", src.text));
                    src.labelIcon.setIcon(ImageHelper.createImageIcon(ImageNames.CHECK_16));
                } else {
                    src.label.setText(src.text);
                    src.labelIcon.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                }
                //src.label.revalidate();
                //src.label.repaint();
                src.repaint();
            }
        }
    }
}
