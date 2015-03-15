/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.orf.api;

import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
class ORFResultHelper {

    static List<Feture> toFetures(ORFResult orfResult) {
        List<Feture> ret = new ArrayList<Feture>();
        Iterator<ORF> itrOrf = orfResult.getOrfSet().getOrfs().iterator();
        while (itrOrf.hasNext()) {
            ORF orf = itrOrf.next();
            Feture feture = toFeture(orf);
            ret.add(feture);
        }
        return ret;
    }

    static Feture toFeture(ORF orf) {
        Feture ret = new Feture();
        ret.setKey(FetureKeyCnst.ORF);
        ret.setLucation(new Lucation(orf.getStartPos(), orf.getEndPos(), orf.isStrand()));
        ret.addQualifier(String.format("frame=%d", orf.getFrame()));
        return ret;
    }
}
