/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.tigr;

import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.domain.ui.editor.tigr.api.ITigrPtEditor;
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
public class CloseTigrPtEditorAction extends AbstractAction {

    private TigrProject tigrPt;
    
    public CloseTigrPtEditorAction(TigrProject tigrPt){
        this.tigrPt = tigrPt;
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
                if (tc instanceof ITigrPtEditor) {
                    ITigrPtEditor editor = (ITigrPtEditor) tc;
                    TigrProject tigrPtInEditor = editor.getTigrPt();
                    if (tigrPt.getHibernateId() != null && tigrPtInEditor.getHibernateId() != null) {
                        if (tigrPt.getHibernateId().equals(tigrPtInEditor.getHibernateId())) {
                            tc.close();
                            break;
                        }
                    } else if (tigrPtInEditor == tigrPt) {
                        tc.close();
                        break;
                    }
                }
            }
        }
    }
}
