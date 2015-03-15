/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import com.gas.domain.core.nexus.api.Blk;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class BlkList extends ArrayList<Blk>{
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        Iterator<Blk> itr = iterator();
        while(itr.hasNext()){
            Blk blk = itr.next();
            ret.append(blk.toString());
            ret.append("\n\n");
        }
        return ret.toString();
    }
}
