package com.gas.entrez.core.gene;

import java.util.HashSet;
import java.util.Set;

public class EntrezGene {
	
	// general information
	private String geneId;
	private String geneSymbol;
	private String geneFullName;
	private String locusTag;
	
	private String geneType;
	private String refSeqStatus;
	private String taxonName;
	private String taxonId;
	
	private String lineage;
	private String summary;
	
	
	private Set<GeneCommentary> locus = new HashSet<GeneCommentary>();
	private Set<GeneCommentary> generifs = new HashSet<GeneCommentary>();
	private Set<GeneCommentary> phenotypes = new HashSet<GeneCommentary>();
	
	private Integer totalNumOfRif ;
	
	
	
	public String getGeneId() {
		return geneId;
	}



	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}



	public Set<GeneCommentary> getPhenotypes() {
		return phenotypes;
	}



	public void setPhenotypes(Set<GeneCommentary> phenotypes) {
		this.phenotypes = phenotypes;
	}



	public Integer getTotalNumOfRif() {
		return totalNumOfRif;
	}



	public void setTotalNumOfRif(Integer totalNumOfRif) {
		this.totalNumOfRif = totalNumOfRif;
	}



	public String getGeneSymbol() {
		return geneSymbol;
	}



	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}



	public String getGeneFullName() {
		return geneFullName;
	}



	public void setGeneFullName(String geneFullName) {
		this.geneFullName = geneFullName;
	}



	public String getLocusTag() {
		return locusTag;
	}



	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}



	public Set<GeneCommentary> getGenerifs() {
		return generifs;
	}



	public void setGenerifs(Set<GeneCommentary> generifs) {
		this.generifs = generifs;
	}



	public String getGeneType() {
		return geneType;
	}



	public void setGeneType(String geneType) {
		this.geneType = geneType;
	}



	public String getRefSeqStatus() {
		return refSeqStatus;
	}



	public void setRefSeqStatus(String refSeqStatus) {
		this.refSeqStatus = refSeqStatus;
	}



	public String getTaxonName() {
		return taxonName;
	}



	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}



	public String getTaxonId() {
		return taxonId;
	}



	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
	}



	public String getLineage() {
		return lineage;
	}



	public void setLineage(String lineage) {
		this.lineage = lineage;
	}



	public String getSummary() {
		return summary;
	}



	public void setSummary(String summary) {
		this.summary = summary;
	}



	public Set<GeneCommentary> getLocus() {
		return locus;
	}



	public void setLocus(Set<GeneCommentary> locus) {
		this.locus = locus;
	}



	//
	public static class GeneCommentary{
		private String text;
		private String type;
		private String heading;
		private String label;
		private String accession;
		private String version;
		private String pubmedId;

		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getHeading() {
			return heading;
		}
		public void setHeading(String heading) {
			this.heading = heading;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getAccession() {
			return accession;
		}
		public void setAccession(String accession) {
			this.accession = accession;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getPubmedId() {
			return pubmedId;
		}
		public void setPubmedId(String pubmedId) {
			this.pubmedId = pubmedId;
		}
		
		
		
	}
}
