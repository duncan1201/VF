/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

import com.gas.domain.core.as.AnnotatedSeq;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IOneClickService {
    AnnotatedSeq createExpressionClone(List<AnnotatedSeq> seqs);
}
