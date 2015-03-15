/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.tigr;

import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.tigr.service.api.ITigrPtService;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.tigr.api.ITigrEditorFactory;
import com.gas.domain.ui.editor.tigr.api.ITigrPtEditor;
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
public class OpenTigrPtEditorAction extends AbstractAction {

    private TigrProject tigrPt;

    public OpenTigrPtEditorAction(TigrProject tigrPt) {
        this.tigrPt = tigrPt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean exist = false;
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
        for (int i = 0; i < tcs.length; i++) {

            if (tcs[i] instanceof ITigrPtEditor) {
                ITigrPtEditor editor = (ITigrPtEditor) tcs[i];
                String hId = editor.getTigrPt().getHibernateId();
                if (hId.equals(tigrPt.getHibernateId())) {
                    exist = true;
                    tcs[i].requestActive();
                    break;
                }
            }
        }



        if (!exist) {
            ITigrEditorFactory factory = Lookup.getDefault().lookup(ITigrEditorFactory.class);
            ITigrPtEditor editor = factory.create();
            UIUtil.setTopCompName((TopComponent)editor, tigrPt.getName());
            ((TopComponent) editor).open();
            ((TopComponent) editor).requestActive();
            //editor.setName(article.getPmid());
            editor.setTigrPt(tigrPt);
            if (!tigrPt.isRead()) {
                tigrPt.setRead(true);
                ITigrPtService service = Lookup.getDefault().lookup(ITigrPtService.class);
                TigrProject merged = service.merge(tigrPt);
                BannerTC.getInstance().updataRowByHibernateId(merged);
            }
        }
    }
}
