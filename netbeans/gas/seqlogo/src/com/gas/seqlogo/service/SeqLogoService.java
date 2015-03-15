/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.seqlogo.service;

import com.gas.seqlogo.service.api.ISeqLogoService;
import com.gas.domain.core.fasta.Fasta;
import com.gas.seqlogo.service.api.Heights;
import com.gas.seqlogo.service.api.HeightsList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ISeqLogoService.class)
public class SeqLogoService implements ISeqLogoService {

    private final String[] DNA_BASES = {"A", "T", "C", "G", "U"};
    private final String[] AMINO_ACIDS = {"A", "C", "D", "E", "F", "G", "H", "I", "K", "L",
        "M", "N", "P", "Q", "R", "S", "T", "V", "W", "Y"};
    private int numberOfSequences = 0;   
    
    public HeightsList calculateHeights(Fasta fasta) {
        boolean isDNA = fasta.isDNAByGuess();
        return _calculateHeights(fasta.toMap(), isDNA);
    }
    
    private HeightsList _calculateHeights(Map<String, String> _data, boolean dna) {
        numberOfSequences = _data.size();
        HeightsList ret = new HeightsList();
        ret.setProtein(!dna);
        List<String> records = new ArrayList<String>(_data.values());
        if (!records.isEmpty()) {
            int length = records.get(0).length();
            Counter counter = new Counter();
            for (int i = 0; i < length; i++) {
                counter.clear();
                counter.setPos(i + 1);
                for (int recordIndex = 0; recordIndex < records.size(); recordIndex++) {
                    String base = records.get(recordIndex).substring(i, i + 1);

                    counter.increaseCount(base);
                }
                Heights height = calculateHeightsAtPos(counter, dna);
                ret.add(height);
            }
        }
        return ret;
    }

    // height = f(b,l) * R(l)
    private Heights calculateHeightsAtPos(Counter c, boolean isDNA) {
        Heights ret = new Heights();
        ret.setPos(c.getPos());
        String[] bases = null;
        if (isDNA) {
            bases = DNA_BASES;
        } else {
            bases = AMINO_ACIDS;
        }
        double r = c.r(isDNA);
        for (int i = 0; i < bases.length; i++) {
            String base = bases[i];
            double f = c.getFrequency(base);
            if(f > 0){
                ret.put(base, f * r);                
            }
        }
        return ret;
    }

    /*
     * e(n) = (s-1) / (2 * ln2 * n) (4) Where s is 4 for nucleotides, 20 for
     * amino acids ; n is the number of sequences in the alignment. e(n) also
     * gives the height of error bars.
     */
    private double e(boolean isDNA, int n) {
        double ret = 0;
        int s = 0;
        if (isDNA) {
            s = 4;
        } else {
            s = 20;
        }
        ret = (s - 1) / (2 * Math.log(2) * n);
        return ret;
    }

    private class Counter extends HashMap<String, Integer> {

        int totalCount = 0;
        private Integer pos ;

        public void increaseCount(String base) {
            String b = base.toUpperCase();
            if (!containsKey(b)) {
                put(b, 1);
            } else {
                Integer old = get(b);
                put(b, old + 1);
            }
            totalCount++;
        }

        public Integer getPos() {
            return pos;
        }

        public void setPos(Integer pos) {
            this.pos = pos;
        }
        
        @Override
        public void clear() {
            super.clear();
            totalCount = 0;
            pos = null;
        }

        //R(l) for amino acids   = log(20) - (H(l) + e(n))    (2a)
        //R(l) for nucleic acids =    2    - (H(l) + e(n))    (2b)
        private double r(boolean dna){
            double ret = 0;
            if(dna){
                ret = 2;
            }else{
                ret = Math.log(20)/Math.log(2);                
            }
            double h = h(dna);
            double e = e(dna, numberOfSequences);
            ret = ret - (h + e);
            return ret;
        }

        //H(l) = - (Sum f(b,l) * log[ f(b,l) ])             (3)
        private double h(boolean dna) {
            double ret = 0;
            String[] bases = null;
            if (dna) {
                bases = DNA_BASES;
            } else {
                bases = AMINO_ACIDS;
            }
            for (int i = 0; i < bases.length; i++) {
                String b = bases[i];
                double f = getFrequency(b);
                if(f != 0){
                    double logf = Math.log10(f) / Math.log10(2);
                    ret = ret + f * logf;
                }
            }
            return -ret;
        }

        private double getFrequency(String base) {
            double ret = 0;
            Integer old = get(base.toUpperCase());
            if (old != null) {
                ret = 1.0 * old / totalCount;
            }
            return ret;
        }
    }
}
