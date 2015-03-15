/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tigr.Kromatogram;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class ChromatogramCompListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ChromatogramComp src = (ChromatogramComp)evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(name.equals("kromatogram")){
                Kromatogram k = (Kromatogram)v;
                src.repaint();
            }
        }
    }
}
