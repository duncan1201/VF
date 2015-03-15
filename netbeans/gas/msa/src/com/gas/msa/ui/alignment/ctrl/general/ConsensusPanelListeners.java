/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.general;

import com.gas.common.ui.util.UIUtil;
import com.gas.msa.ui.MSAEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class ConsensusPanelListeners {

    static class PtyListener implements PropertyChangeListener {


        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
        }
    }

    static class GapListener implements ItemListener {

        WeakReference<ConsensusPanel> ref;

        GapListener(WeakReference<ConsensusPanel> ref) {
            this.ref = ref;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (ConsensusPanel.populatingUI) {
                return;
            }
            JComponent src = (JComponent)e.getSource();
            ref.get().getAlignPane().getMsa().getConsensusParam().setIgnoreGaps(e.getStateChange() == ItemEvent.SELECTED);
            ref.get().recalculateConsensus();
            
            setCanSave(src);
        }
    }
    
    private static void setCanSave(JComponent src){
        if(ConsensusPanel.populatingUI){
            return;
        }
        MSAEditor editor = UIUtil.getParent(src, MSAEditor.class);
        if(editor != null){
            editor.setCanSave();
        }
    }
    
    static class ThresholdListener implements ChangeListener{

        WeakReference<ConsensusPanel> ref;
        
        ThresholdListener(WeakReference<ConsensusPanel> ref){
            this.ref = ref;
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner)e.getSource();
            Number threshold = (Number)spinner.getValue();
            ref.get().getAlignPane().getMsa().getConsensusParam().setThreshold(threshold.floatValue());
            ref.get().recalculateConsensus();
            
            setCanSave(spinner);
        }
    }

    static class MajorityListener implements ActionListener {

        WeakReference<ConsensusPanel> ref;

        MajorityListener(WeakReference<ConsensusPanel> ref) {
            this.ref = ref;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (ConsensusPanel.populatingUI) {
                return;
            }
            JComponent src = (JComponent)e.getSource();
            ref.get().getAlignPane().getMsa().getConsensusParam().setPlurality(false);
            ref.get().thresholdRef.get().setEnabled(true);
            ref.get().recalculateConsensus();
            
            setCanSave(src);
        }
    }

    static class PluralityListener implements ActionListener {

        WeakReference<ConsensusPanel> ref;
        
        PluralityListener(WeakReference<ConsensusPanel> ref){
            this.ref = ref;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (ConsensusPanel.populatingUI) {
                return;
            }
            JComponent src = (JComponent)e.getSource();
            ref.get().getAlignPane().getMsa().getConsensusParam().setPlurality(true);
            ref.get().thresholdRef.get().setEnabled(false);
            ref.get().recalculateConsensus();
            
            setCanSave(src);
        }
    }
}
