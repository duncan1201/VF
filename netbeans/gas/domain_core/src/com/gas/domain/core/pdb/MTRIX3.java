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
public class MTRIX3 implements Cloneable {

    public final static String RECORD_NAME = "MTRIX3 ";
    private Integer hibernateId;
    private Integer serial;
    private Float m1;
    private Float m2;
    private Float m3;
    private Float v;
    private Integer iGiven;

    @Override
    public MTRIX3 clone() {
        MTRIX3 ret = new MTRIX3();
        ret.setM1(m1);
        ret.setM2(m2);
        ret.setM3(m3);
        ret.setSerial(serial);
        ret.setV(v);
        ret.setiGiven(iGiven);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Integer getiGiven() {
        return iGiven;
    }

    public void setiGiven(Integer iGiven) {
        this.iGiven = iGiven;
    }

    public Float getM1() {
        return m1;
    }

    public void setM1(Float m1) {
        this.m1 = m1;
    }

    public Float getM2() {
        return m2;
    }

    public void setM2(Float m2) {
        this.m2 = m2;
    }

    public Float getM3() {
        return m3;
    }

    public void setM3(Float m3) {
        this.m3 = m3;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Float getV() {
        return v;
    }

    public void setV(Float v) {
        this.v = v;
    }

    /*
     Record Format
     COLUMNS DATA TYPE FIELD DEFINITION
     -------------------------------------------------------------------------------
     1 - 6 Record name "MTRIXn" n=1, 2, or 3
     8 - 10 Integer serial Serial number.
     11 - 20 Real(10.6) m[n][1] Mn1
     21 - 30 Real(10.6) m[n][2] Mn2
     31 - 40 Real(10.6) m[n][3] Mn3
     46 - 55 Real(10.5) v[n] Vn
     60 Integer iGiven 1 if coordinates for the representations
     which are approximately related by the
     transformations of the molecule are
     contained in the entry. Otherwise, blank.          
     */
    public void parse(String line) {
        serial = StrUtil.substring(line, 8, 10, Integer.class, false, false);
        m1 = StrUtil.substring(line, 11, 20, Float.class, false, false);
        m2 = StrUtil.substring(line, 21, 30, Float.class, false, false);
        m3 = StrUtil.substring(line, 31, 40, Float.class, false, false);
        v = StrUtil.substring(line, 46, 55, Float.class, false, false);
        iGiven = StrUtil.substring(line, 60, 60, Integer.class, false, false);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
        StrUtil.replace(ret, serial, 8, 10, true);
        StrUtil.replace(ret, m1, 11, 20, true);
        StrUtil.replace(ret, m2, 21, 30, true);
        StrUtil.replace(ret, m3, 31, 40, true);
        StrUtil.replace(ret, v, 46, 55, true);
        StrUtil.replace(ret, iGiven, 60, 60, true);
        StrUtil.replace(ret, '\n', 81, 81, true);
        return ret.toString();
    }
}
