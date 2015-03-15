/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

/**
 *
 * @author dq
 */
class FreeAcademicLicense {
    private String fingerprint;
    
    static FreeAcademicLicense parse(String hex){
        String clear = LicenseUtil.decrypt(hex);
        FreeAcademicLicense ret = new FreeAcademicLicense(clear);
        return ret;
    }
    
    /**
     * free [fingerprint]
     */
    static boolean isFreeAcademicLicense(String hex){
        boolean ret = false;
        if(hex == null){
            return false;
        }
        String clear = LicenseUtil.decrypt(hex);
        if (clear != null) {
            String[] splits = clear.split(" ");
            ret = splits.length == 2 && splits[0].equalsIgnoreCase("free");
        }
        return ret;
    }  
    
    FreeAcademicLicense(String clear){
        String[] splits = clear.split(" ");
        this.fingerprint = splits[1].trim();
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
