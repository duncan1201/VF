/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.edit;

import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class RENComboItem {
    
    public static RENComboItem[] create(RENList renList){
        List<RENComboItem> ret = new ArrayList();
        Iterator<REN> itr = renList.getIterator();
        while(itr.hasNext()){
            REN ren = itr.next();
            ret.add(new RENComboItem(ren));
        }
        RENComboItem[] _ret = ret.toArray(new RENComboItem[ret.size()]);
        return _ret;
    }
    
    private REN ren;
    public RENComboItem(REN ren){
        this.ren = ren;
    }

    public REN getRen() {
        return ren;
    }
    
    @Override
    public String toString(){
        return ren.getName();
    }
}
