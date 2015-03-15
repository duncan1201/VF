package com.gas.qblast.core.parameter;

public enum Repeats {
	Human("repeat_9606","Human"),
	Primates("repeat_9443", "Primates"),	
	Rodents("repeat_9989", "Rodents"),	
	Arabidopsis("repeat_3702", "Arabidopsis"),
	Rice("repeat_4530" , "Rice"),
	Mammals("repeat_40674" , "Mammals"),
	Fungi("repeat_4751" , "Fungi"),
	C_elegans("repeat_6239" , "C. elegans"),
	A_gambiae("repeat_7165" , "A. gambiae"),
	Zebrafish("repeat_7955" , "Zebrafish"),
	Fruitfly("repeat_7227" , "Fruit fly"),
	ChlamydomonasReinhardtii("repeat_3055" , "Chlamydomonas reinhardtii"),
	Fugu("repeat_31032" , "Fugu"),
	DiatomThalassiostraPseudonana("repeat_35128" , "Diatom thalassiostra pseudonana"),
	CaenorhabditisBriggsae("repeat_6238" , "Caenorhabditis briggsae"),
	CionaIntestinalis("repeat_7720" , "Ciona intestinalis");
	
	private String value;
	private String desc;
	
	Repeats(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
	
	public String toString(){
		return this.value;
	}
	
	public String getDesc(){
		return desc;
	}
}
