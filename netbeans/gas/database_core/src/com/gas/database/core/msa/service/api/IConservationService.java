/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service.api;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.matrix.api.Matrix;
import com.gas.database.core.msa.service.api.CounterList;

/**
 *
 * @author dq
 */
public interface IConservationService {
    int[] calculate(CounterList counterList, Matrix matrix);
}
