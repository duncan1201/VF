package com.gas.uniprot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniProt {
	
	private Date created;
	private Date modified;
	private String version ;
	
	private List<String> accessions = new ArrayList<String>();
	private String name;
	private Protein protein ;
	private Gene gene;
	private Organism organism ;
	private Set<Reference> refs = new HashSet<Reference>();
	private ProteinExistence proteinExistence;
	private Set<DbReference> dbrefs = new HashSet<DbReference>();
	private Set<Keyword> keywords = new HashSet<Keyword>();
	private Sequence sequence;
	private Set<Comment> comments = new HashSet<Comment>();
	private Set<Feature> features = new HashSet<Feature>();
	private Set<Evidence> evidences = new HashSet<Evidence>();
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Set<Evidence> getEvidences() {
		return evidences;
	}

	public void setEvidences(Set<Evidence> evidences) {
		this.evidences = evidences;
	}

	public Set<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getAccessions() {
		return accessions;
	}

	public void setAccessions(List<String> accessions) {
		this.accessions = accessions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Protein getProtein() {
		return protein;
	}

	public void setProtein(Protein protein) {
		this.protein = protein;
	}

	public Gene getGene() {
		return gene;
	}

	public void setGene(Gene gene) {
		this.gene = gene;
	}

	public Organism getOrganism() {
		return organism;
	}

	public void setOrganism(Organism organism) {
		this.organism = organism;
	}

	public Set<Reference> getRefs() {
		return refs;
	}

	public void setRefs(Set<Reference> refs) {
		this.refs = refs;
	}

	public ProteinExistence getProteinExistence() {
		return proteinExistence;
	}

	public void setProteinExistence(ProteinExistence proteinExistence) {
		this.proteinExistence = proteinExistence;
	}

	public Set<DbReference> getDbrefs() {
		return dbrefs;
	}

	public void setDbrefs(Set<DbReference> dbrefs) {
		this.dbrefs = dbrefs;
	}

	public Set<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<Keyword> keywords) {
		this.keywords = keywords;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public static class Protein{
		
		private String recommendedName;
		private Set<String> alternativeNames = new HashSet<String>();
		
		public String getRecommendedName() {
			return recommendedName;
		}
		public void setRecommendedName(String recommendedName) {
			this.recommendedName = recommendedName;
		}
		public Set<String> getAlternativeNames() {
			return alternativeNames;
		}
		public void setAlternativeNames(Set<String> alternativeNames) {
			this.alternativeNames = alternativeNames;
		}
		
		
	}
	
	public static class Gene{
		private Set<Name> names = new HashSet<Name>();

		public Set<Name> getNames() {
			return names;
		}

		public void setNames(Set<Name> names) {
			this.names = names;
		}
		
		
	}
	
	public static class Organism{
		private Set<Name> names = new HashSet<Name>();
		private Lineage lineage ;
		private DbReference dbRef;
		
		
		public DbReference getDbRef() {
			return dbRef;
		}
		public void setDbRef(DbReference dbRef) {
			this.dbRef = dbRef;
		}
		public Set<Name> getNames() {
			return names;
		}
		public void setNames(Set<Name> names) {
			this.names = names;
		}
		public Lineage getLineage() {
			return lineage;
		}
		public void setLineage(Lineage lineage) {
			this.lineage = lineage;
		}
		
		
	}
	
	public static class Lineage{
		private List<String> names = new ArrayList<String>();

		public List<String> getNames() {
			return names;
		}

		public void setNames(List<String> names) {
			this.names = names;
		}
		
		
	}
	
	public static class Name{
		private String type;
		private String value;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		
	}
	
	public static class DbReference{
		private String type;
		private String id;
		private String key;
		
		private Set<Property> properties = new HashSet<Property>();

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Set<Property> getProperties() {
			return properties;
		}

		public void setProperties(Set<Property> properties) {
			this.properties = properties;
		}
		
		
	}
	
	public static class Property {
		private String type;
		private String value;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		
	}
	
	public static class Citation{
		private String type;
		private String date;
		private String name;
		private String volume;
		private String first;
		private String last;
		private String db;
		private String title;
		private Set<String> authors = new HashSet<String>();
		private Set<DbReference> refs = new HashSet<DbReference>();
		
		
		public String getDb() {
			return db;
		}
		public void setDb(String db) {
			this.db = db;
		}
		public String getFirst() {
			return first;
		}
		public void setFirst(String first) {
			this.first = first;
		}
		public String getLast() {
			return last;
		}
		public void setLast(String last) {
			this.last = last;
		}
		public String getVolume() {
			return volume;
		}
		public void setVolume(String volume) {
			this.volume = volume;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Set<String> getAuthors() {
			return authors;
		}
		public void setAuthors(Set<String> authors) {
			this.authors = authors;
		}
		public Set<DbReference> getRefs() {
			return refs;
		}
		public void setRef(Set<DbReference> refs) {
			this.refs = refs;
		}
		
		
	}
	
	public static class Reference{
		private Citation citation ;
		private Set<String> scopes = new HashSet<String>();
		public Citation getCitation() {
			return citation;
		}
		public void setCitation(Citation citation) {
			this.citation = citation;
		}
		public Set<String> getScopes() {
			return scopes;
		}
		public void setScopes(Set<String> scopes) {
			this.scopes = scopes;
		}

		
	}
	
	public static class Source {
		private DbReference dbRef; 
		private Set<String> tissues = new HashSet<String>();
		public DbReference getDbRef() {
			return dbRef;
		}
		public void setDbRef(DbReference dbRef) {
			this.dbRef = dbRef;
		}
		public Set<String> getTissues() {
			return tissues;
		}
		public void setTissues(Set<String> tissues) {
			this.tissues = tissues;
		}
		
		
	}
	
	public static class Comment{
		private String type;
		private Text text;
		private String phDependence;
		
		
		public String getPhDependence() {
			return phDependence;
		}
		public void setPhDependence(String phDependence) {
			this.phDependence = phDependence;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Text getText() {
			return text;
		}
		public void setText(Text text) {
			this.text = text;
		}
		
		
		
	}
	
	public static class Text{
		private String evidence;
		private String value;
		
		public String getEvidence() {
			return evidence;
		}
		public void setEvidence(String evidence) {
			this.evidence = evidence;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	
		
		
	}
	
	public static class ProteinExistence{
		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
	}
	
	public static class Keyword{
		private String id;
		private String value;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		
	}
	
	public static class Feature{
		private String type;
		private String desc;
		private String id;
		private String original;
		private String variation;
		private Location location;
		
		
		
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getOriginal() {
			return original;
		}
		public void setOriginal(String original) {
			this.original = original;
		}
		public String getVariation() {
			return variation;
		}
		public void setVariation(String variation) {
			this.variation = variation;
		}
		
		
		
	}
	
	public static class Location{
		private Begin begin;
		private End end;
		public Begin getBegin() {
			return begin;
		}
		public void setBegin(Begin begin) {
			this.begin = begin;
		}
		public End getEnd() {
			return end;
		}
		public void setEnd(End end) {
			this.end = end;
		}
		
		
	}
	
	public static class Begin{
		private Integer position;

		public Integer getPosition() {
			return position;
		}

		public void setPosition(Integer position) {
			this.position = position;
		}
		
		
	}
	
	public static class End{
		private Integer position;

		public Integer getPosition() {
			return position;
		}

		public void setPosition(Integer position) {
			this.position = position;
		}
		
		
	}
	
	public static class Evidence{
		private String key;
		private String type;
		private Source source;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Source getSource() {
			return source;
		}
		public void setSource(Source source) {
			this.source = source;
		}
	}
	
	public static class Sequence{
		private Integer length ;
		private String mass;
		private String version;
		private String modified;
		private String text;
		public Integer getLength() {
			return length;
		}
		public void setLength(Integer length) {
			this.length = length;
		}
		public String getMass() {
			return mass;
		}
		public void setMass(String mass) {
			this.mass = mass;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getModified() {
			return modified;
		}
		public void setModified(String modified) {
			this.modified = modified;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		
	}
}
