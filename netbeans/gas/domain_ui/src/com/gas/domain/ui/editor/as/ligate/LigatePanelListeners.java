/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.ui.editor.as.ligate.LigateTblMdl.MOD;
import com.gas.domain.ui.editor.as.ligate.LigateTblMdl.dNTP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dq
 */
class LigatePanelListeners {

    static class NameListener implements DocumentListener {

        LigatePanel p;

        NameListener(LigatePanel p) {
            this.p = p;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            p.validateInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            p.validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            p.validateInput();
        }
    }

    static class MoveUpListener implements ActionListener {

        WeakReference<LigatePanel> panelRef;

        MoveUpListener(LigatePanel panelRef) {
            this.panelRef = new WeakReference<LigatePanel>(panelRef);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LigatePanel panel = this.panelRef.get();
            panel.getLigateTable().moveUp();
            panel.validateInput();
            panel.previewComp.repaint();
        }
    }

    static class MoveDownListener implements ActionListener {

        private WeakReference<LigatePanel> panelRef;

        MoveDownListener(WeakReference<LigatePanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LigatePanel panel = this.panelRef.get();
            panel.getLigateTable().moveDown();
            panel.validateInput();
            panel.previewComp.repaint();
        }
    }

    static class TblSelectListener implements ListSelectionListener {

        private LigatePanel panel;

        TblSelectListener(LigatePanel panel) {
            this.panel = panel;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            LigateTbl table = this.panel.getLigateTable();
            AnnotatedSeq as = table.getSelectedAs();
            if(as != null){
                panel.previewComp.setSelectedName(as.getName());
            }
            
            LigateTblMdl.MOD mod = table.getSelectedMod();
            List<dNTP> dNTPs = table.getSelected_dNTP();
            if (as != null && mod != null) {
                List<Overhang> overhang3 = as.get3Overhang();
                List<Overhang> overhang5 = as.get5Overhang();

                boolean has5Overhang = overhang5.size() > 0;
                boolean has3Overhang = overhang3.size() > 0;

                LigateTblMdl tableModel = (LigateTblMdl) table.getModel();
                ListSelectionModel model = table.getSelectionModel();
                int leadIndex = model.getLeadSelectionIndex();

                panel.exo3to5Btn.setSelected(mod == MOD.exo3to5);
                panel.exo5to3Btn.setSelected(mod == MOD.exo5to3);
                panel.klenowFillInBtn.setSelected(mod == MOD.klenowFillIn);
                panel.noModBtn.setSelected(mod == MOD.none);
                panel.partialFillInBtn.setSelected(mod == MOD.partialFillIn);
                panel.aBox.setSelected(dNTPs.contains(dNTP.dATP));
                panel.tBox.setSelected(dNTPs.contains(dNTP.dTTP));
                panel.cBox.setSelected(dNTPs.contains(dNTP.dCTP));
                panel.gBox.setSelected(dNTPs.contains(dNTP.dGTP));

                panel.upBtn.setEnabled(leadIndex > 0);
                panel.downBtn.setEnabled(leadIndex < tableModel.getRowCount() - 1);
                panel.flipBtn.setEnabled(leadIndex > -1);
                panel.klenowFillInBtn.setEnabled(leadIndex > -1 && has5Overhang);
                panel.exo3to5Btn.setEnabled(leadIndex > -1 && has3Overhang);
                panel.exo5to3Btn.setEnabled(leadIndex > -1 && has5Overhang);
                panel.partialFillInBtn.setEnabled(leadIndex > -1 && has5Overhang);
                panel.noModBtn.setEnabled(leadIndex > -1);
            }
        }
    }

    static class ModBtnListener implements ActionListener {

        private WeakReference<LigatePanel> panelRef;

        ModBtnListener(LigatePanel panel) {
            this.panelRef = new WeakReference<LigatePanel>(panel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final String cmd = e.getActionCommand();
            LigatePanel panel = this.panelRef.get();
            LigateTbl table = panel.getLigateTable();
            int row = table.getSelectedRow();
            if (cmd.equals(panel.noModBtn.getActionCommand())) {
                if (row > -1) {
                    table.setMod(row, LigateTblMdl.MOD.none);
                }
            } else if (cmd.equals(panel.klenowFillInBtn.getActionCommand())) {
                if (row > -1) {
                    table.setMod(row, LigateTblMdl.MOD.klenowFillIn);
                }
            } else if (cmd.equals(panel.exo5to3Btn.getActionCommand())) {
                if (row > -1) {
                    table.setMod(row, LigateTblMdl.MOD.exo5to3);
                }
            } else if (cmd.equals(panel.exo3to5Btn.getActionCommand())) {
                if (row > -1) {
                    table.setMod(row, LigateTblMdl.MOD.exo3to5);
                }
            }
            panel.validateInput();
            panel.previewComp.repaint();
        }
    }

    static class FlipBtnListener implements ActionListener {

        private LigatePanel panel;

        FlipBtnListener(LigatePanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LigateTbl table = panel.getLigateTable();
            int row = table.getSelectedRow();
            AnnotatedSeq as = table.getSelectedAs();
            AnnotatedSeq flipped = AsHelper.flip(as);
            table.setRow(row, flipped);
            panel.validateInput();
            panel.previewComp.repaint();
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        private WeakReference<LigatePanel> ref;

        PtyChangeListener(LigatePanel panel) {
            this.ref = new WeakReference<LigatePanel>(panel);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object newVal = evt.getNewValue();
            if (pName.equals("circularize")) {
                Boolean circularize = (Boolean) newVal;
                ref.get().circularizeBox.setSelected(circularize);
            } else if (pName.equals("initName")) {
                ref.get().nameFieldRef.get().setText((String) newVal);
                ref.get().nameFieldRef.get().selectAll();
            }
        }
    }

    static class CircularizeBoxListener implements ItemListener {

        private LigatePanel panel;

        CircularizeBoxListener(LigatePanel panel) {
            this.panel = panel;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            panel.getLigateTable().setCircularize(state == ItemEvent.SELECTED);
            panel.validateInput();
            panel.previewComp.repaint();
        }
    }

    static class DNTPListener implements ItemListener {

        private WeakReference<LigatePanel> panelRef;

        DNTPListener(LigatePanel panel) {
            this.panelRef = new WeakReference<LigatePanel>(panel);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            LigatePanel panel = panelRef.get();
            LigateTbl table = panel.getLigateTable();
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            int state = e.getStateChange();

            JCheckBox src = (JCheckBox) e.getSource();
            String cmd = src.getActionCommand();
            if (cmd.equalsIgnoreCase("dATP")) {
                table.update_dNTP(row, LigateTblMdl.dNTP.dATP, state == ItemEvent.SELECTED);
            } else if (cmd.equalsIgnoreCase("dTTP")) {
                table.update_dNTP(row, LigateTblMdl.dNTP.dTTP, state == ItemEvent.SELECTED);
            } else if (cmd.equalsIgnoreCase("dCTP")) {
                table.update_dNTP(row, LigateTblMdl.dNTP.dCTP, state == ItemEvent.SELECTED);
            } else if (cmd.equalsIgnoreCase("dGTP")) {
                table.update_dNTP(row, LigateTblMdl.dNTP.dGTP, state == ItemEvent.SELECTED);
            }
            panel.validateInput();
            panel.previewComp.repaint();
        }
    }

    static class PartialFillInListener implements ItemListener {

        private WeakReference<LigatePanel> panelRef;

        PartialFillInListener(LigatePanel panel) {
            this.panelRef = new WeakReference<LigatePanel>(panel);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            LigatePanel panel = this.panelRef.get();
            LigateTbl table = panel.getLigateTable();
            int row = table.getSelectedRow();
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                if (row > -1) {
                    panel.aBox.setEnabled(true);
                    panel.cBox.setEnabled(true);
                    panel.gBox.setEnabled(true);
                    panel.tBox.setEnabled(true);
                    table.setMod(row, MOD.partialFillIn);
                }
            } else {
                panel.aBox.setEnabled(false);
                panel.cBox.setEnabled(false);
                panel.gBox.setEnabled(false);
                panel.tBox.setEnabled(false);
            }
            panel.previewComp.repaint();
        }
    }
}
