/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.RecomType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/**
 *
 * @author dq
 */
class RecomTypePanelListeners {
    protected static class BtnListener implements ActionListener{

        private AddAttbSitesPanel panel;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(panel == null){
                JRadioButton btn = (JRadioButton)e.getSource();
                AddAttbSitesPanel _panel = UIUtil.getParent(btn, AddAttbSitesPanel.class);
                if(_panel == null) return;
                this.panel = _panel;
            }
            if(panel == null) return;
            
            final String cmd = e.getActionCommand();
            RecomType recomType = RecomType.getRecomTypeByDesc(cmd);                      
            panel.setRecombinationType(recomType);            
        }
    }
}
