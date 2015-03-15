/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.util.UIUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author dq
 */
class PrimerDetailsPanelListeners {
    
    static class BtnListeners implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton)e.getSource();
            String cmd = e.getActionCommand();
            if(cmd.equals("back")){
                GibsonAssemblyPanel panel = UIUtil.getParent(src, GibsonAssemblyPanel.class);
                panel.showCenterPanel(GibsonAssemblyPanel.SETTINGS_PANEL);
            }
        }
    }
}
