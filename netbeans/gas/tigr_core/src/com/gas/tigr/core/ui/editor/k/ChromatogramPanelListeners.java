/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tigr.Kromatogram;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class ChromatogramPanelListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ChromatogramPanel src = (ChromatogramPanel)evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(name.equals("kromatogram")){
                Kromatogram kromatogram = (Kromatogram)v;
                Integer maxPeak = kromatogram.getMaxPeak();                
                final FontMetrics fm = FontUtil.getFontMetrics(src.rowHeaderUI.getFont());
                final Insets insets = src.rowHeaderUI.getInsets();
                
                UIUtil.setPreferredWidth(src.rowHeaderUI, fm.stringWidth(maxPeak.toString() + insets.left + insets.right));
                UIUtil.setPreferredWidth(src.chromatogramComp, Math.round(kromatogram.getTraceLength() * 2.1f));                
                src.chromatogramComp.setKromatogram(kromatogram);
                src.rowHeaderUI.setMax(maxPeak);
            }
        }
    }
    
}
