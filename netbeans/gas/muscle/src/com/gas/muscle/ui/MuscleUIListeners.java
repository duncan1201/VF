/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.muscle.ui;

import com.gas.common.ui.util.ExecutableFileFilter;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.muscle.MuscleParam;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class MuscleUIListeners {
    
    static class ChangeBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Component source = (Component)e.getSource();
            MuscleUI muscleUI = UIUtil.getParent(source, MuscleUI.class);
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(new ExecutableFileFilter());
            Integer answer = UIUtil.showDialog(fc, source);
            if (answer.equals(JFileChooser.APPROVE_OPTION)) {
                File file = fc.getSelectedFile();
                muscleUI.setExtPath(file.getAbsolutePath());
            }
        }
    }
    
    static class SwitchListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton)e.getSource();
            MuscleUI muscleUI = UIUtil.getParent(src, MuscleUI.class);
            String tmp = muscleUI.profile1;
            muscleUI.profile1 = muscleUI.profile2;
            muscleUI.profile2 = tmp;
            
            muscleUI.update();
        }
    }
    
    static class BoxListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            if(PtyListener.busy){
                return;
            }            
            JCheckBox src = (JCheckBox)e.getSource();
            String cmd = src.getActionCommand();
            MuscleUI muscleUI = UIUtil.getParent(src, MuscleUI.class);
            if(cmd.equalsIgnoreCase("anchorOpt")){
                muscleUI.getMuscleParam().setAnchorOpt(src.isSelected());
            }else if(cmd.equalsIgnoreCase("diagnalOpt")){
                muscleUI.getMuscleParam().setDiagnalOpt(src.isSelected());
            }
        }
    }
    
    static class PtyListener implements PropertyChangeListener{

        static boolean busy = false;
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            busy = true;
            MuscleUI muscleUI = (MuscleUI)evt.getSource();
            String pName = evt.getPropertyName();
            if(pName.equals("muscleParam")){
                MuscleParam params = (MuscleParam)evt.getNewValue();
                muscleUI.getMaxTreeSpinner().setValue(params.getMaxTree());
                muscleUI.getMaxItrSpinner().setValue(params.getMaxItrs());
                if(muscleUI.diagnalOptRef != null){
                    muscleUI.diagnalOptRef.get().setSelected(params.isDiagnalOpt());
                }
                if(muscleUI.anchorOptRef != null){
                    muscleUI.anchorOptRef.get().setSelected(params.isAnchorOpt());
                }
            }
            busy = false;
        }
    }
    
    static class SpinnersListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            if(PtyListener.busy){
                return;
            }
            JSpinner spinner = (JSpinner)e.getSource();
            MuscleUI muscleUI = UIUtil.getParent(spinner, MuscleUI.class);
            Integer value = (Integer)spinner.getValue();
            if(spinner == muscleUI.maxItrSpinner){
                muscleUI.getMuscleParam().setMaxItrs(value);
            }else if(spinner == muscleUI.maxTreeSpinner){
                muscleUI.getMuscleParam().setMaxTree(value);
            }
        }
    }
}
