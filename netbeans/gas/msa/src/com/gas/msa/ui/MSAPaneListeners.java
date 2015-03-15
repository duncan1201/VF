/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui;

import com.gas.common.ui.tabbedpanel.TabbedPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.domain.core.msa.MSA;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class MSAPaneListeners {

    static class TabbedPanelSelectListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            TabbedPanel src = (TabbedPanel)e.getSource();
            MSAPane msaPane = UIUtil.getParent(src, MSAPane.class);
            msaPane.getMsa().getMsaSetting().setSelectedTab(src.getSelected());
            System.out.println();
        }
    }
    
    static class PtyListener implements PropertyChangeListener {

        IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            MSAPane src = (MSAPane) evt.getSource();
            if (pName.equals("msa")) {
                MSA msa = (MSA) v;
                if (msa.getConsensus() == null) {
                    msaService.createConsensus(msa);
                }
                if(msa.getQualityScores() == null || msa.getQualityScores().length == 0){
                    msaService.createQualityScores(msa);
                }
                src.getAlignPane().setMsa(msa);

                if (msa.getNewick() != null) {
                    src.initTreePane();
                    src.getTreePane().refreshUI(msa);                    
                }
                
                msa.getMsaSetting().addPropertyChangeListener(new SettingListener(src));
                src.zoomPanel.setZoom(msa.getMsaSetting().getZoom());
                src.tabbedPanel.setSelectedTab(msa.getMsaSetting().getSelectedTab());
            }
        }
    }
    
    static class SettingListener implements PropertyChangeListener{

        WeakReference<MSAPane> msaPaneRef;
        
        SettingListener(MSAPane msaPane){
            msaPaneRef = new WeakReference<MSAPane>(msaPane);
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(name.equals("zoom")){
                Integer zoom = (Integer)v;
                msaPaneRef.get().getAlignPane().setZoom(zoom);
            }
        }
    }
}
