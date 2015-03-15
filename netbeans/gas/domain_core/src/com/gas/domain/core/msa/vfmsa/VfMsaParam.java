/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.vfmsa;

import com.gas.common.ui.util.CommonUtil;

/**
 *
 * @author dq
 */
public class VfMsaParam implements Cloneable{
    
    private Integer hibernateId;
    private String alignType;
    private boolean identicalOnly;
    private String matrixName;
    private int openPenalty = 10;
    private int extPenalty = 1;

    @Override
    public VfMsaParam clone(){
        VfMsaParam ret = CommonUtil.cloneSimple(this);
        return ret;
    }
    
    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }
            
    public void touchAll(){
        this.getAlignType();
        this.getAlignTypeEnum();
        this.getExtPenalty();
        this.getMatrixName();
        this.getOpenPenalty();
    }

    public AlignType getAlignTypeEnum() {
        AlignType ret = null;
        if(alignType != null){
            ret = AlignType.valueOf(alignType);
        }
        return ret;
    }

    public void setAlignTypeEnum(AlignType alignTypeEnum) {
        this.alignType = alignTypeEnum.toString();
    }
    
    /**
     * for hibernate use only
     */
    protected String getAlignType() {
        return alignType;
    }        

    /**
     * for hibernate use only
     */
    protected void setAlignType(String alignType) {
        this.alignType = alignType;
    }

    public boolean isIdenticalOnly() {
        return identicalOnly;
    }

    public void setIdenticalOnly(boolean identicalOnly) {
        this.identicalOnly = identicalOnly;
    }
    
    public SubMatrix getMatrix() {
        SubMatrix ret = null;
        if(matrixName != null){
            ret = SubMatrix.valueOf(matrixName);
        }
        return ret;
    }

    public void setMatrix(SubMatrix matrix) {
        this.matrixName = matrix.toString();
    }
        
    /**
     * for hibernate use only
     */
    protected String getMatrixName() {
        return matrixName;
    }

    /**
     * for hibernate use only
     */
    protected void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }

    public int getOpenPenalty() {
        return openPenalty;
    }

    public void setOpenPenalty(int openPenalty) {
        this.openPenalty = openPenalty;
    }

    public int getExtPenalty() {
        return extPenalty;
    }

    public void setExtPenalty(int extPenalty) {
        this.extPenalty = extPenalty;
    }
}
