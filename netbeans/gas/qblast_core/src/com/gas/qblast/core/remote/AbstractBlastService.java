package com.gas.qblast.core.remote;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.gas.qblast.core.output.BlastOutput;
import com.gas.qblast.core.parameter.BlastParams;
import com.gas.qblast.core.parameter.BlastParams.BLAST_PROGRAMS;
import com.gas.qblast.core.parameter.BlastParams.PROGRAM;
import com.gas.qblast.core.parameter.Database;
import com.gas.qblast.core.remote.GetCommand.FORMAT_TYPE;
import com.gas.qblast.parser.BlastOutputParser;

public abstract class AbstractBlastService {

    public final static String PUT_COMMAND_COMPLETED_PROPERTY = "PUT_COMMAND_COMPLETED";
    public final static String GET_COMMAND_COMPLETED_PROPERTY = "GET_COMMAND_COMPLETED";
    public final static String PARSING_OUTPUT_COMPLETED_PROPERTY = "PARSING_OUTPUT_COMPLETED_PROPERTY";
    private PropertyChangeSupport propertyChangeSupport;
    private PutResult putResult;
    private String getResult;
    private BlastOutput blastOutput;
    private BlastParams params;
    protected PutCommand putCommand = new PutCommand();
    protected GetCommand getCommand = new GetCommand();

    protected abstract void validatePutCommand(BlastParams params);

    protected abstract void validateGetCommand(GetCommand getCommand);

    protected abstract Integer getMinWordSize();

    protected abstract Integer getMaxWordSize();

    protected abstract PROGRAM getAllowedProgram();

    protected abstract List<BLAST_PROGRAMS> getAllowedBlastPrograms();

    protected abstract List<Database> getAllowedDatabases();

    public AbstractBlastService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public String getGetResult() {
        return getResult;
    }

    public void setGetResult(String getResult) {
        this.getResult = getResult;
    }

    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private void commonPutCommandValidation(BlastParams params) {
        if (params.getProgram() == null || params.getProgram() != getAllowedProgram()) {
            throw new IllegalArgumentException("Program must be " + getAllowedProgram());
        }
        if (params.getBlastPrograms() == null || !getAllowedBlastPrograms().contains(params.getBlastPrograms())) {
            throw new IllegalArgumentException("Blast program " + params.getBlastPrograms() + " is not allowed");
        }
        if (params.getDatabase() == null || !getAllowedDatabases().contains(params.getDatabase())) {
            throw new IllegalArgumentException("Database " + params.getDatabase().getDesc() + " is not allowed");
        }
        if (params.getWordSize() == null || params.getWordSize() > getMaxWordSize() || params.getWordSize() < getMinWordSize()) {
            throw new IllegalArgumentException("Word Size must be between " + getMinWordSize() + " and " + getMaxWordSize());
        }


    }

    public void execute() {
        commonPutCommandValidation(params);
        validatePutCommand(params);
        putResult = putCommand.execute(params);
        propertyChangeSupport.firePropertyChange(PUT_COMMAND_COMPLETED_PROPERTY, null, putResult);

        validateGetCommand(getCommand);
        getResult = getCommand.execute(putResult);

        propertyChangeSupport.firePropertyChange(GET_COMMAND_COMPLETED_PROPERTY, null, getResult);

        if (getCommand.getFormatType() == FORMAT_TYPE.XML) {

            blastOutput = BlastOutputParser.parse(getResult);

            propertyChangeSupport.firePropertyChange(PARSING_OUTPUT_COMPLETED_PROPERTY, null, blastOutput);
        }
    }

    public BlastParams getParams() {
        if (params == null) {
            params = new BlastParams();
        }
        return params;
    }

    public void setParams(BlastParams params) {
        this.params = params;
    }

    public BlastOutput getBlastOutput() {
        return blastOutput;
    }

    public PutResult getPutResult() {
        return putResult;
    }

    public PutCommand getPutCommand() {
        return putCommand;
    }

    public GetCommand getGetCommand() {
        return getCommand;
    }
}
