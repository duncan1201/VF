/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.general;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.matrix.api.IMatrixService;
import com.gas.common.ui.matrix.api.Matrix;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.database.core.msa.service.api.CounterList;
import com.gas.database.core.msa.service.api.IConservationService;
import com.gas.database.core.msa.service.api.ICounterListService;
import com.gas.msa.ui.MSAEditor;
import com.gas.msa.ui.alignment.AlignPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class PropertiesPanelListeners {

    static class CoverageHeightListener implements ChangeListener {

        AlignPane alignPane;

        @Override
        public void stateChanged(ChangeEvent e) {
            if(PropertiesPanel.populatingUI){
                return;
            }
            JSpinner src = (JSpinner) e.getSource();
            Integer v = (Integer) src.getValue();
            if (alignPane == null) {
                alignPane = UIUtil.getParent(src, AlignPane.class);
            }
            alignPane.getMsa().getMsaSetting().setCoverageHeight(v);
            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().refresh();
        }
    }

    static class QualityHeightListener implements ChangeListener {

        AlignPane alignPane;

        @Override
        public void stateChanged(ChangeEvent e) {
            if(PropertiesPanel.populatingUI){
                return;
            }
            JSpinner src = (JSpinner) e.getSource();
            Integer v = (Integer) src.getValue();
            if (alignPane == null) {
                alignPane = UIUtil.getParent(src, AlignPane.class);
            }
            alignPane.getMsa().getMsaSetting().setQualityHeight(v);
            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().refresh();
        }
    }

    static class MatrixListener implements ActionListener {

        IMatrixService matrixService = Lookup.getDefault().lookup(IMatrixService.class);
        IConservationService consService = Lookup.getDefault().lookup(IConservationService.class);
        ICounterListService counterService = Lookup.getDefault().lookup(ICounterListService.class);

        @Override
        public void actionPerformed(ActionEvent e) {
            if (PropertiesPanel.populatingUI) {
                return;
            }
            JComboBox src = (JComboBox) e.getSource();
            GeneralPanel generalPanel = UIUtil.getParent(src, GeneralPanel.class);

            String name = (String) src.getSelectedItem();
            generalPanel.msa.setSubMatrix(name);

            Matrix matrix = matrixService.getAllMatrices().getMatrix(name, generalPanel.msa.isDnaByGuess());
            CounterList counterList = counterService.createCounterList(generalPanel.msa.getEntriesMapCopy());
            int[] qualities = consService.calculate(counterList, matrix);
            generalPanel.msa.setQualityScores(qualities);

            IMSAEditor editor = UIUtil.getParent(src, IMSAEditor.class);
            if (editor != null) {
                editor.refreshUI();
                editor.setCanSave();
            }
        }
    }

    static class SeqLogoHeightListener implements ChangeListener {

        AlignPane alignPane;

        @Override
        public void stateChanged(ChangeEvent e) {
            if(PropertiesPanel.populatingUI){
                return;
            }
            JSpinner src = (JSpinner) e.getSource();
            Integer v = (Integer) src.getValue();
            if (alignPane == null) {
                alignPane = UIUtil.getParent(src, AlignPane.class);
            }
            alignPane.getMsa().getMsaSetting().setSeqLogoHeight(v);
            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().refresh();
        }
    }

    static class SampleCorrectListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();
            AlignPane alignPane = UIUtil.getParent(src, AlignPane.class);
            if (alignPane == null) {
                return;
            }
            alignPane.getMsa().getSeqLogoParam().setSmallSampleCorrection(src.isSelected());
            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().createUIPlotObjects();

            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().repaint();

            if (!PropertiesPanel.populatingUI) {
                MSAEditor editor = UIUtil.getParent(src, MSAEditor.class);
                if (editor != null) {
                    editor.setCanSave();
                }
            }
        }
    }

    static class SeqLogoListener implements ItemListener {

        AlignPane alignPane;
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(PropertiesPanel.populatingUI){
                return;
            }            
            JComponent src = (JComponent)e.getItem();
            if(alignPane == null){
                alignPane = UIUtil.getParent(src, AlignPane.class);
            }
            alignPane.getMsa().getMsaSetting().setSeqLogoDisplay(e.getStateChange() == ItemEvent.SELECTED);
            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().refresh();
            alignPane.getMsaScroll().getColumnHeaderUI().revalidate();
        }
    }

    static class CoveragePlotListener implements ItemListener {

        AlignPane alignPane;
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(PropertiesPanel.populatingUI){
                return;
            }
            JComponent src = (JComponent)e.getItem();
            if(alignPane == null){
                alignPane = UIUtil.getParent(src, AlignPane.class);
            }
            alignPane.getMsa().getMsaSetting().setCoverageDisplay(e.getStateChange() == ItemEvent.SELECTED);
            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().refresh();
            alignPane.getMsaScroll().getColumnHeaderUI().revalidate();
        }
    }

    static class QualityPlotListener implements ItemListener {

        AlignPane alignPane;
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(PropertiesPanel.populatingUI){
                return;
            }            
            JComponent src = (JComponent)e.getItem();
            if(alignPane == null){
                alignPane = UIUtil.getParent(src, AlignPane.class);
            }
            alignPane.getMsa().getMsaSetting().setQualityDisplay(e.getStateChange() == ItemEvent.SELECTED);
            alignPane.getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().refresh();
            alignPane.getMsaScroll().getColumnHeaderUI().revalidate();
        }
    }

    static class ColorComboListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox) e.getSource();
            GeneralPanel generalPanel = UIUtil.getParent(combo, GeneralPanel.class);
            IColorProvider p = (IColorProvider) combo.getSelectedItem();
            if (generalPanel.msa.isDNA()) {
                MSAPref.getInstance().setColorProviderName(MSAPref.KEY.DNA, p.getName());
            } else {
                MSAPref.getInstance().setColorProviderName(MSAPref.KEY.PROTEIN, p.getName());
            }

        }
    }
}
