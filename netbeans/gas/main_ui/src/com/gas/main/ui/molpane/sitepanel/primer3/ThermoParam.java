/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.core.StringList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
enum ThermoParam {

    BRESLAUER("Breslauer et al. 1986", 0),
    SANTALUCIA("SantaLucia 1998", 1);
    private Integer value;
    private String name;

    ThermoParam(String name, int value) {
        this.name = name;
        this.value = value;
    }

    static StringList getAllNames() {
        List<ThermoParam> all = getAll();
        StringList ret = new StringList();
        for(ThermoParam p: all){
            ret.add(p.getName());
        }
        return ret;
    }
    
    static ThermoParam getByValue(int value){
        List<ThermoParam> all = getAll();
        ThermoParam ret = null;
        for(ThermoParam p: all){
            if(p.getValue().equals(value)){
                ret = p;
                break;
            }
        }
        return ret;
    }
    
    static ThermoParam getByName(String name){
        List<ThermoParam> all = getAll();
        ThermoParam ret = null;
        for(ThermoParam p: all){
            if(p.getName().equals(name)){
                ret = p;
                break;
            }
        }
        return ret;
    }    
    
    private static List<ThermoParam> getAll(){
        List<ThermoParam> ret = new ArrayList<ThermoParam>();
        ret.add(BRESLAUER);
        ret.add(SANTALUCIA);
        return ret;
    }

    String getName() {
        return name;
    }

    Integer getValue() {
        return this.value;
    }
}
