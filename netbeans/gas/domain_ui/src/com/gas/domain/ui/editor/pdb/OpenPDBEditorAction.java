/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.pdb;

import com.gas.database.core.pdb.api.IPDBService;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.pdb.api.IPDBEditor;
import com.gas.domain.ui.editor.pdb.api.IPDBEditorFactory;
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
public class OpenPDBEditorAction extends AbstractAction {

    private PDBDoc doc;

    public OpenPDBEditorAction(PDBDoc doc) {
        this.doc = doc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean exist = false;
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
        for (int i = 0; i < tcs.length; i++) {

            if (tcs[i] instanceof IPDBEditor) {
                IPDBEditor ed = (IPDBEditor) tcs[i];
                String pdbId = ed.getPdbDoc().getPdbId();
                if (pdbId.equals(doc.getPdbId())) {
                    exist = true;
                    tcs[i].requestActive();
                    break;
                }
            }
        }

        if (!exist) {
            IPDBEditorFactory factory = Lookup.getDefault().lookup(IPDBEditorFactory.class);
            IPDBEditor editor = factory.create();
            TopComponent tc = (TopComponent) editor;
            tc.open();            
            editor.setPdbDoc(doc);
            tc.requestActive();
            if (!doc.isRead()) {
                doc.setRead(true);
                IPDBService service = Lookup.getDefault().lookup(IPDBService.class);
                PDBDoc merged = service.merge(doc);
                BannerTC.getInstance().updataRowByHibernateId(merged);
            }
        }
    }
}
