/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.edit;

import com.gas.domain.core.ren.RENList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author dq
 */
public class RENListComboItem {
    
        public static RENListComboItem[] create(List<RENList> renLists){
        List<RENListComboItem> ret = new ArrayList<RENListComboItem>();
        for(RENList renList: renLists){
            ret.add(new RENListComboItem(renList));
        }
        RENListComboItem[] _ren = ret.toArray(new RENListComboItem[ret.size()]);
        Arrays.sort(_ren, new Comparator());
        return _ren;
    }
    
    private RENList renList;
    
    public RENListComboItem(RENList renList){
        this.renList = renList;
    }

    public RENList getRenList() {
        return renList;
    }
    
    @Override
    public String toString(){
        return this.renList.getName();
    }
    
    static class Comparator implements java.util.Comparator<RENListComboItem>{

        @Override
        public int compare(RENListComboItem o1, RENListComboItem o2) {
            int ret = o1.getRenList().getName().compareToIgnoreCase(o2.getRenList().getName());
            return ret;
        }
    }
}
