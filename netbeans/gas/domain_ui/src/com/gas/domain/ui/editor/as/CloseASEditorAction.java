/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as;

import com.gas.domain.core.as.AnnotatedSeq;
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
public class CloseASEditorAction extends AbstractAction {

    private AnnotatedSeq as;

    public CloseASEditorAction(AnnotatedSeq as) {

        this.as = as;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        boolean eventThread = SwingUtilities.isEventDispatchThread();
        if (!eventThread) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    actionPerformed(e);
                }
            });
        } else {
            Mode editorMode = WindowManager.getDefault().findMode("editor");
            TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
            for (int i = 0; i < tcs.length; i++) {
                if (tcs[i] instanceof IASEditor) {
                    IASEditor asEditor = (IASEditor) tcs[i];
                    if (isSame(asEditor.getAnnotatedSeq(), as)) {
                        tcs[i].close();
                        break;
                    }
                }
            }
        }
    }
    
    private boolean isSame(AnnotatedSeq one, AnnotatedSeq another){        
        return one == another || (one.getHibernateId() != null && another.getHibernateId() != null && one.getHibernateId().equals(another.getHibernateId()));
    }
}