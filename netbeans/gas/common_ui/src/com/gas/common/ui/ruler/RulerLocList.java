/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class RulerLocList extends ArrayList<RulerLoc> {

    public RulerLocList() {
    }

    public RulerLocList(RulerLoc loc) {
        add(loc);
    }

    /**
     * @return 1-based
     */
    int getRowNo(RulerLoc loc) {
        int ret = 1;
        Iterator<RulerLoc> itr = iterator();
        while (itr.hasNext()) {
            RulerLoc cur = itr.next();
            if (loc == cur) {
                break;
            }
            if (loc.intersect(cur)) {
                ret++;
            }
        }
        return ret;
    }

    int getRowCount() {
        int intersectCount = 0;
        for (int i = 0; i < size(); i++) {
            RulerLoc cur = get(i);
            RulerLoc next = null;
            if ((i + 1) < size()) {
                next = get(i + 1);
                intersectCount += cur.intersect(next) ? 1 : 0;
            }
        }
        return intersectCount + 1;
    }
}
