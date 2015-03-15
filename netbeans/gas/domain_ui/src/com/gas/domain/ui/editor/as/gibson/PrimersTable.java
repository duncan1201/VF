/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.domain.core.primer3.OverlapPrimer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author dq
 */
public class PrimersTable extends JTable {

    public PrimersTable() {
        super(new TableModel());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.getTableHeader().setReorderingAllowed(false);
        hookupListeners();
                       
        final FontMetrics fm = FontUtil.getFontMetrics(getTableHeader());
        final int margin = fm.charWidth('A') * 2;
        for(int i = 0; i < getColumnCount(); i++){
            String columnName = getModel().getColumnName(i);
            int strWidth = fm.stringWidth(columnName);
            getColumnModel().getColumn(i).setMinWidth(strWidth + margin);
        }
    }
    
    private void hookupListeners(){
        getSelectionModel().addListSelectionListener(new PrimersTableListeners.TblListener(this));
    }
    
    void setSelectedRow(String asName, boolean forward){
        int rowCount = getRowCount();
        TableModel model = (TableModel) getModel();
        for(int i = 0; i < rowCount; i++){
            OverlapPrimer overlapPrimer = model.data.get(i);
            if(overlapPrimer.getName().equals(asName) && overlapPrimer.isForward() == forward){
                int viewId = convertRowIndexToView(i);
                //getSelectionModel().setAnchorSelectionIndex(viewId);
                getSelectionModel().setSelectionInterval(viewId, viewId);
                //repaint();
                break;
            }
        }        
    }

    void setOverlapPrimers(List<OverlapPrimer> primers) {
        TableModel model = (TableModel) getModel();
        model.setData(primers);
    }
    
    OverlapPrimer getSelectedOverlapPrimer(){
        OverlapPrimer ret = null;
        int row = getSelectedRow();
        if(row > -1){
            int modelIndex = this.convertRowIndexToModel(row);
            TableModel model = (TableModel) getModel();
            ret = model.data.get(modelIndex);
        }
        return ret;
    }
    
    List<OverlapPrimer> getOverlapPrimers(){
        TableModel model = (TableModel) getModel();
        return model.data;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension ret = new Dimension(0, rowHeight * 6);
        return ret;
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row,
            int column) {
        Component ret = super.prepareRenderer(renderer, row, column);
        if (!ret.getBackground().equals(getSelectionBackground())) {
            if (row % 2 == 0) {
                ret.setBackground(ColorCnst.ALICE_BLUE);
            } else {
                ret.setBackground(Color.WHITE);
            }
        }
        return ret;
    }

    static class TableModel extends AbstractTableModel {

        final String COL_NAME = "Name";
        final String COL_STRAND = "Strand";
        final String COL_ANNEAL_LENGTH = "Anneal Length";
        final String COL_OVERLAP_LENGTH = "Overlap Length";
        final String COL_TOTAL_LENGTH = "Total Length";
        final String COL_TM_ANNEAL = "Tm anneal";
        final String COL_WARNINGS = "Warnings";
        final String[] COL_NAMES = {COL_NAME, COL_STRAND, COL_TM_ANNEAL, COL_ANNEAL_LENGTH, COL_OVERLAP_LENGTH, COL_TOTAL_LENGTH, COL_WARNINGS};
        List<OverlapPrimer> data = new ArrayList<OverlapPrimer>();

        void setData(List<OverlapPrimer> data) {
            this.data = data;
            this.fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.size();
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
        public Object getValueAt(int rowIndex, int columnIndex) {
            String name = getColumnName(columnIndex);
            if (name.equals(COL_NAME)) {
                return data.get(rowIndex).getName();
            } else if (name.equals(COL_STRAND)) {
                return data.get(rowIndex).getOligoElement().getForward() ? "Forward" : "Reverse";
            } else if (name.equals(COL_ANNEAL_LENGTH)) {
                return data.get(rowIndex).getOligoElement().getLength() + " bp";
            } else if (name.equals(COL_OVERLAP_LENGTH)) {
                return data.get(rowIndex).getOverlapLength() + " bp";
            } else if (name.equals(COL_TM_ANNEAL)) {
                return data.get(rowIndex).getOligoElement().getTm() + " " + Unicodes.CELSIUS;
            } else if (name.equals(COL_TOTAL_LENGTH)) {
                return data.get(rowIndex).getLength() + " bp";
            } else if (name.equals(COL_WARNINGS)){
                return data.get(rowIndex).getProblemCount();
            } else {
                return "-";
            }
        }
    }
}
