package com.gas.qblast.core.parameter;


public enum Database{
	
	// for blastp , blastx
	NON_REDUNDANT_PROTEIN_SEQUENCE("nr", "Non-redundant protein sequences (nr)"),
	REFSEQ_PROTEIN("refseq_protein", "Reference proteins (refseq_protein)"),
	SWISSPROT("swissprot", "Swissprot protein sequences(swissprot)"),
	PATENTED_PROTEIN_SEQ("pat", "Patent sequences(pat)"),
	PROTEIN_DATA_BANK_PROTEINS("pdb", "Protein Data Bank (pdb)"),
	ENV_SAMPLES("env_nr", "Environmental samples (env_nt)"),
	
	// for blastn
	HUMAN_G_T("dbindex/9606/ref_contig dbindex/9606/alt_contig_HuRef dbindex/9606/rna", "Human genomic plus transcript (Human G+T)"),
	MOUSE_G_T("dbindex/10090/alt_contig dbindex/10090/ref_contig dbindex/10090/rna", "Mouse genomic plus transcript (Mouse G+T)"),
	// other database for blastn, tblastn, and tblastx
	NUCL_COLLECTION("nr", "Nucleotide collection (nr/nt)"),        
	REF_RNA_SEQ("refseq_rna", "Reference RNA sequences (refseq_rna)"),
	REF_GENOMIC_SEQ("refseq_genomic", "Reference genomic sequences (refseq_genomic)"),
	NCBI_GENOMES("chromosome", "NCBI Genomes (chromosome)"),
	EST("est", "Expressed sequence tags (est)"),
	EST_OTHERS("est_others", "Non-human, non-mouse ESTs (est_others)"),
	GSS("gss", "Genomic survey sequences (gss)"),
	HTGS("htgs", "High throughput genomic sequences (HTGS)"),
	PAT_SEQ("pat", "Patent sequences(pat)"),
	PROTEIN_DATA_BANK("pdb", "Protein Data Bank (pdb)"),
	ALU("alu", "Human ALU repeat elements (alu_repeats)"),
	DBSTS("dbsts", "Sequence tagged sites (dbsts)"),
	WGS("wgs",  "Whole-genome shotgun reads (wgs)"),
	ENV_NT("env_nr", "Environmental samples (env_nt)"),
	TSA("tsa_nt", "Transcriptome Shotgun Assembly (TSA)");
	
	// blastn
	public static Database DEFAULT_BLASTN_DATABASE = HUMAN_G_T;
	public static Database[] BLASTN_DATABASES = {HUMAN_G_T, MOUSE_G_T, NUCL_COLLECTION, REF_RNA_SEQ,
		REF_GENOMIC_SEQ, NCBI_GENOMES, EST, EST_OTHERS, GSS, HTGS, PAT_SEQ, PROTEIN_DATA_BANK, ALU,
		DBSTS, WGS, ENV_NT, TSA};
	
	// blastp
	public static Database DEFAULT_BLASTP_DATABASE = NON_REDUNDANT_PROTEIN_SEQUENCE;
	public static Database[] BLASTP_DATABASES = {NON_REDUNDANT_PROTEIN_SEQUENCE,	
		REFSEQ_PROTEIN,
		SWISSPROT,
		PATENTED_PROTEIN_SEQ,
		PROTEIN_DATA_BANK_PROTEINS,
		ENV_SAMPLES};
	
	// blastx
	public static Database DEFAULT_BLASTX_DATABASE = NON_REDUNDANT_PROTEIN_SEQUENCE;
	public static Database[] BLASTX_DATABASES = {NON_REDUNDANT_PROTEIN_SEQUENCE,	
		REFSEQ_PROTEIN,
		SWISSPROT,
		PATENTED_PROTEIN_SEQ,
		PROTEIN_DATA_BANK_PROTEINS,
		ENV_SAMPLES};	
	
	// tblastn
	public static Database DEFAULT_TBLASTN_DATABASE = NUCL_COLLECTION;	
	public static Database[] TBLASTN_DATABASES = {	NUCL_COLLECTION,        
		REF_RNA_SEQ, REF_GENOMIC_SEQ,
		NCBI_GENOMES, EST,
		EST_OTHERS, GSS,
		HTGS, PAT_SEQ,
		PROTEIN_DATA_BANK, ALU,
		DBSTS, WGS,
		ENV_NT, TSA};
	
	// tblastx
	public static Database DEFAULT_TBLASTX_DATABASE = NUCL_COLLECTION;	
	public static Database[] TBLASTX_DATABASES = {	NUCL_COLLECTION,        
		REF_RNA_SEQ, REF_GENOMIC_SEQ,
		NCBI_GENOMES, EST,
		EST_OTHERS, GSS,
		HTGS, PAT_SEQ,
		PROTEIN_DATA_BANK, ALU,
		DBSTS, WGS,
		ENV_NT, TSA};	
	
	private String value;
	private String desc;
	Database(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
	
	public String toString(){
		return this.value;
	}
	
	public String getDesc(){
		return desc;
	}
	
	public static Database getValue(String desc, Database[] databases){
		for(Database db: databases){
			if(db.getDesc().equals(desc)){
				return db;
			}
		}
		return null;
	}
	
	public static Database[] ALL = {	NON_REDUNDANT_PROTEIN_SEQUENCE,
		REFSEQ_PROTEIN,
		SWISSPROT,
		PATENTED_PROTEIN_SEQ,
		PROTEIN_DATA_BANK_PROTEINS,
		ENV_SAMPLES,
		
		// for blastn
		HUMAN_G_T,
		MOUSE_G_T,
		// other database for blastn, tblastn, and tblastx
		NUCL_COLLECTION,        
		REF_RNA_SEQ,
		REF_GENOMIC_SEQ,
		NCBI_GENOMES,
		EST,
		EST_OTHERS,
		GSS,
		HTGS,
		PAT_SEQ,
		PROTEIN_DATA_BANK,
		ALU,
		DBSTS,
		WGS,
		ENV_NT,
		TSA};
	
}
