/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.actions;

import com.gas.common.ui.misc.CNST;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.gateway.core.service.api.GW_STATE;
import com.gas.gateway.core.service.api.IGWValidateService;
import com.gas.gateway.core.service.api.IOneClickService;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
        id = "com.gas.gateway.ui.actions.OneClickAction")
@ActionRegistration(displayName = "#CTL_OneClickAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/Gateway", position = 2465)
})
@NbBundle.Messages("CTL_OneClickAction=One-Click Gateway")
public class OneClickAction extends AbstractAction {

    private IOneClickService oneClickService = Lookup.getDefault().lookup(IOneClickService.class);
    private IGWValidateService validateService = Lookup.getDefault().lookup(IGWValidateService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

    static String TITLE = "One-Click Gateway";
    
    public OneClickAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> objs = BannerTC.getInstance().getCheckedObjects(AnnotatedSeq.class);
        List<AnnotatedSeq> seqs = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq as : objs) {
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            seqs.add(full);
        }
        GW_STATE state = validateService.validateOneClick(seqs);
        if (state != GW_STATE.VALID) {
            DialogDescriptor.Message msg = new DialogDescriptor.Message(getErrorMsg(state), DialogDescriptor.INFORMATION_MESSAGE);
            msg.setTitle("Cannot Perform One-Click Gateway");
            DialogDisplayer.getDefault().notify(msg);
            return;
        }
        AnnotatedSeq exp = oneClickService.createExpressionClone(seqs);
        Folder folder = ExplorerTC.getInstance().getSelectedFolder();
        exp.setFolder(folder);
        asService.persist(exp);
        Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
        BannerTC.getInstance().updateFolder(updatedFolder);
        ExplorerTC.getInstance().updateFolder(updatedFolder);
        
        String msg = String.format("<html>Expression clone \"%s\" created<br/><br/>Do you want to open it?</html>", exp.getName());
        DialogDescriptor.Confirmation m = new DialogDescriptor.Confirmation(msg, TITLE, DialogDescriptor.YES_NO_OPTION);        
        Object answer = DialogDisplayer.getDefault().notify(m);
        if(answer.equals(DialogDescriptor.OK_OPTION)){
            final Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
            exp = asService.getFullByHibernateId(exp.getHibernateId(), rootFolder);
            OpenASEditorAction action = new OpenASEditorAction(exp);
            action.actionPerformed(null);
        }
    }

    private String getErrorMsg(GW_STATE state) {
        String ret = null;
        if (state == GW_STATE.DONOR_INSERT_NOT_MATCHING) {
            ret = String.format(CNST.ERROR_FORMAT, "The donor and the insert are incompatible", getInstruction());
        }else{ 
            ret = state.getErrorMsg();
        }
        return ret;
    }
    
    private String getInstruction() {
        return "Please select an equal number of compatible donors and inserts";
    }
}
