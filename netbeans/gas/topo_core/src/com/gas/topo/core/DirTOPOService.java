/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core;

import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.topo.core.api.IDirTOPOService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IDirTOPOService.class)
public class DirTOPOService implements IDirTOPOService {

    @Override
    public AnnotatedSeq ligate(final AnnotatedSeq insert, final AnnotatedSeq vector) {
        Overhang startOverhang = vector.getStartOverhang();
        Overhang endOverhang = vector.getEndOverhang();
        Overhang vectorOverhang = startOverhang != null ? startOverhang : endOverhang;

        Overhang overhang = vectorOverhang.clone();
        overhang.flip();

        final AnnotatedSeq insertClone = insert.clone(AnnotatedSeq.ELEMENT.SEQ, AnnotatedSeq.ELEMENT.FEATURE);
        insertClone.addOverhang(overhang);
  
        AnnotatedSeq ret = AsHelper.concatenate(vector, insertClone);
        ret = AsHelper.circularize(ret);
        annotate(ret, vector, insertClone);
        return ret;
    }
    
    private void annotate(AnnotatedSeq clone, AnnotatedSeq vector, AnnotatedSeq insert){
        Feture feture = new Feture();
        feture.setKey(FetureKeyCnst.Parent);
        feture.getQualifierSet().add(String.format("sub_clone=%s", vector.getName()));
        Lucation lucation = new Lucation(1, vector.getLength(), true);
        feture.setLucation(lucation);
        clone.getFetureSet().add(feture);
        
        feture = new Feture();
        feture.setKey(FetureKeyCnst.Parent);
        feture.getQualifierSet().add(String.format("sub_clone=%s", insert.getName()));
        lucation = new Lucation(vector.getLength() - 3, insert.getLength() + vector.getLength() - 4, true);
        feture.setLucation(lucation);
        clone.getFetureSet().add(feture);
    }
    
    @Override
    public STATE isInsertValid(AnnotatedSeq insert) {
        STATE ret = STATE.INVALID;
        Overhang startOverhang = insert.getStartOverhang();
        Overhang endOverhang = insert.getEndOverhang();
        if (startOverhang == null && endOverhang == null) {

            final String startSeq = insert.getSiquence().getData(1, 4);
            final String compEndSeq = insert.getSiquence().getData(insert.getLength() - 3, insert.getLength(), true);
              
            float similarity = StrUtil.getSimilarity(startSeq, compEndSeq);
            
            if(insert.isCircular()){
                ret = STATE.NOT_LINEAR;
            } else if (startSeq.equalsIgnoreCase("cacc") && compEndSeq.equalsIgnoreCase("cacc")) {
                ret = STATE.TOO_MANY_CACC_ENDS;
            } else if(startSeq.equalsIgnoreCase("cacc") && similarity >= 0.75){
                ret = STATE.GT_75_END;
            } else if (startSeq.equalsIgnoreCase("cacc")
                    || compEndSeq.equalsIgnoreCase("cacc")) {
                ret = STATE.VALID;
            } else {
                ret = STATE.NO_CACC_END;
            }
        }else{
            ret = STATE.NOT_BLUNT_ENDED;
        }
        return ret;
    }

    @Override
    public STATE isVectorValid(AnnotatedSeq vector) {
        STATE ret = STATE.VALID;
        Overhang startOverhang = vector.getStartOverhang();
        Overhang endOverhang = vector.getEndOverhang();
        if (vector.isCircular()) {            
            ret = STATE.NOT_LINEAR;
        } else if (startOverhang != null && endOverhang != null) {         
            ret = STATE.TOO_MANY_OVERHANGSs;
        } else if (startOverhang != null || endOverhang != null) {
            final String overhangStart = vector.getOverhangSeq(startOverhang);
            final String overhangEnd = vector.getOverhangSeq(endOverhang);
            if(!overhangStart.equalsIgnoreCase("GGTG") && !overhangEnd.equalsIgnoreCase("GGTG")){             
                ret = STATE.NO_VALID_OVERHANG;
            }else{
                String seqStart = null;
                String seqEnd = null;
                if(startOverhang != null){
                    seqStart = vector.getSiquence().getData(5, 9);
                    seqEnd = vector.getSiquence().getData(vector.getLength() - 4, vector.getLength());
                }else if(endOverhang != null){
                    seqStart = vector.getSiquence().getData(1, 5);
                    seqEnd = vector.getSiquence().getData(vector.getLength() - 4 - 4, vector.getLength() - 4);
                }
                if(!isCCCTT(seqStart) || !isCCCTT(seqEnd)){
                    ret = STATE.NO_CCCTT;
                }
            }
        } else if (startOverhang == null && endOverhang == null) {
            ret = STATE.NO_OVERHANG;
        }
        return ret;
    }
    
    private boolean isCCCTT(String seq){
        return seq != null && seq.equalsIgnoreCase("CCCTT") || seq.equalsIgnoreCase("AAGGG");
    }
}
