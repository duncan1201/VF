/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import com.gas.domain.core.tasm.Condig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dq
 */
public class ContigsTableModel extends AbstractTableModel {

    // shared column
    private final static String NAME_COL = "Name";
    private final static String LENGTH_COL = "Length";
    private final static String READ_COUNT = "Reads";
    private final static String QUAL_SCORE_COL = "Q. Score";
    private final static String MISMATCH_COL = "Mismatch";
    private final static String N_PERCENT_COL = "Ns %";
    private final static String COVERAGE_COL = "COVRG*";
    
    
    private final static String[] COL_NAMES = {NAME_COL, LENGTH_COL, READ_COUNT, QUAL_SCORE_COL,  COVERAGE_COL};
    final static Map<String, String> TOOLTIPS = new HashMap<String, String>();
    static {
        TOOLTIPS.put(NAME_COL, "Contig name");
        TOOLTIPS.put(LENGTH_COL, "Contig length");
        TOOLTIPS.put(READ_COUNT, "Read count");
        TOOLTIPS.put(QUAL_SCORE_COL, "Quality score");
        TOOLTIPS.put(COVERAGE_COL, "Contig coverage defined as sum of kromatograms length divided by contig length");
    }
    // Condig from Lassie/Tasm
    private final static String REDUNDANCY_COL = "Redundancy";

    
    private List<Condig> data = new ArrayList<Condig>();

    public String getTooltip(int columnIndex){        
        String colName = getColumnName(columnIndex);
        String ret = TOOLTIPS.get(colName);
        return ret;
    }
    
    public List getData() {
        return data;
    }

    public void setData(List<Condig> data) {
        this.data = data;              
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    public String getColumnNames(int column) {
        return COL_NAMES[column];
    }

    @Override
    public int getColumnCount() {                
        return COL_NAMES.length;
    }

    
    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {        
        String name = getColumnName(columnIndex);
        if(name.equals(NAME_COL)){
            return String.class;
        }else if(name.equals(READ_COUNT)){
            return Integer.class;
        }else if(name.equals(LENGTH_COL)){
            return Integer.class;
        }else if(name.equals(QUAL_SCORE_COL)){
            return Float.class;
        }else if(name.equals(N_PERCENT_COL)){
            return Float.class;
        }else if(name.equals(COVERAGE_COL)){
            return Float.class;
        }else{
            return String.class;
        }        
    }
    
    public Object getRow(int i){
        Object ret = null;
        if(i > -1 && i < data.size()){
            ret = data.get(i);
        }
        return ret;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = null;
        Object row = data.get(rowIndex);
        String colName = getColumnName(columnIndex);
        if(data.get(0) instanceof Condig){
            Condig condig = (Condig)row;
            if(colName.equals(NAME_COL)){
                ret = condig.getAsmblId().toString();
            }else if(colName.equals(LENGTH_COL)){
                ret = condig.getGappedLength();
            }else if(colName.equals(MISMATCH_COL)){
                ret = condig.getMismatchCount();
            }else if(colName.equals(QUAL_SCORE_COL)){
                ret = (40 - condig.getAvgQualityScore()) * 2.5f;
            }else if(colName.equals(N_PERCENT_COL)){
                ret = condig.getPercentN();
            }else if(colName.equals(READ_COUNT)){
                ret = condig.getRids().size();
            }else if(colName.equals(COVERAGE_COL)){
                ret = condig.getRedundancy();
            }else{
                throw new IllegalArgumentException("");
            }
        }
        return ret;
    }   
}
