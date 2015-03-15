/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.main.ui.molpane.MolPane;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import java.util.Map;
import javax.swing.AbstractAction;

/**
 *
 * @author dq
 */
public class CopyBasesReverseToClipboardAction extends AbstractAction {

    private WeakReference<MolPane> molPaneRef;

    public CopyBasesReverseToClipboardAction(WeakReference<MolPane> molPaneRef) {
        super("Copy selected bases(reverse complement)");
        this.molPaneRef = molPaneRef;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<Loc, String> seqs = molPaneRef.get().getSelectedSeqs();
        if (seqs.isEmpty()) {
            return;
        }
        String seq = seqs.values().iterator().next().toUpperCase();
        seq = BioUtil.reverseComplement(seq);
        Clipboard clipboard = UIUtil.getClipboard();
        StringSelection strSelection = new StringSelection(seq);
        clipboard.setContents(strSelection, strSelection);
    }
}
