package com.gas.qblast.core.parameter;

public enum CompositionBasedStatistics {
	NO("no", "No adjustment"),
	NO_ADJUSTMENT("0", "No adjustment"),
	COMPOSITION_BASED_STATISTICS("1", "Composition-based statistics"),
	CONDITIONAL("2", "Conditional compositional score matrix adjustment"),
	UNIVERSAL("3", "Universal compositional score matrix adjustment");
	
	private String value;
	CompositionBasedStatistics(String value, String desc){
		this.value = value;
	}
	
	public String toString(){
		return this.value;
	}
	
}
