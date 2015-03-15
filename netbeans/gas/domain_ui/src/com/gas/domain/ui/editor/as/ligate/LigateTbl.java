/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.*;

/**
 *
 * @author dq
 */
class LigateTbl extends JTable {

    protected static Color SELECTED_BACKGROUND = new Color(64, 160, 255);

    LigateTbl(LigateTblMdl model) {
        super(model);
        setShowGrid(true);
        setShowHorizontalLines(true);
        setShowVerticalLines(true);
        setSelectionBackground(SELECTED_BACKGROUND);
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // disable sorting
        setAutoCreateRowSorter(false);
        TableRowSorter<LigateTblMdl> sorter =
                new TableRowSorter<LigateTblMdl>(model);
        for (int c = 0; c < model.getColumnCount(); c++) {
            sorter.setSortable(c, false);
        }
        setRowSorter(sorter);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension ret = this.getPreferredSize();
        ret.width = ret.width * 2;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ret.width = Math.min(ret.width, Math.round(screenSize.width * 0.3f));
        return ret;
    }

    boolean compatible() {
        return getModel().rows.compatible();
    }

    @Override
    public LigateTblMdl getModel() {
        return (LigateTblMdl) super.getModel();
    }

    public AnnotatedSeqList getModifiedData() {
        LigateTblMdl mdl = (LigateTblMdl) getModel();
        return mdl.getModifiedData();
    }    
    
    void setSelected(String asName){
        LigateTblMdl mdl = (LigateTblMdl) getModel();
        for(int i = 0; i < mdl.getRowCount(); i++){
            AnnotatedSeq as = mdl.getAs(i);
            if(as.getName().equals(asName)){
                ListSelectionModel selectMdl = getSelectionModel();
                selectMdl.setSelectionInterval(i, i);
                break;
            }
        }
    }

    public AnnotatedSeq getSelectedAs() {
        AnnotatedSeq ret = null;
        int row = getSelectedRow();
        if (row > -1) {
            LigateTblMdl model = (LigateTblMdl) getModel();
            ret = model.getAs(row);
        }
        return ret;
    }

    public LigateTblMdl.MOD getSelectedMod() {
        LigateTblMdl.MOD ret = null;
        int row = getSelectedRow();
        if (row > -1) {
            LigateTblMdl model = (LigateTblMdl) getModel();
            ret = model.getMod(row);
        }
        return ret;
    }

    public List<LigateTblMdl.dNTP> getSelected_dNTP() {
        List<LigateTblMdl.dNTP> ret = null;
        int row = getSelectedRow();
        if (row > -1) {
            LigateTblMdl mdl = (LigateTblMdl) getModel();
            ret = mdl.get_dNTP(row);
        }
        return ret;
    }

    public void setRow(int i, AnnotatedSeq as) {
        LigateTblMdl model = (LigateTblMdl) getModel();
        model.setRow(i, as);
        model.fireTableDataChanged();
        doubleRowHeights();
        getSelectionModel().setSelectionInterval(i, i);
    }

    public void setCircularize(boolean circularize) {
        final int row = getSelectedRow();
        LigateTblMdl model = getModel();
        model.rows.circularize = circularize;
        model.fireTableDataChanged();
        doubleRowHeights();
        if (row > -1) {
            getSelectionModel().setSelectionInterval(row, row);
        }
    }

    public void setMod(int i, LigateTblMdl.MOD mod) {
        LigateTblMdl model = getModel();
        model.setMod(i, mod);
        model.fireTableDataChanged();
        doubleRowHeights();
        getSelectionModel().setSelectionInterval(i, i);
    }

    public void update_dNTP(int i, LigateTblMdl.dNTP _dNTP, boolean on) {
        LigateTblMdl model = (LigateTblMdl) getModel();
        model.updateNTP(i, _dNTP, on);
        model.fireTableDataChanged();
        doubleRowHeights();
        getSelectionModel().setSelectionInterval(i, i);
    }

    private void doubleRowHeights() {
        LigateTblMdl model = (LigateTblMdl) getModel();
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int height = getRowHeight(i);
            setRowHeight(i, height * 2);
        }
    }

    public void setData(List<AnnotatedSeq> objs) {
        LigateTblMdl model = (LigateTblMdl) getModel();
        model.setData(objs);
        model.fireTableDataChanged();
        doubleRowHeights();

        UIUtil.initColumnSizes(this);
    }

    public void moveUp() {
        int row = this.getSelectedRow();
        row = this.convertRowIndexToModel(row);
        LigateTblMdl tableModel = (LigateTblMdl) getModel();
        boolean success = tableModel.moveUp(row);
        if (success) {
            doubleRowHeights();
            boolean empty = getSelectionModel().isSelectionEmpty();
            getSelectionModel().setSelectionInterval(row - 1, row - 1);
        }
    }

    public void moveDown() {
        int row = this.getSelectedRow();
        LigateTblMdl tableModel = (LigateTblMdl) getModel();
        boolean success = tableModel.moveDown(row);
        if (success) {
            doubleRowHeights();
            boolean empty = getSelectionModel().isSelectionEmpty();
            getSelectionModel().setSelectionInterval(row + 1, row + 1);
        }
    }
}
