/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.msa;

import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.domain.ui.editor.msa.api.IMSAEditorFactory;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.Lookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class OpenMSAAction extends AbstractAction {

    private MSA msa;

    public OpenMSAAction(MSA msa) {
        this.msa = msa;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean exist = false;
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
        for (int i = 0; i < tcs.length; i++) {

            if (tcs[i] instanceof IMSAEditor) {
                IMSAEditor ed = (IMSAEditor) tcs[i];
                String hId = ed.getMsa().getHibernateId();
                if (hId != null) {
                    if (hId.equals(msa.getHibernateId())) {
                        exist = true;
                        tcs[i].requestActive();
                        break;
                    }
                }
            }
        }

        if (!exist) {
            IMSAEditorFactory factory = Lookup.getDefault().lookup(IMSAEditorFactory.class);
            IMSAEditor editor = factory.create();
            TopComponent tc = (TopComponent) editor;
            tc.open();           
            tc.requestActive();
            if (!msa.isRead()) {
                msa.setRead(true);
                IMSAService service = Lookup.getDefault().lookup(IMSAService.class);
                MSA merged = service.merge(msa);
                BannerTC.getInstance().updataRowByHibernateId(merged);
            }
            editor.setMsa(msa);
        }
    }
}
