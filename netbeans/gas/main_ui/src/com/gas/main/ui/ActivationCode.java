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
class ActivationCode {
    
    String email;
    String productName;
    String productEdition;
    long serialNo;
    long productMaxReleaseTime;
    String fingerprint;
    
    ActivationCode(String clear){
        String[] splits = clear.split(" ");
        email = splits[1];
        productName = splits[2];
        productEdition = splits[3];
        serialNo = Long.parseLong(splits[4]);
        productMaxReleaseTime = Long.parseLong(splits[5]);
        fingerprint = splits[6];
    }
    
    static ActivationCode parse(String hex){
        String clear = LicenseUtil.decrypt(hex);
        ActivationCode ret = new ActivationCode(clear);
        return ret;
    }
    
    /**
     * key email [productName] [product edition] [serial no] [productMaxReleaseTime] [fingerprint]
     */    
    static boolean isActivationCode(String hex){
        boolean ret = false;        
        if(hex == null){
            return false;
        }
        String clear = LicenseUtil.decrypt(hex);
        if(clear != null){
            String[] splits = clear.split(" ");
            ret = splits.length > 0 && splits[0].equals("key");
        }
        return ret;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getProductName() {
        return productName;
    }

    void setProductName(String productName) {
        this.productName = productName;
    }

    String getProductEdition() {
        return productEdition;
    }

    void setProductEdition(String productEdition) {
        this.productEdition = productEdition;
    }

    long getSerialNo() {
        return serialNo;
    }

    void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    long getProductMaxReleaseTime() {
        return productMaxReleaseTime;
    }

    void setProductMaxReleaseTime(long productMaxReleaseTime) {
        this.productMaxReleaseTime = productMaxReleaseTime;
    }

    String getFingerprint() {
        return fingerprint;
    }

    void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }        
}
