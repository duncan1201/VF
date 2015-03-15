package com.gas.entrez.core.elink.api;

import java.util.HashSet;
import java.util.Set;

public class ELinkResult {

	private String dbFrom;
	private String idList;
	private Set<LinkSetDb> dbs = new HashSet<LinkSetDb>();
	
	
	public String getDbFrom() {
		return dbFrom;
	}



	public void setDbFrom(String dbFrom) {
		this.dbFrom = dbFrom;
	}



	public String getIdList() {
		return idList;
	}



	public void setIdList(String idList) {
		this.idList = idList;
	}



	public Set<LinkSetDb> getDbs() {
		return dbs;
	}



	public void setDbs(Set<LinkSetDb> dbs) {
		this.dbs = dbs;
	}



	public static class LinkSetDb{
		private String dbTo;
		private String linkName;
		private Set<String> linkIds = new HashSet<String>();
		public String getDbTo() {
			return dbTo;
		}
		public void setDbTo(String dbTo) {
			this.dbTo = dbTo;
		}
		public String getLinkName() {
			return linkName;
		}
		public void setLinkName(String linkName) {
			this.linkName = linkName;
		}
		public Set<String> getLinkIds() {
			return linkIds;
		}
		public void setLinkIds(Set<String> linkIds) {
			this.linkIds = linkIds;
		}
		
		
		
	}
}
