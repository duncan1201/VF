/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.actions;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.SeqSelectPanel;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
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
import org.openide.windows.TopComponent;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
        id = "com.gas.topo.core.actions.CreateDirTOPOInsertAction")
@ActionRegistration(displayName = "#CTL_CreateDirTOPOInsertAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/TOPO", position = 2435)
})
@NbBundle.Messages("CTL_CreateDirTOPOInsertAction=Create Directional TOPO" + Unicodes.TRADEMARK + " Insert...")
public class CreateDirTOPOInsertAction extends AbstractAction {

    private Boolean overhangStrand = true;
    final static String NAME_TEMPLATE = String.format("Create Directional TOPO%s Insert", Unicodes.TRADEMARK);
    IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    IAnnotatedSeqService asSeqService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    final AnnotatedSeq.ELEMENT[] elts = {AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.FOLDER, AnnotatedSeq.ELEMENT.SEQ};
    final String instruction = getInstruction();
    private static String TITLE = String.format("Create Directional TOPO%s Insert", Unicodes.TRADEMARK);

    public CreateDirTOPOInsertAction() {
        super(TITLE);
    }

    private String getInstruction() {
        StringBuilder ret = new StringBuilder();
        List<String> conditions = new ArrayList<String>();
        conditions.add("linear");
        conditions.add("blunt-ended");
        conditions.add(String.format("non-complementary to the overhang(%s) of directional TOPO%s vector", "5'-GGTG-3'", Unicodes.TRADEMARK));
        ret.append("Please select one and only one nucleotide with the following conditions:");
        ret.append(StrUtil.toHtmlList(conditions));
        return ret.toString();
    }

    public Boolean isOverhangStrand() {
        return overhangStrand;
    }

    public void setOverhangStrand(Boolean overhangStrand) {
        Boolean old = this.overhangStrand;
        this.overhangStrand = overhangStrand;
        firePropertyChange("overhangStrand", old, this.overhangStrand);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Folder folder = ExplorerTC.getInstance().getSelectedFolder();
        TopComponent tc = UIUtil.getEditorMode().getSelectedTopComponent();
        AnnotatedSeq candidate;
        AnnotatedSeq parent = null;

        if (tc != null && tc instanceof IASEditor) {
            IASEditor asEditor = (IASEditor) tc;
            LocList locList = asEditor.getSelections();
            boolean wholeSeq;
            if (locList != null && !locList.isEmpty()) {
                SeqSelectPanel seqSelectPanel = new SeqSelectPanel(true, null, true, false);
                DialogDescriptor d = new DialogDescriptor(seqSelectPanel, "");
                Object answer = DialogDisplayer.getDefault().notify(d);
                if (!answer.equals(DialogDescriptor.OK_OPTION)) {
                    return;
                }
                wholeSeq = seqSelectPanel.isWholeSequence();
            } else {
                wholeSeq = true;
            }

            parent = asEditor.getAnnotatedSeq();

            if (wholeSeq) {
                candidate = parent.clone(elts);
            } else {
                candidate = AsHelper.subAs(parent, locList.get(0).getStart(), locList.get(0).getEnd(), elts);
            }

        } else {
            List<AnnotatedSeq> nucleotides = BannerTC.getInstance().getSelectedNucleotides();
            String errorMsg = "";
            boolean valid = true;
            if (nucleotides.isEmpty()) {
                errorMsg = "No nucleotides selected";
                valid = false;
            } else if (nucleotides.size() > 1) {
                errorMsg = "More than one nucleotide selected";
                valid = false;
            }
            if (!valid) {
                String msg = String.format(CNST.ERROR_FORMAT, errorMsg, instruction);
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                return;
            }
            parent = asSeqService.getFullByHibernateId(nucleotides.get(0).getHibernateId());
            candidate = parent.clone(elts);

            parent = asSeqService.getFullByHibernateId(nucleotides.get(0).getHibernateId());
            candidate = parent.clone(elts);
        }
        boolean candidateValid = isCandidateValid(candidate);

        if (!candidateValid) {
            return;
        }
        AnnotatedSeq sub = _createInsert(candidate, overhangStrand);
        final String newName = folder.getNewElementName(String.format("Directional TOPO%s Insert", Unicodes.TRADEMARK));
        sub.setName(newName);
        sub.setDesc(String.format("Directional TOPO%s Insert from %s", Unicodes.TRADEMARK, parent.getName()));
        sub.setFolder(folder);

        final String folderPath = folderService.loadWithParents(folder.getHibernateId()).getAbsolutePath();
        Operation operation = new Operation();
        operation.setNameEnum(Operation.NAME.DIR_TOPO_INSERT);
        operation.setDate(new Date());
        Operation.Participant part = new Operation.Participant(folderPath + "\\" + parent.getName(), true);
        operation.addParticipant(part);

        sub.setOperation(operation);
        asSeqService.persist(sub);

        Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
        BannerTC.getInstance().updateFolder(updatedFolder);
        ExplorerTC.getInstance().updateFolder(updatedFolder);

        String confirmMsg = String.format("<html>\"%s\" created.</br></br> Do you want to open it?</html>", newName);
        DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(confirmMsg, NAME);
        c.setTitle(TITLE);
        Object answer = DialogDisplayer.getDefault().notify(c);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            sub = asSeqService.getFullByHibernateId(sub.getHibernateId());
            OpenASEditorAction openAction = new OpenASEditorAction(sub);
            openAction.actionPerformed(null);
        }
    }

    private boolean isCandidateValid(AnnotatedSeq candidate) {
        String errorMsg = "";
        boolean valid = true;
        if (candidate.isCircular()) {
            errorMsg = String.format("Molecule \"%s\" is circular", candidate.getName());
            valid = false;
        } else if (candidate.getOverhangSize() > 0) {
            errorMsg = String.format("Molecule \"%s\" is not blunt-ended", candidate.getName());
            valid = false;
        } else {
            String seq = candidate.getSiquence().getData(candidate.getLength() - 3, candidate.getLength(), true);
            float similarity = StrUtil.getSimilarity(seq, "CACC");

            if (similarity == 1) {
                errorMsg = String.format("The start of reverse strand(%s) is complementary to the overhang(%s) of directional TOPO%s vector ", "5'-CACC-3'", "5'-GGTG-3'", Unicodes.TRADEMARK);
                valid = false;
            }
        }
        if (!valid) {
            String msg = String.format(CNST.ERROR_FORMAT, errorMsg, instruction);
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
        }
        return valid;
    }

    private AnnotatedSeq _createInsert(AnnotatedSeq sub, boolean strand) {
        int lengthOld = sub.getLength();
        final String CACC = "CACC";
        if (strand) {
            sub.addFrontData(CACC);
        } else {
            String data = BioUtil.reverseComplement(CACC);
            sub.appendData(data);
        }

        ParentLoc parentLoc = new ParentLoc(1, lengthOld, CACC.length());
        parentLoc.setTotalPos(lengthOld);
        sub.getParentLocSet().clear();
        sub.getParentLocSet().add(parentLoc);

        sub.setCreationDate(new Date());
        sub.setLastModifiedDate(new Date());
        sub.setRead(false);
        return sub;
    }
}
