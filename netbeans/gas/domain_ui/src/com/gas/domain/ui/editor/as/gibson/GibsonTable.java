/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.util.ColorCnst;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author dq
 */
public class GibsonTable extends JTable implements Scrollable{
    
    public GibsonTable(){        
        setModel(new GibsonModel());
    }
    
    public void addAnnotatedSeqs(List<AnnotatedSeq> asList){
        GibsonModel model = (GibsonModel)getModel();
        model.data.addAll(asList);
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
    
    @Override
    public Dimension getPreferredScrollableViewportSize(){
        return this.getPreferredSize();
    }
}

class GibsonModel extends AbstractTableModel{

    AnnotatedSeqList data = new AnnotatedSeqList();
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }
    
    @Override
    public String getColumnName(int column) {
        return "Name";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String ret = data.get(rowIndex).getName();
        return ret;
    }
}
