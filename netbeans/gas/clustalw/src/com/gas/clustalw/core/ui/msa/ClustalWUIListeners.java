/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;

/**
 *
 * @author dq
 */
class ClustalWUIListeners {

    static class SwitchListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton)e.getSource();
            ClustalWUI clustalwUI = UIUtil.getParent(src, ClustalWUI.class);
            String tmp = clustalwUI.profile1;
            clustalwUI.profile1 = clustalwUI.profile2;
            clustalwUI.profile2 = tmp;
            clustalwUI.updateProfilesLabel();
        }
    }
    
    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ClustalWUI src = (ClustalWUI)evt.getSource();
            PairwiseOptionsUI pairwiseUI = src.getPairwiseOptionsUI();
            MultipleOptionsUI multipleUI = src.getMultipleOptionsUI();
            String pName = evt.getPropertyName();
            Object nValue = evt.getNewValue();
            if(pName.equals("clustalwParam")){
                ClustalwParam param = (ClustalwParam)nValue;
                pairwiseUI.setMsaParams(param);
                multipleUI.setMsaParams(param);
                if(param.getGeneralParam().getType() == GeneralParam.TYPE.DNA){
                    src.remove(src.proteinGapsPanel);
                }
                src.proteinGapsPanel.populateUI(param);
            }
        }
    }
}
