/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.pdb;

import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.ui.editor.pdb.api.IPDBEditor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class ClosePDBEditorAction extends AbstractAction {

    private PDBDoc pdbDoc;

    public ClosePDBEditorAction(PDBDoc pdbDoc) {
        this.pdbDoc = pdbDoc;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        boolean isEventThread = SwingUtilities.isEventDispatchThread();
        if (!isEventThread) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    actionPerformed(e);
                }
            });
        } else {
            Mode editorMode = WindowManager.getDefault().findMode("editor");
            TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
            for (TopComponent tc : tcs) {
                if (tc instanceof IPDBEditor) {
                    IPDBEditor editor = (IPDBEditor) tc;
                    String hId = editor.getPdbDoc().getHibernateId();
                    if (hId != null && pdbDoc.getHibernateId() != null && pdbDoc.getHibernateId().equals(hId)) {
                        tc.close();
                        break;
                    }
                }
            }
        }
    }
}
