/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.actions;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.SeqSelectPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
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
import java.util.Iterator;
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
        id = "com.gas.topo.core.actions.CreateTAInsertAction")
@ActionRegistration(displayName = "#CTL_CreateTAInsertAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/TOPO", position = 2425)
})
@NbBundle.Messages("CTL_CreateTAInsertAction=Create TOPO" + Unicodes.TRADEMARK + " TA Insert")
public class CreateTAInsertAction extends AbstractAction {

    IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    IAnnotatedSeqService asSeqService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private static String TITLE = String.format("Create TOPO%s TA Insert", Unicodes.TRADEMARK);

    public CreateTAInsertAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = UIUtil.getEditorMode().getSelectedTopComponent();
        boolean isWholeSeq = true;
        AnnotatedSeq as;
        List<AnnotatedSeq> subs = new ArrayList<AnnotatedSeq>();
        final Folder folder = ExplorerTC.getInstance().getSelectedFolder();

        if (tc != null && tc instanceof IASEditor) {
            IASEditor asEditor = (IASEditor) tc;

            LocList locList = asEditor.getSelections();

            if (locList != null && !locList.isEmpty()) {
                SeqSelectPanel seqSelectPanel = new SeqSelectPanel(isWholeSeq, null, true, false);
                DialogDescriptor d = new DialogDescriptor(seqSelectPanel, "Sequence Data");
                Object answer = DialogDisplayer.getDefault().notify(d);
                if (answer.equals(DialogDescriptor.OK_OPTION)) {
                    isWholeSeq = seqSelectPanel.isWholeSequence();
                } else {
                    return;
                }
            }
            as = asEditor.getAnnotatedSeq();

            if (isWholeSeq) {
                subs.add(as.clone(AnnotatedSeq.ELEMENT.FEATURE,
                        AnnotatedSeq.ELEMENT.OVERHANG,
                        AnnotatedSeq.ELEMENT.SEQ));
            } else {
                Iterator<Loc> itr = locList.iterator();
                while (itr.hasNext()) {
                    Loc loc = itr.next();
                    AnnotatedSeq sub = AsHelper.subAs(as, loc.getStart(), loc.getEnd(), AnnotatedSeq.ELEMENT.FEATURE,
                            AnnotatedSeq.ELEMENT.OVERHANG,
                            AnnotatedSeq.ELEMENT.SEQ);
                    subs.add(sub);
                }
            }
        } else {
            List<AnnotatedSeq> objs = BannerTC.getInstance().getSelectedNucleotides();
            if (objs.isEmpty()) {
                String msg = String.format(CNST.ERROR_FORMAT, "No nucleotides selected", "Please select one and only one linearized nucleotide");
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                return;
            } else if (objs.size() > 1) {
                String msg = String.format(CNST.ERROR_FORMAT, "More than one nucleotide selected", "Please select one and only one linearized nucleotide");
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                return;
            } else if (objs.get(0).isCircular()) {
                String msg = String.format(CNST.ERROR_FORMAT, "The selected nucleotide is circular", "Please select one and only one linearized nucleotide");
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                return;
            } else {
                as = asSeqService.getFullByHibernateId(objs.get(0).getHibernateId());
                AnnotatedSeq clone = as.clone(AnnotatedSeq.ELEMENT.FEATURE,
                        AnnotatedSeq.ELEMENT.OVERHANG,
                        AnnotatedSeq.ELEMENT.SEQ);
                subs.add(clone);
            }
        }

        modifyEndsAndParentLoc(subs.toArray(new AnnotatedSeq[subs.size()]));
        setMisc(as, folder, subs.toArray(new AnnotatedSeq[subs.size()]));

        Folder folderUpdated = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
        BannerTC.getInstance().updateFolder(folderUpdated);
        ExplorerTC.getInstance().updateFolder(folderUpdated);

        if (subs.size() == 1) {
            String msg = String.format("<html>\"%s\" created. <br/><br/>Do you want to open it?</html>", subs.get(0).getName());
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, TITLE);
            Object answer = DialogDisplayer.getDefault().notify(c);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                OpenASEditorAction openAction = new OpenASEditorAction(subs.get(0));
                openAction.actionPerformed(null);
            }
        }
    }

    /**
     * sets the name, desc, date, and operation
     */
    private void setMisc(AnnotatedSeq parent, Folder folder, AnnotatedSeq... subs) {
        String folderPath = folderService.loadWithParents(parent.getFolder().getHibernateId()).getAbsolutePath();
        Operation operation = new Operation();
        operation.setNameEnum(Operation.NAME.TOPO_TA_INSERT);
        operation.setDate(new Date());
        Operation.Participant part = new Operation.Participant(folderPath + "\\" + parent.getName(), true);
        operation.addParticipant(part);

        for (AnnotatedSeq sub : subs) {
            final String newName = folder.getNewElementName("TOPO\u00AE TA Insert");
            sub.setName(newName);
            sub.setDesc(String.format("TOPO%s TA Insert from %s", Unicodes.TRADEMARK, parent.getName()));
            sub.setFolder(folder);
            sub.setOperation(operation.clone());
            sub.setCreationDate(new Date());
            sub.setLastModifiedDate(new Date());
            asSeqService.persist(sub);
        }
    }

    private void modifyEndsAndParentLoc(AnnotatedSeq... subs) {
        for (AnnotatedSeq sub : subs) {
            int lengthOld = sub.getLength();
            sub.addFrontData("T");
            sub.appendData("A");

            // start overhang
            Overhang overhang = new Overhang();
            overhang.setFivePrime(false);
            overhang.setStrand(false);
            overhang.setLength(1);
            overhang.setName("TA");
            sub.setStartOverhang(overhang);

            // end overhang
            overhang = new Overhang();
            overhang.setFivePrime(false);
            overhang.setStrand(true);
            overhang.setLength(1);
            overhang.setName("TA");
            sub.setEndOverhang(overhang);

            ParentLoc parentLoc = new ParentLoc(1, lengthOld, 1);
            parentLoc.setTotalPos(lengthOld);
            sub.getParentLocSet().clear();
            sub.getParentLocSet().add(parentLoc);
        }
    }
}
