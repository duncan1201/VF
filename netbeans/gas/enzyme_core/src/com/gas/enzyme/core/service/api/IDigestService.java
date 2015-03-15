/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.service.api;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.ren.RMap;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IDigestService {

    List<AnnotatedSeq> digest(AnnotatedSeq as);

    List<AnnotatedSeq> digestSeparately(AnnotatedSeq as);

    List<AnnotatedSeq> digest(AnnotatedSeq as, Collection<RMap.Entry> collection);

}
