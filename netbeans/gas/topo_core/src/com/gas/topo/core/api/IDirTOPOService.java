/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.api;

import com.gas.domain.core.as.AnnotatedSeq;

/**
 *
 * @author dq
 */
public interface IDirTOPOService {
    
    public enum STATE{INVALID, VALID, NOT_BLUNT_ENDED, NO_OVERHANG, NO_VALID_OVERHANG, NO_CCCTT, TOO_MANY_OVERHANGSs, GT_75_END, NO_CACC_END, TOO_MANY_CACC_ENDS, NOT_LINEAR};
    
    AnnotatedSeq ligate(final AnnotatedSeq insert, final AnnotatedSeq vector);
    STATE isInsertValid(AnnotatedSeq insert);    
    STATE isVectorValid(AnnotatedSeq vector);    
}
