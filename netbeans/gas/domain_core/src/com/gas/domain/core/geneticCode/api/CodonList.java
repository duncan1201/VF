/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode.api;

import com.gas.common.ui.core.StringList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class CodonList extends ArrayList<Codon> {

    public StringList getBases() {
        StringList ret = new StringList();
        for (int i = 0; i < size(); i++) {
            Codon codon = get(i);
            ret.add(codon.getNucleotides());
        }
        return ret;
    }

    public Set<String> getBasesAsSet() {
        Set<String> ret = new HashSet<String>();
        for (int i = 0; i < size(); i++) {
            Codon codon = get(i);
            ret.add(codon.getNucleotides());
        }
        return ret;
    }
}
