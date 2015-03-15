/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.orf.api;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.as.Feture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class ORFResult implements Cloneable {

    private Integer hibernateId;
    private ORFSet orfSet = new ORFSet();
    private ORFParam orfParams = new ORFParam();

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public ORFResult clone() {
        ORFResult ret = CommonUtil.cloneSimple(this);
        ret.setOrfParams(orfParams.clone());
        ret.setOrfSet(orfSet.clone());
        return ret;
    }

    public List<Feture> toFetures() {
        return ORFResultHelper.toFetures(this);
    }

    public void touchAll() {
        if (getOrfParams() != null) {
            getOrfParams().touchAll();
        }
        getOrfSet().size();
    }

    public ORFSet getOrfSet() {
        return orfSet;
    }

    public void setOrfSet(ORFSet orfSet) {
        this.orfSet = orfSet;
    }

    public ORFParam getOrfParams() {
        return orfParams;
    }

    public void setOrfParams(ORFParam orfParams) {
        this.orfParams = orfParams;
    }
}
