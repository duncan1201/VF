/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.blunt;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.topo.core.api.IBluntTOPOService;
import com.gas.topo.core.blunt.BluntCloningPanel;
import java.awt.event.ActionEvent;
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
        id = "com.gas.topo.core.actions.BluntTOPOCloningAction")
@ActionRegistration(displayName = "#CTL_BluntTOPOCloningAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/TOPO", position = 2420, separatorAfter = 2422)
})
@NbBundle.Messages("CTL_BluntTOPOCloningAction=TOPO" + Unicodes.TRADEMARK + " Blunt-end cloning...")
public class BluntTOPOCloningAction extends AbstractAction {

    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IBluntTOPOService topoService = Lookup.getDefault().lookup(IBluntTOPOService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

    public BluntTOPOCloningAction() {
        super(String.format("TOPO%s Blunt-end cloning...", Unicodes.TRADEMARK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> checked = BannerTC.getInstance().getCheckedNucleotides();
        BluntCloningPanel panel = new BluntCloningPanel();
        final String titleError = String.format("Cannot perform TOPO%s cloning", Unicodes.TRADEMARK);
        if (!checked.isEmpty()) {
            if (checked.size() > 2) {
                DialogDescriptor.Message m = new DialogDescriptor.Message(String.format(CNST.ERROR_FORMAT, "More than two nucleotides selected", "Please select no more than two nucleotides"), DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(titleError);
                DialogDisplayer.getDefault().notify(m);
                return;
            } else {
                List<AnnotatedSeq> fulls = asService.getFull(checked);

                for (AnnotatedSeq full : fulls) {
                    IBluntTOPOService.STATE vectorState = topoService.isVectorValid(full, IBluntTOPOService.STATE.class);
                    IBluntTOPOService.STATE insertState = topoService.isInsertValid(full, IBluntTOPOService.STATE.class);

                    if (vectorState != IBluntTOPOService.STATE.VALID && insertState != IBluntTOPOService.STATE.VALID) {
                        final String errorMsg = String.format("\"%s\" is neither a valid insert nor a valid blunt TOPO%s vector", full.getName(), Unicodes.TRADEMARK);

                        DialogDescriptor.Message m = new DialogDescriptor.Message(String.format(CNST.ERROR_FORMAT, errorMsg, createInstruction(insertState, vectorState)), DialogDescriptor.INFORMATION_MESSAGE);
                        m.setTitle(titleError);
                        DialogDisplayer.getDefault().notify(m);
                        return;
                    } else if (vectorState == IBluntTOPOService.STATE.VALID) {
                        panel.setVector(full);
                    } else if (insertState == IBluntTOPOService.STATE.VALID) {
                        panel.setInsert(full);
                    }
                }
            }
        }

        DialogDescriptor dd = new DialogDescriptor(panel, String.format("TOPO%s Blunt-end Cloning", Unicodes.TRADEMARK));
        panel.setDialogDescriptor(dd);
        panel.validateInput();
        Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);

        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            final AnnotatedSeq insert = panel.getInsert();
            final AnnotatedSeq vector = panel.getVector();
            List<AnnotatedSeq> clones = topoService.clone(insert, vector);

            final Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            for (AnnotatedSeq clone : clones) {
                final String name = folder.getNewElementName("TOPO Clone");
                clone.setName(name);
                folder.addObject(clone);
                asService.persist(clone);
            }

            final Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
            BannerTC.getInstance().updateFolder(updatedFolder);
            ExplorerTC.getInstance().updateFolder(updatedFolder);

            String title = String.format("Blunt TOPO%s Cloning", Unicodes.TRADEMARK);
            String msg = String.format("TOPO%s clones created. Do you want to open them?", Unicodes.TRADEMARK);
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
            Object answerOpen = DialogDisplayer.getDefault().notify(c);
            if (answerOpen.equals(DialogDescriptor.OK_OPTION)) {
                final Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
                for (AnnotatedSeq clone : clones) {
                    clone = asService.getFullByHibernateId(clone.getHibernateId(), rootFolder);
                    OpenASEditorAction action = new OpenASEditorAction(clone);
                    action.actionPerformed(null);
                }
            }
        }
    }

    private String createInstruction(IBluntTOPOService.STATE insertState, IBluntTOPOService.STATE vectorState) {
        StringBuilder ret = new StringBuilder();

        ret.append(String.format("A TOPO%s insert must", Unicodes.TRADEMARK));
        StringList strList = new StringList();
        String msg = "be linearized";
        if (insertState == IBluntTOPOService.STATE.NOT_LINEAR) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);

        msg = "be blunt-ended";
        if (insertState == IBluntTOPOService.STATE.NOT_BLUNT_ENDED) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);
        ret.append(StrUtil.toHtmlList(strList));

        ret.append("<br/>");

        strList = new StringList();
        ret.append(String.format("A valid blunt TOPO%s vector must", Unicodes.TRADEMARK));
        msg = "be linearized";
        if (insertState == IBluntTOPOService.STATE.NOT_LINEAR) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);

        msg = "be blunt-ended";
        if (insertState == IBluntTOPOService.STATE.NOT_BLUNT_ENDED) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);

        msg = "have sequence <b>5´-CCCTT-3'</b> at both ends";
        if (insertState == IBluntTOPOService.STATE.NO_CCCTT) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);
        ret.append(StrUtil.toHtmlList(strList));

        return ret.toString();
    }
}
