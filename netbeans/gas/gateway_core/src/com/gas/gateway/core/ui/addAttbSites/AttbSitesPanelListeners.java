/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.PrimerAdapter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class AttbSitesPanelListeners {
    
    static class AttbSiteRadioBtnListener implements ItemListener{

        private AddAttbSitesPanel panel;
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(panel == null){
                Object src = e.getSource();                
                AddAttbSitesPanel _panel = UIUtil.getParent((Component)src, AddAttbSitesPanel.class);
                if(_panel != null){
                    this.panel = _panel;
                }
            }
            if(panel == null){
                return ;
            }
            PrimerAdapter leftPrimerAdapter = panel.getLeftPrimerAdapter();            
            if(leftPrimerAdapter != null){
                panel.getSensePanel().updateUI(leftPrimerAdapter);
            }
            PrimerAdapter rightPrimerAdapter = panel.getRightPrimerAdapter();
            if(rightPrimerAdapter != null){
                panel.getAntisensePanel().updateUI(rightPrimerAdapter);
            }
            panel.updatePreview();
        }
    }
}
