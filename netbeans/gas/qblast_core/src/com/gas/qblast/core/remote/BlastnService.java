package com.gas.qblast.core.remote;

import java.util.ArrayList;
import java.util.List;

import com.gas.qblast.core.parameter.BlastParams;
import com.gas.qblast.core.parameter.BlastParams.BLAST_PROGRAMS;
import com.gas.qblast.core.parameter.BlastParams.PROGRAM;
import com.gas.qblast.core.parameter.Database;
import com.gas.qblast.core.remote.api.IBlastnService;

public class BlastnService extends AbstractBlastService implements IBlastnService{

    public BlastnService() {
    }

    @Override
    protected void validatePutCommand(BlastParams params) {
        if (params.getProgram() == null || params.getProgram() != BlastParams.PROGRAM.blastn) {
            throw new IllegalArgumentException("Program must be blastn");
        }
        BLAST_PROGRAMS blastProgram = params.getBlastPrograms();
        if (blastProgram == null
                || (blastProgram != BlastParams.BLAST_PROGRAMS.blastn && blastProgram != BlastParams.BLAST_PROGRAMS.megaBlast && blastProgram != BlastParams.BLAST_PROGRAMS.discoMegablast)) {
            throw new IllegalArgumentException("Blast Program must be blastn");
        }
    }

    @Override
    protected void validateGetCommand(GetCommand getCommand) {
        // TODO Auto-generated method stub
    }

    @Override
    protected Integer getMinWordSize() {
        return 16;
    }

    @Override
    protected Integer getMaxWordSize() {
        return 256;
    }

    @Override
    protected PROGRAM getAllowedProgram() {
        return PROGRAM.blastn;
    }
    public final static List<BLAST_PROGRAMS> ALLOWED_BLAST_PROGRAMS = new ArrayList<BLAST_PROGRAMS>();
    public final static List<Database> ALLOWED_DATABASES = new ArrayList<Database>();

    static {
        ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.blastn);
        ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.megaBlast);
        ALLOWED_BLAST_PROGRAMS.add(BLAST_PROGRAMS.discoMegablast);

        ALLOWED_DATABASES.add(Database.HUMAN_G_T);
        ALLOWED_DATABASES.add(Database.MOUSE_G_T);
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
