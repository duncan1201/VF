/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.common.ui.util.ReflectHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.misc.api.NewickName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class TreeNodeName {

    private String name = "";
    private Float prob;
    private Float probStddev;
    
    private static final String ATT_NAME = "name";
    private static final String ATT_PROB = "prob";
    private static final String ATT_PROB_STD_DEV = "probStddev";

    public TreeNodeName(NewickName newickName) {
        if(newickName != null){
            this.name = newickName.getName();
            this.prob = newickName.getProb();
            this.probStddev = newickName.getProbStddev();
        }
    }
    
    public String[] getAttributeNames() {
        List<String> ret = new ArrayList<String>();
        ret.add(ATT_NAME);
        if(prob != null){
            ret.add(ATT_PROB);
        }
        if(probStddev != null){
            ret.add(ATT_PROB_STD_DEV);
        }
        return ret.toArray(new String[ret.size()]);
    }
    
    public Object getAttribute(String name) {       
        if(name.equals(ATT_NAME)){
            return getName();
        }else if(name.equals(ATT_PROB)){
            return getProb();
        }else if(name.equals(ATT_PROB_STD_DEV)){
            return getProbStddev();
        }else{
            throw new IllegalArgumentException(String.format("attribute %s not found", name));
        }       
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getProb() {
        return prob;
    }

    public void setProb(Float prob) {
        this.prob = prob;
    }

    public Float getProbStddev() {
        return probStddev;
    }

    public void setProbStddev(Float probStddev) {
        this.probStddev = probStddev;
    }
    
    
}
