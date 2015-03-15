/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.util.LocUtil;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class OverlapSeq {

    int start;
    int end;
    int totalLength;
    String seq;

    public OverlapSeq() {
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public Integer length() {
        Double ret = LocUtil.width(start, end, totalLength);
        return ret.intValue();
    }

    static class LengthComparator implements Comparator<OverlapSeq> {

        @Override
        public int compare(OverlapSeq o1, OverlapSeq o2) {
            int ret = o1.length().compareTo(o2.length());
            return ret;
        }
    }
}
