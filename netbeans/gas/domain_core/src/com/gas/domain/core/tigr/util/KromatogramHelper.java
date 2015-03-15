/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr.util;

import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.Rid;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.tigr.Kromatogram;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class KromatogramHelper {

    public static Fasta toFasta(List<Kromatogram> ks) {
        Fasta ret = new Fasta();
        for (Kromatogram k : ks) {
            Fasta.Record r = k.toFastaRecord();
            ret.getRecords().add(r);
        }
        return ret;
    }

    public static Kromatogram alterKromatogram(Rid rid, Kromatogram input) {
        Kromatogram ret = null;
        Integer lend = rid.getSeq_lend();
        Integer rend = rid.getSeq_rend();

        int originalLength = input.getBases().size();

        int start, end; // 1-based
        if (lend > rend) {
            ret = input.reverseComplement();
            start = originalLength - lend + 1;
            end = originalLength - rend + 1;
        } else {
            ret = input.clone();
            start = lend;
            end = rend;
        }

        ret.trimStart(start - 1);
        ret.trimEnd(originalLength - end);

        List<Integer> gaps = new ArrayList<Integer>();
        String lSeq = rid.getLsequence();
        for (int i = 0; i < lSeq.length(); i++) {
            char c = lSeq.charAt(i);
            if (c == '-') {
                gaps.add(i + 1);
            }
        }

        ret.addGaps(gaps);

        int result = StrUtil.toString(ret.getBases()).compareTo(lSeq);
        assert result == 0;
        return ret;
    }
}
