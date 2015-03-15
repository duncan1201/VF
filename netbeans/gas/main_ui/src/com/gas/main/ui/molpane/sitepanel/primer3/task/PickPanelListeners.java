/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.task;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.main.ui.molpane.MolPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.ref.WeakReference;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class PickPanelListeners {

    static class CopyLocListener implements ActionListener {

        WeakReference<PickPanel> pickPanelRef;

        CopyLocListener(PickPanel pickPanel) {
            pickPanelRef = new WeakReference<PickPanel>(pickPanel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            MolPane molPane = UIUtil.getParent(btn, MolPane.class);
            LocList locList = molPane.getSelections();
            if (!locList.isEmpty()) {
                Loc loc = locList.get(0);
                pickPanelRef.get().fromSpinnerRef.get().setValue(loc.getStart());
                pickPanelRef.get().toSpinnerRef.get().setValue(loc.getEnd());
            }
        }
    }

    static class BoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();

            PickPanel p = UIUtil.getParent(src, PickPanel.class);
            if (src.getActionCommand().equals("useExisting")) {
                p.fromSpinnerRef.get().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                p.toSpinnerRef.get().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                p.copyLocBtnRef.get().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            } else if (src.getActionCommand().equals("pick")) {
                p.useExistingBox.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    p.fromSpinnerRef.get().setEnabled(false);
                    p.toSpinnerRef.get().setEnabled(false);
                    p.copyLocBtnRef.get().setEnabled(false);
                } else {
                    if (p.useExistingBox.isSelected()) {
                        p.fromSpinnerRef.get().setEnabled(true);
                        p.toSpinnerRef.get().setEnabled(true);
                        p.copyLocBtnRef.get().setEnabled(true);
                    }
                }
            }
        }
    }
}
