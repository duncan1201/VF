/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.clustalw;

import com.gas.domain.core.msa.MSA;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class TreePanelListeners {
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            TreePanel src = (TreePanel)evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(name.equals("msa")){
                MSA msa = (MSA)v;
                src.clustalwPanelRef.get().populateUI(msa);
            }
        }
    }
}
