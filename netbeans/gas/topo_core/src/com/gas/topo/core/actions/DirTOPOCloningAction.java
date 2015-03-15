/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.actions;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.topo.core.api.IDirTOPOService;
import com.gas.topo.core.dir.DirCloningPanel;
import java.awt.event.ActionEvent;
import java.util.Date;
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
        id = "com.gas.topo.core.actions.DirTOPOCloningAction")
@ActionRegistration(displayName = "#CTL_DirTOPOCloningAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/TOPO", position = 2440, separatorAfter = 2441)
})
@NbBundle.Messages("CTL_DirTOPOCloningAction=Directional TOPO" + Unicodes.TRADEMARK + " Cloning...")
public class DirTOPOCloningAction extends AbstractAction {

    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IDirTOPOService topoService = Lookup.getDefault().lookup(IDirTOPOService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

    public DirTOPOCloningAction() {
        super(String.format("Directional TOPO%s Cloning...", Unicodes.TRADEMARK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final DirCloningPanel panel = new DirCloningPanel();

        List<AnnotatedSeq> objs = BannerTC.getInstance().getCheckedNucleotides();
        boolean valid = validate(objs, panel);
        if (!valid) {
            return;
        }
        DialogDescriptor d = new DialogDescriptor(panel, String.format("Directional TOPO%s Cloning", Unicodes.TRADEMARK));
        panel.setDialogDescriptor(d);
        panel.validateInput();

        Integer answer = (Integer) DialogDisplayer.getDefault().notify(d);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            final AnnotatedSeq insert = panel.getInsert();
            final AnnotatedSeq vector = panel.getVector();
            String insertFolderPath = folderService.loadWithParents(insert.getFolder().getHibernateId()).getAbsolutePath();
            String vectorFolderPath = folderService.loadWithParents(vector.getFolder().getHibernateId()).getAbsolutePath();

            Operation operation = new Operation();
            operation.setNameEnum(Operation.NAME.DIR_TOPO);
            operation.setDate(new Date());
            Operation.Participant part = new Operation.Participant(insertFolderPath + "\\" + insert.getName(), true);
            operation.addParticipant(part);
            Operation.Participant part2 = new Operation.Participant(vectorFolderPath + "\\" + vector.getName(), true);
            operation.addParticipant(part2);

            AnnotatedSeq clone = topoService.ligate(insert, vector);

            final Folder folder = ExplorerTC.getInstance().getSelectedFolder();

            final String name = folder.getNewElementName(String.format("Dir TOPO%s Clone", Unicodes.TRADEMARK));
            clone.setName(name);
            clone.setDesc(String.format("a recombinant vector created from %s and %s", insert.getName(), vector.getName()));
            clone.setFolder(folder);
            clone.setOperation(operation);
            asService.persist(clone);

            Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());

            BannerTC.getInstance().updateFolder(updatedFolder);
            ExplorerTC.getInstance().updateFolder(updatedFolder);

            final String title = String.format("Directional TOPO%s Cloning", Unicodes.TRADEMARK);
            final String msg = String.format("<html>Directional TOPO%s \"%s\" clone created<br/><br/><br/>Do you want to open it?</html>", Unicodes.TRADEMARK, clone.getName());
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
            Object answerOpen = DialogDisplayer.getDefault().notify(c);
            if (answerOpen.equals(DialogDescriptor.OK_OPTION)) {
                Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
                clone = asService.getFullByHibernateId(clone.getHibernateId(), rootFolder);
                OpenASEditorAction action = new OpenASEditorAction(clone);
                action.actionPerformed(null);
            }
        }
    }

    private boolean validate(List<AnnotatedSeq> seqs, DirCloningPanel panel) {
        String titleError = String.format("Cannot perform directional TOPO%s cloning", Unicodes.TRADEMARK);
        boolean ret = true;
        if (seqs.size() > 2) {
            final String errorMsg = "More than two nucleotides selected";
            final String msg = String.format(CNST.ERROR_FORMAT, errorMsg, "Please select no more than two nucleotides");
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(titleError);
            DialogDisplayer.getDefault().notify(m);
            ret = false;
        } else {
            List<AnnotatedSeq> fulls = asService.getFull(seqs);
            for (AnnotatedSeq full : fulls) {
                IDirTOPOService.STATE stateInsert = topoService.isInsertValid(full);
                IDirTOPOService.STATE stateVector = topoService.isVectorValid(full);
                if (stateInsert != IDirTOPOService.STATE.VALID && stateVector != IDirTOPOService.STATE.VALID) {
                    String errorMsg = String.format("\"%s\" is neither a valid directional TOPO%s insert nor a valid directional TOPO%s vector", full.getName(), Unicodes.TRADEMARK, Unicodes.TRADEMARK);
                    String msg = String.format(CNST.ERROR_FORMAT, errorMsg, getInstruction(stateInsert, stateVector));
                    DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                    m.setTitle(titleError);
                    DialogDisplayer.getDefault().notify(m);
                    ret = false;
                    break;
                }
                if (stateVector == IDirTOPOService.STATE.VALID) {
                    panel.setVector(full);
                } else if (stateInsert == IDirTOPOService.STATE.VALID) {
                    panel.setInsert(full);
                }
            }
        }
        return ret;
    }

    private String getInstruction(IDirTOPOService.STATE insertState, IDirTOPOService.STATE vectorState) {
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("A valid directional TOPO%s insert must", Unicodes.TRADEMARK));

        StringList msgInsert = new StringList();
        String msg = "be linearized";
        if (insertState == IDirTOPOService.STATE.NOT_LINEAR) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgInsert.add(msg);

        msg = "be blunt-ended";
        if (insertState == IDirTOPOService.STATE.NOT_BLUNT_ENDED) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgInsert.add(msg);

        msg = "start with 5'-CACC-3' in one and only one strand";
        if (insertState == IDirTOPOService.STATE.NO_CACC_END) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgInsert.add(msg);

        msg = "not start with 5'-CACC-3' in both strands ";
        if (insertState == IDirTOPOService.STATE.TOO_MANY_CACC_ENDS) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgInsert.add(msg);

        msg = "not start with 5'-CACC-3' in both strands ";
        if (insertState == IDirTOPOService.STATE.TOO_MANY_CACC_ENDS) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgInsert.add(msg);

        msg = "not have >= 75% identity to 'CACC' at both 5' ends";
        if (insertState == IDirTOPOService.STATE.GT_75_END) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgInsert.add(msg);

        ret.append(StrUtil.toHtmlList(msgInsert));
        ret.append(String.format("A valid directional TOPO%s vector must", Unicodes.TRADEMARK));

        StringList msgVector = new StringList();
        msg = "be linearized";
        if (vectorState == IDirTOPOService.STATE.NOT_LINEAR) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgVector.add(msg);

        msg = "contain one and only one 3' overhang 5'-GGTG-3'";
        if (vectorState == IDirTOPOService.STATE.NO_OVERHANG
                || insertState == IDirTOPOService.STATE.NO_VALID_OVERHANG) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgVector.add(msg);

        msg = "contain sequence 5'-CCCTT-3' at both ends";
        if (vectorState == IDirTOPOService.STATE.NO_CCCTT) {
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        msgVector.add(msg);

        ret.append(StrUtil.toHtmlList(msgVector));
        return ret.toString();
    }
}
