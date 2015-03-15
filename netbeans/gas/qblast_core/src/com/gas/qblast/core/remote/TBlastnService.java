package com.gas.qblast.core.remote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.gas.qblast.core.parameter.BlastParams;
import com.gas.qblast.core.parameter.BlastParams.BLAST_PROGRAMS;
import com.gas.qblast.core.parameter.BlastParams.PROGRAM;
import com.gas.qblast.core.parameter.BlastParamsFactory;
import com.gas.qblast.core.parameter.Database;

/*
 * Search translated nucleotide database using a protein query
 * **/
public class TBlastnService extends AbstractBlastService{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String query = "MRVISRARSACTWTSCTSLSPCSTSCPPSPAAPTLLRRRSLPQQRRRPSSSPNRRVRGVTTSPCPTRSLVYKRRVGAPQRLCAETVATMQAQEANALLLSRMEALEWFKKFTVWLRVYAIFIFQLAFSFGLGSVFWLGFPQNRNFCVENYSFFLTVLVPIVCMFITYTLGNEHPSNATVLFIYLLANSLTAAIFQMCSESRVLVGSYVMTLALFISFTGLAFLGGRDRRRWKCISCVYVVMLLSFLTLALLSDADWLQKIVVTLCAFSISFFLGILAYDSLMVIFFCPPNQCIRHAVCLYLDSMAIFLTLLLMLSGPRWISLSDGVPLDNGTLTAASTTGKS";

		
		TBlastnService service = new TBlastnService();
		service.setParams(BlastParamsFactory.createInstance(PROGRAM.tblastn));
		BlastParams b = service.getParams();
		service.addListener(new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String propertyName = evt.getPropertyName();
				if(propertyName.equals(AbstractBlastService.PUT_COMMAND_COMPLETED_PROPERTY)){
					AbstractBlastService service = (AbstractBlastService)evt.getSource();
					PutResult putResult = service.getPutResult();
					System.out.println(putResult);
					System.exit(0);
				}
			}});
	

		b.setQuery(query);
		b.setQueryFrom(1);
		b.setQueryTo(query.length());
	
		service.execute();
	}

	@Override
	protected void validatePutCommand(BlastParams params) {
		if(params.getWordSize()==null || params.getWordSize() > 3 || params.getWordSize() < 2){
			throw new IllegalArgumentException("Word Size must be 2 or 3");
		}
	}

	@Override
	protected void validateGetCommand(GetCommand getCommand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Integer getMinWordSize() {
		return 2;
	}

	@Override
	protected Integer getMaxWordSize() {
		return 3;
	}

	@Override
	protected PROGRAM getAllowedProgram() {
		return PROGRAM.tblastn;
	}

	public final static List<BLAST_PROGRAMS> ALLOWED_BLAST_PROGRAMS = new ArrayList<BLAST_PROGRAMS>();
	public final static List<Database> ALLOWED_DATABASES = new ArrayList<Database>();
	
	static {
		ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.tblastn);
		
		ALLOWED_DATABASES.add(Database.NUCL_COLLECTION);
		ALLOWED_DATABASES.add(Database.REF_RNA_SEQ);
		ALLOWED_DATABASES.add(Database.REF_GENOMIC_SEQ);
		ALLOWED_DATABASES.add(Database.NCBI_GENOMES);
		ALLOWED_DATABASES.add(Database.EST);
		ALLOWED_DATABASES.add(Database.EST_OTHERS);
		ALLOWED_DATABASES.add(Database.GSS);
		ALLOWED_DATABASES.add(Database.HTGS);
		ALLOWED_DATABASES.add(Database.PAT_SEQ);
		ALLOWED_DATABASES.add(Database.PROTEIN_DATA_BANK);
		ALLOWED_DATABASES.add(Database.ALU);
		ALLOWED_DATABASES.add(Database.DBSTS);
		ALLOWED_DATABASES.add(Database.WGS);
		ALLOWED_DATABASES.add(Database.ENV_NT);
		ALLOWED_DATABASES.add(Database.TSA);

	}
	
	@Override
	protected List<BLAST_PROGRAMS> getAllowedBlastPrograms() {
		return ALLOWED_BLAST_PROGRAMS;
	}

	@Override
	protected List<Database> getAllowedDatabases() {
		return ALLOWED_DATABASES;
	}

}
