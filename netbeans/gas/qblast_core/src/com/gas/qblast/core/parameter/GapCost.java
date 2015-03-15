package com.gas.qblast.core.parameter;

import java.util.ArrayList;
import java.util.List;

import com.gas.qblast.core.parameter.BlastParams.BLAST_PROGRAMS;

public class GapCost {
	
	public int gapOpenCost;
	public int gapExtCost;
	
	public GapCost(int gapOpenCost, int gapExtCost){
		this.gapOpenCost = gapOpenCost;
		this.gapExtCost = gapExtCost;
	}
	
	public String toString(boolean verbose){
		StringBuffer ret = new StringBuffer();
		if(verbose){
			if(gapOpenCost == 0 && gapExtCost == 0){
				ret.append("Linear");
			}else{
				ret.append("Existence:");
				ret.append(' ');
				ret.append(gapOpenCost);
				ret.append(' ');
				ret.append("Extension:");
				ret.append(' ');
				ret.append(gapExtCost);
			}
		}else{
			ret.append(gapOpenCost);
			ret.append(' ');
			ret.append(gapExtCost);
		}
		return ret.toString();
	}
	

	
	public String toString(){
		return toString(false);
	}
	
}
