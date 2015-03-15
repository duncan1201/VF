/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.general;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.common.ui.util.Pref;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class FontPanelListeners {

    static class RulerSizeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner)e.getSource();
            Integer val = (Integer)spinner.getModel().getValue();
            Pref.CommonPtyPrefs.getInstance().setRulerFontSize((float)val);
        }
    }
    
    static class BaseSizeListener implements ChangeListener {      
        
        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner)e.getSource();
            Integer val = (Integer)spinner.getModel().getValue();
            
            Pref.CommonPtyPrefs.getInstance().setBaseFontSize(val.floatValue());
        }
    }   
    
    static class AnnotationLabelSizeListener implements ChangeListener {        
        
        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner)e.getSource();
            Integer val = (Integer)spinner.getModel().getValue();
                   
            Pref.CommonPtyPrefs.getInstance().setAnnotationLabelSize(val.floatValue());
        }
    }      
}
