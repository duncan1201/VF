/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.common.ui.util.CommonUtil;

/**
 *
 * @author dq
 */
public class RichConsensus implements Cloneable {

    private Integer hibernateId;
    private String bases;
    private String conservation;
    private int[] qualities;

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getBases() {
        return bases;
    }

    public void setBases(String bases) {
        this.bases = bases;
    }

    public int[] getQualities() {
        return qualities;
    }

    public void setQualities(int[] qualities) {
        this.qualities = qualities;
    }

    public String getConservation() {
        return conservation;
    }

    public void setConservation(String conservation) {
        this.conservation = conservation;
    }

    @Override
    public RichConsensus clone() {
        RichConsensus ret = CommonUtil.cloneSimple(this);
        ret.setQualities(CommonUtil.copyOf(qualities));
        return ret;
    }

    public void touchAll() {
        if (bases != null) {
            bases.length();
        }
        int length = qualities.length;
    }
}
