package com.gas.domain.core.as;

import com.gas.common.ui.util.StrUtil;

public class Overhang implements Cloneable {

    private Integer hibernateId;
    private boolean strand; // true means forward, false means compl
    private boolean fivePrime;
    private Integer length;
    private String name;

    public Overhang() {
    } // for hibernate use

    @Override
    public Overhang clone() {
        Overhang ret = new Overhang();
        ret.setLength(length);
        ret.setStrand(strand);
        ret.setFivePrime(fivePrime);
        ret.setName(name);
        return ret;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void flip() {
        this.strand = !strand;
    }

    public Overhang(boolean strand, boolean fivePrime) {
        this.strand = strand;
        this.fivePrime = fivePrime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public boolean isStrand() {
        return strand;
    }

    public boolean isStartOverhang() {
        return (isFivePrime() && strand) || (isThreePrime() && !strand);
    }

    public boolean isEndOverhang() {
        return (isFivePrime() && !strand) || (isThreePrime() && strand);
    }

    public void setStrand(boolean strand) {
        this.strand = strand;
    }

    public boolean isFivePrime() {
        return fivePrime;
    }

    public boolean isThreePrime() {
        return !isFivePrime();
    }

    public void setFivePrime(boolean fivePrime) {
        this.fivePrime = fivePrime;
    }
}
