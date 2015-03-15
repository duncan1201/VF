/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

/**
 *
 * @author dq
 */
public interface IBasicTmService {
    Float simplest(String seq);
    OverlapSeqList pickPrimers(Float minTm, Integer minLength, OverlapSeqList seqs);
}
