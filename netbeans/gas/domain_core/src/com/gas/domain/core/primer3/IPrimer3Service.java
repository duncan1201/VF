/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.primer3;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import java.io.File;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IPrimer3Service {
    public final static Integer PRIMER_LENGTH_MAX = 36;
    public final static Integer PRIMER_LENGTH_MIN = 1;
    <T> T execute(UserInput userInput, Class<T> clazz);
    <T> T checkPrimer(UserInput userInput, Class<T> retType);
    void checkPrimer(AnnotatedSeq oligo, Boolean strand);
    void setErrorFile(File file);
    void setFormatOutput(boolean formatOutput);
    AnnotatedSeq convertToOligo(AnnotatedSeq parent, int start, int end, Boolean forward);
    boolean isFormatOutput();
}