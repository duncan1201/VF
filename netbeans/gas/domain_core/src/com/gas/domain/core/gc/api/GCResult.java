/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.gc.api;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.util.CommonUtil;

/**
 *
 * @author dq
 */
public class GCResult implements Cloneable {

    private Integer hibernateId;
    private int windowSize;
    private float[] contents;

    @Override
    public GCResult clone() {
        GCResult ret = CommonUtil.cloneSimple(this);
        ret.setContents(CommonUtil.copyOf(contents));
        return ret;
    }

    protected Integer getHibernateId() {
        return hibernateId;
    }

    protected void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public void touchAll() {
        int a = contents.length;
    }

    public boolean isEmpty() {
        return contents == null || contents.length == 0;
    }

    public void clearResult() {
        contents = null;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public float[] getContents() {
        return contents;
    }

    public void setContents(float[] contents) {
        this.contents = contents;
    }

    public FloatList getPaddedContents() {
        return getPaddedContents(true);
    }

    public FloatList getPaddedContents(boolean padding) {
        FloatList ret = new FloatList();
        if (contents == null) {
            return ret;
        }

        for (int i = 0; padding && i < windowSize / 2; i++) {
            ret.add(null);
        }
        for (int i = 0; i < contents.length; i++) {
            ret.add(contents[i]);
        }
        for (int i = 0; padding && i < windowSize / 2; i++) {
            ret.add(null);
        }
        return ret;
    }
}
