/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.LocUtil;

/**

 @author dq
 */
public class DegreeFloatList extends FloatList {

    private final int NEAR_ORIGIN = 30;
    
    public DegreeFloatList() {
        setAutoStatistics(false);
    }

    public Float count() {
        Float ret = 0f;
        Float prev = null;
        for (int i = 0; i < size(); i++) {
            Float cur = get(i);
            if (prev != null) {
                int crossOrigin = crossOrigin(cur.floatValue(), prev.floatValue());
                if (crossOrigin > 0) {
                    ret += (360 - Math.max(cur, prev)) + Math.min(cur, prev);
                } else if (crossOrigin < 0) {
                    ret -= (360 - Math.max(cur, prev)) + Math.min(cur, prev);
                } else {
                    ret += cur - prev;
                }
            }
            prev = cur;
        }
        return ret;
    }

    public void addIfDiffDirection(Float deg) {
        Float last = last();
        Float secondToLast = secondToLast();
        if (secondToLast == null) {
            add(deg);
        } else {
            if(nearOrigin(deg)){
                add(deg);
                return;
            }
            if (deg > last && last >= secondToLast) {
                removeLast();
                add(deg);
            } else if (deg < last && last <= secondToLast) {
                removeLast();
                add(deg);
            } else if (deg.intValue() != last.intValue()) {
                add(deg);
            }
        }
    }

    public void addIfNotSameAsLast(Float deg) {
        if (isEmpty()) {
            add(deg);
        } else {
            Float last = last();
            if (last.intValue() != deg.intValue()) {
                add(deg);
            }
        }
    }

    public boolean isClockwise() {
        Float count = count();
        return count >= 0;
    }

    /**
     @return 1 if from left of origin to right of origin
             -1 if from right of origin to left of origin
             0 no crossing origin
     */
    private int crossOrigin(Float cur, Float prev) {
        int ret = 0;
        if (LocUtil.contains(360 - NEAR_ORIGIN, 360, prev) && LocUtil.contains(0, NEAR_ORIGIN, cur)) {
            ret = 1;
        } else if (LocUtil.contains(330 - NEAR_ORIGIN, 360, cur) && LocUtil.contains(0, NEAR_ORIGIN, prev)) {
            ret = -1;
        }
        return ret;
    }
    
    private boolean nearOrigin(float pos){
        return LocUtil.contains(360 - (NEAR_ORIGIN + 10), 360, pos) || LocUtil.contains(0, (NEAR_ORIGIN + 10), pos);
    }
}
