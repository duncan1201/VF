/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.orf.api;

import com.gas.common.ui.util.CommonUtil;

/**
 *
 * @author dq
 */
public class ORF implements Cloneable {

    private Integer hibernateId;
    private int startPos;
    private int endPos;
    private int length;
    private int frame;
    private boolean strand;

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public ORF clone() {
        ORF ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public boolean isStrand() {
        return strand;
    }

    public void setStrand(boolean strand) {
        this.strand = strand;
    }
}
