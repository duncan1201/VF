/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.ruler.RulerLoc;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.LocUtil;

/**
 *
 * @author dq
 */
public class ParentLoc implements Cloneable {

    private Integer hibernateId;
    // start in parent's ruler
    private Integer start;
    // end in parent's ruler
    private Integer end;
    private Integer offset;
    private Integer totalPos;

    public ParentLoc() {
    }

    /**
     * @param start the start of the parent
     * @param end the end of the parent
     * @param offset the offset in the child context
     */
    public ParentLoc(Integer start, Integer end, Integer offset) {
        this.start = start;
        this.end = end;
        this.offset = offset;
    }

    public ParentLoc(Integer start, Integer end) {
        this(start, end, 0);
    }

    @Override
    public ParentLoc clone() {
        ParentLoc ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    public Integer getTotalPos() {
        return totalPos;
    }

    public void setTotalPos(Integer totalPos) {
        this.totalPos = totalPos;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public boolean isSubsetOf(int _start, int _end) {
        return LocUtil.isSubsetOf(start, end, _start, _end);
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    /**
     * <ul>is <b>superset</b> of point 2
     * <li>if both linear, i.e.:
     * <b>case a)</b>
     * <pre>
     * ++++++++++
     * &&[del]&&&</pre>, then {@code end -= width2}
     * </ul>
     * <ul><b>Overlaps</b>
     * <li>if both linear, then <b>case b)</b>
     * <pre>++++++++++++++
     * &&[&&&  ]</pre>, then {@code end -= (getChildEnd() - start2 + 1)}
     * <li>if both linear, then <b>case c)</b>
     * <pre>
     * ++++++++++++++
     * [  &&]&&&&
     * </pre>, then
     * {@code start -= (end2 -getChildStart() + 1); offset -= width2;}
     * </ul>
     * <ul>No <b>overlaps</b>
     * <li>both linear,
     * <b>case d)</b><pre>
     * ++++++++++
     * [del]&&&&&,</pre>then {@code offset-=width2;}
     * </ul>
     *
     * @param _start in the context of the child
     * @param _end in the context of the child*
     */
    public void removeSeq(final int start2, final int end2) {
        if (isSupersetOf(start2, end2)) {
            if (start <= end) {
                // case a)
                if (start2 <= end2) {
                    end -= (end2 - start2 + 1);
                }
            }
        } else if (isOverlapped(start2, end2)) {
            // case b)
            if (getChildStart() < start2) {
                end -= (getChildEnd() - start2 + 1);
            }// case c)
            else if (getChildStart() > start2) {
                start -= (end2 - getChildStart() + 1);
                offset -= (end2 - start2 + 1);
            }
        } else {// no overlaps
            if (getChildStart() > end2) {
                int width2 = end2 - start2 + 1;
                offset -= width2;
            }
        }
    }

    /**
     * <ul>Overlaps of the insertion point
     * <li><b>case a)</b>
     * <pre>
     * +++++++++++
     * &&[ins]&&
     * </pre>, then {@code end += width2}
     * </ul>
     * <ul>No overlaps
     * <li>case b)
     * <pre>
     * ++++++++++++
     * [ins]&&&&&
     * </pre>, then {@code offset += width2}
     * </ul>
     */
    void insertSeq(int pos, int length) {
        // case a)
        if(LocUtil.contains(getChildStart(), getChildEnd(), pos)){
            end += length;
        }// case b)
        else{
            if(getChildStart() > pos){
                offset += length;
            }
        }
    }        

    private int getChildStart() {
        return offset + 1;
    }

    private int getChildEnd() {
        return getOffset() + (end - start + 1);
    }

    /**
     * @param _start in the context of the child
     * @param _end in the context of the child
     */
    public boolean isOverlapped(int _start, int _end) {
        return LocUtil.isOverlapped(getChildStart(), getChildEnd(), _start, _end);
    }

    /**
     * @param _start in the context of the child
     * @param _end in the context of the child
     */
    public boolean isSupersetOf(int _start, int _end) {
        return LocUtil.isSupersetOf(getChildStart(), getChildEnd(), _start, _end);
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public RulerLoc toRulerLoc() {
        RulerLoc ret = new RulerLoc();
        ret.setStart(start);
        ret.setEnd(end);
        ret.setOffset(offset);
        ret.setTotalPos(totalPos);
        return ret;
    }
}
