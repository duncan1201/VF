/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.coverage;

import com.gas.common.ui.core.FloatList;
import com.gas.database.core.msa.service.api.CounterList;
import com.gas.msa.coverage.api.ICoverageService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=ICoverageService.class)
public class CoverageService implements ICoverageService {
    public FloatList getCoverage(CounterList cList){        
        return cList.getLetterFrequencies();
    }
}
