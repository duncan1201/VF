/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
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
public class OpenASEditorAction extends AbstractAction {

    private AnnotatedSeq as;
    private IASEditor asEditor;

    /**
     * @param as it should be fully loaded already
     */
    public OpenASEditorAction(AnnotatedSeq as) {
        this.as = as;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
        for (int i = 0; i < tcs.length; i++) {

            if (tcs[i] instanceof IASEditor) {
                IASEditor editor = (IASEditor) tcs[i];
                AnnotatedSeq _as = editor.getAnnotatedSeq();
                if (_as != null) {
                    if (isSame(_as, as)) {
                        tcs[i].requestActive();
                        asEditor = (IASEditor) tcs[i];
                        return;
                    }
                }
            }
        }

        IASEditorFactory factory = Lookup.getDefault().lookup(IASEditorFactory.class);
        asEditor = factory.create();
        ((TopComponent) asEditor).open();
        
        UIUtil.setTopCompName((TopComponent) asEditor, as.getName());

        if (AsHelper.isNucleotide(as)) {
            if (as.isOligo()) {
                UIUtil.setTopCompIcon((TopComponent) asEditor, ImageHelper.createImage(ImageNames.PRIMER_SINGLE_16));
            } else if (as.getReadOnlyOverhangs().isEmpty()){
                UIUtil.setTopCompIcon((TopComponent) asEditor, ImageHelper.createImage(ImageNames.NUCLEOTIDE_16));
            } else{
                UIUtil.setTopCompIcon((TopComponent) asEditor, ImageHelper.createImage(ImageNames.FRAGMENT_16));
            }
        } else {
            UIUtil.setTopCompIcon((TopComponent) asEditor, ImageHelper.createImage(ImageNames.PROTEIN_16));
        }

        asEditor.setAnnotatedSeq(as);
        ((TopComponent) asEditor).requestActive();
        ((TopComponent) asEditor).requestFocus();
        if (!as.isRead()) {
            as.setRead(true);
            if (as.getHibernateId() != null) {
                IAnnotatedSeqService service = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
                AnnotatedSeq merged = service.merge(as);
                BannerTC.getInstance().updataRowByHibernateId(merged);
            } else {
                Folder folder = ExplorerTC.getInstance().getSelectedFolder();
                BannerTC.getInstance().fireTableDataChanged(folder);
            }
        }
    }

    private boolean isSame(AnnotatedSeq as, AnnotatedSeq as2) {
        boolean ret = false;
        if (as == as2) {
            ret = true;
        } else if (as.getHibernateId() != null && as2.getHibernateId() != null && as.getHibernateId().equals(as2.getHibernateId())) {
            ret = true;
        }
        return ret;
    }
    
    public IASEditor getEditor(){
        return asEditor;
    }
}
