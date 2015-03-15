/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.renlist;

import com.gas.domain.core.ren.RENList;
import com.gas.domain.ui.editor.renlist.api.IRENListEditor;
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
public class CloseRENListEditorAction extends AbstractAction {

    private RENList renList;

    public CloseRENListEditorAction(RENList renList) {
        this.renList = renList;
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
                if (tc instanceof IRENListEditor) {
                    IRENListEditor editor = (IRENListEditor) tc;
                    String hId = editor.getRENList().getHibernateId();
                    if (hId != null && renList.getHibernateId() != null && renList.getHibernateId().equals(hId)) {
                        tc.close();
                        break;
                    }
                }
            }
        }
    }
}
