/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.api;

import com.gas.domain.core.as.AnnotatedSeq;
import java.util.List;

/**
 *
 * @author dq
 */
public interface ILigateService {

    AnnotatedSeq ligate(List<AnnotatedSeq> list, boolean circularize);
}
