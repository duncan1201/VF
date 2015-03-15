/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.al2co.service.api;

import com.gas.common.ui.core.FloatList;

/**
 *
 * @author dq
 */
public interface IAl2CoService {

    public enum WeightingScheme {

        Unweighted("Unweighted", 0),
        Henikoff_Henikoff("Henikoff & Henikoff", 1),
        Independent_count("Independent count", 2),;
        private String name;
        private int value;

        WeightingScheme(String name, int v) {
            this.name = name;
            this.value = v;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    };

    public enum ConsCalMethod {

        Entropy("Entropy-based", 0),
        Variance("Variance-based", 1),
        Sum_of_pairs("Sum Of Pairs", 2),;
        private String name;
        private int value;

        ConsCalMethod(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    };
    
    public enum MatrixTrans{
        No_Trans("No Transformation", 0),
        Normalization("Normalization", 1),
        Adjustment("Adjustment", 2),
        ;
        
        String name;
        int value;
        MatrixTrans(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }        
    }
    
    FloatList calculateConservation(Al2CoParams params);
}
