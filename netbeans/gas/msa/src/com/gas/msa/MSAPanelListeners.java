/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa;

import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class MSAPanelListeners {

    static class TabbedPaneListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            MSAPanel msaPanel = UIUtil.getParent(pane, MSAPanel.class);
            Component comp = pane.getSelectedComponent();
            msaPanel.setSelected(comp);
        }
    }
}
