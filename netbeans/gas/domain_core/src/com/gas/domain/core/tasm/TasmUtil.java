package com.gas.domain.core.tasm;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.ace.ACE;
import com.gas.domain.core.ace.ACE.Contig;
import com.gas.domain.core.ace.ACE.Read;
import com.gas.domain.core.shotgun.Coverage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class TasmUtil {

    private final static Logger logger = Logger.getLogger(TasmUtil.class.getName());

    public static ACE convert(List<Condig> condigs) {
        ACE ret = null;
        if (condigs != null && !condigs.isEmpty()) {
            ret = new ACE();
        }
        for (Condig condig : condigs) {
            Contig contig = new Contig();
            ret.getContigs().add(contig);

            contig.setName(condig.getAsmblId().toString());
            List<Integer> qualities = condig.getQualities();
            String str = CommonUtil.toString(qualities);
            contig.setBaseQualities(str);

            contig.setComplemented(false);
            contig.setSequence(condig.getLsequence());

            Iterator<Rid> rdItr = condig.getRids().iterator();

            while (rdItr.hasNext()) {
                Rid rid = rdItr.next();
                Read read = new Read();

                read.setAlignClippingEnd(0);
                read.setAlignClippingStart(0);
                read.setComplemented(rid.isComplementary());

                read.setName(rid.getSeqName());
                read.setPaddedStart(rid.getOffset() + 1);
                read.setQualClippingEnd(Math.min(rid.getLsequence().length(), Math.max(rid.getSeq_lend(), rid.getSeq_rend())));
                read.setQualClippingStart(Math.min(rid.getSeq_lend(), rid.getSeq_rend()));


                read.setSequence(rid.getLsequence());

                contig.getReads().put(read.getName(), read);
                //read.setQualClippingStart(Integer.SIZE);
            }
        }
        return ret;
    }

    public Coverage toCoverage(Condig condig) {
        Coverage ret = new Coverage();

        List<Integer> starts = new ArrayList<Integer>();
        List<Integer> ends = new ArrayList<Integer>();

        // get the starts and ends
        Iterator<Rid> itr = condig.getRids().iterator();
        while (itr.hasNext()) {
            Rid rid = itr.next();
            starts.add(rid.getOffset() + 1);
            ends.add(rid.getLsequence().length() + rid.getOffset());
        }

        Set<Integer> tmpSet = new HashSet<Integer>(starts);

        starts = new ArrayList<Integer>(tmpSet);

        tmpSet = new HashSet<Integer>(ends);
        ends = new ArrayList<Integer>(tmpSet);

        Collections.sort(starts);
        Collections.sort(ends);

        Integer cStart = starts.remove(0);
        Integer cEnd = null;

        while (!ends.isEmpty()) {
            //System.out.println("cStart="+cStart);
            Integer tmpStart = null;
            if (!starts.isEmpty()) {
                tmpStart = starts.get(0);
            }
            Integer tmpEnd = ends.get(0);
            if (tmpStart == null) {
                ends.remove(0);
                Coverage.Element e = new Coverage.Element();
                e.setFrom(cStart);
                e.setTo(tmpEnd);
                ret.getElements().add(e);
                cStart = e.getTo() + 1;
            } else if (tmpEnd < tmpStart) {
                ends.remove(0);
                Coverage.Element e = new Coverage.Element();
                e.setFrom(cStart);
                e.setTo(tmpEnd);
                ret.getElements().add(e);
                cStart = e.getTo() + 1;
            } else if (tmpStart < tmpEnd) {
                if (cStart.intValue() > tmpStart.intValue() - 1) {
                    // ignore and purge
                    starts.remove(0);
                } else if (cStart.intValue() == tmpStart.intValue() - 1) {
                    Coverage.Element e = new Coverage.Element();
                    e.setFrom(cStart);
                    e.setTo(tmpStart.intValue() - 1);
                    ret.getElements().add(e);
                    cStart = e.getTo() + 1;
                    starts.remove(0); // purgne
                } else if (cStart.intValue() < tmpStart.intValue() - 1) {
                    Coverage.Element e = new Coverage.Element();
                    e.setFrom(cStart);
                    e.setTo(tmpStart.intValue() - 1);
                    ret.getElements().add(e);
                    cStart = e.getTo() + 1;
                    starts.remove(0); // purge      
                }
            } else if (tmpStart.equals(tmpEnd)) {
                Coverage.Element e = new Coverage.Element();
                e.setFrom(cStart);
                e.setTo(tmpEnd);
                ret.getElements().add(e);
                cStart = e.getTo() + 1;
                starts.remove(0);  // purge
                ends.remove(0); // purge
            }
        }

        Iterator<Coverage.Element> eItr = ret.getElements().iterator();
        while (eItr.hasNext()) {
            Coverage.Element e = eItr.next();
            int count = 0;
            Iterator<Rid> rItr = condig.getRids().iterator();
            while (rItr.hasNext()) {
                Rid rid = rItr.next();
                if (rid.getLoc().isSuperset(e.getLoc())) {
                    count++;
                }
            }
            e.setCount(count);
        }
        return ret;
    }

    public static VariantMapMdl toVariantMapMdl(Condig condig) {
        return toVariantMapMdl(condig, true, true, true, true, true);
    }

    public static int getMismatchCount(Condig condig) {
        int ret = 0;
        Iterator<Rid> ridItr = condig.getSortedRids().iterator();
        String consensus = condig.getLsequence();        
        while (ridItr.hasNext()) {
            Rid rid = ridItr.next();

            String readSeq = rid.getLsequence();
            for (int i = 0; i < readSeq.length(); i++) {
                Character readBase = readSeq.charAt(i);
                Character consensusBase = consensus.charAt(i + rid.getOffset());
                
                if (!readBase.toString().equalsIgnoreCase(consensusBase.toString())) {
                    ret++;
                }
            }
        }
        return ret;
    }

    public static VariantMapMdl toVariantMapMdl(Condig condig, boolean includeGaps,
            boolean includeAmbiguity, boolean includeInsertions, boolean includeTransitions, boolean includeTransversions) {
        VariantMapMdl ret = new VariantMapMdl();
        Iterator<Rid> ridItr = condig.getSortedRids().iterator();

        String consensus = condig.getLsequence();
        ret.setLength(consensus.length());
        while (ridItr.hasNext()) {
            Rid rid = ridItr.next();
            VariantMapMdl.Read read = new VariantMapMdl.Read();
            read.setReadName(rid.getSeqName());
            int start = rid.getOffset() + 1;
            read.setStart(start);
            int end = rid.getLsequence().length() + start - 1;
            read.setEnd(end);

            ret.getReads().add(read);

            String readSeq = rid.getLsequence();
            for (int i = 0; i < readSeq.length(); i++) {
                Character readBase = readSeq.charAt(i);
                readBase = Character.toUpperCase(readBase);
                Character consensusBase = consensus.charAt(i + rid.getOffset());
                consensusBase = Character.toUpperCase(consensusBase);

                VariantMapMdl.Variant v = null;

                if (BioUtil.isGap(consensusBase, readBase) && includeGaps) {
                    v = new VariantMapMdl.Variant();
                    v.setReadName(rid.getSeqName());
                    v.setGap(Boolean.TRUE);
                    v.setToBase('-');
                    v.setFromBase(consensusBase);
                } else if (BioUtil.isAnyNucleotide(consensusBase, readBase) && includeAmbiguity) {
                    v = new VariantMapMdl.Variant();
                    v.setReadName(rid.getSeqName());
                    v.setAny(Boolean.TRUE);
                    v.setToBase('n');
                    v.setFromBase(consensusBase);
                } else if (BioUtil.isInsertion(consensusBase, readBase) && includeInsertions) {
                    v = new VariantMapMdl.Variant();
                    v.setReadName(rid.getSeqName());
                    v.setInsertion(Boolean.TRUE);
                    v.setToBase(readBase);
                    v.setFromBase(consensusBase);
                } else if (BioUtil.isTransition(readBase, consensusBase) && includeTransitions) {
                    v = new VariantMapMdl.Variant();
                    v.setReadName(rid.getSeqName());
                    v.setToBase(readBase);
                    v.setFromBase(consensusBase);
                    v.setTransition(true);
                } else if (BioUtil.isTransverion(readBase, consensusBase) && includeTransversions) {
                    v = new VariantMapMdl.Variant();
                    v.setReadName(rid.getSeqName());
                    v.setToBase(readBase);
                    v.setFromBase(consensusBase);
                    v.setTransversion(true);
                }

                if (v != null) {
                    v.setPos(i + 1);
                    v.setReadStart(read.getStart());
                    read.getVariants().add(v);
                }
            }
        }
        return ret;
    }
}
