/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.muscle;

import com.gas.common.ui.util.CommonUtil;
import java.io.File;

/**
 *
 * @author dq
 */
public class MuscleParam implements Cloneable {

    public enum SEQ_TYPE {

        AUTO, PROTEIN, nucleo
    };

    public enum OUT_FORMAT {

        msf, clw, clwstrict, fasta
    };
    private Integer hibernateId;
    private File in;
    private File in1;
    private File in2;
    private File out;
    private SEQ_TYPE seqType = SEQ_TYPE.AUTO;
    private Integer maxItrs = 16;
    private Integer maxTree = 1;
    private boolean anchorOpt = true;
    private boolean diagnalOpt = false;
    private OUT_FORMAT outFormat;

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (in != null) {
            ret.append("-in");
            ret.append(' ');
            ret.append(in.getAbsolutePath());
            ret.append(' ');
        }
        if (in1 != null) {
            ret.append("-profile");
            ret.append(' ');
        }
        if (in1 != null) {
            ret.append("-in1");
            ret.append(' ');
            ret.append(in1.getAbsolutePath());
            ret.append(' ');
        }
        if (in2 != null) {
            ret.append("-in2");
            ret.append(' ');
            ret.append(in2.getAbsolutePath());
            ret.append(' ');
        }
        ret.append("-seqtype");
        ret.append(' ');
        ret.append(seqType.toString());
        ret.append(' ');
        ret.append("-out");
        ret.append(' ');
        ret.append(out.getAbsolutePath());
        ret.append(' ');
        ret.append("-maxiters");
        ret.append(' ');
        ret.append(maxItrs);
        ret.append(' ');
        if (diagnalOpt) {
            ret.append("–diags1");
            ret.append(' ');
        }
        if (!anchorOpt) {
            ret.append("–noanchors");
            ret.append(' ');
        }
        ret.append("-maxtrees");
        ret.append(' ');
        ret.append(maxTree);
        ret.append(' ');
        ret.append("-quiet");
        ret.append(' ');
        if (outFormat != null && outFormat != OUT_FORMAT.fasta) {
            ret.append(' ');
            ret.append('-');
            ret.append(outFormat.toString());
            ret.append(' ');
        }
        return ret.toString();
    }
    
    public void touchAll(){
    }

    public Integer getMaxTree() {
        return maxTree;
    }

    public void setMaxTree(Integer maxTree) {
        this.maxTree = maxTree;
    }

    public boolean isAnchorOpt() {
        return anchorOpt;
    }

    public void setAnchorOpt(boolean anchorOpt) {
        this.anchorOpt = anchorOpt;
    }

    public boolean isDiagnalOpt() {
        return diagnalOpt;
    }

    public void setDiagnalOpt(boolean diagnalOpt) {
        this.diagnalOpt = diagnalOpt;
    }

    public void setIn1(File in1) {
        this.in1 = in1;
    }

    public void setIn2(File in2) {
        this.in2 = in2;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public MuscleParam clone() {
        MuscleParam ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    public File getIn() {
        return in;
    }

    public void setIn(File in) {
        this.in = in;
    }

    public Integer getMaxItrs() {
        return maxItrs;
    }

    public void setMaxItrs(Integer maxItrs) {
        this.maxItrs = maxItrs;
    }

    public File getOut() {
        return out;
    }

    public void setOut(File out) {
        this.out = out;
    }

    public void setSeqType(SEQ_TYPE seqType) {
        this.seqType = seqType;
    }

    public OUT_FORMAT getOutFormat() {
        return outFormat;
    }

    public void setOutFormat(OUT_FORMAT outFormat) {
        this.outFormat = outFormat;
    }
}
