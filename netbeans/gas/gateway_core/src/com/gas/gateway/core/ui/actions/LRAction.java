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
import com.gas.gateway.core.service.api.ILRService;
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
        id = "com.gas.gateway.ui.actions.LRAction")
@ActionRegistration(displayName = "#CTL_LRAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/Gateway", position = 2460)
})
@NbBundle.Messages("CTL_LRAction=LR Reaction...")
public class LRAction extends AbstractAction {

    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private ILRService lrService = Lookup.getDefault().lookup(ILRService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IGWValidateService validateService = Lookup.getDefault().lookup(IGWValidateService.class);

    public LRAction() {
        super("LR Reaction");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> list = BannerTC.getInstance().getCheckedNucleotides();
        List<AnnotatedSeq> fulls = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq as : list) {
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            fulls.add(full);
        }
        GW_STATE state = validateService.validateLR(fulls);
        if (state != GW_STATE.VALID) {
            String errorMsg = getErrorMsg(state);
            String instruction = getInstruction(state);
            String msg = String.format(CNST.ERROR_FORMAT, errorMsg, instruction);
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle("Cannot perform LR reaction");
            DialogDisplayer.getDefault().notify(m);
            return;
        }
        AnnotatedSeq expClone = lrService.createExpressionClone(fulls);
        String oldName = expClone.getName();
        Folder folder = ExplorerTC.getInstance().getSelectedFolder();

        String newName = folder.getNewElementName(oldName);
        if (newName.equals(oldName)) {
            expClone.setName(newName);
        }
        expClone.setFolder(folder);
        asService.persist(expClone);
        Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
        BannerTC.getInstance().updateFolder(updatedFolder);
        ExplorerTC.getInstance().updateFolder(updatedFolder);

        final String title = "LR Reaction";
        final String msg = String.format("<html>Expression clone \"%s\" created<br/><br/>Do you want to open it?</html>", expClone.getName());
        DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
        Object answer = DialogDisplayer.getDefault().notify(c);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
            expClone = asService.getFullByHibernateId(expClone.getHibernateId(), rootFolder);
            OpenASEditorAction action = new OpenASEditorAction(expClone);
            action.actionPerformed(null);
        }
    }

    private String getErrorMsg(GW_STATE state) {
        String ret ;
        if (state == GW_STATE.DEST_NOT_FOUND) {
            ret = state.getErrorMsg();
        } else if (state == GW_STATE.DEST_TOO_MANY) {
            ret = "More than one destination vector found";
        } else if (state == GW_STATE.ENTRY_TOO_MANY) {
            ret = "Too many entry clones found";
        } else if(state == GW_STATE.DEST_VECTOR_INVALID){
            ret = String.format("The destination vector \"%s\" is invalid", state.getData());
        }else if (state == GW_STATE.ENTRY_WRONG_SITE) {
            ret = String.format("The entry clone \"%s\" is incompatible with the destination vector", state.getData());
        } else {
            ret = state.getErrorMsg();
        }
        return ret;
    }
    
    private String getInstruction(GW_STATE state){
        String ret = null;
        if(state == GW_STATE.DEST_VECTOR_INVALID){
            ret = "Please select a destination vector which contains appropriate attR sites";
        }else{
            ret = "Please select one destination vector and one or more entry clones";
        }
        return ret;
    }
}
