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
public class ChromatogramEditorListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            ChromatogramEditor src = (ChromatogramEditor)evt.getSource();
            Object v = evt.getNewValue();
            if(name.equals("kromatogram")){
                Kromatogram k = (Kromatogram)v;
                UIUtil.setTopCompName(src, k.getName());
                src.chromatogramPanel.setKromatogram(k);           
            }
        }
    }
}
