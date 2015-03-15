/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.UserInput;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IGibsonService {
    public enum STATE {VALID};
    String getFinalConstruct(List<AnnotatedSeq> seqs);
    AnnotatedSeq assembly(List<AnnotatedSeq> seqs);
    Oligo generateAnnealingPrimerPair(UserInput userInput, float minTmAnnealing, float maxTmAnnealing);
    boolean checkErrors(List<AnnotatedSeq> seqs);
}
