package com.gas.quickgo.term;

import java.util.HashSet;
import java.util.Set;

public class GOTerm {
	
	private String id;
	private String name;
	private String namespace;
	private String def;
	private String synonymScope;
	private String synonym;

	private Set<Xref> refs = new HashSet<Xref>();
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	public String getNamespace() {
		return namespace;
	}



	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}



	public String getDef() {
		return def;
	}



	public void setDef(String def) {
		this.def = def;
	}



	public String getSynonymScope() {
		return synonymScope;
	}



	public void setSynonymScope(String synonymScope) {
		this.synonymScope = synonymScope;
	}



	public String getSynonym() {
		return synonym;
	}



	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}



	public Set<Xref> getRefs() {
		return refs;
	}



	public void setRefs(Set<Xref> refs) {
		this.refs = refs;
	}



	public static class Xref{
		
		private String acc ;
		private String dbname;
		
		public String getAcc() {
			return acc;
		}
		public void setAcc(String acc) {
			this.acc = acc;
		}
		public String getDbname() {
			return dbname;
		}
		public void setDbname(String dbname) {
			this.dbname = dbname;
		}
	}
}
