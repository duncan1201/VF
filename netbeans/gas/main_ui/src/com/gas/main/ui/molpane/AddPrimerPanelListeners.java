/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author dq
 */
class AddPrimerPanelListeners {

    static class RadioBtnsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Component src = (Component) e.getSource();
            AddPrimerPanel p = UIUtil.getParent(src, AddPrimerPanel.class);
            final String cmd = e.getActionCommand();
            if (cmd.equals("probe")) {
                p.showProbeTmSettings();
            } else {
                p.showPrimersTmSettings();
            }
        }
    }
}
