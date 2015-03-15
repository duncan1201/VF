package com.gas.qblast.core.parameter;

import com.gas.qblast.core.parameter.BlastParams.BLAST_PROGRAMS;
import com.gas.qblast.core.parameter.BlastParams.FILTER_LOW_COMPLEXITY;
import com.gas.qblast.core.parameter.BlastParams.FILTER_MASK;
import com.gas.qblast.core.parameter.BlastParams.LCASE_MASK;
import com.gas.qblast.core.parameter.BlastParams.PROGRAM;
import com.gas.qblast.core.parameter.BlastParams.SHORT_QUERY_ADJUST;

public class BlastParamsFactory {
	public static BlastParams createInstance(PROGRAM program){
		BlastParams ret = new BlastParams();
		if(program == PROGRAM.blastn){
			ret.setBlastPrograms(BLAST_PROGRAMS.megaBlast);
			ret.setDatabase(Database.HUMAN_G_T);			
			ret.setExpect(10);
			ret.setGapCost(new GapCost(0, 0));
			ret.setFilterMask(FILTER_MASK.m);
			ret.setHspRangeMax(0);
			ret.setMatchScores(new MatchScores(1,-2));
			ret.setMaxNumSeq(100);
			ret.setProgram(PROGRAM.blastn);
			ret.setShortQueryAdjust(SHORT_QUERY_ADJUST.on);
			ret.setWordSize(28);

		}else if(program == PROGRAM.blastp){
			ret.setBlastPrograms(BLAST_PROGRAMS.blastp);
			ret.setCompositionBasedStatistics(CompositionBasedStatistics.COMPOSITION_BASED_STATISTICS);
			ret.setDatabase(Database.NON_REDUNDANT_PROTEIN_SEQUENCE);
			ret.setExpect(10);
			ret.setGapCost(new GapCost(11, 1));
			ret.setHitListSize(56);
			ret.setHspRangeMax(0);
			ret.setMatrixName(MATRIX_NAME.BLOSUM62);
			ret.setMaxNumSeq(100);
			ret.setProgram(PROGRAM.blastp);
			ret.setShortQueryAdjust(SHORT_QUERY_ADJUST.on);
			
			ret.setWordSize(3);
			
			
			
			
			
			
			
		}else if(program == PROGRAM.blastx){
			ret.setBlastPrograms(BLAST_PROGRAMS.blastx);
			ret.setDatabase(Database.NON_REDUNDANT_PROTEIN_SEQUENCE);
			ret.setExpect(10);
			ret.setFilterLowComplexity(FILTER_LOW_COMPLEXITY.L);
			ret.setGapCost(new GapCost(11, 2));
			ret.setHitListSize(56);
			ret.setHspRangeMax(0);
			ret.setProgram(PROGRAM.blastx);
			ret.setMatrixName(MATRIX_NAME.BLOSUM62);
			ret.setMaxNumSeq(100);			
			ret.setWordSize(3);
			
			
		}else if(program == PROGRAM.tblastn){
			ret.setBlastPrograms(BLAST_PROGRAMS.tblastn);
			ret.setCompositionBasedStatistics(CompositionBasedStatistics.COMPOSITION_BASED_STATISTICS);
			ret.setDatabase(Database.NUCL_COLLECTION);
			ret.setExpect(10);
			ret.setFilterLowComplexity(FILTER_LOW_COMPLEXITY.L);
			ret.setGapCost(new GapCost(11, 1));
			ret.setHspRangeMax(0);
			ret.setMatrixName(MATRIX_NAME.BLOSUM62);
			ret.setMaxNumSeq(100);
			ret.setProgram(PROGRAM.tblastn);
			ret.setWordSize(3);
		}else if(program == PROGRAM.tblastx){
			ret.setBlastPrograms(BLAST_PROGRAMS.tblastx);
			ret.setCompositionBasedStatistics(CompositionBasedStatistics.NO);
			ret.setDatabase(Database.NUCL_COLLECTION);
			ret.setExpect(10);
			ret.setFilterLowComplexity(FILTER_LOW_COMPLEXITY.L);
			ret.setGapCost(new GapCost(5, 2));
			ret.setHspRangeMax(0);
			ret.setLcaseMask(LCASE_MASK.no);
			ret.setMaxNumSeq(100);
			ret.setProgram(PROGRAM.tblastx);
			ret.setWordSize(3);
									
		}
		return ret;
	}
}
