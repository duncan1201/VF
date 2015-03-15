/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate.actions;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.InputPanel;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.api.ILigateService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.editor.as.ligate.LigatePanel;
import com.gas.domain.ui.explorer.ExplorerTC;
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
        id = "com.gas.domain.ui.editor.as.ligate.actions.LigateAction")
@ActionRegistration(displayName = "#CTL_LigateAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/Cloning", position = 2415, separatorAfter = 2416)
})
@NbBundle.Messages("CTL_LigateAction=Ligate...")
public class LigateAction extends AbstractAction {

    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    static String TITLE = "Ligate";

    public LigateAction() {
        super(TITLE + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> objs = BannerTC.getInstance().getCheckedObjects(AnnotatedSeq.class);

        boolean valid = validate(objs, true);
        if (!valid) {
            return;
        }
        final List<AnnotatedSeq> fullObjs = asService.getFull(objs);
        final Folder fromFolder = folderService.loadWithDataAndChildren(fullObjs.get(0).getFolder().getHibernateId());
        StringList elementNames = fromFolder.getElementNames(AnnotatedSeq.class);
        AnnotatedSeq ret = null;
        if (fullObjs.size() > 1) {
            String initName = fromFolder.getNewElementName("Ligated Sequence");

            LigatePanel ligatePanel = new LigatePanel(new AnnotatedSeqList(AsHelper.clone(fullObjs, true)));
            ligatePanel.setInitName(initName);
            ligatePanel.setExistingNames(elementNames);
            
            DialogDescriptor dd = new DialogDescriptor(ligatePanel, TITLE);
            ligatePanel.setDialogDescriptor(dd);
            ligatePanel.validateInput();

            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
            if (!answer.equals(DialogDescriptor.OK_OPTION)) {
                return;
            }
            ILigateService ligateService = Lookup.getDefault().lookup(ILigateService.class);
            List<AnnotatedSeq> modifiedData = ligatePanel.getModifiedData();
            Boolean circularize = ligatePanel.isCircularize();
            AnnotatedSeq ligated = ligateService.ligate(modifiedData, circularize);
            String newName = ligatePanel.getNewName();
            ligated.setName(newName);
            ligated.setFolder(fromFolder);
            String id = asService.save(ligated);

            ret = asService.getFullByHibernateId(id);

        } else if (fullObjs.size() == 1){
            AnnotatedSeq full = fullObjs.get(0);
            boolean endsCompatible = AsHelper.areEndsCompatible(full);
            if (!endsCompatible) {
                String msgError = "";
                if (full.getOverhangSize() == 1) {
                    Overhang overhang = full.getOverhangItr().next();
                    boolean startOverhang = overhang.isStartOverhang();
                    int startPos;
                    int endPos;
                    if (startOverhang) {
                        startPos = 1;
                        endPos = overhang.getLength();
                    } else {
                        startPos = full.getLength() - overhang.getLength() + 1;
                        endPos = full.getLength();
                    }
                    msgError = String.format("Only one overhang found at %d - %d bp", startPos, endPos);
                } else if (full.getOverhangSize() == 2) {
                    final Overhang overhangStart = full.getStartOverhang();
                    final String ovSeqStart = full.getOverhangSeq(overhangStart);
                    final Overhang overhangEnd = full.getEndOverhang();
                    final String ovSeqEnd = full.getOverhangSeq(overhangEnd);
                    msgError = String.format("The %s overhang 5'-%s-3' at %d - %d bp is incompatible with the %s overhang 5'-%s-3' at %d - %d bp", overhangStart.isFivePrime() ? "5'" : "3'", ovSeqStart, 1, overhangStart.getLength(), overhangEnd.isFivePrime() ? "5'" : "3'", ovSeqEnd, full.getLength() - ovSeqEnd.length() + 1, full.getLength());
                }
                final String in = "Please select a nucleotide with two compatible ends";
                final String msg = String.format(CNST.ERROR_FORMAT, msgError, in);
                DialogDescriptor.Message mg = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                mg.setTitle("Cannot circularize");
                DialogDisplayer.getDefault().notify(mg);
                return;
            }

            final String nameNew = fromFolder.getNewElementName(full.getName());
            InputPanel inputPanel = new InputPanel("Name:");
            inputPanel.setInitInputText(nameNew);
            inputPanel.setExistingNames(elementNames.toArray(new String[elementNames.size()]));
            DialogDescriptor dd = new DialogDescriptor(inputPanel, String.format("Circularize '%s'", full.getName()));
            inputPanel.setDialogDescriptor(dd);

            Object answer = DialogDisplayer.getDefault().notify(dd);
            if (!answer.equals(DialogDescriptor.OK_OPTION)) {
                return;
            }
            AnnotatedSeq circularized = AsHelper.circularize(full);
            circularized.setName(nameNew);
            circularized.setLastModifiedDate(new Date());
            circularized.setCreationDate(new Date());

            String hId = asService.save(circularized);
            ret = asService.getFullByHibernateId(hId);
        }

        Folder updatedFolder = folderService.loadWithDataAndParentAndChildren(fromFolder.getHibernateId());
        BannerTC.getInstance().updateFolder(updatedFolder);
        ExplorerTC.getInstance().updateFolder(updatedFolder);

        String msg = String.format(CNST.MSG_FORMAT, "One molecule created", "Do you want to open it?");
        DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, TITLE, DialogDescriptor.YES_NO_OPTION);
        Object answer = DialogDisplayer.getDefault().notify(c);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            OpenASEditorAction action = new OpenASEditorAction(ret);
            action.actionPerformed(null);
        }
    }

    /**
     *
     */
    public static boolean validate(List<AnnotatedSeq> objs, boolean display) {
        boolean ret = true;
        String msg = null;
        String title = "No nucleotide sequence selected";
        if (objs.isEmpty()) {
            msg = String.format(CNST.ERROR_FORMAT, "No nucleotide sequence selected", "Please select at least one linear nucleotide sequence to ligate");
            ret = false;
        }

        if (ret) {
            for (AnnotatedSeq as : objs) {
                boolean isNucleotide = AsHelper.isNucleotide(as);
                if (!isNucleotide) {
                    msg = String.format(CNST.ERROR_FORMAT, "No nucleotide sequence selected", "Please select at least one linear nucleotide sequence to ligate");
                    ret = false;
                    break;
                }
            }
        }

        if (ret) {
            for (AnnotatedSeq as : objs) {
                if (as.isCircular()) {
                    final String a = objs.size() > 1 ? "ligate" : "circularize";
                    msg = String.format(CNST.ERROR_FORMAT, "Cannot " + a + " due to a circular sequence:" + as.getName(), "Please select at least one linear nucleotide sequence");
                    if (objs.size() > 1) {
                        title = "Cannot " + a;
                    } else {
                        title = "Cannot " + a;
                    }
                    ret = false;
                    break;
                }
            }
        }

        if (display && !ret && msg != null && !msg.isEmpty()) {
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(title);
            DialogDisplayer.getDefault().notify(m);
        }

        return ret;
    }
}
