/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service.api;

import com.gas.common.ui.core.FloatList;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class CounterList extends ArrayList<Counter>{
        
    public CounterList(){
    }  
    
    @Override
    public boolean add(Counter c){
        boolean ret = super.add(c);
        updateStatistics(c);
        return ret;
    }
    
    public FloatList getLetterFrequencies(){
        FloatList ret = new FloatList();
        for(Counter c: this){
            ret.add(c.getLetterFrequency());
        }
        return ret;
    }
    
    public FloatList getLetterModeFrequency(){
        FloatList ret = new FloatList();
        for(Counter c: this){
            ret.add(c.getLetterModeFrequency());
        }
        return ret;
    }
    
    private void updateStatistics(Counter c){
    }
}
