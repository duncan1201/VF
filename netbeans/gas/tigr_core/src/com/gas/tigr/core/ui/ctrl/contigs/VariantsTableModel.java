/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dq
 */
public class VariantsTableModel extends AbstractTableModel {

    private String NO_COL = "NO";
    private String POS_COL = "Position";
    private String BASE_COL = "Bases";
    private String[] COL_NAMES = {NO_COL, POS_COL, BASE_COL};
    private List<Row> rows = new ArrayList<Row>();

    public List<Row> getRows() {
        return rows;
    }
    
    public Row getRow(int i){
        Row ret = null;
        if(i < rows.size() && i > -1){
            ret = rows.get(i);
        }
        return ret;
    }
    
    @Override
    public Class<?> getColumnClass(int colIndex) {
	Class ret = null;
        if(COL_NAMES[colIndex].equals(NO_COL)){
            ret = Integer.class;
        }else if(COL_NAMES[colIndex].equals(POS_COL)){
            ret = Integer.class;
        }else if(COL_NAMES[colIndex].equals(BASE_COL)){
            ret = String.class;
        }
        return ret;
        
    }    

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    @Override
    public int getColumnCount() {
        return COL_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = null;
        Row row = rows.get(rowIndex);
        String colName = getColumnName(columnIndex);
        if(colName.equals(NO_COL)){
            ret = row.getNo();
        }else if(colName.equals(POS_COL)){
            ret = row.getPos();
        }else if(colName.equals(BASE_COL)){
            ret = row.getBase();
        }
        return ret;
    }

    public static class Row {

        private Integer no;
        private Integer pos;
        private String base;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }

        public Integer getPos() {
            return pos;
        }

        public void setPos(Integer pos) {
            this.pos = pos;
        }
    }
}
