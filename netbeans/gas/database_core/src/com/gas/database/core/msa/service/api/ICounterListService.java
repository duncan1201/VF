/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service.api;

import com.gas.domain.core.fasta.Fasta;
import java.util.Map;

/**
 *
 * @author dq
 */
public interface ICounterListService {
    /**
     * returns the occurrence of each base, including gaps
     */    
    CounterList createCounterList(Fasta fasta);
    /**
     * returns the occurrence of each base, including gaps
     */
    CounterList createCounterList(Map<String,String> map);
}
