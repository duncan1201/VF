package com.gas.qblast.core.parameter;

import java.util.ArrayList;
import java.util.List;

import com.gas.qblast.core.parameter.BlastParams.BLAST_PROGRAMS;

public class WordSizeList {
	
	public final static WordSizeList MEGABLAST_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList DISMEGABLAST_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList BLASTN_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList BLASTP_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList PSI_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList PHI_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList BLASTX_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList TBLASTN_WORD_SIZE_LIST = new WordSizeList();
	public final static WordSizeList TBLASTX_WORD_SIZE_LIST = new WordSizeList();
	
	public static WordSizeList getWordSizeList(BLAST_PROGRAMS blastPrograms){
		if(blastPrograms == BLAST_PROGRAMS.megaBlast){
			return MEGABLAST_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.discoMegablast){
			return DISMEGABLAST_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.blastn){
			return BLASTN_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.blastp){
			return BLASTP_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.psi){
			return PSI_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.phi){
			return PHI_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.blastx){
			return BLASTX_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.tblastn){
			return TBLASTN_WORD_SIZE_LIST;
		}else if(blastPrograms == BLAST_PROGRAMS.tblastx){
			return TBLASTX_WORD_SIZE_LIST;
		}else{
			throw new IllegalArgumentException("{0} is not recognized".format(blastPrograms.toString()));
		}
	}
	
	static{
		MEGABLAST_WORD_SIZE_LIST.add(16).add(20).add(24).add(28).add(32).add(48).add(64).add(128).add(256);
		MEGABLAST_WORD_SIZE_LIST.setDefault(28);
		
		DISMEGABLAST_WORD_SIZE_LIST.add(11).add(12);
		DISMEGABLAST_WORD_SIZE_LIST.setDefault(11);

		BLASTN_WORD_SIZE_LIST.add(7).add(11).add(15);
		BLASTN_WORD_SIZE_LIST.setDefault(11);
		
		BLASTP_WORD_SIZE_LIST.add(2).add(3);
		BLASTP_WORD_SIZE_LIST.setDefault(3);

		PSI_WORD_SIZE_LIST.add(2).add(3);
		PSI_WORD_SIZE_LIST.setDefault(3);
		
		PHI_WORD_SIZE_LIST.add(2).add(3);
		PHI_WORD_SIZE_LIST.setDefault(3);	
		
		BLASTX_WORD_SIZE_LIST.add(2).add(3);
		BLASTX_WORD_SIZE_LIST.setDefault(3);	
		
		TBLASTN_WORD_SIZE_LIST.add(2).add(3);
		TBLASTN_WORD_SIZE_LIST.setDefault(3);
		
		TBLASTX_WORD_SIZE_LIST.add(2).add(3);
		TBLASTX_WORD_SIZE_LIST.setDefault(3);			
	}
	
	private List<Integer> value = new ArrayList<Integer>();
	private Integer _default;
	
	public Integer getDefault() {
		return _default;
	} 

	public void setDefault(Integer _default) {
		if(!value.contains(_default)){
			throw new IllegalArgumentException("Selected {0} is invalid".format(_default.toString()));
		}
		this._default = _default;
	}

	public List<Integer> getValue() {
		return value;
	}

	private WordSizeList add(int i){
		value.add(i);
		return this;
	}


}
