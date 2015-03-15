/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.mafft.service.api;

/**
 *
 * @author dq
 */
public interface IMafftService {

    public enum METHODS {
        L_INS_i("L-INS-i", ""),
        G_INS_i("G-INS-i", ""),
        E_INS_i("E-INS-i", ""),
        
        ;
        private String name;
        private String desc;
        METHODS(String name, String desc) {
            this.desc = desc;
        }
    };

    void align(MafftParams params);
}
