/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.common.ui.util.UIUtil;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.ref.WeakReference;
import javax.swing.JCheckBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class ProteinGapsPanelListeners {

    static class DocListener implements DocumentListener {

        WeakReference<ProteinGapsPanel> proteinGapsPanelRef;

        DocListener(ProteinGapsPanel proteinGapsPanel) {
            proteinGapsPanelRef = new WeakReference<ProteinGapsPanel>(proteinGapsPanel);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update(e);
        }

        private void update(DocumentEvent e) {
            try {
                Document doc = e.getDocument();
                String text = e.getDocument().getText(0, doc.getLength());
                ClustalWUI clustalwUI = UIUtil.getParent(proteinGapsPanelRef.get(), ClustalWUI.class);
                if(clustalwUI == null){
                    return;
                }
                clustalwUI.getClustalwParam().getMultiple().setHydrophilicResidue(text);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    static class CheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();
            ClustalWUI clustalwUI = UIUtil.getParent(src, ClustalWUI.class);
            if (clustalwUI == null) {
                return;
            }
            final String cmd = src.getActionCommand();
            if (cmd.equalsIgnoreCase("ignoreEndGaps")) {
                clustalwUI.getClustalwParam().getMultiple().setIgnoreEndGaps(src.isSelected());
            } else if (cmd.equalsIgnoreCase("hydrophilic")) {
                clustalwUI.getClustalwParam().getMultiple().setHydrophilicPenalty(src.isSelected());
            } else if (cmd.equalsIgnoreCase("residueSpecific")) {
                clustalwUI.getClustalwParam().getMultiple().setResidueSpecificPenalty(src.isSelected());
            }
        }
    }
}
