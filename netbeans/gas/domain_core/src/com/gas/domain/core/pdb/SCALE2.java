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
public class SCALE2 implements Cloneable {

    public final static String RECORD_NAME = "SCALE2 ";
    private Integer hibernateId;
    private Float s1;
    private Float s2;
    private Float s3;
    private Float u;

    @Override
    public SCALE2 clone() {
        SCALE2 ret = new SCALE2();
        ret.setS1(s1);
        ret.setS2(s2);
        ret.setS3(s3);
        ret.setU(u);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Float getS1() {
        return s1;
    }

    public void setS1(Float s1) {
        this.s1 = s1;
    }

    public Float getS2() {
        return s2;
    }

    public void setS2(Float s2) {
        this.s2 = s2;
    }

    public Float getS3() {
        return s3;
    }

    public void setS3(Float s3) {
        this.s3 = s3;
    }

    public Float getU() {
        return u;
    }

    public void setU(Float u) {
        this.u = u;
    }

    /*
     COLUMNS DATA TYPE FIELD DEFINITION
     ------------------------------------------------------------------
     1 - 6 Record name "SCALEn" n=1, 2, or 3
     11 - 20 Real(10.6) s[n][1] Sn1
     21 - 30 Real(10.6) s[n][2] Sn2
     31 - 40 Real(10.6) s[n][3] Sn3
     46 - 55 Real(10.5) u[n] Un     
     */
    public void parse(String line) {
        s1 = StrUtil.substring(line, 11, 20, Float.class, false, false);
        s2 = StrUtil.substring(line, 21, 30, Float.class, false, false);
        s3 = StrUtil.substring(line, 31, 40, Float.class, false, false);
        u = StrUtil.substring(line, 46, 55, Float.class, false, false);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
        StrUtil.replace(ret, s1, 11, 20, false);
        StrUtil.replace(ret, s2, 21, 30, false);
        StrUtil.replace(ret, s3, 31, 40, false);
        StrUtil.replace(ret, u, 46, 55, false);
        StrUtil.replace(ret, '\n', 81, 81, false);
        return ret.toString();
    }
}
