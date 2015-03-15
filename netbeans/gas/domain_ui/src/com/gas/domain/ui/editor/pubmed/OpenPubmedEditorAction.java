/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.pubmed;

import com.gas.database.core.pubmed.api.IPubmedDBService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.ui.banner.BannerTC;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.Lookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author dunqiang
 */
public class OpenPubmedEditorAction extends AbstractAction {

    private PubmedArticle article;
    private IPubmedDBService service = Lookup.getDefault().lookup(IPubmedDBService.class);

    public OpenPubmedEditorAction(PubmedArticle pmid) {
        this.article = pmid;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        boolean exist = false;
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
        for (int i = 0; i < tcs.length; i++) {

            if (tcs[i] instanceof IPubmedEditor) {
                IPubmedEditor editor = (IPubmedEditor) tcs[i];
                if (isSame(editor.getPubmedArticle(), this.article)) {
                    exist = true;
                    tcs[i].requestActive();
                    break;
                }
            }
        }

        if (!exist) {
            IPubmedEditorFactory factory = Lookup.getDefault().lookup(IPubmedEditorFactory.class);
            IPubmedEditor editor = factory.create();
            ((TopComponent) editor).open();            
            editor.setName(article.getPmid());
            editor.setPubmedArticle(article);
            ((TopComponent) editor).requestActive();
            if (!article.isRead()) {
                article.setRead(true);
                PubmedArticle merged = service.merge(article);
                BannerTC.getInstance().updataRowByHibernateId(merged);
            }
        }
    }
    
    private boolean isSame(IFolderElement one, IFolderElement another){
        return one == another || (one.getHibernateId() != null && another.getHibernateId() != null && one.getHibernateId().equals(another.getHibernateId()));
    }
}
