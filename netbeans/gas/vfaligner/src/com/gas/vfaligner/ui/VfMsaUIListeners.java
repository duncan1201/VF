/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.vfaligner.ui;

import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class VfMsaUIListeners {
    
    static class PtyListner implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            VfMsaUI src = (VfMsaUI)evt.getSource();
            String name = evt.getPropertyName();
            if(name.equals("aminoAcids")){
                Boolean aminoAcids = (Boolean)evt.getNewValue();
                src.initComponents(aminoAcids);
            }else if(name.equals("vfMsaParam")){
                VfMsaParam param = (VfMsaParam)evt.getNewValue();
                String matrixDisplayName = param.getMatrix().getDisplayName();
                src.getMatrixCombo().setSelectedItem(matrixDisplayName);
                src.getGapExtSpinner().setValue(param.getExtPenalty());
                src.getGapOpenSpinner().setValue(param.getOpenPenalty());
                String displayName = param.getAlignTypeEnum().getDisplayName();
                src.getAlignTypeCombo().setSelectedItem(displayName);
                src.getIdenticalOnlyCheck().setSelected(param.isIdenticalOnly());
            }
        }
    }
}
