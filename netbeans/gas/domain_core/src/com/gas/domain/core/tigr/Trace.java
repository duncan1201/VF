/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.MathUtil;
import java.io.Serializable;

/**
 *
 * @author dq
 */
public class Trace implements Serializable, Cloneable {

    private transient Integer hibernateId;
    private String name;
    private transient int[] data;// do not persist
    private String dataStr;// for hibernate usage
    private Integer max;

    public Trace() {
    }

    public Trace(String name, int[] data) {
        this.name = name;
        this.data = data;
        dataStr = CommonUtil.toString(data, Character.MAX_RADIX);
    }

    @Override
    public Trace clone() {
        Trace ret = new Trace();
        ret.setData(CommonUtil.copyOf(data));
        ret.setDataStr(dataStr);
        ret.setName(name);        
        return ret;
    }

    /*
     * for hibernate use only
     */
    protected String getDataStr() {
        return dataStr;
    }

    /*
     * for hibernate use only
     */
    protected void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public Integer getMax() {
        if (max == null) {
            data = getData();
            max = MathUtil.max(data);
        }
        return max;
    }

    public IntList toIntegerList() {
        IntList ret = new IntList();
        ret.setName(name);
        int[] _data = getData();
        for (int i : _data) {
            ret.add(i);
        }
        return ret;
    }

    /*
     * Not for hibernate use
     */
    public int[] getData() {
        if (data == null && dataStr != null) {
            data = CommonUtil.toIntArray(dataStr, Character.MAX_RADIX);
        }
        return data;
    }

    /*
     * Not for hibernate use
     */
    public void setData(int[] data) {
        this.data = data;
        dataStr = CommonUtil.toString(data, Character.MAX_RADIX);
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Trace reverse() {
        Trace ret = new Trace();
        int[] reversedData = CommonUtil.reverse(data);
        ret.setData(reversedData);
        String rName = BioUtil.reverseComplementByName(name);
        ret.setName(rName);
        ret.max = getMax();
        return ret;
    }
}
