/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.muscle.service.api;

import com.gas.domain.core.msa.muscle.MuscleParam;
import com.gas.domain.core.msa.MSA;

/**
 *
 * @author dq
 */
public interface IMuscleService {

    MSA align(MuscleParam params);   
    boolean validate(MuscleParam params);
    String getExecutablePath();
}
