/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.util.UIUtil;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Locale;
import javax.swing.AbstractAction;
import org.openide.util.Exceptions;

public class PasteBasesAction extends AbstractAction {

    ICaretParent caretParent;

    public PasteBasesAction(ICaretParent caretParent) {
        super("Paste bases");
        this.caretParent = caretParent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean allowed = this.caretParent.isEditingAllowed();
        if (!allowed) {
            JCaret.displayInfo();
            return;
        }
        try {
            boolean contains = UIUtil.containsDataFlavor(DataFlavor.stringFlavor);
            if (!contains) {
                return;
            }
            String content = (String) UIUtil.getClipboard().getData(DataFlavor.stringFlavor);
            if (content == null) {
                return;
            }
            boolean valid = caretParent.isInputValid(content);
            if (!valid) {
                return;
            }
            caretParent.insert(caretParent.getCaretPos(), content.toUpperCase(Locale.ENGLISH));
            UIUtil.clearClipboard();
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
