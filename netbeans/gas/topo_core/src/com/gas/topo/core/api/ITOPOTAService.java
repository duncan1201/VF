/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.api;

import com.gas.domain.core.as.AnnotatedSeq;
import java.util.List;

/**
 *
 * @author dq
 */
public interface ITOPOTAService {

    public static String INSERT_ONE_LINE_INSTRUCTION = "Please select a linear insert with 3' A overhangs at both ends";

    public enum STATE {

        VALID, NOT_LINEAR, NO_3_A_OVERHANGS, NO_CCCTT
    };

    List<AnnotatedSeq> ligate(final AnnotatedSeq insert, final AnnotatedSeq vector);

    <T> T isInsertValid(final AnnotatedSeq insert, Class<T> retType);

    /**
     * @return null if valid, error message otherwise
     */
    String isInsertValid(final AnnotatedSeq insert);

    /**
     * @return null if valid, error message otherwise
     */
    String isVectorValid(AnnotatedSeq vector);
    
    <T> T isVectorValid(AnnotatedSeq vector, Class<T> retType);
}
