/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.util.MathUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/**
 *
 * @author dunqiang
 */
public class Loc implements Comparable<Loc>, Cloneable {

    private Integer start;
    private Integer end;
    private Integer totalPos;
    private Boolean strand;

    public Loc() {
        this(0, 0, null);
    }

    public Loc(int start, int end) {
        this(start, end, null);
    }

    public Loc(int start, int end, boolean strand) {
        this(start, end, null, strand);
    }

    public Loc(int start, int end, Integer totalPos) {
        this(start, end, totalPos, null);
    }

    public Loc(int start, int end, Integer totalPos, Boolean strand) {
        this.start = start;
        this.end = end;
        this.totalPos = totalPos;
        this.strand = strand;
    }

    @Override
    public Loc clone() {
        Loc ret = new Loc();
        ret.setEnd(end);
        ret.setStart(start);
        ret.setStrand(strand);
        ret.setTotalPos(totalPos);
        return ret;
    }

    /*
     * return null if a pos lies within the loc's range
     */
    public Boolean towards(int pos) {
        Boolean ret = null;
        // assume forward strand first
        if (strand) {
            if (end < pos) {
                ret = true;
            } else if (start > pos) {
                ret = false;
            }
        } else {
            if (start > pos) {
                ret = true;
            } else if (end < pos) {
                ret = false;
            }
        }
        return ret;
    }

    public Loc reverseComplement() {
        Loc ret = new Loc();
        ret.start = totalPos - start + 1;
        ret.end = totalPos - end + 1;
        ret.totalPos = totalPos;
        if (strand != null) {
            ret.strand = !strand;
        }
        return ret;
    }

    public Loc StartEndSwitch() {
        Loc ret = new Loc();
        ret.start = end;
        ret.end = start;
        ret.totalPos = totalPos;
        ret.strand = strand;
        return ret;
    }

    public Boolean isStrand() {
        return strand;
    }

    public void setStrand(Boolean strand) {
        this.strand = strand;
    }

    public Integer getTotalPos() {
        return totalPos;
    }

    public void setTotalPos(Integer totalPos) {
        this.totalPos = totalPos;
    }

    public boolean contains(int x) {
        return LocUtil.contains(start, end, x);
    }

    public boolean isSuperset(Loc another) {
        return isSuperset(another.getStart(), another.getEnd());
    }

    public boolean isSuperset(int _start, int _end) {
        return LocUtil.isSupersetOf(start, end, _start, _end);
    }

    public boolean isSubset(int _start, int _end) {
        return LocUtil.isSubsetOf(start, end, _start, _end);
    }

    public boolean isSubset(Loc another) {
        return isSubset(another.getStart(), another.getEnd());
    }

    public LocList subtract(LocList list) {
        Collections.sort(list);
        LocList ret = new LocList();
        Iterator<Loc> itr = list.iterator();
        boolean first = true;

        Loc previous = null;
        Loc current = null;
        Loc next = null;

        if (current == null && itr.hasNext()) {
            current = itr.next();
        }

        while (current != null) {

            if (itr.hasNext()) {
                next = itr.next();
            } else {
                next = null;
            }

            Loc tmp;

            if (first) {
                tmp = new Loc(getStart(), current.getStart() - 1);
                first = false;
                ret.add(tmp);
            }

            if (next != null) {
                tmp = new Loc(current.getEnd() + 1, next.getStart() - 1);
            } else {
                tmp = new Loc(current.getEnd() + 1, getEnd());
            }
            ret.add(tmp);

            previous = next;
            current = next;
        }
        return ret;
    }

    public LocList subtract(Loc another) {
        LocList ret = new LocList();

        boolean equals = equals(another);

        if (!equals) {
            boolean intersect = isOverlapped(another);
            if (intersect) {
                if (isSuperset(another)) {
                    if (getStart() <= another.getStart() - 1) {
                        ret.add(new Loc(getStart(), another.getStart() - 1));
                    }
                    if (another.getEnd() + 1 <= getEnd()) {
                        ret.add(new Loc(another.getEnd() + 1, getEnd()));
                    }
                } else if (isSubset(another)) {
                    // do nothing
                } else {
                    if (another.getStart() > start && another.getStart() < end) {
                        ret.add(new Loc(start, another.getStart() - 1));
                    } else if (another.getEnd() > start && another.getEnd() < end) {
                        ret.add(new Loc(another.getEnd() + 1, getEnd()));
                    }
                }
            } else {
                ret.add(this);
            }
        }
        return ret;
    }

    public int center() {
        int width = width();
        if (start <= end) {
            if (width % 2 == 0) {
                return start + width / 2;
            } else {
                return MathUtil.round((start + width / 2));
            }
        } else if (totalPos != null) {
            int ret = 0;
            int dis = totalPos - start + 1;
            if (dis > end) { //
                ret = start + Math.round(width / 2.0f);
            } else { // 
                ret = end - Math.round(width / 2.0f);
            }
            return ret;
        } else {
            throw new IllegalArgumentException("Circular location and totalPos is not set!");
        }
    }

    public Integer getEnd() {
        return end;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getMin() {
        if (start == null || end == null) {
            return null;
        } else {
            return Math.min(start, end);
        }
    }
    
    public void setMax(int max){
        if(start > end){
            start = max;
        }else{
            end = max;
        }
    }

    public Integer getMax() {
        if (start == null || end == null) {
            return null;
        } else {
            return Math.max(start, end);
        }
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public int width() {
        if (start == 0 && end == 0) {
            return 0;
        } else if (start <= end) { // linear
            return end - start + 1;
        } else if (totalPos != null) { // circular
            return totalPos - start + 1 + end;
        } else {
            throw new IllegalArgumentException(String.format("Circular location and total pos not set.start: %d, end: %d", start, end));
        }
    }

    public boolean isOverlapped(Loc another) {
        return LocUtil.isOverlapped(start, end, another.getStart(), another.getEnd());
    }

    public boolean isOverlapped(int _start, int _end) {
        return LocUtil.isOverlapped(start, end, _start, _end);
    }

    public Loc getTranslated(int x) {
        Loc loc = new Loc(this.start, this.end);
        loc.start += x;
        loc.end += x;
        return loc;
    }

    @Override
    public String toString() {
        return toString(true, true);
    }

    public String toString(boolean length, boolean _strand) {
        StringBuilder ret = new StringBuilder();
        ret.append(start);
        ret.append(" bp");
        ret.append("-");
        ret.append(end);
        ret.append(" bp");
        if (length) {
            ret.append('(');
            ret.append(Math.abs(start - end) + 1);
            ret.append(')');
        }
        if (strand != null && _strand) {
            ret.append(strand);
        }
        return ret.toString();
    }

    @Override
    public int compareTo(Loc o) {
        int ret = 0;
        if (getStart() > o.getStart()) {
            ret = 1;
        } else if (getStart() < o.getStart()) {
            ret = -1;
        }
        return ret;
    }

    @Override
    public int hashCode() {
        return toString().toUpperCase(Locale.ENGLISH).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        if (o instanceof Loc) {
            Loc another = (Loc) o;
            if (another.getStart().equals(getStart()) && another.getEnd().equals(getEnd())) {
                ret = true;
            }
        }
        return ret;
    }
}
