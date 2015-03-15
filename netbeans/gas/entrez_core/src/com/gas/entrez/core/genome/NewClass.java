/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.genome;

import com.gas.entrez.core.ESearch.ESearchCmd;
import com.gas.entrez.core.genome.api.NewInterface;

/**
 *
 * @author dq
 */
public class NewClass implements NewInterface {
    public void query(){
        ESearchCmd searchCmd = new ESearchCmd();
        searchCmd.setDb("genome");
        
    }
}
