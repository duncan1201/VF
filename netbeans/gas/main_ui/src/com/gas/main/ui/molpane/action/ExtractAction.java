/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.InputPanel;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.main.ui.editor.as.ASEditor;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Iterator;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

public class ExtractAction extends AbstractAction {

    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private StringList existingNames;

    public ExtractAction() {
        super("Extract Region...", ImageHelper.createImageIcon(ImageNames.EXTRACT_16));
    }

    public void setExistingNames(StringList names) {
        this.existingNames = names;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ASEditor asEditor = (ASEditor) UIUtil.getEditorMode().getSelectedTopComponent();
        AnnotatedSeq as = asEditor.getAnnotatedSeq();

        InputPanel inputPanel = new InputPanel("Extraction Name:");
        String proposedName = String.format("%s extraction", as.getName());
        if (existingNames != null && !existingNames.isEmpty()) {
            proposedName = StrUtil.getNewName(proposedName, existingNames);
        }
        inputPanel.setInitInputText(proposedName);
        inputPanel.setForbiddenChars(AnnotatedSeq.forbiddenChars);
        if (existingNames != null && !existingNames.isEmpty()) {
            inputPanel.setExistingNames(existingNames.toArray(new String[existingNames.size()]));
        }
        DialogDescriptor dd = new DialogDescriptor(inputPanel, "Extract Region");
        inputPanel.setDialogDescriptor(dd);
        inputPanel.validateInput();
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (!answer.equals(DialogDescriptor.OK_OPTION)) {
            return;
        }
        AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());

        final Folder folder = ExplorerTC.getInstance().getSelectedFolder();
        final String folderPath = folderService.loadWithParents(folder.getHibernateId()).getAbsolutePath();
        Operation operation = new Operation();
        operation.setNameEnum(Operation.NAME.Extraction);
        operation.setDate(new Date());
        Operation.Participant part = new Operation.Participant(folderPath + "\\" + full.getName(), true);
        operation.addParticipant(part);

        LocList locList = asEditor.getMolPane().getSelections();
        Iterator<Loc> itr = locList.iterator();
        while (itr.hasNext()) {
            Loc loc = itr.next();
            final String newName = folder.getNewElementName(String.format("%s extraction", full.getName()));
            AnnotatedSeq subAs = AsHelper.subAs(full, loc.getStart(), loc.getEnd());
            ParentLoc parentLoc = new ParentLoc(loc.getStart(), loc.getEnd());
            parentLoc.setTotalPos(full.getLength());
            subAs.getParentLocSet().add(parentLoc);
            subAs.setName(newName);
            subAs.setAccession("N/A");
            subAs.setRead(false);
            subAs.setFolder(folder);
            subAs.setOperation(operation.clone());
            asService.persist(subAs);
        }

        Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());

        if (BannerTC.getInstance() != null) {
            BannerTC.getInstance().updateFolder(updatedFolder);
        }
        if (ExplorerTC.getInstance() != null) {
            ExplorerTC.getInstance().updateFolder(updatedFolder);
        }
    }
}