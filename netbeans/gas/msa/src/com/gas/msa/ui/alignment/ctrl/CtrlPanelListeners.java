/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl;

import com.gas.domain.core.msa.MSA;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class CtrlPanelListeners {
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            CtrlPanel ctrlPanel = (CtrlPanel)evt.getSource();
            Object v = evt.getNewValue();
            String pName = evt.getPropertyName();
            if(pName.equals("msa")){
                MSA msa = (MSA)v;
                ctrlPanel.getGeneralPanel().setMsa(msa);
                if(msa.getClustalwParam() != null){
                    ctrlPanel.paramsPanel.setTitle("ClustalW");
                    ctrlPanel.paramsPanel.createUI(msa.getClustalwParam());
                }else if(msa.getMuscleParam() != null){
                    ctrlPanel.paramsPanel.setTitle("Muscle");
                    ctrlPanel.paramsPanel.createUI(msa.getMuscleParam());
                }else if(msa.getVfMsaParam() != null){
                    ctrlPanel.paramsPanel.setTitle("VectorFriends Aligner");
                    ctrlPanel.paramsPanel.createUI(msa.getVfMsaParam(), !msa.isDnaByGuess());
                }else{
                    throw new IllegalArgumentException();
                }
            }
        }
    }
}
