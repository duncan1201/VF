/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.StrUtil;

/**
 *
 * @author dq
 */
public class ORIGX3 implements Cloneable {

    public final static String RECORD_NAME = "ORIGX3";
    private Integer hibernateId;
    private Float o1;
    private Float o2;
    private Float o3;
    private Float t;

    @Override
    public ORIGX3 clone() {
        ORIGX3 ret = new ORIGX3();
        ret.setO1(o1);
        ret.setO2(o2);
        ret.setO3(o3);
        ret.setT(t);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Float getO1() {
        return o1;
    }

    public void setO1(Float o1) {
        this.o1 = o1;
    }

    public Float getO2() {
        return o2;
    }

    public void setO2(Float o2) {
        this.o2 = o2;
    }

    public Float getO3() {
        return o3;
    }

    public void setO3(Float o3) {
        this.o3 = o3;
    }

    public Float getT() {
        return t;
    }

    public void setT(Float t) {
        this.t = t;
    }

    /*
     Record Format
     COLUMNS DATA TYPE FIELD DEFINITION
     ----------------------------------------------------------------
     1 - 6 Record name "ORIGXn" n=1, 2, or 3
     11 - 20 Real(10.6) o[n][1] On1
     21 - 30 Real(10.6) o[n][2] On2
     31 - 40 Real(10.6) o[n][3] On3
     46 - 55 Real(10.5) t[n] Tn     
     */
    public void parse(String line) {
        o1 = StrUtil.substring(line, 11, 20, Float.class, false, false);
        o2 = StrUtil.substring(line, 21, 30, Float.class, false, false);
        o3 = StrUtil.substring(line, 31, 40, Float.class, false, false);
        t = StrUtil.substring(line, 46, 55, Float.class, false, false);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
        StrUtil.replace(ret, o1, 11, 20, true);
        StrUtil.replace(ret, o2, 21, 30, true);
        StrUtil.replace(ret, o3, 31, 40, true);
        StrUtil.replace(ret, t, 46, 55, true);
        StrUtil.replace(ret, '\n', 81, 81, true);
        return ret.toString();
    }
}
