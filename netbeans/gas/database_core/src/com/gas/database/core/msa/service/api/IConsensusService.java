/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service.api;

import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.msa.ConsensusParam;
import java.util.Map;

/**
 *
 * @author dq
 */
public interface IConsensusService {
    String calculate(Fasta fasta, ConsensusParam param);
    String calculate(Map<String, String> data, ConsensusParam param, Boolean isNucleotide);
}
