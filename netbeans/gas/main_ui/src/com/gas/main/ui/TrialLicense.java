/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import java.util.Date;

/**
 *
 * @author dq
 */
class TrialLicense {
        
    long expiredTime;
    String fingerprint;
    
    /**
     * trial [email] [expiration time]
     */
    static boolean isTrialLicense(String hex){
        boolean ret = false;
        if(hex == null){
            return false;
        }
        String clear = LicenseUtil.decrypt(hex);
        if (clear != null) {
            String[] splits = clear.split(" ");
            ret = splits.length > 0 && splits[0].equalsIgnoreCase("trial");
        }
        return ret;
    }
    
    static TrialLicense parse(String hex){
        String clear = LicenseUtil.decrypt(hex);
        TrialLicense ret = new TrialLicense(clear);
        return ret;
    }

    TrialLicense(){}
    
    /**
     * trial expirationDate fingerprint
     */    
    TrialLicense(String clear){
        String[] splits = clear.split(" ");        
        expiredTime = Long.parseLong(splits[1]);
        fingerprint = splits[2];
    }      

    long getExpiredTime() {
        return expiredTime;
    }

    void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
     
    boolean isExpired(){
        Date date = new Date();
        return date.getTime() > expiredTime;
    }
}
