/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.mrbayers.api;

/**
 *
 * @author dq
 */
public enum Nst {
    ONE("1"),
    TWO("2"),
    SIX("6"),
    Mixed("mixed");
    
    String value;
    
    Nst(String value){
        this.value = value;
    };
}
