/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.consensus.service;

import com.gas.common.ui.core.CharList;
import com.gas.common.ui.util.BioUtil;
import com.gas.database.core.msa.service.api.IConsensusService;
import com.gas.domain.core.fasta.Fasta;
import com.gas.database.core.msa.service.api.Counter;
import com.gas.database.core.msa.service.api.CounterList;
import com.gas.database.core.msa.service.api.ICounterListService;
import com.gas.domain.core.msa.ConsensusParam;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IConsensusService.class)
public class ConsensusService implements IConsensusService {

    private ICounterListService service = Lookup.getDefault().lookup(ICounterListService.class);

    @Override
    public String calculate(Fasta fasta, ConsensusParam param) {
        CounterList cList = service.createCounterList(fasta);
        boolean dna = fasta.isDNAByGuess();
        String ret = _calculate(cList, param, dna);
        return ret;
    }

    @Override
    public String calculate(Map<String, String> data, ConsensusParam param, Boolean isNucleotide) {
        CounterList cList = service.createCounterList(data);
        String ret = _calculate(cList, param, isNucleotide);
        return ret;
    }

    private String _calculate(CounterList cList, ConsensusParam param, boolean isDNA) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < cList.size(); i++) {
            Counter c = cList.get(i);
            CharList modes ;
            if(param.isIgnoreGaps()){
                modes = c.getModes(true);
            }else{
                modes = c.getModes(false);
            }
            if (modes.isEmpty()) {
                ret.append(isDNA ? 'N' : 'X');
                continue;
            }
            float threshold = param.getThreshold();
            boolean ignoreGaps = param.isIgnoreGaps();
            boolean useCommon = param.isPlurality();
            if (useCommon) {                
                if (modes.size() == 1) {
                    ret.append(modes.get(0));
                } else if (modes.size() > 1) { // get the consensus here
                    if (isDNA) {
                        Character am = BioUtil.getAmbiguity(modes);
                        ret.append(am);
                    } else {
                        ret.append('X');
                    }
                }
            } else { // by threshold
                double f;
                if (ignoreGaps) {
                    f = c.getFrequency(modes.get(0), BioUtil.GAP);
                } else {
                    f = c.getFrequency(modes.get(0));
                }
                if (f < threshold) {
                    ret.append(isDNA ? 'N' : 'X');
                } else {
                    if (modes.size() == 1) {
                        if (f >= threshold) {
                            ret.append(modes.get(0));
                        }
                    } else if (modes.size() > 1) {
                        if (isDNA) {
                            Character am = BioUtil.getAmbiguity(modes);
                            ret.append(am);
                        } else {
                            ret.append('X');
                        }
                    }
                }
            }
        }
        return ret.toString();
    }
}
