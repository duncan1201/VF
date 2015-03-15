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
import com.gas.gateway.core.service.api.IAttSiteService;
import com.gas.gateway.core.service.api.IBPService;
import com.gas.gateway.core.service.api.IGWValidateService;
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
        id = "com.gas.gateway.ui.actions.BPAction")
@ActionRegistration(displayName = "#CTL_BPAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/Gateway", position = 2455)
})
@NbBundle.Messages("CTL_BPAction=BP Reaction...")
public class BPAction extends AbstractAction {

    private IBPService bpService = Lookup.getDefault().lookup(IBPService.class);
    private IGWValidateService validateService = Lookup.getDefault().lookup(IGWValidateService.class);
    private IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

    public BPAction() {
        super("BP Reaction");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> list = BannerTC.getInstance().getCheckedNucleotides();
        List<AnnotatedSeq> fulls = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq as : list) {
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            fulls.add(full);
        }
        GW_STATE state = validateService.validateBP(fulls);
        if (state != GW_STATE.VALID) {
            DialogDescriptor.Message m = new DialogDescriptor.Message(getErrorMsg(state), DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle("Cannot perform BP reaction");
            DialogDisplayer.getDefault().notify(m);
            return;
        }
        List<AnnotatedSeq> entryClones = bpService.createEntryClones(fulls, true);
        Folder folder = ExplorerTC.getInstance().getSelectedFolder();

        for (AnnotatedSeq entryClone : entryClones) {
            final String oldName = entryClone.getName();
            final String newName = folder.getNewElementName(oldName);
            entryClone.setName(newName);

            entryClone.setFolder(folder);
            asService.persist(entryClone);

            if (!newName.equals(oldName)) {
                entryClone.setName(newName);
                folder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
            }
        }

        Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
        BannerTC.getInstance().updateFolder(updatedFolder);
        ExplorerTC.getInstance().updateFolder(updatedFolder);

        if (!entryClones.isEmpty()) {
            final String title = "BP Reaction";
            String msgCreated ;
            if(entryClones.size() == 1){
                AnnotatedSeq entryClone = entryClones.iterator().next();
                msgCreated = String.format("Entry clone \"%s\" created", entryClone.getName());
            }else{
                msgCreated = String.format("Entry clone%s created", entryClones.size() > 1 ? "s" : "");
            }
            String msg = String.format("<html>%s<br/><br/>Do you want to open %s?</html>", msgCreated, entryClones.size() > 1 ? "them" : "it");
            DialogDescriptor.Confirmation m = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
            Object answer = DialogDisplayer.getDefault().notify(m);
            if(answer.equals(DialogDescriptor.OK_OPTION)){
                final Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
                for(AnnotatedSeq entryClone: entryClones){
                    entryClone = asService.getFullByHibernateId(entryClone.getHibernateId(), rootFolder);
                    OpenASEditorAction action = new OpenASEditorAction(entryClone);
                    action.actionPerformed(null);
                }
            }
        }
    }

    private String getErrorMsg(GW_STATE state) {
        String ret;
        if (state == GW_STATE.DONOR_INSERT_COUNT_NO_MATCH) {
            String errorMsg = state.getErrorMsg();
            ret = String.format(CNST.ERROR_FORMAT, errorMsg, getInstruction());
        } else if (state == GW_STATE.DONOR_NOT_FOUND) {
            String errorMsg = state.getErrorMsg();
            ret = String.format(CNST.ERROR_FORMAT, errorMsg, getInstruction());
        } else if (state == GW_STATE.INSERT_NOT_FOUND) {
            String errorMsg = state.getErrorMsg();
            ret = String.format(CNST.ERROR_FORMAT, errorMsg, getInstruction());
        } else if (state == GW_STATE.DONOR_INSERT_NOT_MATCHING) {
            ret = String.format(CNST.ERROR_FORMAT, "The donor and the insert are incompatible", getInstruction());
        } else {
            ret = state.getErrorMsg();
        }
        return ret;
    }

    private String getInstruction() {
        return "Please select an equal number of compatible donors and inserts";
    }
}
