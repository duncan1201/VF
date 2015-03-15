/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.kromatogram;

import com.gas.domain.ui.editor.kromatogram.api.IChromatogramEditorFactory;
import com.gas.database.core.tigr.service.api.IKromatogramService;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.kromatogram.api.IChromatogramEditor;
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
public class OpenKromatogramEditorAction extends AbstractAction {

    Kromatogram kromatogram;
    
    public OpenKromatogramEditorAction(Kromatogram kromatogram){
        this.kromatogram = kromatogram;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean exist = false;
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
        for (int i = 0; i < tcs.length; i++) {
            if (tcs[i] instanceof IChromatogramEditor) {
                IChromatogramEditor ed = (IChromatogramEditor) tcs[i];
                String hId = ed.getKromatogram().getHibernateId();
                if (hId != null) {
                    if (hId.equals(this.kromatogram.getHibernateId())) {
                        exist = true;
                        tcs[i].requestActive();
                        break;
                    }
                }
            }
        }
        
        if (!exist) {
            IChromatogramEditorFactory factory = Lookup.getDefault().lookup(IChromatogramEditorFactory.class);
            IChromatogramEditor editor = factory.create();
            TopComponent tc = (TopComponent) editor;
            tc.open();           
            tc.requestActive();
            IKromatogramService service = Lookup.getDefault().lookup(IKromatogramService.class);
            if (!kromatogram.isRead()) {
                kromatogram.setRead(true);                
                Kromatogram merged = service.merge(kromatogram);
                BannerTC.getInstance().updataRowByHibernateId(merged);
            }
            Kromatogram kromatogramFull = service.getFullByHibernateId(kromatogram.getHibernateId());
            editor.setKromatogram(kromatogramFull);
        }
    }
}
