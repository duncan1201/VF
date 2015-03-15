package com.gas.qblast.core.remote;

public class PutResult {
	
	private String RID;
	private int RTOE;
	
	public String getRID() {
		return RID;
	}
	public void setRID(String rID) {
		RID = rID;
	}
	public int getRTOE() {
		return RTOE;
	}
	public void setRTOE(int rTOE) {
		RTOE = rTOE;
	}
	
	public String toString(){
		return "["+RID + "," + Integer.toString(RTOE) + "]";
	}
	
}
