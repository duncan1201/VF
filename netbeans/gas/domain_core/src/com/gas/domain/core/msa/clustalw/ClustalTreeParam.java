/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.clustalw;

import com.gas.domain.core.msa.clustalw.GeneralParam.CLUSTERING;
import com.gas.domain.core.msa.clustalw.GeneralParam.OUTPUT;
import java.io.File;

/**
 *
 * @author dq
 */
public class ClustalTreeParam {
    /*
     ***Trees:***
     -OUTPUTTREE=nj OR phylip OR dist OR nexus
     -SEED=n        :seed number for bootstraps.
     -KIMURA        :use Kimura's correction.   
     -TOSSGAPS      :ignore positions with gaps.
     -BOOTLABELS=node OR branch :position of bootstrap values in tree display
     -CLUSTERING=   :NJ or UPGMA 
     */

    public ClustalTreeParam() {
    }
    private Integer hibernateId;
    private transient File infile;
    private OUTPUT output = OUTPUT.PHYLIP;
    private Integer seed;
    private boolean kimura = false;
    private boolean tossGaps = false;
    private String clustering = CLUSTERING.NJ.getName();

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("-TREE");
        ret.append(' ');
        if (infile != null) {
            ret.append("-INFILE=");
            ret.append(infile.getAbsolutePath());
        }
        ret.append(' ');
        if (output != null) {
            ret.append("-OUTPUTTREE=");
            ret.append(output);
            ret.append(' ');
        }
        if (kimura) {
            ret.append("-KIMURA");
            ret.append(' ');
        }
        if (tossGaps) {
            ret.append("-TOSSGAPS");
            ret.append(' ');
        }
        ret.append("-CLUSTERING=");
        ret.append(clustering);
        return ret.toString();
    }

    public void setInfile(File infile) {
        this.infile = infile;
    }

    public File getInfile() {
        return infile;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    /**
     * for hibernate use only
     */
    protected String getClustering() {
        return clustering;
    }

    /**
     * for hibernate use only
     */
    protected void setClustering(String clustering) {
        this.clustering = clustering;
    }

    public CLUSTERING getClusteringEnum() {
        return CLUSTERING.get(clustering);
    }

    public void setClusteringEnum(CLUSTERING clusteringEnum) {
        this.clustering = clusteringEnum.getName();
    }

    public boolean isKimura() {
        return kimura;
    }

    public void setKimura(boolean kimura) {
        this.kimura = kimura;
    }

    public OUTPUT getOutputTree() {
        return output;
    }

    public void setOutputTree(OUTPUT outputTree) {
        this.output = outputTree;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public boolean isTossGaps() {
        return tossGaps;
    }

    public void setTossGaps(boolean tossGaps) {
        this.tossGaps = tossGaps;
    }
}
