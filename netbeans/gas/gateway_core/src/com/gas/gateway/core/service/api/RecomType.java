/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

import com.gas.common.ui.util.Unicodes;

/**
 *
 * @author dq
 */
public enum RecomType {
    
    GW("1-fragment recombination"),
    Pro2(String.format("MultiSite Gateway%s Pro 2", Unicodes.TRADEMARK)),
    Pro3(String.format("MultiSite Gateway%s Pro 3", Unicodes.TRADEMARK)),
    Pro4(String.format("MultiSite Gateway%s Pro 4", Unicodes.TRADEMARK)),
    ThreeFrag(String.format("MultiSite Gateway%s 3-Fragment Vector Construction", Unicodes.TRADEMARK));
    
    private String desc;
    RecomType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    
    public static RecomType getRecomTypeByDesc(String desc){
        RecomType ret = null;
        if(desc.equals(GW.getDesc())){
            ret = GW;
        }else if(desc.equals(Pro2.getDesc())){
            ret = Pro2;
        }else if(desc.equals(Pro3.getDesc())){
            ret = Pro3;
        }else if(desc.equals(Pro4.getDesc())){
            ret = Pro4;
        }else if(desc.equals(ThreeFrag.getDesc())){
            ret = ThreeFrag;
        }
        return ret;
    }
    
}
