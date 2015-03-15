package com.gas.domain.core.tasm;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.MathUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Condig implements Serializable, Cloneable {

    private transient Integer hibernateId;
    private Integer asmblId; //asmbl_id   -> contig ID
    private String sequence; //sequence   -> contig ungapped consensus sequence (ambiguities are lowercase)
    private String lsequence; //lsequence  -> gapped consensus sequence (lowercase ambiguities)
    private List<Integer> qualities; //quality    -> gapped consensus quality score (in hexadecimal)
    private Float redundancy;//redundancy -> fold coverage of the contig consensus
    private Float percentN;//perc_N     -> percent of ambiguities in the contig consensus
    private Integer seqNO;//seq#       -> number of sequences in the contig
    private Set<Rid> rids = new HashSet<Rid>();
    private transient Integer mismatchCount;

    public Condig() {
    }

    @Override
    public Condig clone() {
        Condig ret = CommonUtil.cloneSimple(this);
        ret.setQualities(CommonUtil.copyOf(qualities));
        ret.setRids(CommonUtil.copyOf(rids));
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Integer getAvgQualityScore() {
        if (qualities != null) {
            Integer ret = MathUtil.avg(qualities.toArray(new Integer[qualities.size()]), Integer.class);
            return ret;
        } else {
            return null;
        }
    }

    public Integer getMismatchCount(boolean update) {
        if (mismatchCount == null) {
            mismatchCount = TasmUtil.getMismatchCount(this);
        } else if (update) {
            mismatchCount = TasmUtil.getMismatchCount(this);
        }
        return mismatchCount;
    }

    public Integer getMismatchCount() {
        return getMismatchCount(false);
    }

    public Integer getAsmblId() {
        return asmblId;
    }

    public void setAsmblId(Integer asmblId) {
        this.asmblId = asmblId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getGappedLength() {
        if (lsequence != null) {
            return lsequence.length();
        } else {
            return null;
        }
    }

    public String getLsequence() {
        return lsequence;
    }

    public void setLsequence(String lsequence) {
        this.lsequence = lsequence;
    }

    public List<Integer> getQualities() {
        return qualities;
    }

    public void setQualities(List<Integer> qualities) {
        this.qualities = qualities;
    }

    public Float getRedundancy() {
        return redundancy;
    }

    public void setRedundancy(Float redundancy) {
        this.redundancy = redundancy;
    }

    public Float getPercentN() {
        return percentN;
    }

    public void setPercentN(Float percentN) {
        this.percentN = percentN;
    }

    public Integer getSeqNO() {
        return seqNO;
    }

    public void setSeqNO(Integer seqNO) {
        this.seqNO = seqNO;
    }

    public Set<Rid> getRids() {
        return rids;
    }

    public List<Rid> getSortedRids() {
        List<Rid> ret = new ArrayList<Rid>(rids);
        Collections.sort(ret, new Rid.Sorter());
        return ret;
    }

    public void setRids(Set<Rid> rids) {
        this.rids = rids;
    }
}
