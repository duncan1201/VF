/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.vfmsa;

import com.gas.common.ui.core.StringList;

/**
 *
 * @author dq
 */
public enum AlignType {
           
    SMITH_WATERMAN("Smith–Waterman"),
    NEEDLEMAN_WUNSCH("Needleman–Wunsch");
    String displayName;
    AlignType(String displayName){
        this.displayName = displayName;
    }
    
    public String getDisplayName(){
        return displayName;
    }
    
    public static AlignType getByDisplayName(String displayName){
        AlignType ret = null;
        AlignType[] alignTypes = AlignType.values();
        for(AlignType alignType: alignTypes){
            if(alignType.getDisplayName().equalsIgnoreCase(displayName)){
                ret = alignType;
                break;
            }
        }
        return ret;
    }
    
    public static StringList getDisplayNames(){
        StringList ret = new StringList();
        AlignType[] alignTypes = AlignType.values();
        for(AlignType alignType: alignTypes){
            ret.add(alignType.getDisplayName());
        }
        return ret;
    }
}
