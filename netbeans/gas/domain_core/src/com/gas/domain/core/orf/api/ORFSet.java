/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.orf.api;

import com.gas.common.ui.util.CommonUtil;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dq
 */
public class ORFSet implements Cloneable {

    private Integer hibernateId;
    private Set<ORF> orfs = new HashSet<ORF>();

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public ORFSet clone() {
        ORFSet ret = CommonUtil.cloneSimple(this);
        ret.setOrfs(CommonUtil.copyOf(orfs));
        return ret;
    }

    public Set<ORF> getOrfs() {
        return orfs;
    }

    public void setOrfs(Set<ORF> orfs) {
        this.orfs = orfs;
    }

    public int size() {
        return this.orfs.size();
    }

    public boolean isEmpty() {
        return orfs.isEmpty();
    }

    public void clear() {
        orfs.clear();
    }

    public void add(ORF orf) {
        this.orfs.add(orf);
    }
}
