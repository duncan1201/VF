/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.seqlogo.service.api;

import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class HeightsList extends ArrayList<Heights>{
    
    private boolean protein;

    public boolean isProtein() {
        return protein;
    }

    public void setProtein(boolean protein) {
        this.protein = protein;
    }
    
}
