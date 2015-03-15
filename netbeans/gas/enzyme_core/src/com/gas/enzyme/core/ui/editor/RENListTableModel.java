/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.editor;

import com.gas.common.ui.core.StringList;
import com.gas.domain.core.ren.REN;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dq
 */
public class RENListTableModel extends AbstractTableModel {

    private List<Row> rows = new ArrayList<Row>();
    private final String NAME_COL = "Name";
    private final String SEQ_COL = "Recognition site";
    private final String OVERHANG_COL = "Overhang";
    private final String PALINDROMIC_COL = "Palindromic";
    private final String AMBIGUOUS_COL = "Ambiguous";
    private final String VISUAL_COL = "Visual";
    private String[] COL_NAMES = {NAME_COL, SEQ_COL, OVERHANG_COL, VISUAL_COL, PALINDROMIC_COL, AMBIGUOUS_COL};

    @Override
    public int getRowCount() {
        return rows.size();
    }

    public Row getRow(int rowIndex) {
        return rows.get(rowIndex);
    }
    
    public StringList getNames(){
        StringList ret = new StringList();
        Iterator<Row> itr = rows.iterator();
        while(itr.hasNext()){
            Row row = itr.next();
            ret.add(row.getName());
        }
        return ret;
    }

    public void removeRow(int rowIndex) {
        rows.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void set(List<REN> rens) {
        rows.clear();
        for (REN ren : rens) {
            rows.add(new Row(ren));
        }
        fireTableDataChanged();
    }
    
    public void remove(List<REN> rens){
        for(REN ren: rens){
            remove(ren);
        }
    }

    public void remove(REN ren) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getRen().equals(ren)) {
                rows.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }

    public void add(REN ren) {
        rows.add(0, new Row(ren));
        fireTableRowsInserted(0, 0);
    }

    @Override
    public int getColumnCount() {
        return COL_NAMES.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> ret = null;
        String colName = getColumnName(columnIndex);
        if (colName.equals(NAME_COL)) {
            ret = String.class;
        } else if (colName.equals(SEQ_COL)) {
            ret = String.class;
        } else if (colName.equals(OVERHANG_COL)) {
            ret = String.class;
        } else if (colName.equals(PALINDROMIC_COL)) {
            ret = String.class;
        } else if (colName.equals(VISUAL_COL)) {
            ret = String.class;
        } else if (colName.equals(AMBIGUOUS_COL)) {
            ret = String.class;
        } else {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COL_NAMES[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = null;
        String colName = getColumnName(columnIndex);
        Row row = rows.get(rowIndex);
        if (colName.equals(NAME_COL)) {
            ret = row.getName();
        } else if (colName.equals(SEQ_COL)) {
            ret = row.getSeq();
        } else if (colName.equals(PALINDROMIC_COL)) {
            ret = row.isPalindromic()? "Yes" : "No";
        } else if (colName.equals(OVERHANG_COL)) {
            ret = row.getOverhang();
        } else if (colName.equals(VISUAL_COL)) {
            ret = row.getVisual();
        } else if (colName.equals(AMBIGUOUS_COL)) {
            ret = row.isAmbiguous()? "Yes" : "No";
        } else {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    public static class Row {

        private String name;
        private String seq;
        private String overhang;
        private boolean palindromic;
        private boolean ambiguous;
        private String visual;        
        private REN ren;

        public Row(REN ren) {
            this.ren = ren;
            this.name = ren.getName();
            this.seq = ren.getRecognitionSite();
            this.overhang = createOverhangStr(ren);
            this.palindromic = ren.isPalindromic();
            this.ambiguous = ren.isAmbiguous();
            this.visual = ren.getVisual();
        }

        public REN getRen() {
            return ren;
        }

        public void setRen(REN ren) {
            this.ren = ren;
        }

        public String getName() {
            return name;
        }

        public boolean isAmbiguous() {
            return ambiguous;
        }
        
        public String getOverhang() {
            return overhang;
        }

        public String getSeq() {
            return seq;
        }

        public boolean isPalindromic() {
            return palindromic;
        }
        
        public String getVisual() {
            return visual;
        }

        private String createOverhangStr(REN ren) {
            StringBuilder ret = new StringBuilder();

            int downEndType = ren.getDownstreamEndType();
            if (downEndType == REN.OVERHANG_3PRIME || downEndType == REN.OVERHANG_5PRIME) {
                ret.append(downEndType == REN.OVERHANG_3PRIME ? "3'" : "5'");
                ret.append('(');
                String o = ren.getOverhang();
                if(o == null || o.isEmpty()){
                    ret.append("Outside of recognition site");
                }else{
                    ret.append(o);
                }
                ret.append(')');
            } else if (downEndType == REN.BLUNT) {
                ret.append("blunt");
            }
            return ret.toString().toUpperCase(Locale.ENGLISH);
        }
    }
}
