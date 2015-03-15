/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.kromatogram;

import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.ui.editor.kromatogram.api.IChromatogramEditor;
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
public class CloseKromatogramEditorAction extends AbstractAction {

    private Kromatogram kromatogram;

    public CloseKromatogramEditorAction(Kromatogram kromatogram) {
        this.kromatogram = kromatogram;
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
                if (tc instanceof IChromatogramEditor) {
                    IChromatogramEditor editor = (IChromatogramEditor) tc;
                    Kromatogram domainObjInEditor = editor.getKromatogram();
                    if (kromatogram.getHibernateId() != null && domainObjInEditor.getHibernateId() != null) {
                        if (kromatogram.getHibernateId().equals(domainObjInEditor.getHibernateId())) {
                            tc.close();
                            break;
                        }
                    } else if (domainObjInEditor == kromatogram) {
                        tc.close();
                        break;
                    }
                }
            }
        }
    }
}
