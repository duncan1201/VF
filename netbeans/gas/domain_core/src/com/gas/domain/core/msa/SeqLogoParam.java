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
public class SeqLogoParam implements Cloneable {

    private Integer hibernateId;
    private boolean smallSampleCorrection = false;

    @Override
    public SeqLogoParam clone() {
        SeqLogoParam ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public boolean isSmallSampleCorrection() {
        return smallSampleCorrection;
    }

    public void setSmallSampleCorrection(boolean smallSampleCorrection) {
        this.smallSampleCorrection = smallSampleCorrection;
    }
}
