/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.primer3.P3OutputHelper;
import com.gas.domain.core.primer3.UserInput;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.main.ui.molpane.Convert2OligoPanel;
import com.gas.main.ui.molpane.MolPane;
import com.gas.primer3.core.input.UserInputFactory;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */

    public class Convert2OligoAction extends AbstractAction {

        public static String TEXT = "Convert to Oligo";
        JComponent comp;
        //Feture feture;
        Integer no;
        String which;

        public Convert2OligoAction(JComponent comp) {
            super(TEXT + "...");
            this.comp = comp;
        }
  
        /**
         * @param no: 0-based
         */
        public void setNo(Integer no) {
            this.no = no;
        }

        /**
         * @param which: possible values: left, right, internal
         */
        public void setWhich(String which) {
            this.which = which;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MolPane molPane = UIUtil.getParent(comp, MolPane.class);

            boolean valid = validate();
            if (!valid) {
                return;
            }

            IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
            IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

            final AnnotatedSeq parentAs = molPane.getAs();

            AnnotatedSeq ret;
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            if (no != null && which != null) {
                ret = createOligo(parentAs);
                copyAnnotation(ret);
            } else {
                UserInput userInput = UserInputFactory.getP3WEB_V_3_0_0(true);
                Convert2OligoPanel p = new Convert2OligoPanel(userInput);
                userInput = displayTmCalculationSettings(p);
                if (userInput == null) {
                    return;
                }
                IPrimer3Service primer3Service = Lookup.getDefault().lookup(IPrimer3Service.class);
                Loc selection = molPane.getSelections().get(0);
                int start = selection.getStart();
                int end = selection.getEnd();
                Boolean forward = p.isForward() || p.isProbe() ? true : false ;
                ret = primer3Service.convertToOligo(parentAs, start, end, forward);
                String proposedName = String.format("%s extraction", parentAs.getName());
                setName(ret, proposedName);
            }

            addOperation(ret, folder, parentAs);
            asService.persist(ret);
            Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());

            BannerTC.getInstance().updateFolder(updatedFolder);
            ExplorerTC.getInstance().updateFolder(updatedFolder);
        }

        private UserInput displayTmCalculationSettings(Convert2OligoPanel p) {
            DialogDescriptor dd = new DialogDescriptor(p, TEXT);
            Object answer = DialogDisplayer.getDefault().notify(dd);
            if (!answer.equals(DialogDescriptor.OK_OPTION)) {
                return null;
            }
            UserInput userInput = p.getUserInputFromUI();

            return userInput;
        }    

        private AnnotatedSeq createOligo(AnnotatedSeq as) {
            OligoElement oe = as.getP3output().getOligoElement(no, Oligo.WHICH.get(which));
            MolPane molPane = UIUtil.getParent(comp, MolPane.class);

            Loc selection = molPane.getSelections().get(0);
            int start = selection.getStart();
            int end = selection.getEnd();
            AnnotatedSeq ret = AsHelper.subAs(as, start, end, AnnotatedSeq.ELEMENT.DESC,
                    AnnotatedSeq.ELEMENT.FEATURE,
                    AnnotatedSeq.ELEMENT.OVERHANG,
                    AnnotatedSeq.ELEMENT.SEQ);
            
            if(oe.getTail() != null && !oe.getTail().isEmpty()){
                if(oe.getForward()){
                    ret.getSiquence().replace(1, oe.getTail().length(), oe.getTail());
                }else{
                    String tailSeq = BioUtil.reverseComplement(oe.getTail());
                    ret.getSiquence().replace(ret.getLength() - oe.getTail().length() + 1, ret.getLength(), tailSeq);
                }
            }
            
            ret.setOligo(true);
            ret.setP3output(null);
            ret.getFetureSet().clear();

            ParentLoc parentLoc = new ParentLoc();
            parentLoc.setStart(start);
            parentLoc.setEnd(end);
            parentLoc.setTotalPos(as.getLength());
            parentLoc.setOffset(0);
            ret.getParentLocSet().clear();
            ret.getParentLocSet().add(parentLoc);

            return ret;
        }

        private void copyAnnotation(AnnotatedSeq ret) {
            if (no == null || which == null) {
                throw new IllegalArgumentException();
            }
            MolPane molPane = UIUtil.getParent(comp, MolPane.class);

            final AnnotatedSeq as = molPane.getAs();

            P3Output p3output = as.getP3output();
            Oligo oligo = p3output.getOligoByNo(no);
            OligoElement oe = oligo.get(which);

            Feture feture = P3OutputHelper.toFeture(oe, oligo);
            feture.getLucation().setContiguousMin(1);
            feture.getLucation().setContiguousMax(ret.getLength());
            ret.getFetureSet().add(feture);


            String proposedName = String.format("%s-%s", feture.getDisplayName(), as.getName());
            setName(ret, proposedName);

        }

        private void setName(AnnotatedSeq oligo, String proposedName) {
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            String newName = folder.getNewElementName(proposedName);
            oligo.setName(newName);
            oligo.setFolder(folder);
        }

        private boolean validate() {
            MolPane molPane = UIUtil.getParent(comp, MolPane.class);

            LocList locList = molPane.getSelections();
            if (locList.isEmpty()) {
                DialogDescriptor.Message m = new DialogDescriptor.Message(String.format("Please select 1 to %d bases to convert to oligo", IPrimer3Service.PRIMER_LENGTH_MAX), DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TEXT);
                DialogDisplayer.getDefault().notify(m);
                return false;
            } else {
                Loc loc = locList.get(0);
                if (loc.width() > IPrimer3Service.PRIMER_LENGTH_MAX) {
                    DialogDescriptor.Message m = new DialogDescriptor.Message(String.format("The oligo length cannot be more than %d bp", IPrimer3Service.PRIMER_LENGTH_MAX), DialogDescriptor.INFORMATION_MESSAGE);
                    m.setTitle(TEXT);
                    DialogDisplayer.getDefault().notify(m);
                    return false;
                }
            }
            return true;
        }

        private void addOperation(AnnotatedSeq asNew, Folder folder, AnnotatedSeq parent) {
            Operation op = new Operation();
            op.setNameEnum(Operation.NAME.Convert2Oligo);

            String aPath = String.format("%s\\%s", folder.getAbsolutePath(), parent.getName());
            Operation.Participant p = new Operation.Participant(aPath);
            op.addParticipant(p);

            asNew.setOperation(op);
        }
    }
