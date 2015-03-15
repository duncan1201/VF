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
public interface IBluntTOPOService {
    
    public enum STATE{VALID, NOT_LINEAR, NOT_BLUNT_ENDED, NO_CCCTT};
    
    <T> T isVectorValid(final AnnotatedSeq vector, Class<T> retType);
    
    /**
     * @return null if valid, otherwise the error message
     */
    String isVectorValid(final AnnotatedSeq vector);
    /**
     * @return null if valid, otherwise the error message
     */
    String isInsertValid(final AnnotatedSeq insert);
    
    <T> T isInsertValid(final AnnotatedSeq insert, Class<T> retType);
    
    List<AnnotatedSeq> clone(final AnnotatedSeq insert, final AnnotatedSeq vector);
}
