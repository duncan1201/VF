/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.BioUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
class TblRowList extends ArrayList<TblRow> {

    boolean circularize;

    public void updateCompatibilities() {
        for (int i = 0; i < size(); i++) {
            get(i).startCompatible = true;
            get(i).endCompatible = true;
        }
        for (int i = 0; i < size(); i++) {
            TblRow curRow = get(i);

            TblRow nextRow;
            if (i == size() - 1) {
                nextRow = get(0);
            } else {
                nextRow = get(i + 1);
            }
            if (i + 1 <= size() - 1) {
                get(i).endCompatible = ligatable(curRow, nextRow);
                get(i + 1).startCompatible = ligatable(curRow, nextRow);
            } else {
                if (circularize) {
                    get(size() - 1).endCompatible = ligatable(curRow, nextRow);
                    get(0).startCompatible = ligatable(curRow, nextRow);
                } else {
                    get(size() - 1).endCompatible = true;
                    get(0).startCompatible = true;
                }
            }
        }
    }
    
    boolean compatible(){
        boolean ret = true;
        for(int i = 0; i < size(); i++){
            TblRow tblRow = get(i);
            ret = ret && tblRow.endCompatible && tblRow.startCompatible;
            if(!ret){
                break;
            }
        }
        return ret;
    }

    /**
     * @param gaps: downstream(5'-3') direction
     * @return the fill-ins(3'-5')
     */
    static String getPartialFillInSeq(String gaps, List<LigateTblMdl.dNTP> dNTPList) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < gaps.length(); i++) {
            String gap = gaps.substring(i, i + 1);
            if (gap.equalsIgnoreCase("A") && dNTPList.contains(LigateTblMdl.dNTP.dATP)) {
                ret.append("A");
            } else if (gap.equalsIgnoreCase("T") && dNTPList.contains(LigateTblMdl.dNTP.dTTP)) {
                ret.append("T");
            } else if (gap.equalsIgnoreCase("C") && dNTPList.contains(LigateTblMdl.dNTP.dCTP)) {
                ret.append("C");
            } else if (gap.equalsIgnoreCase("G") && dNTPList.contains(LigateTblMdl.dNTP.dGTP)) {
                ret.append("G");
            } else {
                break;
            }
        }

        return ret.toString();
    }

    private boolean ligatable(TblRow r1, TblRow r2) {        
        String oh1 = r1.getModifiedOverhang(false);
        String oh2 = r2.getModifiedOverhang(true);
        String revComp_oh2 = BioUtil.reverseComplement(oh2);
        if(oh1.isEmpty() && oh2.isEmpty()){
            return true;
        }else if(oh1.isEmpty() || oh2.isEmpty()){
            return false;
        }else {// if(!oh1.isEmpty() && !oh2.isEmpty())
            Boolean s1 = r1.getModifiedFivePrime(false);
            Boolean s2 = r2.getModifiedFivePrime(true);
            return oh1.equalsIgnoreCase(revComp_oh2) && s1.equals(s2);
        }        
    }
}
