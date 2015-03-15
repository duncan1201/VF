/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.LocUtil;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class RulerLoc extends Loc {

    private int offset;

    public RulerLoc() {
    }

    public RulerLoc(int start, int end, int offset) {
        super(start, end);
        this.offset = offset;
    }

    public int getAbsoluteStart() {
        return offset + 1;
    }

    public int toRelativePos(int absolutePos) {
        if (getStart() <= getEnd()) {
            return absolutePos + getStart() - 1 - getOffset();
        } else {
            int ret = absolutePos + getStart() - 1 - getOffset();
            if (ret <= getTotalPos()) {
                return ret;
            } else {
                return ret % getTotalPos();
            }
        }
    }

    public int getAbsoluteEnd() {
        return offset + getEnd() - getStart() + 1;
    }

    public RulerLoc(int start, int end) {
        this(start, end, 0);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean intersect(RulerLoc rulerLoc) {
        int start = 1 + getOffset();
        int end = width() + getOffset();
        int start2 = 1 + rulerLoc.getOffset();
        int end2 = rulerLoc.width() + rulerLoc.getOffset();
        return LocUtil.intersect(start, end, start2, end2).size() > 0;
    }

    public static class Sorter implements Comparator<RulerLoc> {

        @Override
        public int compare(RulerLoc o1, RulerLoc o2) {
            int ret = 0;
            Integer start1 = o1.getStart() + o1.getOffset();
            Integer start2 = o2.getStart() + o2.getOffset();
            ret = start1.compareTo(start2);
            if (ret == 0) {
                Integer width1 = o1.width();
                Integer width2 = o2.width();
                ret = width1.compareTo(width2) * -1;
            }
            return ret;
        }
    }
}
