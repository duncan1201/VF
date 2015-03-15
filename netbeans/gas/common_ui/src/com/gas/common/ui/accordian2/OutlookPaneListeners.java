/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JSplitPane;

/**
 *
 * @author dq
 */
class OutlookPaneListeners {

    protected static class CompAdpt extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            updateDividerLocation(e);
        }

        @Override
        public void componentShown(ComponentEvent e) {
            updateDividerLocation(e);
        }

        private void updateDividerLocation(ComponentEvent e) {
            final OutlookPane src = (OutlookPane) e.getSource();
            final Dimension size = src.getSize();
            final BottomPanel bPanel = src.bottomPanel;
            final JSplitPane sPane = src.splitPane;
            final int dividerSize = sPane.getDividerSize();
            Dimension pSize = bPanel.getPreferredSize();

            sPane.setDividerLocation(size.height - pSize.height - dividerSize - 1);
        }
    }

    protected static class PtyListener implements PropertyChangeListener {

        WeakReference<OutlookPane> outlookPaneRef;
        boolean busy = false;

        PtyListener(OutlookPane outlookPane) {
            outlookPaneRef = new WeakReference<OutlookPane>(outlookPane);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            JSplitPane src = (JSplitPane) evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("dividerLocation")) {
                if (busy) {
                    return;
                }
                Integer dividerLoc = (Integer) v;

                final Dimension size = outlookPaneRef.get().getSize();
                final BottomPanel bPanel = outlookPaneRef.get().bottomPanel;
                final int rowHeight = bPanel.getRowHeight();
                final int dividerSize = src.getDividerSize();
                Dimension pSize = bPanel.getPreferredSize();
                final int curLoc = size.height - pSize.height - 1;
                int dividerMaxY = dividerLoc + dividerSize;
                if (dividerMaxY > curLoc) {
                    double rowCount = Math.round((dividerMaxY - curLoc) * 1.0f / rowHeight);
                    bPanel.shrink((int) rowCount);
                    pSize = bPanel.getPreferredSize();
                    busy = true;
                    src.setDividerLocation(size.height - pSize.height - dividerSize - 1);
                    busy = false;
                }
                if (dividerMaxY < curLoc) {
                    double rowCount = Math.round((curLoc - dividerMaxY) * 1.0f / rowHeight);
                    bPanel.expand((int) rowCount);
                    pSize = bPanel.getPreferredSize();
                    busy = true;
                    src.setDividerLocation(size.height - pSize.height - dividerSize - 1);
                    busy = false;
                }
            }
        }
    }
}
