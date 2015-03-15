/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.sitesfound;

import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.ren.RMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dunqiang
 */
public class SitesFoundTableModel extends AbstractTableModel {

    private final String COL_SITE = "Site";
    private final String COL_SEQ = "Sequence";
    private final String COL_NO = "No";
    private final String COL_POSITION = "Position";
    private String[] COLUMNS = {COL_SITE, COL_SEQ, COL_POSITION};
    private List<Row> rows = new ArrayList<Row>();

    public SitesFoundTableModel(){}
    
    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    public void setRMap(RMap rmap, int totalPos) {
        Iterator<RMap.Entry> itr = rmap.getEntriesIterator();
        Map<String, Integer> counts = rmap.getCount();

        rows.clear();

        int no = 1;
        while (itr.hasNext()) {
            RMap.Entry entry = itr.next();
            Row row = new Row();
            row.setData(entry);
            row.setName(entry.getName());
            row.setSequence(entry.getVisual());            
            Loc loc = new Loc(entry.getStart(), entry.getEnd());
            loc.setTotalPos(totalPos);
            row.setPosition(loc);
            int count = counts.get(entry.getName());
            row.setTotal(count);

            row.setNo(no);
            if (no >= count) {
                no = 1;
            } else {
                no++;
            }
            rows.add(row);
        }
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String colName = COLUMNS[columnIndex];
        if (colName.equals(COL_SITE)) {
            return String.class;
        } else if (colName.equals(COL_SEQ)) {
            return String.class;
        } else if (colName.equals(COL_POSITION)) {
            return Loc.class;
        } else {
            throw new IllegalArgumentException("Fix me. Developers");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Row row = rows.get(rowIndex);
        String colName = COLUMNS[columnIndex];
        if (colName.equals(COL_SITE)) {
            return row.getName();
        } else if (colName.equals(COL_SEQ)) {
            return row.getSequence();
        } else if (colName.equals(COL_NO)) {
            return String.format("%d/%d", row.getNo(), row.getTotal());
        } else if (colName.equals(COL_POSITION)) {
            return row.getPosition();
        } else {
            throw new IllegalArgumentException("Fix me. Developers");
        }
    }

    public RMap.EntryList getRMapEntries(int[] rowIndexes) {
        RMap.EntryList ret = new RMap.EntryList();
        for (int rowIndex : rowIndexes) {
            Row row = rows.get(rowIndex);
            ret.add((RMap.Entry) row.getData());
        }
        return ret;
    }

    public Row getRow(int rowIndex) {
        Row ret = null;
        if (rowIndex > -1 && rowIndex < rows.size()) {
            ret = rows.get(rowIndex);
        }
        return ret;
    }
    
    public RowList getRows(int[] rowIndexes){
        RowList ret = new RowList();
        for(int index: rowIndexes){
            Row row = getRow(index);
            ret.add(row);
        }
        return ret;
    }
    
    public static class RowList extends ArrayList<Row> {
        
        public RMap.EntryList getEntries(){
            RMap.EntryList ret = new RMap.EntryList();
            Iterator<Row> itr = iterator();
            while(itr.hasNext()){
                Row row = itr.next();
                ret.add((RMap.Entry)row.getData());
            }
            return ret;
        }
    }

    public static class Row {

        private String name;
        private String sequence;
        private int total;
        private int no;
        private Loc position;
        private Object data;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public int[] getDownstreamCutPos(){
            return ((RMap.Entry)data).getDownstreamCutPos();
        }

        /**
         * @see com.gas.domain.core.ren.REN#OVERHANG_5PRIME
         * @see com.gas.domain.core.ren.REN#OVERHANG_3PRIME
         * @see com.gas.domain.core.ren.REN#BLUNT
         */        
        public Integer getDownstreamCutType() {
            return ((RMap.Entry)data).getDownstreamCutType();
        }
        
        public Loc getPosition() {
            return position;
        }

        public void setPosition(Loc position) {
            this.position = position;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
