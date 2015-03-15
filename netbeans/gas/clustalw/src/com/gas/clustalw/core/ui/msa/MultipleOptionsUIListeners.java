/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.clustalw.core.service.api.IClustalwService;
import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class MultipleOptionsUIListeners {

    static class IterationListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox) e.getSource();
            MultipleOptionsUI optionsUI = UIUtil.getParent(src, MultipleOptionsUI.class);
            GeneralParam.ITERATION itr = (GeneralParam.ITERATION) src.getSelectedItem();
            optionsUI.getMsaParams().getMultiple().setIterationEnum(itr);
        }
    }

    static class GapOpenListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            MultipleOptionsUI optionsUI = UIUtil.getParent(spinner, MultipleOptionsUI.class);
            Double value = (Double) spinner.getValue();
            optionsUI.getMsaParams().getMultiple().setGapOpenPenalty(value);
        }
    }

    static class GapExtListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            MultipleOptionsUI optionsUI = UIUtil.getParent(spinner, MultipleOptionsUI.class);
            Double value = (Double) spinner.getValue();
            optionsUI.getMsaParams().getMultiple().setGapExtPenalty(value);
        }
    }


    static class TransitionsWeightListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner) e.getSource();
            MultipleOptionsUI optionsUI = UIUtil.getParent(src, MultipleOptionsUI.class);            
            optionsUI.getMsaParams().getMultiple().setTransWeight((Double)src.getValue());
        }
    }

    static class MatrixListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox) e.getSource();
            MultipleOptionsUI optionsUI = UIUtil.getParent(src, MultipleOptionsUI.class);
            GeneralParam.TYPE type = optionsUI.getMsaParams().getGeneralParam().getType();
            if (type == GeneralParam.TYPE.DNA) {
                GeneralParam.DNA_WEIGHT_MATRIX matrix = (GeneralParam.DNA_WEIGHT_MATRIX) src.getSelectedItem();
                optionsUI.getMsaParams().getMultiple().setDnaMatrixEnum(matrix);
            } else if (type == GeneralParam.TYPE.PROTEIN) {
                GeneralParam.PRO_WEIGHT_MATRIX matrix = (GeneralParam.PRO_WEIGHT_MATRIX) src.getSelectedItem();
                optionsUI.getMsaParams().getMultiple().setProteinMatrixEnum(matrix);
            }
            src.getSelectedItem();
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            MultipleOptionsUI optionsUI = (MultipleOptionsUI) evt.getSource();
            String pName = evt.getPropertyName();
            Object nValue = evt.getNewValue();
            if (pName.equals("msaParams")) {
                ClustalwParam msaParams = (ClustalwParam) nValue;
                GeneralParam gParams = msaParams.getGeneralParam();
                ClustalwParam.Multiple multiple = msaParams.getMultiple();
                if (gParams.getType() == GeneralParam.TYPE.DNA) {
                    optionsUI.weightMatrixCombo.setModel(new DefaultComboBoxModel(GeneralParam.DNA_WEIGHT_MATRIX.values()));
                    optionsUI.weightMatrixCombo.setSelectedItem(multiple.getDnaMatrixEnum());
                    optionsUI.createComponentsDynamically(true);
                    
                    optionsUI.transitionsWeightSpinner.setValue(multiple.getTransWeight());
                } else if (gParams.getType() == GeneralParam.TYPE.PROTEIN) {
                    optionsUI.weightMatrixCombo.setModel(new DefaultComboBoxModel(GeneralParam.PRO_WEIGHT_MATRIX.values()));
                    optionsUI.weightMatrixCombo.setSelectedItem(multiple.getProteinMatrixEnum());
                }

                optionsUI.hookupListenersDynamically();
                optionsUI.iterationCombo.setModel(new DefaultComboBoxModel(GeneralParam.ITERATION.values()));
                optionsUI.iterationCombo.setSelectedItem(GeneralParam.ITERATION.get(msaParams.getMultiple().getIterationEnum().toString()));

                optionsUI.gapOpenSpinner.setValue(multiple.getGapOpenPenalty());
                optionsUI.gapExtSpinner.setValue(multiple.getGapExtPenalty());
            }
        }
    }
}
