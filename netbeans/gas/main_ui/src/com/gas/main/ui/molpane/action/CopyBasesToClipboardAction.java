/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.main.ui.molpane.MolPane;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class CopyBasesToClipboardAction extends AbstractAction {

    private WeakReference<MolPane> molPaneRef;
    private JComponent comp;

    public CopyBasesToClipboardAction(JComponent comp) {
        super("Copy selected bases");
        this.comp = comp;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (molPaneRef == null || molPaneRef.get() == null) {
            MolPane molPane = UIUtil.getParent(comp, MolPane.class);
            molPaneRef = new WeakReference<MolPane>(molPane);
        }
        if (molPaneRef == null) {
            return;
        }
        Map<Loc, String> seqs = molPaneRef.get().getSelectedSeqs();
        if (seqs.isEmpty()) {
            return;
        }
        final String seq = seqs.values().iterator().next().toUpperCase();
        Clipboard clipboard = UIUtil.getClipboard();
        StringSelection strSelection = new StringSelection(seq);
        clipboard.setContents(strSelection, strSelection);
    }
}
