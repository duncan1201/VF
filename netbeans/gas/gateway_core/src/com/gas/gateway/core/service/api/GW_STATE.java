/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

/**
 *
 * @author dq
 */
public enum GW_STATE {
    
    NO_SELECTION("No molecule selected"),
    NOT_BLUNT_ENDED("No blunt-ended"),
    VALID("VALID"), 
    INVALID("INVALID"),
    INSERT_NOT_FOUND("Insert not flanked by att sites"),
    DONOR_NOT_FOUND("No donor found!"), 
    DONOR_INSERT_COUNT_NO_MATCH("The insert count does not equal to the donor count"), 
    DONOR_INSERT_NOT_MATCHING("DONOR_NO_MATCHING"), 
    LR_INVALID("LR_INVALID"), 
    NOT_CIRCULAR("NOT_CIRCULAR"),
    NOT_LINEAR("NOT_LINEAR"), 
    ENTRY_NOT_FOUND("Entry clones not found"),
    ENTRY_TOO_LITTLE("ENTRY_TOO_LITTLE"),
    ENTRY_TOO_MANY("ENTRY_TOO_MANY"),
    ENTRY_WRONG_SITE("ENTRY_WRONG_SITE"),
    DEST_NOT_FOUND("No destination vector found"), 
    DEST_TOO_MANY("More than one destination vector found"),
    DEST_VECTOR_INVALID("The destination vector %s is invalid"),
    B_SITES_NOT_FOUND("B_SITES_NOT_FOUND"), 
    B_SITES_TOO_MANY("B_SITES_TOO_MANY"), 
    P_SITES_NOT_FOUND("P_SITES_NOT_FOUND"), 
    P_SITES_TOO_MANY("P_SITES_TOO_MANY");
    
    private String errorMsg;
    private String data;
    GW_STATE(String errorMsg){
        this.errorMsg = errorMsg;
    }
    
    public String getErrorMsg(){
        String ret = null;
        if(data != null && !data.isEmpty()){
            ret = String.format(errorMsg, "\"" + data + "\"");
        }else{
            if(errorMsg.indexOf("%s") > -1){
                ret = String.format(errorMsg, "");    
            }else{
                ret = errorMsg;
            }
        }
        return ret;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }    
};
