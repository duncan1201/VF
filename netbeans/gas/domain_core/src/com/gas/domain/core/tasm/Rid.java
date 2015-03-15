package com.gas.domain.core.tasm;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.tigr.Kromatogram;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Rid implements Serializable, Cloneable{

    private transient Integer hibernateId;
    private String seqName;// -> read name
    private Integer asm_lend; //-> position of first base on contig ungapped consensus sequence
    private Integer asm_rend; //-> position of last base on contig ungapped consensus sequence
    private Integer seq_lend;// -> start of quality-trimmed sequence (aligned read coordinates)
    private Integer seq_rend;//-> end of quality-trimmed sequence (aligned read coordinates)
    private Integer best; // -> always '0' *
    private Integer offset;//  -> offset of the sequence (gapped consensus coordinates)
    private String lsequence; // -> aligned read sequence (ambiguities are uppercase)
    private transient Loc loc;    
    private Set<Kromatogram> kromatograms = new HashSet<Kromatogram>();

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }
    
    @Override
    public Rid clone(){
        Rid ret = CommonUtil.cloneSimple(this);
        ret.setKromatograms(CommonUtil.copyOf(kromatograms));
        return ret;
    }

    public Kromatogram getKromatogram() {
        if(this.kromatograms.isEmpty()){
            return null;
        }else{
            return kromatograms.iterator().next();
        }
    }

    public void setKromatogram(Kromatogram kromatogram) {
        this.kromatograms.clear();
        this.kromatograms.add(kromatogram);
    }

    // for hibernate use only
    protected Set<Kromatogram> getKromatograms() {
        return kromatograms;
    }

    // for hibernate use only
    protected void setKromatograms(Set<Kromatogram> kromatograms) {
        this.kromatograms = kromatograms;
    }
    
    public Loc getLoc() {
        if (loc == null) {
            loc = new Loc(offset + 1, lsequence.length() + offset + 1 - 1);
        }
        return loc;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public Integer getAsm_lend() {
        return asm_lend;
    }

    public void setAsm_lend(Integer asm_lend) {
        this.asm_lend = asm_lend;
    }

    public Integer getAsm_rend() {
        return asm_rend;
    }

    public void setAsm_rend(Integer asm_rend) {
        this.asm_rend = asm_rend;
    }

    public Integer getBest() {
        return best;
    }

    public void setBest(Integer best) {
        this.best = best;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSeq_lend() {
        return seq_lend;
    }

    public void setSeq_lend(Integer seq_lend) {
        this.seq_lend = seq_lend;
    }

    public Integer getSeq_rend() {
        return seq_rend;
    }

    public void setSeq_rend(Integer seq_rend) {
        this.seq_rend = seq_rend;
    }

    public String getLsequence() {
        return lsequence;
    }

    public void setLsequence(String lsequence) {
        this.lsequence = lsequence;
    }

    public Boolean isComplementary() {
        return asm_rend < asm_lend;
    }

    public static class Sorter implements Comparator<Rid> {

        @Override
        public int compare(Rid o1, Rid o2) {
            int ret = 0;
            ret = o1.getOffset().compareTo(o2.getOffset());
            if (ret == 0) {
                Integer l1 = o1.getLsequence().length();
                Integer l2 = o2.getLsequence().length();
                ret = l1.compareTo(l2);
            }
            return ret;
        }
    }
}
