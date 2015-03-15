/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.seqlogo.service.api;

import com.gas.domain.core.fasta.Fasta;
import com.gas.common.ui.light.StyledTextSetMap;
import com.gas.database.core.msa.service.api.CounterList;

/**
 *
 * @author dq
 */
public interface ISeqLogoService {
    HeightsList calculateHeights(Fasta fasta, boolean smallSampleCorrection);
    StyledTextSetMap createStyledTextListMap(CounterList cList, boolean dna, boolean smallSampleCorrection);
}
