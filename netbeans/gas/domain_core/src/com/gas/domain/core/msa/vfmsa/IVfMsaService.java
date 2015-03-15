package com.gas.domain.core.msa.vfmsa;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.msa.MSA;
import java.util.List;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author dq
 */
public interface IVfMsaService {
    MSA getMSA(Map<String, String> seqs, VfMsaParam param, boolean isAminoAcid);
    MSA getMSA(List<AnnotatedSeq> seqs, VfMsaParam param);
}
