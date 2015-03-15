package com.gas.qblast.core.parameter;


public class BlastParams {
	public static enum PROGRAM{blastn, blastp, blastx, tblastn, tblastx};
	public static enum DATABASE{nr};
	public static enum BLAST_PROGRAMS{blastn, megaBlast, discoMegablast, psi, blastp, phi, blastx, tblastn, tblastx};
	public static enum FILTER_LOW_COMPLEXITY{L};
	public static enum FILTER_REPEATS{R};
	public static enum FILTER_MASK{m};
	public static enum LCASE_MASK{yes, no};
	public static enum EXCLUDE_MODELS{on};
	public static enum EXCLUDE_SEQ_UNCULT{on};
	public static enum SHORT_QUERY_ADJUST{on};
	
	private PROGRAM program;
	private BLAST_PROGRAMS blastPrograms;
	private String query;
	private int queryFrom;
	private int queryTo;
	private int hitListSize;
	private Integer expect;
	private GapCost gapCost;
	private MatchScores matchScores;
	private FILTER_LOW_COMPLEXITY filterLowComplexity;
	private FILTER_REPEATS filterRepeats;
	private FILTER_MASK filterMask;	
	private LCASE_MASK lcaseMask;
	private Integer wordSize;
	private WordSizeList wordSizeList;
	private EXCLUDE_MODELS excludeModels;
	private EXCLUDE_SEQ_UNCULT excludeSeqUncult ;
	private Integer hspRangeMax;
	private Integer maxNumSeq;
	private Database database;
	private MATRIX_NAME matrixName;
	private CompositionBasedStatistics compositionBasedStatistics;
	private SHORT_QUERY_ADJUST shortQueryAdjust;
	private String eqText;
	private Repeats repeats;
	
	
	public WordSizeList getWordSizeList() {
		return wordSizeList;
	}
	public void setWordSizeList(WordSizeList wordSizeList) {
		this.wordSizeList = wordSizeList;
	}
	public MatchScores getMatchScores() {
		return matchScores;
	}
	public void setMatchScores(MatchScores matchScores) {
		this.matchScores = matchScores;
	}
	public Repeats getRepeats() {
		return repeats;
	}
	public void setRepeats(Repeats repeats) {
		this.repeats = repeats;
	}
	public String getEqText() {
		return eqText;
	}
	public void setEqText(String eqText) {
		this.eqText = eqText;
	}
	public SHORT_QUERY_ADJUST getShortQueryAdjust() {
		return shortQueryAdjust;
	}
	public void setShortQueryAdjust(SHORT_QUERY_ADJUST shortQueryAdjust) {
		this.shortQueryAdjust = shortQueryAdjust;
	}
	public PROGRAM getProgram() {
		return program;
	}
	public void setProgram(PROGRAM program) {
		this.program = program;
	}
	public BLAST_PROGRAMS getBlastPrograms() {
		return blastPrograms;
	}
	public void setBlastPrograms(BLAST_PROGRAMS blastPrograms) {
		this.blastPrograms = blastPrograms;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public int getQueryFrom() {
		return queryFrom;
	}
	public void setQueryFrom(int queryFrom) {
		this.queryFrom = queryFrom;
	}
	public int getQueryTo() {
		return queryTo;
	}
	public void setQueryTo(int queryTo) {
		this.queryTo = queryTo;
	}
	public int getHitListSize() {
		return hitListSize;
	}
	public void setHitListSize(int hitListSize) {
		this.hitListSize = hitListSize;
	}
	public Integer getExpect() {
		return expect;
	}
	public void setExpect(Integer expect) {
		this.expect = expect;
	}
	public GapCost getGapCost() {
		return gapCost;
	}
	public void setGapCost(GapCost gapCost) {
		this.gapCost = gapCost;
	}
	
	public FILTER_LOW_COMPLEXITY getFilterLowComplexity() {
		return filterLowComplexity;
	}
	public void setFilterLowComplexity(FILTER_LOW_COMPLEXITY filterLowComplexity) {
		this.filterLowComplexity = filterLowComplexity;
	}
	public FILTER_REPEATS getFilterRepeats() {
		return filterRepeats;
	}
	public void setFilterRepeats(FILTER_REPEATS filterRepeats) {
		this.filterRepeats = filterRepeats;
	}
	public FILTER_MASK getFilterMask() {
		return filterMask;
	}
	public void setFilterMask(FILTER_MASK filterMask) {
		this.filterMask = filterMask;
	}
	public LCASE_MASK getLcaseMask() {
		return lcaseMask;
	}
	public void setLcaseMask(LCASE_MASK lcaseMask) {
		this.lcaseMask = lcaseMask;
	}
	public Integer getWordSize() {
		return wordSize;
	}
	public void setWordSize(Integer wordSize) {
		this.wordSize = wordSize;
	}
	public EXCLUDE_MODELS getExcludeModels() {
		return excludeModels;
	}
	public void setExcludeModels(EXCLUDE_MODELS excludeModels) {
		this.excludeModels = excludeModels;
	}
	public EXCLUDE_SEQ_UNCULT getExcludeSeqUncult() {
		return excludeSeqUncult;
	}
	public void setExcludeSeqUncult(EXCLUDE_SEQ_UNCULT excludeSeqUncult) {
		this.excludeSeqUncult = excludeSeqUncult;
	}
	public Integer getHspRangeMax() {
		return hspRangeMax;
	}
	public void setHspRangeMax(Integer hspRangeMax) {
		this.hspRangeMax = hspRangeMax;
	}
	public Integer getMaxNumSeq() {
		return maxNumSeq;
	}
	public void setMaxNumSeq(Integer maxNumSeq) {
		this.maxNumSeq = maxNumSeq;
	}
	public Database getDatabase() {
		return database;
	}
	public void setDatabase(Database database) {
		this.database = database;
	}
	public MATRIX_NAME getMatrixName() {
		return matrixName;
	}
	public void setMatrixName(MATRIX_NAME matrixName) {
		this.matrixName = matrixName;
	}
	public CompositionBasedStatistics getCompositionBasedStatistics() {
		return compositionBasedStatistics;
	}
	public void setCompositionBasedStatistics(
			CompositionBasedStatistics compositionBasedStatistics) {
		this.compositionBasedStatistics = compositionBasedStatistics;
	} 
	

	
}
