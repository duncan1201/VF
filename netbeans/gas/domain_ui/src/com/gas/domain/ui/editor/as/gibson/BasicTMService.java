/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IBasicTmService.class)
public class BasicTMService implements IBasicTmService {

    /**
     * Tm = 4°C X (num of C/Gs ) + 2°C X (num of A/Ts)
     */
    @Override
    public Float simplest(String seq) {
        Float ret = 0f;
        int atCount = 0;
        int gcCount = 0;
        for (int i = 0; i < seq.length(); i++) {
            char c = seq.charAt(i);
            if (c == 'A' || c == 'T' || c == 'a' || c == 't') {
                ret += 2;
                atCount++;
            } else if (c == 'C' || c == 'G' || c == 'c' || c == 'g') {
                ret += 4;
                gcCount++;
            }
        }
        ret = gcCount * 4f + atCount * 2f;
        return ret;
    }
    
    @Override
    public OverlapSeqList pickPrimers(Float minTm, Integer minLength, OverlapSeqList seqs){
        OverlapSeqList ret = new OverlapSeqList();        
        for(OverlapSeq seq: seqs){
            Float tm = simplest(seq.getSeq());
            if(tm >= minTm && seq.length() >= minLength){
                ret.add(seq);
            }
        }
        return ret;
    }
}