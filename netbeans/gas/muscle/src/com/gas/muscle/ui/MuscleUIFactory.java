/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.muscle.ui;

import com.gas.domain.core.msa.muscle.IMuscleUIFactory;
import com.gas.domain.core.msa.muscle.IMuscleUI;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IMuscleUIFactory.class)
public class MuscleUIFactory implements IMuscleUIFactory{
    @Override
    public IMuscleUI create(String profile1, String profile2){
        IMuscleUI muscleUI = new MuscleUI(profile1, profile2);
        return muscleUI;
    }
}
