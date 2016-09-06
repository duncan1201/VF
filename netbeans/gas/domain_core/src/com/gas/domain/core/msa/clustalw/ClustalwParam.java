/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.clustalw;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.msa.clustalw.GeneralParam.DNA_WEIGHT_MATRIX;
import com.gas.domain.core.msa.clustalw.GeneralParam.ITERATION;
import com.gas.domain.core.msa.clustalw.GeneralParam.PRO_WEIGHT_MATRIX;
import com.gas.domain.core.msa.clustalw.GeneralParam.TYPE;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dq
 */
public class ClustalwParam implements Cloneable {

    private Integer hibernateId;
    private transient DataParams dataParams;
    private Set<GeneralParam> generalParams = new HashSet<GeneralParam>();
    private Set<Fast> fasts = new HashSet<Fast>();
    private Set<Slow> slows = new HashSet<Slow>();
    private Set<Multiple> multiples = new HashSet<Multiple>();

    public ClustalwParam() {
        dataParams = new DataParams();
        setGeneralParam(new GeneralParam());
        setFast(new Fast());
        setSlow(new Slow());
        setMultiple(new Multiple());
    }

    @Override
    public ClustalwParam clone() {
        ClustalwParam ret = CommonUtil.cloneSimple(this);
        ret.setFasts(CommonUtil.copyOf(fasts));
        ret.setGeneralParams(CommonUtil.copyOf(generalParams));
        ret.setMultiples(CommonUtil.copyOf(multiples));
        ret.setSlows(CommonUtil.copyOf(slows));
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public String toString() {
        if (getGeneralParam().getType() == TYPE.DNA) {
            getMultiple().setProteinMatrixEnum(null);
        } else if (getGeneralParam().getType() == TYPE.PROTEIN) {
            getMultiple().setDnaMatrixEnum(null);
        }
        StringBuilder ret = new StringBuilder();
        ret.append("-ALIGN");
        ret.append(' ');
        ret.append(dataParams.toString());
        ret.append(' ');
        ret.append(getGeneralParam().toString());
        ret.append(' ');
        if (getGeneralParam().isQuickTree()) {
            ret.append(getFast().toString());
        } else {
            ret.append(getSlow().toString());
        }
        ret.append(' ');
        ret.append(getMultiple().toString(getGeneralParam().getType() == TYPE.DNA));
        return ret.toString();
    }

    public void touchAll() {
        if (getGeneralParam() != null) {
            getGeneralParam();
        }
        Iterator<Fast> itrFast = getFasts().iterator();
        while (itrFast.hasNext()) {
            itrFast.next();
        }

        Iterator<Slow> itrSlow = getSlows().iterator();
        while (itrSlow.hasNext()) {
            itrSlow.next();
        }
        Iterator<Multiple> itrMultiple = getMultiples().iterator();
        while (itrMultiple.hasNext()) {
            itrMultiple.next();
        }
    }

    public DataParams getDataParams() {
        return dataParams;
    }

    public final void setFast(Fast fast) {
        fasts.clear();
        fasts.add(fast);
    }

    public Fast getFast() {
        if (fasts.isEmpty()) {
            return null;
        } else {
            return fasts.iterator().next();
        }
    }

    /**
     * for hibernate use only
     */
    protected Set<GeneralParam> getGeneralParams() {
        return generalParams;
    }

    /**
     * for hibernate use only
     */
    protected void setGeneralParams(Set<GeneralParam> generalParams) {
        this.generalParams = generalParams;
    }

    public final void setGeneralParam(GeneralParam param) {
        this.generalParams.clear();
        this.generalParams.add(param);
    }

    public GeneralParam getGeneralParam() {
        if (generalParams.isEmpty()) {
            return null;
        } else {
            return generalParams.iterator().next();
        }
    }

    /**
     * for hibernate use only
     */
    protected Set<Fast> getFasts() {
        return fasts;
    }

    /**
     * for hibernate use only
     */
    protected void setFasts(Set<Fast> fasts) {
        this.fasts = fasts;
    }

    /**
     * for hibernate use only
     */
    protected Set<Slow> getSlows() {
        return slows;
    }

    /**
     * for hibernate use only
     */
    protected void setSlows(Set<Slow> slows) {
        this.slows = slows;
    }

    /**
     * for hibernate use only
     */
    protected Set<Multiple> getMultiples() {
        return multiples;
    }

    /**
     * for hibernate use only
     */
    protected void setMultiples(Set<Multiple> multiples) {
        this.multiples = multiples;
    }

    public Multiple getMultiple() {
        if (multiples.isEmpty()) {
            return null;
        } else {
            return multiples.iterator().next();
        }
    }

    public final void setMultiple(Multiple multiple) {
        //multiples.clear();
        //multiples.add(multiple);
        Set<Multiple> tmp = new HashSet<Multiple>();
        tmp.add(multiple);
        multiples = tmp;
    }

    public Slow getSlow() {
        if (slows.isEmpty()) {
            return null;
        } else {
            return slows.iterator().next();
        }
    }

    public final void setSlow(Slow slow) {
        this.slows.clear();
        this.slows.add(slow);
    }

    /*
     ***Fast Pairwise Alignments:***
     -KTUPLE=n    :word size
     -TOPDIAGS=n  :number of best diags.
     -WINDOW=n    :window around best diags.
     -PAIRGAP=n   :gap penalty
     -SCORE       :PERCENT or ABSOLUTE     
     */
    public static class Fast implements Cloneable {

        private Integer hibernateId;
        private static Integer DEFAULT_KTUPLE = 1;
        private static Integer DEFAULT_PAIRGAP = 3;
        private static Integer DEFAULT_TOPDIAGS = 5;
        private static Integer DEFAULT_WINDOWSIZE = 5;
        private Integer ktuple = DEFAULT_KTUPLE; //-KTUPLE=n    :word size
        private Integer pairGap = DEFAULT_PAIRGAP; //-PAIRGAP=n   :gap penalty
        private Integer topDiags = DEFAULT_TOPDIAGS; //-TOPDIAGS=n  :number of best diags.
        private Integer windowSize = DEFAULT_WINDOWSIZE; //-WINDOW=n    :window around best diags.

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        @Override
        public Fast clone() {
            Fast ret = CommonUtil.cloneSimple(this);
            return ret;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append("-KTUPLE=");
            ret.append(ktuple);
            ret.append(' ');
            ret.append("-PAIRGAP=");
            ret.append(pairGap);
            ret.append(' ');
            ret.append("-TOPDIAGS=");
            ret.append(topDiags);
            ret.append(' ');
            ret.append("-WINDOW=");
            ret.append(windowSize);
            ret.append(' ');
            return ret.toString();
        }

        public Integer getWindowSize() {
            return windowSize;
        }

        public void setWindowSize(Integer windowSize) {
            this.windowSize = windowSize;
        }

        public void setKtuple(Integer ktuple) {
            this.ktuple = ktuple;
        }

        public void setPairGap(Integer pairGap) {
            this.pairGap = pairGap;
        }

        public void setTopDiags(Integer topDiags) {
            this.topDiags = topDiags;
        }

        public Integer getKtuple() {
            return ktuple;
        }

        public Integer getPairGap() {
            return pairGap;
        }

        public Integer getTopDiags() {
            return topDiags;
        }
    }

    /*
     ***Slow Pairwise Alignments:***
     -PWMATRIX=    :Protein weight matrix=BLOSUM, PAM, GONNET, ID or filename
     -PWDNAMATRIX= :DNA weight matrix=IUB, CLUSTALW or filename
     -PWGAPOPEN=f  :gap opening penalty        
     -PWGAPEXT=f   :gap opening penalty
     */
    public static class Slow implements Cloneable {

        private Integer hibernateId;
        private static Double DEFAULT_GAPOPEN = 10.0;
        private static Double DEFAULT_GAPEXT = 0.1;
        private Double gapOpen = DEFAULT_GAPOPEN;
        private Double gapExt = DEFAULT_GAPEXT;
        private String dnaWeightMatrix = DNA_WEIGHT_MATRIX.IUB.toString();
        private String proWeightMatrix = PRO_WEIGHT_MATRIX.GONNET.toString();

        @Override
        public Slow clone() {
            Slow ret = CommonUtil.cloneSimple(this);
            return ret;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append("-PWGAPOPEN=");
            ret.append(gapOpen);
            ret.append(' ');
            ret.append("-PWGAPEXT=");
            ret.append(gapExt);
            ret.append(' ');
            ret.append("-PWDNAMATRIX=");
            ret.append(dnaWeightMatrix);
            ret.append(' ');
            ret.append("-PWMATRIX=");
            ret.append(proWeightMatrix);
            ret.append(' ');
            return ret.toString();
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public DNA_WEIGHT_MATRIX getDnaMatrixEnum() {
            return DNA_WEIGHT_MATRIX.get(dnaWeightMatrix);
        }

        public void setDnaMatrixEnum(DNA_WEIGHT_MATRIX dnaMatrix) {
            this.dnaWeightMatrix = dnaMatrix == null ? null : dnaMatrix.toString();
        }

        public Double getGapExt() {
            return gapExt;
        }

        public void setGapExt(Double gapExt) {
            this.gapExt = gapExt;
        }

        public Double getGapOpen() {
            return gapOpen;
        }

        public void setGapOpen(Double gapOpen) {
            this.gapOpen = gapOpen;
        }

        /**
         * for hibernate use only
         */
        protected String getDnaWeightMatrix() {
            return dnaWeightMatrix;
        }

        /**
         * for hibernate use only
         */
        protected void setDnaWeightMatrix(String dnaWeightMatrix) {
            this.dnaWeightMatrix = dnaWeightMatrix;
        }

        /**
         * for hibernate use only
         */
        protected String getProWeightMatrix() {
            return proWeightMatrix;
        }

        /**
         * for hibernate use only
         */
        protected void setProWeightMatrix(String proWeightMatrix) {
            this.proWeightMatrix = proWeightMatrix;
        }

        public PRO_WEIGHT_MATRIX getProteinMatrix() {
            return PRO_WEIGHT_MATRIX.get(proWeightMatrix);
        }

        public void setProteinMatrix(PRO_WEIGHT_MATRIX proteinMatrix) {
            this.proWeightMatrix = proteinMatrix == null ? null : proteinMatrix.toString();
        }
    }

    /*
     ***Multiple Alignments:***
     -NEWTREE=      :file for new guide tree
     -USETREE=      :file for old guide tree
     -MATRIX=       :Protein weight matrix=BLOSUM, PAM, GONNET, ID or filename
     -DNAMATRIX=    :DNA weight matrix=IUB, CLUSTALW or filename
     -GAPOPEN=f     :gap opening penalty        
     -GAPEXT=f      :gap extension penalty
     -ENDGAPS       :no end gap separation pen. 
     -GAPDIST=n     :gap separation pen. range
     -NOPGAP        :residue-specific gaps off  
     -NOHGAP        :hydrophilic gaps off
     -HGAPRESIDUES= :list hydrophilic res.    
     -MAXDIV=n      :% ident. for delay
     -TYPE=         :PROTEIN or DNA
     -TRANSWEIGHT=f :transitions weighting
     -ITERATION=    :NONE or TREE or ALIGNMENT
     -NUMITER=n     :maximum number of iterations to perform
     -NOWEIGHTS     :disable sequence weighting
     */
    public static class Multiple implements Cloneable {

        private Integer hibernateId;
        private Double gapOpenPenalty = 10.0;
        private Double gapExtPenalty = 0.2;
        private Double transWeight = 0.5;
        private Boolean ignoreEndGaps = false;
        private Boolean hydrophilicPenalty = true; // -NOHGAP        :hydrophilic gaps off
        private Boolean residueSpecificPenalty = true; //-NOPGAP        :residue-specific gaps off
        private String hydrophilicResidue = "GPSNDQEKR";
        private String iteration = ITERATION.NONE.toString();
        private String proteinMatrix = PRO_WEIGHT_MATRIX.GONNET.toString();
        private String dnaMatrix = DNA_WEIGHT_MATRIX.IUB.toString();

        @Override
        public Multiple clone() {
            Multiple ret = CommonUtil.cloneSimple(this);
            return ret;
        }

        public String toString(boolean dna) {
            StringBuilder ret = new StringBuilder();
            ret.append("-GAPOPEN=");
            ret.append(gapOpenPenalty);
            ret.append(' ');
            ret.append("-GAPEXT=");
            ret.append(gapExtPenalty);
            ret.append(' ');
            ret.append("-ITERATION=");
            ret.append(iteration);
            ret.append(' ');
            if (dnaMatrix != null) {
                ret.append("-DNAMATRIX=");
                ret.append(dnaMatrix);
                ret.append(' ');
            } else if (proteinMatrix != null) {
                ret.append("-MATRIX=");
                ret.append(proteinMatrix);
                ret.append(' ');
            }
            if (dna) {
                ret.append(' ');
                ret.append("-TRANSWEIGHT=");
                ret.append(transWeight);
                ret.append(' ');
            } else {
                if (ignoreEndGaps) {
                    ret.append(' ');
                    ret.append("-ENDGAPS");
                    ret.append(' ');
                }
                if (hydrophilicPenalty != null && !hydrophilicPenalty) {
                    ret.append(' ');
                    ret.append("-NOHGAP");
                    ret.append(' ');
                }
                if (residueSpecificPenalty != null && !residueSpecificPenalty) {
                    ret.append(' ');
                    ret.append("-NOPGAP");
                    ret.append(' ');
                }
                if (hydrophilicResidue != null) {
                    ret.append(' ');
                    ret.append("-HGAPRESIDUES=");
                    ret.append(hydrophilicResidue);
                    ret.append(' ');
                }
            }
            return ret.toString();
        }

        public String getHydrophilicResidue() {
            return hydrophilicResidue;
        }

        public void setHydrophilicResidue(String hydrophilicResidue) {
            this.hydrophilicResidue = hydrophilicResidue;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Boolean getIgnoreEndGaps() {
            return ignoreEndGaps;
        }

        public void setIgnoreEndGaps(Boolean ignoreEndGaps) {
            this.ignoreEndGaps = ignoreEndGaps;
        }

        /**
         * For hibernate use only
         */
        protected String getIteration() {
            return iteration;
        }

        /**
         * For hibernate use only
         */
        protected void setIteration(String iteration) {
            this.iteration = iteration;
        }

        public ITERATION getIterationEnum() {
            return ITERATION.get(iteration);
        }

        public void setIterationEnum(ITERATION iteration) {
            this.iteration = iteration.toString();
        }

        public DNA_WEIGHT_MATRIX getDnaMatrixEnum() {
            return DNA_WEIGHT_MATRIX.get(dnaMatrix);
        }

        public void setDnaMatrixEnum(DNA_WEIGHT_MATRIX dnaMatrix) {
            this.dnaMatrix = dnaMatrix == null ? null : dnaMatrix.toString();
        }

        protected String getDnaMatrix() {
            return dnaMatrix;
        }

        protected void setDnaMatrix(String dnaMatrix) {
            this.dnaMatrix = dnaMatrix;
        }

        public Double getGapExtPenalty() {
            return gapExtPenalty;
        }

        public void setGapExtPenalty(Double gapExtPenalty) {
            this.gapExtPenalty = gapExtPenalty;
        }

        public Double getGapOpenPenalty() {
            return gapOpenPenalty;
        }

        public void setGapOpenPenalty(Double gapOpenPenalty) {
            this.gapOpenPenalty = gapOpenPenalty;
        }

        protected String getProteinMatrix() {
            return proteinMatrix;
        }

        protected void setProteinMatrix(String proteinMatrix) {
            this.proteinMatrix = proteinMatrix;
        }

        public PRO_WEIGHT_MATRIX getProteinMatrixEnum() {
            return PRO_WEIGHT_MATRIX.get(proteinMatrix);
        }

        public void setProteinMatrixEnum(PRO_WEIGHT_MATRIX proteinMatrix) {
            this.proteinMatrix = proteinMatrix == null ? null : proteinMatrix.toString();

        }

        public Double getTransWeight() {
            return transWeight;
        }

        public void setTransWeight(Double transWeight) {
            this.transWeight = transWeight;
        }

        public boolean isHydrophilicPenalty() {
            return hydrophilicPenalty;
        }

        public void setHydrophilicPenalty(boolean hydrophilicPenalty) {
            this.hydrophilicPenalty = hydrophilicPenalty;
        }

        public boolean isResidueSpecificPenalty() {
            return residueSpecificPenalty;
        }

        public void setResidueSpecificPenalty(boolean residueSpecificPenalty) {
            this.residueSpecificPenalty = residueSpecificPenalty;
        }
    }
}
