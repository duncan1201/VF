/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.annotateAttSites;

import com.gas.common.ui.util.UIUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/**
 *
 * @author dq
 */
public class AnnotateSitesPanelListener {

    protected static class PcrProductsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton btn = (JRadioButton)e.getSource();
            AnnotateSitesPanel panel = UIUtil.getParent(btn, AnnotateSitesPanel.class);
            panel.setSearchType(AnnotateSitesPanel.SEARCH_TYPE.attBSites);            
        }
    }    
    
    protected static class DonorVectorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton btn = (JRadioButton)e.getSource();
            AnnotateSitesPanel panel = UIUtil.getParent(btn, AnnotateSitesPanel.class);
            panel.setSearchType(AnnotateSitesPanel.SEARCH_TYPE.attPSites);            
        }
    }

    protected static class EntryVectorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton btn = (JRadioButton)e.getSource();
            AnnotateSitesPanel panel = UIUtil.getParent(btn, AnnotateSitesPanel.class);
            panel.setSearchType(AnnotateSitesPanel.SEARCH_TYPE.attLSites);            
        }
    }
    
    protected static class ExpVectorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton btn = (JRadioButton)e.getSource();
            AnnotateSitesPanel panel = UIUtil.getParent(btn, AnnotateSitesPanel.class);
            panel.setSearchType(AnnotateSitesPanel.SEARCH_TYPE.attRSites);
        }
    }    
}
