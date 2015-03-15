/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.renlist;

import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.renlist.api.IRENListEditor;
import com.gas.domain.ui.editor.renlist.api.IRENListEditorFactory;
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
public class OpenRENListEditorAction extends AbstractAction {

    private RENList renList;

    public OpenRENListEditorAction(RENList renList) {
        this.renList = renList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        boolean exist = false;
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
        for (int i = 0; i < tcs.length; i++) {

            if (tcs[i] instanceof IRENListEditor) {
                IRENListEditor editor = (IRENListEditor) tcs[i];
                String hId = editor.getRENList().getHibernateId();
                if (hId.equals(renList.getHibernateId())) {
                    exist = true;
                    tcs[i].requestActive();
                    break;
                }
            }
        }



        if (!exist) {
            IRENListEditorFactory factory = Lookup.getDefault().lookup(IRENListEditorFactory.class);
            IRENListEditor editor = factory.create();
            ((TopComponent) editor).open();            
            editor.setRENList(renList);
            ((TopComponent) editor).requestActive();
            if (!renList.isRead()) {
                renList.setRead(true);
                IRENListService service = Lookup.getDefault().lookup(IRENListService.class);
                RENList merged = service.merge(renList);
                BannerTC.getInstance().updataRowByHibernateId(merged);
            }
        }
    }
}
