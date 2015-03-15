/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.domain.core.tasm.Condig;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class AssemblyPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AssemblyPanel src = (AssemblyPanel) evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("condig")) {
                Condig condig = (Condig) v;
                src.assemblySPane.setConsensusSeq(condig.getLsequence());
                src.assemblySPane.setQV(condig.getQualities());
                src.assemblySPane.setReads(TigrPtUIHelper.toReads(condig), condig.getLsequence().length());
                //VariantMapMdl mdl = TasmUtil.toVariantMapMdl(condig);
                //miniMap.setVariantMapMdl(mdl);
            } else if (pName.equals("baseFont")) {
                src.assemblySPane.setBaseFont(src.baseFont);
            }
        }
    }
}
