package com.gas.domain.core.tigr;

import com.gas.common.ui.util.CommonUtil;
import java.io.Serializable;

public class TIGRSettings implements Serializable, Cloneable {

    public static final int DEFAULT_MAX_END = 15;
    public static final int DEFAULT_MIN_LENGTH = 50;
    public static final boolean DEFAULT_LOW_SCORES = false;
    public static final boolean DEFAULT_GENERATE_ACE = false;
    public static final boolean DEFAULT_COVERAGE = false;
    public static final int DEFAULT_MAX_ERROR_32 = 5;
    public static final boolean DEFAULT_NO_NORM = false;
    public static final float DEFAULT_MIN_PERCENT = 88;
    public static final int DEFAULT_RESTART_INC = 64;
    public static final boolean DEFAULT_GENERATE_SINGLETONS = false;
    public static final int DEFAULT_REPEAT_NUM_CUTOFF = 1;
    public static final int DEFAULT_NUM_CONFLICTS = 2;
    public static final int DEFAULT_MAX_SPAN_LENGTH = 5000;
    public static final boolean DEFAULT_ZAP_QUESTIONABLE = false;
    public static final boolean DEFAULT_GENERATE_REPEAT_FILE = false;
    public static final boolean DEFAULT_SEARCH_TANDEM = false;
    private transient Integer hibernateId;
    private String str = null;
    //-b incl_bad_seq - keep all sequences including potential chimeras and splice variants
    private Boolean includeBadSeq = false ; // -b incl_bad_seq
    // -i trimmed_seq - to indicates that input sequences are trimmed and have high    
    private Boolean trimmedSeq = false;
    private Boolean generateACE = DEFAULT_GENERATE_ACE; // -A a flag
    private Boolean generateCoverage = DEFAULT_COVERAGE; // -c a flag
    private Integer maximumEnd = DEFAULT_MAX_END; // -e
    private Boolean generateRepeatFile = DEFAULT_GENERATE_REPEAT_FILE; // -F
    private Integer maxError32 = DEFAULT_MAX_ERROR_32; //-g max_err_32;
    private Integer minimumLength = DEFAULT_MIN_LENGTH;// -l
    private Boolean lowScores = DEFAULT_LOW_SCORES; // -L a flag	
    private Boolean noNorm = DEFAULT_NO_NORM; // -N a flag
    private Float minPercent = DEFAULT_MIN_PERCENT; //-p minimum_percent
    private Integer restart_inc = DEFAULT_RESTART_INC; // -r resort#
    private Boolean generateSingletons = DEFAULT_GENERATE_SINGLETONS;//-s - is a flag for singletons
    private Integer maxSpanLength = DEFAULT_MAX_SPAN_LENGTH; // -S max_span_len
    private Boolean searchTandem = DEFAULT_SEARCH_TANDEM; // -t
    /*(default is 1 - use 0 to include singletons)*/
    private Integer repeatNumCufoff = DEFAULT_REPEAT_NUM_CUTOFF; //-y repeat_num_cutoff
    private Integer numConflicts = DEFAULT_NUM_CONFLICTS;//-z num_conflicts
    /*  -Z - is a flag which causes sequences which appear to be misassembled due to being\n\
     contained in a repeat or having quality conflicts (see -z) and not having a good\n\
     clone mate in the same assembly to be left out of the GDE .align file.\n\
     */
    private Boolean zapQuestionable = DEFAULT_ZAP_QUESTIONABLE; // -Z

    public TIGRSettings() {
    }
    
    public TIGRSettings clone(){
        TIGRSettings ret = CommonUtil.cloneSimple(this);        
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Boolean getTrimmedSeq() {
        return trimmedSeq;
    }

    public void setTrimmedSeq(Boolean trimmedSeq) {
        this.trimmedSeq = trimmedSeq;
    }


    public Boolean getIncludeBadSeq() {
        return includeBadSeq;
    }

    public void setIncludeBadSeq(Boolean includeBadSeq) {
        this.includeBadSeq = includeBadSeq;
    }

    public Boolean getGenerateACE() {
        return generateACE;
    }

    public void setGenerateACE(Boolean generateACE) {
        this.generateACE = generateACE;
    }

    public Boolean getGenerateCoverage() {
        return generateCoverage;
    }

    public void setGenerateCoverage(Boolean generateCoverage) {
        this.generateCoverage = generateCoverage;
    }

    public Boolean getGenerateRepeatFile() {
        return generateRepeatFile;
    }

    public void setGenerateRepeatFile(Boolean generateRepeatFile) {
        this.generateRepeatFile = generateRepeatFile;
    }

    public Boolean getGenerateSingletons() {
        return generateSingletons;
    }

    public void setGenerateSingletons(Boolean generateSingletons) {
        this.generateSingletons = generateSingletons;
    }

    public Boolean getLowScores() {
        return lowScores;
    }

    public void setLowScores(Boolean lowScores) {
        this.lowScores = lowScores;
    }

    public Integer getMaxError32() {
        return maxError32;
    }

    public void setMaxError32(Integer maxError32) {
        this.maxError32 = maxError32;
    }

    public Integer getMaxSpanLength() {
        return maxSpanLength;
    }

    public void setMaxSpanLength(Integer maxSpanLength) {
        this.maxSpanLength = maxSpanLength;
    }

    public Integer getMaximumEnd() {
        return maximumEnd;
    }

    public void setMaximumEnd(Integer maximumEnd) {
        this.maximumEnd = maximumEnd;
    }

    public Float getMinPercent() {
        return minPercent;
    }

    public void setMinPercent(Float minPercent) {
        this.minPercent = minPercent;
    }

    public Integer getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    public Boolean getNoNorm() {
        return noNorm;
    }

    public void setNoNorm(Boolean noNorm) {
        this.noNorm = noNorm;
    }

    public Integer getNumConflicts() {
        return numConflicts;
    }

    public void setNumConflicts(Integer numConflicts) {
        this.numConflicts = numConflicts;
    }

    public Integer getRepeatNumCufoff() {
        return repeatNumCufoff;
    }

    public void setRepeatNumCufoff(Integer repeatNumCufoff) {
        this.repeatNumCufoff = repeatNumCufoff;
    }

    public Integer getRestart_inc() {
        return restart_inc;
    }

    public void setRestart_inc(Integer restart_inc) {
        this.restart_inc = restart_inc;
    }

    public Boolean getSearchTandem() {
        return searchTandem;
    }

    public void setSearchTandem(Boolean searchTandem) {
        this.searchTandem = searchTandem;
    }

    public Boolean getZapQuestionable() {
        return zapQuestionable;
    }

    public void setZapQuestionable(Boolean zapQuestionable) {
        this.zapQuestionable = zapQuestionable;
    }
    
    public void touchAll(){
        this.getGenerateACE();
        this.getGenerateCoverage();
        this.getGenerateRepeatFile();
        this.getGenerateSingletons();
        this.getIncludeBadSeq();
        this.getLowScores();
        this.getMaxError32();
    }
}
