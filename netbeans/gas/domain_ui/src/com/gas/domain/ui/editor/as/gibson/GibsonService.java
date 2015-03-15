/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.misc.CNST;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.primer3.UserInput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IGibsonService.class)
public class GibsonService implements IGibsonService {

    @Override
    public boolean checkErrors(List<AnnotatedSeq> seqs) {
        boolean ret = true;
        String title = "Isothermal Assembly";
        String msg = "";
        final String INST = "Please select two or more linearized fragments";
        if (seqs.size() < 2) {
            String error = "Less than two linearized fragments selected";
            msg = String.format(CNST.ERROR_FORMAT, error, INST);
            ret = false;
        } else {
            for (AnnotatedSeq seq : seqs) {
                if (seq.isCircular()) {
                    ret = false;
                    String error = String.format("%s is circular", seq.getName());
                    msg = String.format(CNST.ERROR_FORMAT, error, INST);
                    break;
                }
            }
        }

        if(!msg.isEmpty()){
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(title);
            DialogDisplayer.getDefault().notify(m);
        }
        return ret;
    }

    private AnnotatedSeq remove5OverhangsIfNecessary(AnnotatedSeq as) {
        Overhang overhangStart = as.getStartOverhang();
        if (overhangStart != null) {
            // chews back;
            if (overhangStart.isFivePrime()) {
                as = AsHelper.subAs(as, overhangStart.getLength() + 1, as.getLength());
            }
        }
        Overhang overhangEnd = as.getEndOverhang();
        if (overhangEnd != null) {
            // chews back;
            if (overhangEnd.isFivePrime()) {
                as = AsHelper.subAs(as, 1, as.getLength() - overhangEnd.getLength());
            }
        }
        as.clearOverhangs();
        return as;
    }

    @Override
    public String getFinalConstruct(List<AnnotatedSeq> seqs) {
        StringBuilder ret = new StringBuilder();
        for (AnnotatedSeq as : seqs) {
            as = remove5OverhangsIfNecessary(as);
            ret.append(as.getSiquence().getData());
        }
        return ret.toString();
    }

    @Override
    public AnnotatedSeq assembly(List<AnnotatedSeq> seqs) {
        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

        AnnotatedSeq ret = null;
        List<Feture> feturesParent = new ArrayList<Feture>();
        int lengthPrevTotal = 0;
        for (AnnotatedSeq as : seqs) {
            as = remove5OverhangsIfNecessary(as);
            if (ret == null) {
                ret = as.clone(AnnotatedSeq.ELEMENT.SEQ, AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.OVERHANG);
            } else {
                ret = AsHelper.concatenate(ret, as);
            }

            Feture feture = new Feture();
            feture.setKey(FetureKeyCnst.Parent);
            feture.getQualifierSet().add(String.format("sub_clone=%s", as.getName()));
            int startFeture = lengthPrevTotal + 1;
            int endFeture = startFeture + as.getLength() - 1;
            feture.setLucation(new Lucation(startFeture, endFeture, true));

            feturesParent.add(feture);

            lengthPrevTotal += as.getLength();
        }
        ret.getFetureSet().addAll(feturesParent);
        ret.setCircular(true);

        Operation ope = new Operation();
        ope.setNameEnum(Operation.NAME.Isothermal_Assembly);
        ope.setDate(new Date());

        for (AnnotatedSeq as : seqs) {
            Folder folder = folderService.loadWithParents(as.getFolder().getHibernateId());
            String absPath = String.format("%s\\%s", folder.getAbsolutePath(), as.getName());
            Operation.Participant part = new Operation.Participant(absPath);
            ope.addParticipant(part);
        }

        ret.setOperation(ope);
        return ret;
    }

    @Override
    public Oligo generateAnnealingPrimerPair(UserInput userInput, float minTmAnnealing, float maxTmAnnealing) {
        IPrimer3Service primer3Svc = Lookup.getDefault().lookup(IPrimer3Service.class);
        P3Output p3output = primer3Svc.execute(userInput, P3Output.class);
        Set<Oligo> oligos = p3output.getOligos();
        Oligo oligo = oligos.iterator().next();
        OligoElement oeLeft = oligo.getLeft();
        int i = 1;
        while (oeLeft.getTm() < minTmAnnealing) {
            Integer forceLeftEnd = oeLeft.getStart() + oeLeft.getLength() - 1 + i;
            Integer forceLeftStart = userInput.getInt("SEQUENCE_FORCE_LEFT_START");
            userInput.getData().put("SEQUENCE_FORCE_LEFT_END", forceLeftEnd.toString());
            if (Math.abs(forceLeftEnd - forceLeftStart) + 1 > IPrimer3Service.PRIMER_LENGTH_MAX) {
                break;
            }
            p3output = primer3Svc.execute(userInput, P3Output.class);
            oligos = p3output.getOligos();
            oligo = oligos.iterator().next();
            oeLeft = oligo.getLeft();
            i++;
        }

        OligoElement oeRight = oligo.getRight();
        i = 1;
        while (oeRight.getTm() < minTmAnnealing) {
            Integer forceRightEnd = oeRight.getStart() - oeRight.getLength() + 1 - i;
            userInput.getData().put("SEQUENCE_FORCE_RIGHT_END", forceRightEnd.toString());
            Integer start = userInput.getInt("SEQUENCE_FORCE_RIGHT_START");
            Integer end = userInput.getInt("SEQUENCE_FORCE_RIGHT_END");
            if (Math.abs(start - end) + 1 > IPrimer3Service.PRIMER_LENGTH_MAX) {
                break;
            }
            p3output = primer3Svc.execute(userInput, P3Output.class);
            oligos = p3output.getOligos();
            oligo = oligos.iterator().next();
            oeRight = oligo.getRight();
            i++;
        }

        oligo.setLeft(oeLeft);
        oligo.setRight(oeRight);
        return oligo;
    }
}
