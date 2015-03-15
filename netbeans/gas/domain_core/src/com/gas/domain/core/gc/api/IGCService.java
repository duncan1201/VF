/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.gc.api;

import com.gas.common.ui.core.FloatList;

/**
 *
 * @author dq
 */
public interface IGCService {

    FloatList calculateGC(String nucleotides, int windowSize, boolean circular);

    GCResult calculate(String nucleotides, int windowSize, boolean circular);
}
