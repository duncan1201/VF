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
public class HEADER implements Cloneable {

    public final static String RECORD_NAME = "HEADER ";
    private Integer hibernateId;
    private String classification;
    private String depDate;
    private String idCode;

    public HEADER() {
    }

    @Override
    public HEADER clone() {
        HEADER ret = new HEADER();
        ret.setClassification(classification);
        ret.setDepDate(depDate);
        ret.setIdCode(idCode);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDepDate() {
        return depDate;
    }

    public void setDepDate(String depDate) {
        this.depDate = depDate;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    /*
     Record Format
     COLUMNS DATA TYPE FIELD DEFINITION
     ------------------------------------------------------------------------------------
     1 - 6 Record name "HEADER"
     11 - 50 String(40) classification Classifies the molecule(s).
     51 - 59 Date depDate Deposition date. This is the date the
     coordinates were received at the PDB.
     63 - 66 IDcode idCode This identifier is unique within the
     PDB.     
     */
    public void parse(String line) {
        classification = StrUtil.substring(line, 11, 50, String.class, false, false);
        depDate = StrUtil.substring(line, 51, 59, String.class, false, false);
        idCode = StrUtil.substring(line, 63, 66, String.class, false, false);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
        StrUtil.replace(ret, classification, 11, 50, true);
        StrUtil.replace(ret, depDate, 51, 59, true);
        StrUtil.replace(ret, idCode, 63, 66, true);
        StrUtil.replace(ret, "\n", 81, 81, true);
        return ret.toString();
    }
}
