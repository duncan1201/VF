/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.topo.core.api.ITOPOTAService;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ITOPOTAService.class)
public class TOPOTAService implements ITOPOTAService {
          
    @Override
    public List<AnnotatedSeq> ligate(final AnnotatedSeq insert, final AnnotatedSeq vector) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();

        String insertValid = isInsertValid(insert);
        if (insertValid != null) {
            return ret;
        }

        final AnnotatedSeq vectorClone = vector.clone(AnnotatedSeq.ELEMENT.SEQ, AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.OVERHANG);
        final AnnotatedSeq insertClone = insert.clone(AnnotatedSeq.ELEMENT.SEQ, AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.OVERHANG);

        final String desc = String.format("a recombinant vector created from %s and %s", insert.getName(), vector.getName());
        final AnnotatedSeq clone1 = AsHelper.concatenate(vectorClone, insertClone);
        final AnnotatedSeq cirClone1 = AsHelper.circularize(clone1);
        cirClone1.setDesc(desc);
        annotate(cirClone1, vector, insertClone);
        ret.add(cirClone1);

        final AnnotatedSeq flipped = insertClone.flip();
        final AnnotatedSeq clone2 = AsHelper.concatenate(vectorClone, flipped);
        final AnnotatedSeq cirClone2 = AsHelper.circularize(clone2);
        cirClone2.setDesc(desc);
        annotate(cirClone2, vector, flipped);
        ret.add(cirClone2);

        return ret;
    }

    private void annotate(AnnotatedSeq clone, AnnotatedSeq vector, AnnotatedSeq insert) {
        Feture feture = new Feture();
        feture.setKey(FetureKeyCnst.Parent);
        feture.getQualifierSet().add(String.format("sub_clone=%s", vector.getName()));
        Lucation lucation = new Lucation(1, vector.getLength(), true);
        feture.setLucation(lucation);
        clone.getFetureSet().add(feture);

        feture = new Feture();
        feture.setKey(FetureKeyCnst.Parent);
        feture.getQualifierSet().add(String.format("sub_clone=%s", insert.getName()));
        lucation = new Lucation(vector.getLength(), 1, true);
        feture.setLucation(lucation);
        clone.getFetureSet().add(feture);
    }

    @Override
    public <T> T isInsertValid(AnnotatedSeq insert, Class<T> retType) {
        if(!retType.isAssignableFrom(String.class) && !retType.isAssignableFrom(ITOPOTAService.STATE.class)){
            throw new IllegalArgumentException(String.format("%s not supported", retType.getName()));
        }
        
        T ret = null;
        String msg;
        final Overhang startOverhang = insert.getStartOverhang();
        final Overhang endOverhang = insert.getEndOverhang();
        if (insert.isCircular()) {
            msg = String.format(CNST.ERROR_FORMAT, "The selected molecule is circular", INSERT_ONE_LINE_INSTRUCTION);
            if(retType.isAssignableFrom(String.class)){
                ret = (T)msg;
            }else{
                ret = (T)STATE.NOT_LINEAR;
            }
        } else if (startOverhang == null
                || !startOverhang.isThreePrime() && !insert.getOverhangSeq(startOverhang).equalsIgnoreCase("A")
                || endOverhang == null && !endOverhang.isThreePrime()
                && !insert.getOverhangSeq(endOverhang).equalsIgnoreCase("A")) {
            msg = String.format(CNST.ERROR_FORMAT, "The selected molecule has no 3' A overhangs at both ends", INSERT_ONE_LINE_INSTRUCTION);
            if(retType.isAssignableFrom(String.class)){
                ret = (T)msg;
            }else{
                ret = (T)STATE.NO_3_A_OVERHANGS;
            }
        } else{
            if(retType.isAssignableFrom(ITOPOTAService.STATE.class)){               
                ret = (T)STATE.VALID;
            }
        }
        return ret;
    }

    @Override
    public String isInsertValid(final AnnotatedSeq insert) {
        return isInsertValid(insert, String.class);
    }
    
    @Override
    public <T> T isVectorValid(AnnotatedSeq vector, Class<T> retType) {
        String instruction = "<html>The vector must be %slinearized%s with %ssequence 5'-CCCTT-3'%s and %s3' T overhangs%s at both ends</html>";
        String msg = null;
        T ret = null;
        Overhang startOverhang = vector.getStartOverhang();
        Overhang endOverhang = vector.getEndOverhang();
        String startSeq = vector.getSiquence().getData(1, 5);
        String endSeq = vector.getSiquence().getData(vector.getLength() - 4, vector.getLength());
        if (vector.isCircular()) {
            msg = String.format(instruction, "<b>", "</b>", "", "", "", "");
            if(String.class.isAssignableFrom(retType)){
                ret = (T)msg;
            }else if(ITOPOTAService.STATE.class.isAssignableFrom(retType)){
                ret = (T)ITOPOTAService.STATE.NOT_LINEAR;
            }
        } else if (startOverhang == null || endOverhang == null
                || !startOverhang.isThreePrime() || !endOverhang.isThreePrime()
                || !vector.getOverhangSeq(startOverhang).equalsIgnoreCase("t")
                || !vector.getOverhangSeq(endOverhang).equalsIgnoreCase("t")) {
            msg = String.format(instruction, "", "", "", "", "<b>", "</b>");
            if(String.class.isAssignableFrom(retType)){
                ret = (T)msg;
            }else if(ITOPOTAService.STATE.class.isAssignableFrom(retType)){
                ret = (T)ITOPOTAService.STATE.NO_3_A_OVERHANGS;
            }
        } else if (!containsCCCTT(startSeq) || !containsCCCTT(endSeq)) {
            msg = String.format(instruction, "", "", "<b>", "</b>", "", "");
            if(String.class.isAssignableFrom(retType)){
                ret = (T)msg;
            }else if(ITOPOTAService.STATE.class.isAssignableFrom(retType)){
                ret = (T)ITOPOTAService.STATE.NO_CCCTT;
            }
        }else{
            if(ITOPOTAService.STATE.class.isAssignableFrom(retType)){
                ret = (T)ITOPOTAService.STATE.VALID;
            }
        }
        return ret;
    }

    @Override
    public String isVectorValid(AnnotatedSeq vector) {
        String instruction = "<html>The vector must be %slinearized%s with %ssequence 5'-CCCTT-3'%s and %s3' T overhangs%s at both ends</html>";
        String ret = null;
        Overhang startOverhang = vector.getStartOverhang();
        Overhang endOverhang = vector.getEndOverhang();
        String startSeq = vector.getSiquence().getData(1, 5);
        String endSeq = vector.getSiquence().getData(vector.getLength() - 4, vector.getLength());
        if (vector.isCircular()) {
            ret = String.format(instruction, "<b>", "</b>", "", "", "", "");
        } else if (startOverhang == null || endOverhang == null
                || !startOverhang.isThreePrime() || !endOverhang.isThreePrime()
                || !vector.getOverhangSeq(startOverhang).equalsIgnoreCase("t")
                || !vector.getOverhangSeq(endOverhang).equalsIgnoreCase("t")) {
            ret = String.format(instruction, "", "", "", "", "<b>", "</b>");
        } else if (!containsCCCTT(startSeq) || !containsCCCTT(endSeq)) {
            ret = String.format(instruction, "", "", "<b>", "</b>", "", "");
        }
        return ret;
    }

    private boolean containsCCCTT(String seq) {
        return seq.equalsIgnoreCase("aaggg") || seq.equalsIgnoreCase("ccctt");
    }
}
