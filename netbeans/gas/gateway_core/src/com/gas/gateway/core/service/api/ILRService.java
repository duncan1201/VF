/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import java.util.List;

/**
 *
 * @author dq
 */
public interface ILRService {
    AnnotatedSeq createExpressionClone(List<AnnotatedSeq> seqs);
    AnnotatedSeq createExpressionClone(AnnotatedSeqList entryClones, AnnotatedSeq dest, boolean includeOperatioin);
}
