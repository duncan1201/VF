package com.gas.primer3.core.mispriminglib;

import java.util.ArrayList;
import java.util.List;

public enum MisprimingLib {

    NONE("None"),
    HUMAN("Human"),
    RODENT("Rodent"),
    RODENT_AND_SIMPLE("Rodent and Simple"),
    DROSOPHILA("Drosophila");
    protected String name;

    MisprimingLib(String name) {
        this.name = name;
    }

    public static MisprimingLib getByName(String name) {
        MisprimingLib[] all = values();
        MisprimingLib ret = null;
        for(MisprimingLib l: all){
            if(l.getName().equalsIgnoreCase(name)){
                ret = l;
                break;
            }
        }
        return ret;
    } 

    public static List<String> getAllNames() {
        MisprimingLib[] all = values();
        List<String> ret = new ArrayList<String>();
        for(MisprimingLib l : all){
            ret.add(l.getName());
        }
        return ret;
    }

    
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
