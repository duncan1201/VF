/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.extractaa;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.api.IDomainUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.FetureSet;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ExtractAAaction extends AbstractAction {

    JComponent comp;
    static String TITLE = "Extract as amino acids";

    public ExtractAAaction(JComponent comp) {
        super("Extract as amino acids...");
        this.comp = comp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMolPane molPane = UIUtil.getParent(comp, IMolPane.class);
        final AnnotatedSeq parent = molPane.getAs();
        LocList selections = molPane.getSelections();
        ExtractAAPanel p = new ExtractAAPanel(selections, parent.getLength());
        DialogDescriptor d = new DialogDescriptor(p, TITLE);
        p.setDialogDescriptor(d);
        p.validateInput();
        Object answer = DialogDisplayer.getDefault().notify(d);

        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            AnnotatedSeq targetAs;
            Loc targetLoc;
            FetureSet targetFetureSet;
            if (p.isSelectedRegion()) {
                targetLoc = selections.iterator().next();
                targetAs = AsHelper.subAs(parent, targetLoc.getStart(), targetLoc.getEnd());
                targetFetureSet = parent.getFetureSet().clone();
                targetFetureSet.subSeq(targetLoc.getStart(), targetLoc.getEnd(), parent.isCircular(), parent.getLength());
            } else {
                targetLoc = new Loc(1, parent.getLength(), true);
                targetAs = parent;
                targetFetureSet = parent.getFetureSet().clone();
            }
            GeneticCodeTable table = p.getSelectedTable();
            int[] frames = p.getSelectedRelativeFrames();
            String seq = null;
            if (p.isEntireSeq()) {
                seq = molPane.getSequence();
            } else {
                seq = molPane.getSelectedSeqs().values().iterator().next();
            }
            IGeneticCodeTableService svc = Lookup.getDefault().lookup(IGeneticCodeTableService.class);
            List<TranslationResult> results = svc.translate(seq, table.getName(), frames);

            Folder selectedFolder = ExplorerTC.getInstance().getSelectedFolder();
            Collection<IFolderElement> asList = new ArrayList<IFolderElement>();

            Operation operation = new Operation();
            operation.setNameEnum(Operation.NAME.Extract_AA);
            String abPath = String.format("%s\\%s", selectedFolder.getAbsolutePath(), parent.getName());
            Operation.Participant part = new Operation.Participant(abPath);
            operation.addParticipant(part);
            
            for (TranslationResult r : results) {
                Integer frame = r.getFrame();

                FetureSet clone = targetFetureSet.clone();
                if(frame < 0){
                    clone.flip(targetAs.getLength());
                }                
                clone.translate(-(Math.abs(frame) - 1), parent.isCircular(), targetAs.getLength());
                
                FetureSet triplet = clone.toTriplet();

                AnnotatedSeq as = new AnnotatedSeq();
                as.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.AA);
                String proposedName = String.format("%s translation", parent.getName());
                proposedName = selectedFolder.getNewElementName(proposedName);
                as.setName(proposedName);
                as.setDesc(parent.getDesc());
                as.setAccession("");
                if(frame < 0){
                    as.setSequence(StrUtil.reverse(r.getData()));
                }else{                    
                    as.setSequence(r.getData());
                }
                as.setCircular(false);
                as.setFolder(selectedFolder);
                as.setLastModifiedDate(new Date());
                as.setFetureSet(triplet);
                as.setOperation(operation.clone());

                asList.add(as);
            }

            IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
            domainUtil.persist(asList);

            IFolderService folderSvc = Lookup.getDefault().lookup(IFolderService.class);
            Folder updatedFolder = folderSvc.loadWithDataAndChildren(selectedFolder.getHibernateId());
            ExplorerTC.getInstance().updateFolder(updatedFolder);
            BannerTC.getInstance().updateFolder(updatedFolder);

            String msg = String.format("%d %s created. Do you want to open %s?", asList.size(), asList.size() > 1 ? "proteins" : "protein", asList.size() > 1 ? "them" : "it");
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, TITLE);
            answer = DialogDisplayer.getDefault().notify(c);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
                Folder root = ExplorerTC.getInstance().getRootFolder();
                for (IFolderElement fe : asList) {
                    AnnotatedSeq as = asService.getFullByHibernateId(fe.getHibernateId(), root);
                    OpenASEditorAction a = new OpenASEditorAction(as);
                    a.actionPerformed(null);
                }
            }
        }
    }
}
