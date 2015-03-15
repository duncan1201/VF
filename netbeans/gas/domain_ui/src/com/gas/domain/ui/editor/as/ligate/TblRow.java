/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.BioUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.ui.editor.as.ligate.LigateTblMdl.MOD;
import static com.gas.domain.ui.editor.as.ligate.TblRowList.getPartialFillInSeq;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
class TblRow {

    AnnotatedSeq as;
    MOD mod;
    boolean startCompatible;
    boolean endCompatible;
    List<LigateTblMdl.dNTP> dNTPList = new ArrayList<LigateTblMdl.dNTP>();

    TblRow(AnnotatedSeq as) {
        this.as = as;
        mod = MOD.none;
    }   
    
    /**
     * @param start: true means START overhang, false means the END overhang
     * @return true if five prime; false if three prime; null if no overhang
     */
    Boolean getModifiedFivePrime(boolean start){
        Boolean ret = null;
        String ohStr = getModifiedOverhang(start);
        if(ohStr != null && !ohStr.isEmpty()){
            Overhang oh = null;
            if(start){
                oh = AsHelper.getStartOverhang(as);
            }else{
                oh = AsHelper.getEndOverhang(as);
            }
            ret = oh.isFivePrime();
        }
        return ret;
    }

    /**
     * @param start: true means START overhang, false means the END overhang
     * @return 5' to 3'; empty string if no overhang
     */
    String getModifiedOverhang(boolean start) {
        String ret = null;
        Overhang overhang;
        if (start) {
            overhang = AsHelper.getStartOverhang(as);
        } else {
            overhang = AsHelper.getEndOverhang(as);
        }
        if (overhang == null) {
            ret = "";
        } else {
            String seq = AsHelper.getOverhangSeq(as, overhang);
            if (mod == MOD.none) {
                ret = seq;
            } else if (mod == MOD.klenowFillIn || mod == MOD.exo3to5 || mod == MOD.exo5to3) {
                ret = "";
            } else if (mod == MOD.partialFillIn) {
                ret = getModifiedOverhang(seq, dNTPList);
            }
        }
        return ret;
    }

    /**
     * @param overhangSeq: 5'-3'
     * @return modified overhang 5'-3'
     */
    String getModifiedOverhang(String overhangSeq, List<LigateTblMdl.dNTP> dNTPList) {
        final String seqComp = BioUtil.reverseComplement(overhangSeq);
        int length = TblRowList.getPartialFillInSeq(seqComp, dNTPList).length();

        return overhangSeq.substring(0, overhangSeq.length() - length);
    }
}
