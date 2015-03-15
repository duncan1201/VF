/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import com.gas.domain.core.tasm.Condig;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 *
 * @author dq
 */
class ContigsPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ContigsPanel src = (ContigsPanel) evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("variantMapMdl")) {
                int vCount = src.variantMapMdl.getVariantsCount();
                src.variantsPane.setTitle(String.format("Variants(%d)", vCount));
                src.variantsPanel.setVariantMapMdl(src.variantMapMdl);
            } else if (pName.equals("condigs")) {
                List<Condig> condigs = (List<Condig>) v;
                src.contigsPane.setTitle(String.format("Contigs(%d)", condigs.size()));
                src.getContigsTable().getModel().setData(condigs);                
            }
        }
    }
}
