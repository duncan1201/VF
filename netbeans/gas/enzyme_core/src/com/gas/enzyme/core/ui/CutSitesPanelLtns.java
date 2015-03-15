/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IMolPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
public class CutSitesPanelLtns {

    protected static class MustCutToSpinnerLtn implements ChangeListener {

        private CutSitesPanel cutSitesPanel;

        public MustCutToSpinnerLtn(CutSitesPanel cutSitesPanel) {
            this.cutSitesPanel = cutSitesPanel;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Integer value = (Integer) cutSitesPanel.mustCutToSpinner.getValue();
            SpinnerNumberModel model = (SpinnerNumberModel) cutSitesPanel.mustCutFromSpinner.getModel();
            model.setMaximum(value);
            cutSitesPanel.isAdjusting = true;
            cutSitesPanel.setMustCutTo(value);
            cutSitesPanel.isAdjusting = false;
        }
    }

    protected static class MustCutFromSpinnerLtn implements ChangeListener {

        private CutSitesPanel cutSitesPanelRef;

        public MustCutFromSpinnerLtn(CutSitesPanel cutSitesPanelRef) {
            this.cutSitesPanelRef = cutSitesPanelRef;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Integer value = (Integer) cutSitesPanelRef.mustCutFromSpinner.getValue();
            SpinnerNumberModel model = (SpinnerNumberModel) cutSitesPanelRef.mustCutToSpinner.getModel();
            model.setMinimum(value);
            cutSitesPanelRef.isAdjusting = true;
            cutSitesPanelRef.setMustCutFrom(value);
            cutSitesPanelRef.isAdjusting = false;
        }
    }

    protected static class MatchMaxSpinnerLtn implements ChangeListener {

        private CutSitesPanel cutSitesPanel;

        public MatchMaxSpinnerLtn(CutSitesPanel cutSitesPanel) {
            this.cutSitesPanel = cutSitesPanel;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Integer value = (Integer) cutSitesPanel.getMatchMaxSpinner().getValue();
            SpinnerNumberModel model = (SpinnerNumberModel) cutSitesPanel.getMatchMinSpinner().getModel();
            model.setMaximum(value);
            cutSitesPanel.isAdjusting = true;
            cutSitesPanel.setMatchMax(value);
            cutSitesPanel.isAdjusting = false;
        }
    }

    protected static class MatchMinSpinnerLtn implements ChangeListener {

        private CutSitesPanel cutSitesPanel;

        public MatchMinSpinnerLtn(CutSitesPanel cutSitesPanel) {
            this.cutSitesPanel = cutSitesPanel;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Integer value = (Integer) cutSitesPanel.getMatchMinSpinner().getValue();
            SpinnerNumberModel model = (SpinnerNumberModel) cutSitesPanel.getMatchMaxSpinner().getModel();
            model.setMinimum(value);
            cutSitesPanel.isAdjusting = true;
            cutSitesPanel.setMatchMin(value);
            cutSitesPanel.isAdjusting = false;
        }
    }

    protected static class MustNotCutBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton src = (JRadioButton) e.getSource();
            CutSitesPanel cutSitesPanel = UIUtil.getParent(src, CutSitesPanel.class);
            cutSitesPanel.setMustNotCutEnabled(true);
        }
    }

    protected static class MustCutBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton src = (JRadioButton) e.getSource();
            CutSitesPanel cutSitesPanel = UIUtil.getParent(src, CutSitesPanel.class);
            cutSitesPanel.setMustCutEnabled(true);
        }
    }
    
    static class SelectBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton)e.getSource();
            CutSitesPanel cutSitesPanel = UIUtil.getParent(btn, CutSitesPanel.class);
            IMolPane molPane = UIUtil.getParent(cutSitesPanel, IMolPane.class);
            LocList locList = molPane.getSelections();
            if(locList == null || locList.isEmpty()){
                return;
            }
            Loc selection = locList.get(0);
            String cmd = e.getActionCommand();
            if(cmd.equalsIgnoreCase("mustSelect")){
                cutSitesPanel.mustCutFromSpinner.setValue(selection.getStart());
                cutSitesPanel.mustCutToSpinner.setValue(selection.getEnd());                             
            }else if(cmd.equalsIgnoreCase("mustNotSelect")){
                cutSitesPanel.mustNotCutToSpinner.setValue(selection.getEnd());
                cutSitesPanel.mustNotCutFromSpinner.setValue(selection.getStart());                
            }
        }
    }

    protected static class AnywhereBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton src = (JRadioButton) e.getSource();
            CutSitesPanel cutSitesPanel = UIUtil.getParent(src, CutSitesPanel.class);
            cutSitesPanel.setAnywhereEnabled(true);
        }
    }

    protected static class PtyChangeListener implements PropertyChangeListener {

        private CutSitesPanel cutSitesPanel;

        public PtyChangeListener(CutSitesPanel cutSitesPanel) {
            this.cutSitesPanel = cutSitesPanel;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            CutSitesPanel src = (CutSitesPanel) evt.getSource();
            Object v = evt.getNewValue();
            if (cutSitesPanel.isAdjusting) {
                return;
            }
            if (name.equals("matchMin")) {
                cutSitesPanel.matchMinSpinner.setValue(cutSitesPanel.matchMin);
            } else if (name.equals("matchMax")) {
                cutSitesPanel.matchMaxSpinner.setValue(cutSitesPanel.matchMax);
            } else if (name.equals("mustCutFrom")) {
                cutSitesPanel.mustCutFromSpinner.setValue(cutSitesPanel.mustCutFrom);
            } else if (name.equals("mustCutTo")) {
                cutSitesPanel.mustCutToSpinner.setValue(cutSitesPanel.mustCutTo);
            } else if (name.equals("mustNotCutFrom")) {
                cutSitesPanel.mustNotCutFromSpinner.setValue(cutSitesPanel.mustNotCutFrom);
            } else if (name.equals("mustNotCutTo")) {
                cutSitesPanel.mustNotCutToSpinner.setValue(cutSitesPanel.mustNotCutTo);
            } else if (name.equals("mustNotCutEnabled")) {
                cutSitesPanel.mustNotCutFromSpinner.setEnabled(cutSitesPanel.mustNotCutEnabled);
                cutSitesPanel.mustNotCutToSpinner.setEnabled(cutSitesPanel.mustNotCutEnabled);
                cutSitesPanel.mustNotSelectBtn.setEnabled(cutSitesPanel.mustNotCutEnabled);
            } else if (name.equals("inputParams") || name.equals("totalLength")) {
                cutSitesPanel.populateUI();
                if (src.getTotalLength() != null) {
                    updateSpinnersMax(src);
                }
            }
        }

        private void updateSpinnersMax(CutSitesPanel src) {
            if (src.getTotalLength() == null) {
                return;
            }

            Integer totalLength = src.getTotalLength();

            SpinnerNumberModel model = (SpinnerNumberModel) src.getMustCutToSpinner().getModel();
            model.setMaximum(totalLength);
            model = (SpinnerNumberModel) src.getMustNotCutToSpinner().getModel();
            model.setMaximum(totalLength);
        }
        
    }
}
