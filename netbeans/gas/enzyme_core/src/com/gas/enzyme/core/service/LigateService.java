/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.service;

import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.api.ILigateService;
import com.gas.domain.core.as.*;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ILigateService.class)
public class LigateService implements ILigateService {

    /**
     * Only AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.SEQ,
     * AnnotatedSeq.ELEMENT.OVERHANG are kept.
     */
    @Override
    public AnnotatedSeq ligate(List<AnnotatedSeq> list, boolean circularize) {
        validate(list.toArray(new AnnotatedSeq[list.size()]));
        AnnotatedSeq ret = null;

        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

        Operation operation = new Operation();
        operation.setNameEnum(Operation.NAME.Ligation);
        operation.setDate(new Date());

        for (int i = 0; i < list.size(); i++) {
            if (ret == null) {
                AnnotatedSeq as = list.get(i);
                ret = as.clone(AnnotatedSeq.ELEMENT.SEQ, AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.OVERHANG);
                ret.setHibernateId(as.getHibernateId());
            } else {
                ret = ligate(ret, list.get(i));
            }
            final String folderPath = folderService.loadWithParents(list.get(i).getFolder().getHibernateId()).getAbsolutePath();
            final String fullPath = folderPath + '\\' + list.get(i).getName();
            Operation.Participant participant = new Operation.Participant(fullPath, true);
            operation.addParticipant(participant);
        }
        if (circularize) {
            ret = AsHelper.circularize(ret);
        }

        ret.setOperation(operation);
        ret.setDesc(String.format("Concatenation of %d sequences", list.size()));
        ret.setDivision(String.format("Concatenation of %d sequences", list.size()));
        ret.setCreationDate(new Date());
        ret.setLastModifiedDate(new Date());
        ret.getParentLocSet().clear();
        annotate(ret, list);
        return ret;
    }

    private void annotate(AnnotatedSeq ret, List<AnnotatedSeq> list) {
        boolean circular = ret.isCircular();
        int totalLength = 0;
        int totalOverhangLength = 0;
        ListIterator<AnnotatedSeq> itr = list.listIterator();
        while (itr.hasNext()) {
            AnnotatedSeq as = itr.next();
            Overhang endOverhang = as.getEndOverhang();

            boolean lastOne = !itr.hasNext();

            Feture feture = new Feture();
            feture.setKey(FetureKeyCnst.Parent);
            feture.getQualifierSet().add(String.format("sub_clone=%s", as.getName()));
            int startPos = 1 + totalLength - totalOverhangLength;
            Lucation lucation;
            if (circular && lastOne && endOverhang != null) {
                lucation = new Lucation(startPos, endOverhang.getLength(), true);
            } else {
                lucation = new Lucation(startPos, startPos + as.getLength() - 1, true);
            }
            feture.setLucation(lucation);

            ret.getFetureSet().add(feture);

            // ruler
            int offset = totalLength - totalOverhangLength;

            ParentLoc loc = new ParentLoc(1, as.getLength(), offset);
            loc.setTotalPos(as.getLength());
            ret.getParentLocSet().add(loc);

            totalLength += as.getLength();
            totalOverhangLength += endOverhang == null ? 0 : endOverhang.getLength();

        }
    }

    private void validate(AnnotatedSeq... list) {
        if (list.length < 2) {
            throw new IllegalArgumentException("Must have at least 2 sequences to ligate");
        }
        boolean ret = true;
        AnnotatedSeq cur = null;
        AnnotatedSeq next = null;
        for (int i = 0; i < list.length; i++) {
            cur = list[i];
            if (i + 1 < list.length) {
                next = list[i + 1];
                ret = cur.ligatable(next);
                if (!ret) {
                    throw new IllegalArgumentException(String.format("Incompatible overhangs: %s, %s", cur.getName(), next.getName()));
                }
            }
        }
    }

    private AnnotatedSeq ligate(final AnnotatedSeq one, AnnotatedSeq another) {
        AnnotatedSeq ret = AsHelper.concatenate(one, another);
        return ret;
    }
}
