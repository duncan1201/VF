/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.pubmed;

import com.gas.domain.core.pubmed.PubmedArticle;
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
public class ClosePubmedEditorAction extends AbstractAction {

    private PubmedArticle pa;

    public ClosePubmedEditorAction(PubmedArticle pa) {
        this.pa = pa;
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
                if (tc instanceof IPubmedEditor) {
                    IPubmedEditor editor = (IPubmedEditor) tc;
                    String hId = editor.getPubmedArticle().getHibernateId();
                    if (hId != null && pa.getHibernateId() != null && pa.getHibernateId().equals(hId)) {
                        tc.close();
                        break;
                    }
                }
            }
        }
    }
}
