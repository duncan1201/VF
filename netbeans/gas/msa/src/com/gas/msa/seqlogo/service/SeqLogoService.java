/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.seqlogo.service;

import com.gas.database.core.msa.service.api.Counter;
import com.gas.domain.core.fasta.Fasta;
import com.gas.database.core.msa.service.api.CounterList;
import com.gas.msa.seqlogo.service.api.Heights;
import com.gas.msa.seqlogo.service.api.HeightsList;
import com.gas.msa.seqlogo.service.api.ISeqLogoService;
import com.gas.common.ui.light.StyledText;
import com.gas.common.ui.light.StyledTextSetMap;
import com.gas.database.core.msa.service.api.ICounterListService;
import java.util.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ISeqLogoService.class)
public class SeqLogoService implements ISeqLogoService {

    private ICounterListService service = Lookup.getDefault().lookup(ICounterListService.class);
    private static final String[] DNA_BASES = {"A", "T", "C", "G", "U", "-"};
    private static final String[] AMINO_ACIDS = {"A", "C", "D", "E", "F", "G", "H", "I", "K", "L",
        "M", "N", "P", "Q", "R", "S", "T", "V", "W", "Y", "-"};
    private int numberOfSequences = 0;
    private boolean smallSampleCorrection;

    @Override
    public HeightsList calculateHeights(Fasta fasta, boolean smallSampleCorrection) {
        boolean isDNA = fasta.isDNAByGuess();
        numberOfSequences = fasta.getSeqCount();
        this.smallSampleCorrection = smallSampleCorrection;
        CounterList counterList = service.createCounterList(fasta);
        return _calculateHeights(counterList, isDNA);
    }

    private HeightsList _calculateHeights(CounterList counterList, boolean dna) {
        HeightsList ret = new HeightsList();
        ret.setProtein(!dna);
        for (int i = 0; i < counterList.size(); i++) {
            Counter counter = counterList.get(i);
            Heights height = calculateHeightsAtPos(counter, dna);
            ret.add(height);
        }
        return ret;
    }
    
    /**
     * height = f(b,l) * R(l)
     */
    private Heights calculateHeightsAtPos(Counter c, boolean isDNA) {
        if (numberOfSequences == 0) {
            throw new IllegalArgumentException();
        }
        Heights ret = new Heights();
        ret.setPos(c.getPos());
        String[] bases = null;
        if (isDNA) {
            bases = DNA_BASES;
        } else {
            bases = AMINO_ACIDS;
        }        
        double r = r(c, isDNA, numberOfSequences);
        for (int i = 0; i < bases.length; i++) {
            char base = bases[i].charAt(0);
            double f = c.getFrequency(base);
            if (f > 0) {
                double height = f*r;
                height = Math.max(height, 0);
                // no gaps in the result, bases only
                if(Character.isLetter(base)){
                    ret.put(base, height);    
                }                
            }
        }
        return ret;
    }

    /**
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

    /**
     * R(l) for amino acids = log(20) - (H(l) + e(n)) (2a) 
     * R(l) for nucleic acids = 2 - (H(l) + e(n))     (2b)
     */
    private double r(Counter c, boolean dna, int _numberOfSequence) {
        double ret = 0;
        if (dna) {
            ret = 2;
        } else {
            ret = Math.log(20) / Math.log(2);
        }
        double h = h(c, dna);
        double e = e(dna, _numberOfSequence);
        if(smallSampleCorrection){
            ret = ret - (h + e);
        }else{
            ret = ret - (h);
        }
        return ret;
    }
    
    /**
     * H(l) = - (Sum f(b,l) * log[ f(b,l) ])             (3)
     */
    private double h(Counter c, boolean dna) {
        double ret = 0;
        String[] bases = null;
        if (dna) {
            bases = DNA_BASES;
        } else {
            bases = AMINO_ACIDS;
        }
        for (int i = 0; i < bases.length; i++) {
            char b = bases[i].charAt(0);
            double f = c.getFrequency(b);
            if (f != 0) {
                double logf = Math.log10(f) / Math.log10(2);
                ret = ret + f * logf;
            }
        }
        return -ret;
    }

    @Override
    public StyledTextSetMap createStyledTextListMap(CounterList cList, boolean dna, boolean smallSampleCorrection) {
        numberOfSequences = cList.get(0).getTotalCount();
        this.smallSampleCorrection = smallSampleCorrection;
        HeightsList hList = _calculateHeights(cList, dna);
        return _createStyledTextListMap(hList);
    }

    private StyledTextSetMap _createStyledTextListMap(HeightsList heightslist) {
        if (numberOfSequences == 0) {
            throw new IllegalArgumentException();
        }
        StyledTextSetMap ret = new StyledTextSetMap();

        for (int posIndex = 0; posIndex < heightslist.size(); posIndex++) {
            Heights heights = heightslist.get(posIndex);
            List<Heights.Entry> entries = heights.getSortedEntries(new Heights.EntrySorter());
            Iterator<Heights.Entry> itr = entries.iterator();
            while (itr.hasNext()) {
                Heights.Entry entry = itr.next();
                Double bits = entry.getBits();

                StyledText styledText = new StyledText();
                styledText.setText(entry.getName().toString().toUpperCase(Locale.ENGLISH));

                styledText.setBits(bits);

                ret.add(posIndex + 1, styledText);
            }
        }
        ret.sort();
        return ret;
    }
}
