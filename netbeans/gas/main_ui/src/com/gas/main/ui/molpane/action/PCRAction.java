/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.PCRPanel;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class PCRAction extends AbstractAction {

    JComponent comp;
    public static final String TEXT = "Extract PCR Product...";
    private Integer no;

    public PCRAction(JComponent comp) {
        super(TEXT);
        this.comp = comp;
    }

    /**
     * @param no: 0-based
     */
    public void setNo(Integer no) {
        this.no = no;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MolPane molPane = UIUtil.getParent(comp, MolPane.class);
        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
        AnnotatedSeq as = molPane.getAs();
        P3Output p3output = as.getP3output();
        boolean valid = validate(p3output);
        if (!valid) {
            return;
        }
        final Folder folder = ExplorerTC.getInstance().getSelectedFolder();
        String nameSuggested = String.format("PCR Product-%s", as.getName());
        String nameProposed = folder.getNewElementName(nameSuggested);
        PCRPanel pcrPanel = new PCRPanel(p3output, nameProposed);
        if (no != null) {
            pcrPanel.setSelectedIndex(no);
        }
        DialogDescriptor dd = new DialogDescriptor(pcrPanel, "PCR");
        pcrPanel.setDialogDescriptor(dd);
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            String nameNew = pcrPanel.getNewName();
            OligoElement forwardPrimer = pcrPanel.getForwardPrimer();
            OligoElement reversePrimer = pcrPanel.getReversePrimer();
            final int start = forwardPrimer.calculateStart();
            final int end = reversePrimer.calculateEnd();
            AnnotatedSeq asNew = AsHelper.subAs(as, start, end, AnnotatedSeq.ELEMENT.DESC,
                    AnnotatedSeq.ELEMENT.FEATURE,
                    AnnotatedSeq.ELEMENT.OVERHANG,
                    AnnotatedSeq.ELEMENT.SEQ);
            ParentLoc parentLoc = new ParentLoc();
            parentLoc.setTotalPos(as.getLength());
            parentLoc.setStart(start);
            parentLoc.setEnd(end);
            parentLoc.setOffset(0);
            
            asNew.getParentLocSet().add(parentLoc);
            int forwardTailLength = 0;
            int reverseTailLength = 0;
            if (forwardPrimer.getTail() != null && forwardPrimer.getTail().length() > 0) {                
                forwardTailLength = forwardPrimer.getTail().length();
                if(forwardTailLength > 0){
                    asNew.getSiquence().replace(1, forwardTailLength, forwardPrimer.getTail());
                }
            }
            if (reversePrimer.getTail() != null && reversePrimer.getTail().length() > 0) {                
                reverseTailLength = reversePrimer.getTail().length();
                if(reverseTailLength > 0){
                    asNew.getSiquence().replace(asNew.getLength() - reverseTailLength + 1, asNew.getLength(), BioUtil.reverseComplement(reversePrimer.getTail()));
                }
            }
            
            asNew.getSiquence().replace(1 + forwardTailLength, forwardPrimer.getSeq().length() + forwardTailLength, forwardPrimer.getSeq());
            asNew.getSiquence().replace(asNew.getLength() - reversePrimer.getSeq().length() - reverseTailLength + 1, asNew.getLength() - reverseTailLength, BioUtil.reverseComplement(reversePrimer.getSeq()));
                        
            
            asNew.setName(nameNew);
            asNew.setDesc(String.format("PCR product of %s", as.getName()));

            //
            addOperation(asNew, folder, as);

            asNew.setFolder(folder);
            asService.persist(asNew);

            Folder folderUpdated = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
            BannerTC.getInstance().updateFolder(folderUpdated);
            ExplorerTC.getInstance().updateFolder(folderUpdated);
        }
    }

    private void addOperation(AnnotatedSeq asNew, Folder folder, AnnotatedSeq parent) {
        Operation op = new Operation();
        op.setNameEnum(Operation.NAME.PCR);
        op.setDate(new Date());
        String abPath = String.format("%s\\%s", folder.getAbsolutePath(), parent.getName());
        Operation.Participant part = new Operation.Participant(abPath);
        op.addParticipant(part);
        asNew.setOperation(op);
    }

    private boolean validate(P3Output p3output) {
        boolean ret = true;
        String title = "Cannot perform PCR";
        String msg = "";
        if (p3output == null) {
            return false;
        }
        if (!p3output.containsLeftOligoElement()) {
            ret = false;
            msg = String.format(CNST.MSG_FORMAT, "No forward primer found", "Please generate at least one forward primer");
        } else if (!p3output.containsRightOligoElement()) {
            ret = false;
            msg = String.format(CNST.MSG_FORMAT, "No reverse primer found", "Please generate at least one reverse primer");
        }
        if (!ret) {
            DialogDescriptor.Message d = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            d.setTitle(title);

            DialogDisplayer.getDefault().notify(d);
        }
        return ret;
    }
}
