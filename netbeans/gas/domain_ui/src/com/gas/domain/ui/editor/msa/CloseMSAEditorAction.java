/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.msa;

import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
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
public class CloseMSAEditorAction extends AbstractAction {

    private MSA msa;

    public CloseMSAEditorAction(MSA msa) {
        this.msa = msa;
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
                if (tc instanceof IMSAEditor) {
                    IMSAEditor editor = (IMSAEditor) tc;
                    MSA msaInEditor = editor.getMsa();
                    if (msa.getHibernateId() != null && msaInEditor.getHibernateId() != null) {
                        if (msa.getHibernateId().equals(msaInEditor.getHibernateId())) {
                            tc.close();
                            break;
                        }
                    } else if (msaInEditor == msa) {
                        tc.close();
                        break;
                    }
                }
            }
        }
    }
}
