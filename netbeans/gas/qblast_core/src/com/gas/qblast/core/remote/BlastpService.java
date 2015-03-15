package com.gas.qblast.core.remote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.gas.qblast.core.parameter.BlastParams;
import com.gas.qblast.core.parameter.BlastParams.BLAST_PROGRAMS;
import com.gas.qblast.core.parameter.BlastParams.PROGRAM;
import com.gas.qblast.core.parameter.BlastParamsFactory;
import com.gas.qblast.core.parameter.Database;

public class BlastpService extends AbstractBlastService{
	
	public BlastpService(){
		
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException{
		String query = "MRVISRARSACTWTSCTSLSPCSTSCPPSPAAPTLLRRRSLPQQRRRPSSSPNRRVRGVTTSPCPTRSLVYKRRVGAPQRLCAETVATMQAQEANALLLSRMEALEWFKKFTVWLRVYAIFIFQLAFSFGLGSVFWLGFPQNRNFCVENYSFFLTVLVPIVCMFITYTLGNEHPSNATVLFIYLLANSLTAAIFQMCSESRVLVGSYVMTLALFISFTGLAFLGGRDRRRWKCISCVYVVMLLSFLTLALLSDADWLQKIVVTLCAFSISFFLGILAYDSLMVIFFCPPNQCIRHAVCLYLDSMAIFLTLLLMLSGPRWISLSDGVPLDNGTLTAASTTGKS";

		BlastpService service = new BlastpService();
		service.setParams(BlastParamsFactory.createInstance(PROGRAM.blastp));
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
		BlastParams b = service.getParams();
		b.setQuery(query);
		b.setQueryFrom(1);
		b.setQueryTo(query.length());

		service.execute();
	}

	@Override
	protected void validatePutCommand(BlastParams params) {
		if(params.getProgram() == null || params.getProgram() != BlastParams.PROGRAM.blastp){
			throw new IllegalArgumentException("Program must be blastp");
		}
		if(params.getBlastPrograms() == null || params.getBlastPrograms() != BlastParams.BLAST_PROGRAMS.blastp){
			throw new IllegalArgumentException("Blast Program must be blastp");
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
		// TODO Auto-generated method stub
		return PROGRAM.blastp;
	}

	public final static List<BLAST_PROGRAMS> ALLOWED_BLAST_PROGRAMS = new ArrayList<BLAST_PROGRAMS>();
	public final static List<Database> ALLOWED_DATABASES = new ArrayList<Database>();
	
	static {
		ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.blastp);
		ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.phi);
		ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.psi);
		
		ALLOWED_DATABASES.add(Database.NON_REDUNDANT_PROTEIN_SEQUENCE);
		ALLOWED_DATABASES.add(Database.REFSEQ_PROTEIN);
		ALLOWED_DATABASES.add(Database.SWISSPROT);
		ALLOWED_DATABASES.add(Database.PATENTED_PROTEIN_SEQ);
		ALLOWED_DATABASES.add(Database.PROTEIN_DATA_BANK_PROTEINS);
		ALLOWED_DATABASES.add(Database.ENV_SAMPLES);

	}
	
	@Override
	protected List<BLAST_PROGRAMS> getAllowedBlastPrograms() {
		// TODO Auto-generated method stub
		return ALLOWED_BLAST_PROGRAMS;
	}

	@Override
	protected List<Database> getAllowedDatabases() {
		// TODO Auto-generated method stub
		return ALLOWED_DATABASES;
	}
}
