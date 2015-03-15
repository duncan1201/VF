/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service.api;

import com.gas.common.ui.core.CharList;
import java.util.*;

/**
 *
 * @author dq
 */
public class Counter extends HashMap<Character, Integer> {

    private int totalCount = 0;
    private int letterCount = 0;
    private Integer pos;  
    private Integer maxCount;
    private Integer maxLetterCount;
    private CharList modes = new CharList();
    private CharList letterModes = new CharList();

    public void increaseCount(Character base) {
        if (!Character.isUpperCase(base)) {
            base = base.toString().toUpperCase(Locale.ENGLISH).charAt(0);
        }
        if (!containsKey(base)) {
            put(base, 1);
        } else {
            Integer old = get(base);
            put(base, old + 1);
        }
        updateStatistics(base);
    }
        
    
    private void updateStatistics(Character base){
        if (Character.isLetter(base)) {
            if (maxLetterCount == null) {
                maxLetterCount = get(base);
                letterModes.add(base);
            } else if (maxLetterCount.intValue() < get(base).intValue()) {
                maxLetterCount = get(base);
                letterModes.clear();
                letterModes.add(base);
            } else if (maxLetterCount.intValue() == get(base).intValue()) {
                letterModes.add(base);
            }
            letterCount++;
        }
        if (maxCount == null) {
            maxCount = get(base);
            modes.add(base);
        } else if (maxCount.intValue() < get(base).intValue()) {
            maxCount = get(base);
            modes.clear();
            modes.add(base);
        } else if (maxCount.intValue() == get(base).intValue()) {
            modes.add(base);
        }

        totalCount++;        
    }

    public int getTotalCount() {
        return totalCount;
    }
    
    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }
    
    public Float getLetterFrequency(){
        return 1.0f * letterCount / totalCount;
    }
    
    public Float getLetterModeFrequency(){
        Float ret = null;
        if(!letterModes.isEmpty()){
            ret = 1.0f * get(letterModes.get(0)) / totalCount;
        }else if(totalCount > 0){
            ret = 0f;
        }
        return ret;
    }

    @Override
    public void clear() {
        super.clear();
        totalCount = 0;       
        letterCount = 0;
        pos = null;
        maxCount = null;
        maxLetterCount = null;
        modes.clear();
        letterModes.clear();        
    }

    public double getFrequency(Character c) {
        return getFrequency(c, null);
    }

    public Integer get(Character c) {
        Integer ret = super.get(c);
        if (ret == null) {
            ret = 0;
        }
        return ret;
    }

    public double getFrequency(Character base, Character ignoreChar) {
        double ret = 0;
        if (!Character.isUpperCase(base)) {
            base = base.toString().toUpperCase(Locale.ENGLISH).charAt(0);
        }
        Integer count = get(base);
        
            Character _base = null;
            if (Character.isUpperCase(base)) {
                _base = base.toString().toLowerCase().charAt(0);
            } else if (Character.isLowerCase(base)) {
                _base = base.toString().toUpperCase().charAt(0);
            }
            if (_base != null) {
                count += get(_base);
            }
        
        Integer ignoreCount = get(ignoreChar);
        if (count != null) {
            ret = 1.0 * count / (totalCount - ignoreCount);
        }
        return ret;
    }

    public CharList getModes() {
        return getModes(false);
    }

    public CharList getModes(boolean lettersOnly) {
        if(lettersOnly){
            return letterModes;                    
        }else{
            return modes;
        }
    }
    
    @Override
    public String toString(){
        String to = super.toString();
        StringBuilder ret = new StringBuilder();
        ret.append(pos);
        ret.append('-');
        ret.append(to);
        return ret.toString();
    }
}
