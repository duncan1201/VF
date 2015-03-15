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
 * Search protein database using a translated nucleotide query
 * */
public class BlastxService extends AbstractBlastService{

	@Override
	protected void validatePutCommand(BlastParams params) {
		if(params.getProgram() == null || params.getProgram() != BlastParams.PROGRAM.blastx){
			throw new IllegalArgumentException("Program must be blastx");
		}
		if(params.getBlastPrograms() == null || params.getBlastPrograms() != BlastParams.BLAST_PROGRAMS.blastx){
			throw new IllegalArgumentException("Blast Program must be blastx");
		}
		if(params.getWordSize()==null || params.getWordSize() > 3 || params.getWordSize() < 2){
			throw new IllegalArgumentException("Word Size must be 2 or 3");
		}
	}

	@Override
	protected void validateGetCommand(GetCommand getCommand) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		BlastxService service = new BlastxService();
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
		
		BlastParams params = BlastParamsFactory.createInstance(PROGRAM.blastx);
		service.setParams(params);
		String query = "AUGGCCGUCCUGCCGCCCCGAACUCUCCUCCUGCUACUCUCGGGGGCCCUGGCCCUGACCCAGACCUGGGCGGGCUCCCACUCCAUGAGGUAUUUCUUCACCUCCGUGUCCCGGCCCGGCCGCGGGGAGCCCCGCUUCAUCGCCGUGGGCUACGUGGACGACACGCAGUUCGUGCGGUUCGACAGCGACGCCGCGAGCCAGAGGAUGGAGCCGCGGGCGCCGUGGAUAGAGCAGGAGGGUCCGGAGUAUUGGGACCAGGAGACACGGAAUGUGAAGGCCCACUCACAGACUGACCGAGUGAACCUGGGGACCCUGCGCGGCUACUACAACCAGAGCGAGGACGGUUCUCACACCAUCCAGAUAAUGUAUGGCUGCGACGUGGGGUCGGACGGGCGCUUCCUCCGCGGGUACCGGCAGGACGCCUACGACGGCAAGGAUUACAUCGCCCUGAACGAGGACCUGCGCUCUUGGACCGCGGCGGACAUGGCAGCUCAGAUCACCAAGCGCAAGUGGGAGGCGGCCCAUGCGGCGGAGCAGCGGAGAGCCUACCUGGAGGGCCUGUGCGUGGACGGGCUCCGCAGAUACCUGGAGAACGGGAAGGAGACGCUGCAGCGCACGGACCCCCCCAAGACACAUAUGACCCACCACCCCAUCUCUGACCAUGAGGCCACCCUGAGGUGCUGGGCCCUGGGCUUCUACCCUGCGGAGAUCACACUGACCUGGCAGCGGGAUGGGGAGGACCAGACCCAGGACACGGAGCUCGUGGAGACCAGGCCCGCAGGGGAUGGAACCUUCCAGAAGUGGGCGGCUGUGGUGGUACCUUCUGGAGAGGAGCAGAGAUACACCUGCCAUGUGCAGCAUGAGGGUCUUCCCAAGCCCCUCACCCUGAGAUGGGAGCCGUCUUCCCAGCCCACCAUCCCCAUUGUGGGCAUCAUUGCUGGCCUGGUUCUCCUUGGAGCUGUGAUCACUGGAGCUGUGGUCGCUGCCGUGAUGUGGAGGAGGAAGAGCUCAGAUAGAAAAGGAGGGAGCUACACUCAGGCUGCAAGCAGUGACAGUGCCCAGGGCUCUGAUGUGUCUCUCACAGCUUGUAAAGUGUGA";

		
		//params.setProgram(PROGRAM.blastx);
		//params.setBlastPrograms(BLAST_PROGRAMS.blastx);
		params.setQuery(query);
		params.setQueryFrom(1);
		params.setQueryTo(query.length());

		service.execute();
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
		return PROGRAM.blastx;
	}

	public final static List<BLAST_PROGRAMS> ALLOWED_BLAST_PROGRAMS = new ArrayList<BLAST_PROGRAMS>();
	public final static List<Database> ALLOWED_DATABASES = new ArrayList<Database>();

	
	static{
		ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.blastx);
		
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
