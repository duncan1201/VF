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
public class ConsensusParam implements Cloneable {

    private Integer hibernateId;
    private boolean ignoreGaps = true;
    private boolean plurality = true;
    private float threshold = 0.5f;

    @Override
    public ConsensusParam clone() {
        ConsensusParam ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public boolean isPlurality() {
        return plurality;
    }

    public void setPlurality(boolean plurality) {
        this.plurality = plurality;
    }

    public boolean isIgnoreGaps() {
        return ignoreGaps;
    }

    public void setIgnoreGaps(boolean ignoreGaps) {
        this.ignoreGaps = ignoreGaps;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}
