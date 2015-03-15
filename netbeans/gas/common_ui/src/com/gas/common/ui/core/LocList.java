/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import com.gas.common.ui.misc.Loc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author dunqiang
 */
public class LocList extends ArrayList<Loc> {

    public LocList() {
    }

    public LocList(Loc loc) {
        add(loc);
    }

    public Loc intersect(int start, int end) {
        Loc ret = null;
        for (int i = 0; i < size(); i++) {
            Loc loc = get(i);
            boolean overlaps = loc.isOverlapped(start, end);
            if (overlaps) {
                ret = loc;
                break;
            }
        }
        return ret;
    }

    public void removeBefore(int pos) {
        Iterator<Loc> itr = iterator();
        while (itr.hasNext()) {
            Loc loc = itr.next();
            if (loc.contains(pos)) {
                itr.remove();
            }
        }
    }

    public void removeAfter(int loc) {
    }

    public boolean isSupersetOf(Loc loc) {
        boolean ret = false;
        for (int i = 0; i < size(); i++) {
            Loc l = get(i);
            ret = l.isSuperset(loc);
            if (ret) {
                break;
            }
        }
        return ret;
    }

    public Loc last() {
        if (size() != 0) {
            return get(size() - 1);
        } else {
            return null;
        }
    }

    public Loc first() {
        if (size() != 0) {
            return get(0);
        } else {
            return null;
        }
    }
    
    public boolean intersect(LocList locList){
        boolean ret = false;
        for(Loc loc : locList){
            ret = intersect(loc);
            if(ret){
                break;                
            }
        }
        return ret;
    }

    public boolean intersect(Loc another) {
        return intersect(another.getStart(), another.getEnd()) != null;
    }

    @Override
    public String toString() {
        return toString(true, true);
    }

    public String toString(boolean length, boolean strand) {
        StringBuilder ret = new StringBuilder();
        Iterator<Loc> itr = iterator();
        while (itr.hasNext()) {
            Loc loc = itr.next();
            ret.append(loc.toString(length, strand));
            if (itr.hasNext()) {
                ret.append(",");
            }
        }
        return ret.toString();
    }
}
