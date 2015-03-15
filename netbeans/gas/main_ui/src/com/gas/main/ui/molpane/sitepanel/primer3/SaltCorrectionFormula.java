package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.core.StringList;
import java.util.ArrayList;
import java.util.List;

enum SaltCorrectionFormula {

    SCHILDKRAUT("Schildkraut and Lifson 1965", 0),
    SANTALUCIA("SantaLucia 1998", 1),
    OWCZARZY("Owczarzy et. 2004", 2);
    private Integer value;
    private String name;

    SaltCorrectionFormula(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public static SaltCorrectionFormula getByValue(int value){
        SaltCorrectionFormula ret = null;
        List<SaltCorrectionFormula> all = getAll();
        for(SaltCorrectionFormula s: all){
            if(s.getValue().equals(value)){
                ret = s;
                break;
            }
        }
        return ret;
    }
    
    public static SaltCorrectionFormula getByName(String name){
        SaltCorrectionFormula ret = null;
        List<SaltCorrectionFormula> all = getAll();
        for(SaltCorrectionFormula s: all){
            if(s.getName().equals(name)){
                ret = s;
                break;
            }
        }
        return ret;        
    }
    
    public static StringList getAllNames() {
        List<SaltCorrectionFormula> all = getAll();
        StringList ret = new StringList();
        for(SaltCorrectionFormula s: all){
            ret.add(s.getName());
        }
        return ret;
    }
    
    private static List<SaltCorrectionFormula> getAll(){
        List<SaltCorrectionFormula> ret = new ArrayList<SaltCorrectionFormula>();
        ret.add(SCHILDKRAUT);
        ret.add(SANTALUCIA);
        ret.add(OWCZARZY);
        return ret;
    }

    public Integer getValue() {
        return this.value;
    }
}