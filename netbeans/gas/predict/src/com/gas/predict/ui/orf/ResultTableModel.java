/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.domain.core.orf.api.ORF;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dq
 */
class ResultTableModel extends AbstractTableModel {

    private final String COL_FROM = "From";
    private final String COL_TO = "To";
    private final String COL_LENGTH = "Length";
    private final String COL_FRAME = "Frame";
    private final String[] COL_NAMES = {COL_FROM, COL_TO, COL_LENGTH, COL_FRAME};
    private List<ORF> orfs = new ArrayList<ORF>();

    @Override
    public int getRowCount() {
        return orfs.size();
    }

    @Override
    public int getColumnCount() {
        return COL_NAMES.length;
    }
    
    ORF getRow(int row){
        return orfs.get(row);
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ORF orf = orfs.get(rowIndex);
        String colName = getColumnName(columnIndex);
        if (colName.equals(COL_FROM)) {
            return orf.getStartPos();
        } else if (colName.equals(COL_TO)) {
            return orf.getEndPos();
        } else if (colName.equals(COL_LENGTH)) {
            return orf.getLength();
        } else if (colName.equals(COL_FRAME)) {
            return orf.getFrame();
        } else {
            return "-";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String name = getColumnName(columnIndex);
        if (name.equals(COL_FRAME)) {
            return Integer.class;
        } else if (name.equals(COL_FROM)) {
            return Integer.class;
        } else if (name.equals(COL_TO)) {
            return Integer.class;
        } else if (name.equals(COL_LENGTH)) {
            return Integer.class;
        } else if (name.equals(COL_FRAME)) {
            return Integer.class;
        } else {
            return String.class;
        }
    }

    public void setOrfs(List<ORF> orfs) {
        this.orfs = orfs;
    }
}
