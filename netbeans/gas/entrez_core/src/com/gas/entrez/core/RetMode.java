/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core;

/**
 *
 * @author dunqiang
 */
public enum RetMode {
    
    xml("xml"), 
    text("text"),
    asn_1("asn.1");
    
    String s ;
    private RetMode(String s){
        this.s = s;
    }
    
    public String toString(){
        return s;
    }
}
