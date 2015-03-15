/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service.api;

import com.gas.domain.core.msa.Apr;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.MSAList;
import com.gas.domain.core.nexus.api.Nexus;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dq
 */
public interface IMSAService {
    MSA toMSA(Apr apr);
    MSA toMSA(Nexus nexus);
    MSA toMSA(Map<String, String> data);
    MSAList getFull(List<MSA> msas);
    MSAList getByHibernateIds(String... hIds);
    MSA merge(MSA msa);
    void mergeSetting(MSA msa);
    void persist(MSA msa);
    void createConsensus(MSA msa);
    void createQualityScores(MSA msa);
}
