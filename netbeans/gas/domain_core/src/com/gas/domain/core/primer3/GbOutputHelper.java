/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.primer3;

import com.gas.common.ui.util.BioUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.as.Qualifier;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author dq
 */
public class GbOutputHelper {
    
    public static List<AnnotatedSeq> toOligos(GbOutput gbOutput, int totalPos){
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        Iterator<OverlapPrimer> itr = gbOutput.getOverlapPrimers().iterator();
        while(itr.hasNext()){
            OverlapPrimer primer = itr.next();
            AnnotatedSeq oligo = toOligo(primer, totalPos);
            ret.add(oligo);
        }
        return ret;
    }
    
    private static AnnotatedSeq toOligo(OverlapPrimer op, int totalPos){
        OligoElement oe = op.getOligoElement();
        AnnotatedSeq ret = new AnnotatedSeq();        
        ret.setOligo(true);
        ret.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.DNA);
        if(op.isForward()){
            ret.setSequence(op.getSeq());
        }else{
            ret.setSequence(BioUtil.reverseComplement(op.getSeq()));
        }
        ret.setCircular(false);
        ret.setAccession("N.A.");
        if(op.isForward()){
            ret.setDesc(String.format("Overlapping forward Primer for %s", op.getName()));
        }else{
            ret.setDesc(String.format("Overlapping reverse Primer for %s", op.getName()));
        }
        ret.setCreationDate(new Date()); 
        ret.setLastModifiedDate(new Date());
        ret.setName(op.getName() + (op.isForward()? " F": " R"));
        int lengthFlappyEnd = op.getFlappyEnd().getLength();
        
        int lengthAnnealing = oe.getLength();
        Feture feture = P3OutputHelper.toFeture(oe);
        if(oe.isForwardPrimer()){            
            feture.setLucation(new Lucation(lengthFlappyEnd + 1, lengthFlappyEnd + lengthAnnealing, true));
        }else{
            feture.setLucation(new Lucation(1, lengthAnnealing, false));
        }
        ret.getFetureSet().add(feture);
        
        feture = new Feture("Flappy end");
        if(oe.isForwardPrimer()){
            feture.setLucation(new Lucation(1, lengthFlappyEnd, true));
        } else if (oe.isReversePrimer()){
            feture.setLucation(new Lucation(lengthAnnealing + 1, lengthAnnealing + lengthFlappyEnd, false));            
        }
        ret.getFetureSet().add(feture);
        ParentLoc parentLoc = new ParentLoc(op.calculateStart(totalPos), op.calculateEnd(totalPos));
        parentLoc.setTotalPos(totalPos);
        ret.getParentLocSet().add(parentLoc);
        
        return ret;
    }
    
    public static List<Feture> toFetures(GbOutput gbOutput, int totalLength){
        List<Feture> ret = new ArrayList<Feture>();
        Iterator<OverlapPrimer> itr = gbOutput.getOverlapPrimers().iterator();
        while(itr.hasNext()){
            OverlapPrimer overlapPrimer = itr.next();
            Feture feture = toFeture(overlapPrimer, totalLength);
            ret.add(feture);
        }
        return ret;
    }
    
    private static Feture toFeture(OverlapPrimer primer, int totalLength){     
        OligoElement oe = primer.getOligoElement();
        final NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        final NumberFormat nfPercent = NumberFormat.getPercentInstance(Locale.ENGLISH);
        nfPercent.setMaximumFractionDigits(2);
        nfPercent.setMinimumFractionDigits(2);

        Feture ret = new Feture();
        ret.setKey(FetureKeyCnst.OVERLAPPING_PRIMER);

        int start = primer.calculateStart(totalLength);
        int end = primer.calculateEnd(totalLength);
        
        Lucation luc = new Lucation(start, end, oe.getForward());
        ret.setLucation(luc);

        float tm = oe.getTm();
        StringBuilder label = new StringBuilder();
        label.append(primer.getName());
        label.append(' ');
        
        if(oe.isForwardPrimer()){
            label.append(" Forward");
        }else if(oe.isReversePrimer()){
            label.append(" Reverse");
        }
        
        ret.getQualifierSet().add(new Qualifier("label=" + label.toString()));
        
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

        ret.getQualifierSet().add(new Qualifier("Monovalent cation conc.=" + oe.getMonovalentCationsConc() + " mM"));
        ret.getQualifierSet().add(new Qualifier("Divalent cation conc.=" + oe.getDivalentCationsConc() + " mM"));        
        ret.getQualifierSet().add(new Qualifier("dNTP conc.=" + oe.getDntpConc() + " mM"));
        ret.getQualifierSet().add(new Qualifier("Annealing oligo conc.=" + oe.getAnnealingOligoConc() + " nM"));

        return ret;        
    }
}