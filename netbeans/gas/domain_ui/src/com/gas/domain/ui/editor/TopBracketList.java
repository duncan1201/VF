/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import com.gas.domain.core.ren.RMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class TopBracketList extends ArrayList<TopBracket> {
    public RMap.EntryList getEntries(){
        RMap.EntryList ret = new RMap.EntryList();
        Iterator<TopBracket> itr = iterator();
        while(itr.hasNext()){
            TopBracket tp = itr.next();
            ret.add((RMap.Entry)tp.getData());
        }
        return ret;
    }
}
