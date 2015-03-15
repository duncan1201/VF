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
public class CRYST1 implements Cloneable {

    public final static String RECORD_NAME = "CRYST1 ";
    private Integer hibernateId;
    private Float a;
    private Float b;
    private Float c;
    private Float alpha;
    private Float beta;
    private Float gamma;
    private String sGroup;
    private Integer z;

    @Override
    public CRYST1 clone() {
        CRYST1 ret = new CRYST1();
        ret.setA(a);
        ret.setAlpha(alpha);
        ret.setB(b);
        ret.setBeta(beta);
        ret.setC(c);
        ret.setGamma(gamma);
        ret.setZ(z);
        ret.setsGroup(sGroup);
        return ret;
    }

    public Float getA() {
        return a;
    }

    public void setA(Float a) {
        this.a = a;
    }

    public Float getAlpha() {
        return alpha;
    }

    public void setAlpha(Float alpha) {
        this.alpha = alpha;
    }

    public Float getB() {
        return b;
    }

    public void setB(Float b) {
        this.b = b;
    }

    public Float getBeta() {
        return beta;
    }

    public void setBeta(Float beta) {
        this.beta = beta;
    }

    public Float getC() {
        return c;
    }

    public void setC(Float c) {
        this.c = c;
    }

    public Float getGamma() {
        return gamma;
    }

    public void setGamma(Float gamma) {
        this.gamma = gamma;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getsGroup() {
        return sGroup;
    }

    public void setsGroup(String sGroup) {
        this.sGroup = sGroup;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    /*
     Record Format
     COLUMNS DATA TYPE FIELD DEFINITION
     -------------------------------------------------------------
     1 - 6 Record name "CRYST1"
     7 - 15 Real(9.3) a a (Angstroms).
     16 - 24 Real(9.3) b b (Angstroms).
     25 - 33 Real(9.3) c c (Angstroms).
     34 - 40 Real(7.2) alpha alpha (degrees).
     41 - 47 Real(7.2) beta beta (degrees).
     48 - 54 Real(7.2) gamma gamma (degrees).
     56 - 66 LString sGroup Space group.
     67 - 70 Integer z Z value.     
     */
    public void parse(String line) {
        a = StrUtil.substring(line, 7, 15, Float.class, false, false);
        b = StrUtil.substring(line, 16, 24, Float.class, false, false);
        c = StrUtil.substring(line, 25, 33, Float.class, false, false);
        alpha = StrUtil.substring(line, 34, 40, Float.class, false, false);
        beta = StrUtil.substring(line, 41, 47, Float.class, false, false);
        gamma = StrUtil.substring(line, 48, 54, Float.class, false, false);
        sGroup = StrUtil.substring(line, 56, 66, String.class, false, false);
        z = StrUtil.substring(line, 67, 70, Integer.class, false, false);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
        StrUtil.replace(ret, a, 7, 15, false);
        StrUtil.replace(ret, b, 16, 24, false);
        StrUtil.replace(ret, c, 25, 33, false);
        StrUtil.replace(ret, alpha, 34, 40, false);
        StrUtil.replace(ret, beta, 41, 47, false);
        StrUtil.replace(ret, gamma, 48, 54, false);
        StrUtil.replace(ret, sGroup, 56, 66, false);
        StrUtil.replace(ret, z, 67, 70, false);
        StrUtil.replace(ret, '\n', 81, 81, true);
        return ret.toString();
    }
}
