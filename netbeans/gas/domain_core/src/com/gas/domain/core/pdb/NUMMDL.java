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
public class NUMMDL implements Cloneable {

    public final static String RECORD_NAME = "NUMMDL ";
    private Integer hibernateId;
    private Integer modelNumber;

    @Override
    public NUMMDL clone() {
        NUMMDL ret = new NUMMDL();
        ret.setModelNumber(modelNumber);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Integer getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(Integer modelNumber) {
        this.modelNumber = modelNumber;
    }
    /*
     Record Format
     COLUMNS DATA TYPE FIELD DEFINITION
     ------------------------------------------------------------------------------------
     1 - 6 Record name "NUMMDL"
     11 - 14 Integer modelNumber Number of models.     
     */

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
        StrUtil.replace(ret, modelNumber, 11, 14, true);
        StrUtil.replace(ret, '\n', 81, 81, true);
        return ret.toString();
    }

    public void parse(String line) {
        modelNumber = StrUtil.substring(line, 11, 14, Integer.class, false, false);
    }
}
