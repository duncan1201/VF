/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.clustalw.core.service.api.IClustalwService;
import com.gas.common.ui.util.UIUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class PairwiseOptionsUIListeners {

    static class TypeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (PtyListener.busy) {
                return;
            }
            JRadioButton btn = (JRadioButton) e.getSource();
            PairwiseOptionsUI optionsUI = UIUtil.getParent(btn, PairwiseOptionsUI.class);
            String cmd = btn.getActionCommand();
            if (cmd.equalsIgnoreCase("fast")) {
                optionsUI.setQuickTree(true);
                optionsUI.getMsaParams().getGeneralParam().setQuickTree(true);
            }
            if (cmd.equalsIgnoreCase("slow")) {
                optionsUI.setQuickTree(false);
                optionsUI.getMsaParams().getGeneralParam().setQuickTree(false);
            }
        }
    }

    static class GapExtListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner) e.getSource();
            Double value = (Double) src.getValue();
            PairwiseOptionsUI optionsUI = UIUtil.getParent(src, PairwiseOptionsUI.class);
            optionsUI.getMsaParams().getSlow().setGapExt(value);
        }
    }

    static class WindowLengthListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            PairwiseOptionsUI optionUI = UIUtil.getParent(spinner, PairwiseOptionsUI.class);
            Integer value = (Integer) spinner.getValue();
            optionUI.getMsaParams().getFast().setWindowSize(value);
        }
    }

    static class WordSizeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            PairwiseOptionsUI optionUI = UIUtil.getParent(spinner, PairwiseOptionsUI.class);
            Integer value = (Integer) spinner.getValue();
            optionUI.getMsaParams().getFast().setKtuple(value);
        }
    }

    static class PairGapListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            PairwiseOptionsUI optionsUI = UIUtil.getParent(spinner, PairwiseOptionsUI.class);
            Integer value = (Integer) spinner.getValue();
            optionsUI.getMsaParams().getFast().setPairGap(value);
        }
    }

    static class TopDiagsListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            PairwiseOptionsUI optionsUI = UIUtil.getParent(spinner, PairwiseOptionsUI.class);
            Integer value = (Integer) spinner.getValue();
            optionsUI.getMsaParams().getFast().setTopDiags(value);
        }
    }

    static class MatrixListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox) e.getSource();
            PairwiseOptionsUI optionsUI = UIUtil.getParent(src, PairwiseOptionsUI.class);
            GeneralParam.TYPE type = optionsUI.getMsaParams().getGeneralParam().getType();
            if (type == GeneralParam.TYPE.DNA) {
                GeneralParam.DNA_WEIGHT_MATRIX matrix = (GeneralParam.DNA_WEIGHT_MATRIX) src.getSelectedItem();
                optionsUI.getMsaParams().getSlow().setDnaMatrixEnum(matrix);
            } else if (type == GeneralParam.TYPE.PROTEIN) {
                GeneralParam.PRO_WEIGHT_MATRIX matrix = (GeneralParam.PRO_WEIGHT_MATRIX) src.getSelectedItem();
                optionsUI.getMsaParams().getSlow().setProteinMatrix(matrix);
            }
        }
    }

    static class GapOpenListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner) e.getSource();
            Double value = (Double) src.getValue();
            PairwiseOptionsUI optionsUI = UIUtil.getParent(src, PairwiseOptionsUI.class);
            optionsUI.getMsaParams().getSlow().setGapOpen(value);
        }
    }

    static class PtyListener implements PropertyChangeListener {

        static boolean busy = false;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            busy = true;
            PairwiseOptionsUI optionsUI = (PairwiseOptionsUI) evt.getSource();
            String pName = evt.getPropertyName();
            Object nValue = evt.getNewValue();
            if (pName.equals("msaParams")) {
                ClustalwParam param = (ClustalwParam) nValue;
                GeneralParam gParam = param.getGeneralParam();
                if (gParam.isQuickTree()) {
                    optionsUI.setQuickTree(true);
                } else {
                    optionsUI.setQuickTree(false);
                }

                ClustalwParam.Fast fast = param.getFast();
                optionsUI.wordSizeSpinner.setValue(fast.getKtuple());
                optionsUI.windowLengthSpinner.setValue(fast.getWindowSize());
                optionsUI.topDiagsSpinner.setValue(fast.getTopDiags());
                optionsUI.pairGapSpinner.setValue(fast.getPairGap());

                ClustalwParam.Slow slow = param.getSlow();
                optionsUI.gapExtSpinner.setValue(slow.getGapExt());
                optionsUI.gapOpenSpinner.setValue(slow.getGapOpen());
                if (gParam.getType() == GeneralParam.TYPE.DNA) {
                    optionsUI.weightMatrixCombo.setModel(new DefaultComboBoxModel(GeneralParam.DNA_WEIGHT_MATRIX.values()));
                    optionsUI.weightMatrixCombo.setSelectedItem(param.getSlow().getDnaMatrixEnum());
                } else if (gParam.getType() == GeneralParam.TYPE.PROTEIN) {
                    optionsUI.weightMatrixCombo.setModel(new DefaultComboBoxModel(GeneralParam.PRO_WEIGHT_MATRIX.values()));
                    optionsUI.weightMatrixCombo.setSelectedItem(param.getSlow().getProteinMatrix());
                }
            }

            busy = false;
        }
    }
}
