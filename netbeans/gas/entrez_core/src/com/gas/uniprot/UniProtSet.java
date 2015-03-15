package com.gas.uniprot;

import java.util.HashSet;
import java.util.Set;

public class UniProtSet {
	private Set<UniProt> sets = new HashSet<UniProt>();

	public Set<UniProt> getSets() {
		return sets;
	}

	public void setSets(Set<UniProt> sets) {
		this.sets = sets;
	}
	
}
