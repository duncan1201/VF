package com.gas.qblast.core.parameter;

public class MatchScores {
	public int matchScore;
	public int mismatchScore;
	
	public MatchScores(int matchScore, int mismatchScore){
		this.matchScore = matchScore;
		this.mismatchScore = mismatchScore;
		if(mismatchScore > 0){
			throw new IllegalArgumentException("Mismatch Score must be less than 0");
		}
		if(matchScore < 0){
			throw new IllegalArgumentException("Match Score must be greater than 0");
		}
	}
	
	public String toString(){
		return Integer.toString(matchScore) + ',' + Integer.toString(mismatchScore);
	}
	
}
