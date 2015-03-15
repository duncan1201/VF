/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.primer3;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.as.Feture;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class GbOutput implements Cloneable{
    
    private Integer hibernateId;
    
    private Set<OverlapPrimer> overlapPrimers = new HashSet<OverlapPrimer>();

    @Override
    public GbOutput clone(){
        GbOutput ret = CommonUtil.cloneSimple(this);
        ret.setOverlapPrimers(CommonUtil.copyOf(overlapPrimers));
        return ret;
    }
    
    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }
    
    public void clear(){
        this.overlapPrimers.clear();
    }

    public Set<OverlapPrimer> getOverlapPrimers() {
        return overlapPrimers;
    }

    public void setOverlapPrimers(Set<OverlapPrimer> overlapPrimers) {
        this.overlapPrimers = overlapPrimers;
    }
    
    public List<Feture> toFetures(int totalLength){
        List<Feture> ret = GbOutputHelper.toFetures(this, totalLength);
        return ret;
    }
    
    public void touchAll(){
        Iterator<OverlapPrimer> itr = overlapPrimers.iterator();
        while(itr.hasNext()){
            OverlapPrimer overlapPrimer = itr.next();
            overlapPrimer.getOligoElement();
            overlapPrimer.getFlappyEnd();
        }
    }
}
