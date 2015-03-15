/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.backup.api;

/**
 *
 * @author dq
 */
public enum BackupOption {
    ALERT_BEFORE_CLOSING, AUTO_BEFORE_CLOSING, MANUAL;    
    
    public static BackupOption findByName(String name){
        for(BackupOption o: BackupOption.values()){
            if(o.name().equals(name)){
                return o;
            }
        }                
        return null;
    }
}
