/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service.api;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.msa.MSA;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IMSAUIService {
    void openDialog(List<AnnotatedSeq> input, List<MSA> profiles);    
    boolean validate(List<AnnotatedSeq> input, List<MSA> profiles);
}
