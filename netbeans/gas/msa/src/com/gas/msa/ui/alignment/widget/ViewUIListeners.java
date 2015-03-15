/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.color.IColorProvider;
import com.gas.domain.core.msa.MSA;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class ViewUIListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ViewUI src = (ViewUI)evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(pName.equals("msa")){
                src.getMsaComp().setMsa((MSA)v);
                Dimension pSize = src.getMsaComp().getPreferredSize();
                src.setPreferredSize(pSize);
            }else if(pName.equals("colorProvider")){
                src.getMsaComp().setColorProvider((IColorProvider)v);
            }
        }
    }
}
