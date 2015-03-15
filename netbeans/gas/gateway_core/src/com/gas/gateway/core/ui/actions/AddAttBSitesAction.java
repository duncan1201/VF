/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.actions;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.gateway.core.service.api.*;
import com.gas.gateway.core.ui.addAttbSites.AddAttbSitesPanel;
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
import org.openide.windows.TopComponent;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
        id = "com.gas.gateway.ui.actions.AddAttBSitesAction")
@ActionRegistration(displayName = "#CTL_AddAttBSitesAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/Gateway", position = 2450)
})
@NbBundle.Messages("CTL_AddAttBSitesAction=Add AttB Sites to PCR product...")
public class AddAttBSitesAction extends AbstractAction {

    private static String TITLE = "Add AttB Sites to PCR product";
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IGWValidateService validateService = Lookup.getDefault().lookup(IGWValidateService.class);

    public AddAttBSitesAction() {
        super(TITLE + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AnnotatedSeq as = null;
        AnnotatedSeq full = null;
        final String errorMsgNotDNAEditor = "The selected editor is not a nucleotide editor";
        TopComponent editor = UIUtil.getEditorMode().getSelectedTopComponent();
        if (editor != null && editor instanceof IASEditor) {
            String msg = null;
            if (editor instanceof IASEditor) {
                IASEditor asEditor = (IASEditor) editor;
                as = asEditor.getAnnotatedSeq();
                if (as.isProtein()) {
                    msg = String.format(CNST.ERROR_FORMAT, errorMsgNotDNAEditor, getInstruction());
                }
            } else {
                msg = String.format(CNST.ERROR_FORMAT, errorMsgNotDNAEditor, getInstruction());
            }

            if (msg != null && !msg.isEmpty()) {
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                return;
            }
        } else {
            List<AnnotatedSeq> objs = BannerTC.getInstance().getSelectedObjects(AnnotatedSeq.class);
            String msg = null;
            if (!objs.isEmpty()) {
                as = objs.get(0);
                if (as.isProtein()) {
                    msg = String.format(CNST.ERROR_FORMAT, errorMsgNotDNAEditor, getInstruction());
                }
            } else {
                msg = String.format(CNST.ERROR_FORMAT, "No nucleotides selected", getInstruction());
            }

            if (msg != null) {
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                return;
            }
        }

        if (as != null) {
            full = asService.getFullByHibernateId(as.getHibernateId());
        }

        GW_STATE state = validateService.validateAddAttbSites(full);
        if (state != GW_STATE.VALID) {
            DialogDescriptor.Message m = new DialogDescriptor.Message(getErrorMsg(state, full.getName()), DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
            return;
        }
        AddAttbSitesPanel panel = new AddAttbSitesPanel();
        DialogDescriptor dd = new DialogDescriptor(panel, String.format("Add attB sites to \"%s\"", full.getName()));
        panel.setDialogDescriptor(dd);
        panel.validateInput();

        Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            final IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

            final IAttSiteService attbService = Lookup.getDefault().lookup(IAttSiteService.class);
            PrimerAdapter left = panel.getLeftPrimerAdapter();
            PrimerAdapter right = panel.getRightPrimerAdapter();
            boolean kozak = panel.isKozakIncluded();
            boolean sd = panel.isSDIncluded();
            boolean stopCodon = panel.isStopCodonIncluded();
            boolean startCodon = panel.isStartCodonIncluded();
            boolean fuseForward = panel.isFuseForwardPrimer();
            boolean fuseReverse = panel.isFuseReversePrimer();

            AnnotatedSeq ret = attbService.addAttBSite(full, left, sd, kozak, fuseForward, startCodon, right, fuseReverse, stopCodon);
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            String newName = folder.getNewElementName(String.format("%s with %s and %s", full.getName(), left.getName(), right.getName()));
            StringList existingNames = folder.getElementNames();
            ret.setName(newName);
            ret.setDesc(newName);
            ret.setFolder(folder);
            existingNames.add(newName);

            asService.persist(ret);

            if (panel.isAdapterPrimers() || panel.isTemplateSpecificPrimers()) {
                AnnotatedSeqList oligos = createOligos(ret, panel.isAdapterPrimers(), panel.isTemplateSpecificPrimers());
                for (AnnotatedSeq oligo : oligos) {
                    oligo.setFolder(folder);
                    newName = StrUtil.getNewName(oligo.getName(), existingNames);
                    oligo.setName(newName);
                    existingNames.add(newName);
                    asService.persist(oligo);
                }
            }

            Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
            BannerTC.getInstance().updateFolder(updatedFolder);
            ExplorerTC.getInstance().updateFolder(updatedFolder);
           
            final String msg = String.format("<html>\"%s\" created.<br/><br/> Do you want to open it?</html>", ret.getName());
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, TITLE);
            Object answerOpen = DialogDisplayer.getDefault().notify(c);
            if (answerOpen.equals(DialogDescriptor.OK_OPTION)) {
                Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
                ret = asService.getFullByHibernateId(ret.getHibernateId(), rootFolder);
                OpenASEditorAction action = new OpenASEditorAction(ret);
                action.actionPerformed(null);
            }
        }
    }

    AnnotatedSeqList createOligos(AnnotatedSeq parent, boolean adapterPrimers, boolean templatePrimers) {
        IAttSiteService service = Lookup.getDefault().lookup(IAttSiteService.class);
        AttSiteList attSiteList = service.getAttBSites(parent, true);
        AttSite first = attSiteList.first();
        AttSite last = attSiteList.last();

        AnnotatedSeqList ret = new AnnotatedSeqList();
        IPrimer3Service primer3Service = Lookup.getDefault().lookup(IPrimer3Service.class);

        // adapter primer
        int start = 1;
        int end = first.getLoc().getEnd();
        AnnotatedSeq adapterForward = primer3Service.convertToOligo(parent, start, end, true);
        adapterForward.setName(String.format("Forward adapter primer for %s", parent.getName()));
        if (adapterPrimers) {
            ret.add(adapterForward);
        }

        start = last.getLoc().getStart();
        end = parent.getLength();
        AnnotatedSeq adapterReverse = primer3Service.convertToOligo(parent, start, end, false);
        adapterReverse.setName(String.format("Reverse adapter primer for %s", parent.getName()));
        if (adapterPrimers) {
            ret.add(adapterReverse);
        }

        // gene-specific primer
        start = first.getLoc().getEnd() - 12;
        end = start + adapterForward.getLength() - 2;
        AnnotatedSeq primerForward = primer3Service.convertToOligo(parent, start, end, true);
        primerForward.setName(String.format("Forward primer for %s", parent.getName()));
        if (templatePrimers) {
            ret.add(primerForward);
        }

        end = last.getLoc().getStart() + 13;
        start = end - adapterReverse.getLength() + 2;
        AnnotatedSeq primerReverse = primer3Service.convertToOligo(parent, start, end, false);
        primerReverse.setName(String.format("Reverse primer for %s", parent.getName()));
        if (templatePrimers) {
            ret.add(primerReverse);
        }
        return ret;
    }

    private String getInstruction() {
        return "Please select or open a linear and blunt-ended nucleotide";
    }

    private String getErrorMsg(GW_STATE state, String name) {
        String ret;
        String inst = "Please select a linearized nucleotide with no overhangs";
        if (state == GW_STATE.NOT_LINEAR) {
            ret = String.format(CNST.ERROR_FORMAT, String.format("The \"%s\" is not linearized", name), inst);
        } else if (state == GW_STATE.NO_SELECTION) {
            ret = String.format(CNST.ERROR_FORMAT, "No nucleotides selected", inst);
        } else if (state == GW_STATE.NOT_BLUNT_ENDED) {
            ret = String.format(CNST.ERROR_FORMAT, String.format("The \"%s\" is not blunt-ended", name), inst);
        } else {
            ret = state.getErrorMsg();
        }
        return ret;
    }

    public static boolean getEnablement() {
        int linearNucleotideCount = 0;
        List<AnnotatedSeq> checkedASs = BannerTC.getInstance().getCheckedObjects(AnnotatedSeq.class);
        for (AnnotatedSeq obj : checkedASs) {
            AnnotatedSeq as = (AnnotatedSeq) obj;
            if (AsHelper.isNucleotide(as)) {
                if (!as.isCircular()) {
                    linearNucleotideCount++;
                }
            }
        }
        return linearNucleotideCount == 1;
    }
}
