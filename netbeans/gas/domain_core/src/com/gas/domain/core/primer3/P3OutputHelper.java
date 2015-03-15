/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.primer3;

import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Qualifier;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author dq
 */
public class P3OutputHelper {

    public static P3Output sub(P3Output p, Integer start, Integer end) {
        if (p == null) {
            throw new IllegalArgumentException("P3Output cannot be null");
        } else if (start == null || end == null) {
            throw new IllegalArgumentException("Start/End is null");
        }
        P3Output ret = p.clone();
        List<Oligo> toBeAdded = new ArrayList<Oligo>();
        Iterator<Oligo> itr = ret.getOligos().iterator();
        while (itr.hasNext()) {
            Oligo oligo = itr.next();
            Oligo oligoSub = sub(oligo, start, end);
            if (oligoSub != null) {
                toBeAdded.add(oligoSub);
            }
        }
        ret.getOligos().clear();
        ret.getOligos().addAll(toBeAdded);
        return ret;
    }

    /**
     *
     */
    private static Oligo sub(Oligo oligo, Integer start, Integer end) {
        Oligo ret = oligo.clone();
        Iterator<OligoElement> itr = ret.getOligoElements().iterator();
        while (itr.hasNext()) {
            OligoElement oe = itr.next();
            boolean subset = oe.isSubsetOf(start, end);
            if (!subset) {
                itr.remove();
            } else {
                oe.translate(-(start - 1));
            }
        }
        return ret;
    }    

    public static List<Feture> toFetures(P3Output p3output, Integer totalPos, Boolean circular) {

        List<Feture> ret = new ArrayList<Feture>();
        Iterator<Oligo> itr = p3output.getOligos().iterator();

        while (itr.hasNext()) {
            Oligo oligo = itr.next();
            OligoElement leftElement = oligo.getLeft();
            OligoElement rightElement = oligo.getRight();
            OligoElement internalElement = oligo.getInternal();
            if (leftElement != null) {
                ret.add(toFeture(leftElement, oligo));
            }
            if (rightElement != null) {
                ret.add(toFeture(rightElement, oligo));
            }
            if (internalElement != null) {
                ret.add(toFeture(internalElement, oligo));
            }
        }
        return ret;
    }
    
    public static Feture toFeture(OligoElement oe){
        return toFeture(oe, null);
    }
   
    public static Feture toFeture(OligoElement oe, Oligo oligo) {
        final NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        final NumberFormat nfPercent = NumberFormat.getPercentInstance(Locale.ENGLISH);
        nfPercent.setMaximumFractionDigits(2);
        nfPercent.setMinimumFractionDigits(2);

        Feture ret = new Feture();
        ret.setKey(FetureKeyCnst.PRIMER_BINDING_SITE);
        ret.setDerived(true);
        ret.setData(oe);

        int start = oe.calculateStart();        
        int end = oe.calculateEnd();
        Lucation luc = new Lucation(start, end, oe.getForward());
        ret.setLucation(luc);

        float tm = oe.getTm();
        StringBuilder label = new StringBuilder();
        if (oe.isForwardPrimer()) {
            label.append("Forward primer ");
        } else if (oe.isReversePrimer()) {
            label.append("Reverse primer ");
        } else if (oe.isInternal()) {
            label.append("DNA probe ");
        }

        label.append(oe.getNo() + 1);

        ret.getQualifierSet().add(new Qualifier("label=" + label));
        
        if(oe.hasProblems()){
            ret.getQualifierSet().add(new Qualifier("Problems=" + oe.getProblems()));
        }

        if(oe.getTail().isEmpty()){
            ret.getQualifierSet().add(new Qualifier(String.format("Sequence=%s", oe.getSeq())));
        }else{
            ret.getQualifierSet().add(new Qualifier(String.format("Sequence=%s-%s", oe.getTail(), oe.getSeq())));
        }

        ret.getQualifierSet().add(new Qualifier("TM=" + nf.format(tm)));

        //ret.getQualifierSet().add(new Qualifier("Length=" + length));

        ret.getQualifierSet().add(new Qualifier("GC%=" + nfPercent.format(oe.getGc() / 100)));

        ret.getQualifierSet().add(new Qualifier("Max Complementarity=" + nf.format(oe.getSelfAny())));

        ret.getQualifierSet().add(new Qualifier("Max 3' Complementarity=" + nf.format(oe.getSelfEnd())));

        ret.getQualifierSet().add(new Qualifier("Hairpin=" + nf.format(oe.getHairpin())));

        if (oligo != null && oligo.getProductSize() != null) {
            ret.getQualifierSet().add(new Qualifier("Product Size=" + oligo.calculateProductSize()));
        }
        
        ret.getQualifierSet().add(new Qualifier("Monovalent cation conc.=" + oe.getMonovalentCationsConc() + " mM"));
        ret.getQualifierSet().add(new Qualifier("Divalent cation conc.=" + oe.getDivalentCationsConc() + " mM"));        
        ret.getQualifierSet().add(new Qualifier("dNTP conc.=" + oe.getDntpConc() + " mM"));
        ret.getQualifierSet().add(new Qualifier("Annealing oligo conc.=" + oe.getAnnealingOligoConc() + " nM"));

        return ret;
    }
}
