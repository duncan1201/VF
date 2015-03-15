/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public enum Primer3Task {
    
    Cloning("Cloning", "pick_cloning_primers"),
    Discriminative("Discriminative", "pick_discriminative_primers"),
    Generic("Generic", "generic");
    
    String name;
    String value;
    Primer3Task(String name, String value){
        this.name = name;
        this.value = value;
    }
    
    static List<String> getAllNames(){
        List<String> ret = new ArrayList<String>();
        List<Primer3Task> all = getAll();
        for(Primer3Task t: all){
            ret.add(t.getName());
        }
        return ret;
    }
    
    static Primer3Task getByName(String name){
        Primer3Task ret = null;
        List<Primer3Task> all = getAll();
        for(Primer3Task t: all){
            if(t.getName().equals(name)){
                ret = t;
                break;
            }
        }
        return ret;
    }    
    static Primer3Task getByValue(String value){
        Primer3Task ret = null;
        List<Primer3Task> all = getAll();
        for(Primer3Task t: all){
            if(t.getValue().equals(value)){
                ret = t;
                break;
            }
        }
        return ret;
    }
    
    static List<Primer3Task> getAll(){
        List<Primer3Task> ret = new ArrayList<Primer3Task>();
        ret.add(Generic);
        ret.add(Cloning);
        ret.add(Discriminative);
        return ret;
    }
    
    String getName(){
        return this.name;
    }
    
    String getValue(){
        return value;
    }
}
