/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.orf.api.ORF;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultRowSorter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author dq
 */
class ORFResultPanel extends JPanel {

    private WeakReference<JTable> tableRef;

    ORFResultPanel() {
        initComponents();        
        hookupListeners();
    }
    
    private void initComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        ResultTableModel model = new ResultTableModel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().setBackground(Color.WHITE);
        JTable table = new JTable(model) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return getPreferredSize();
            }
            
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
                Component ret = super.prepareRenderer(renderer, row, column);
                if(!ret.getBackground().equals(getSelectionBackground())){
                    if(row % 2 == 0){
                        ret.setBackground(ColorCnst.ALICE_BLUE);
                    }else{
                        ret.setBackground(Color.WHITE);
                    }
                }
                return ret;
            }
        }; 
        UIUtil.calibrateTableColumnWidth(new String[]{"From ", "To ", "Length ", "Frame "}, table); 
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        tableRef = new WeakReference<JTable>(table);
        scrollPane.setViewportView(table);
        scrollPane.getViewport().getViewRect();
        add(scrollPane, c);        
    }
    
    private void hookupListeners(){
        tableRef.get().getSelectionModel().addListSelectionListener(new ORFResultPanelListeners.TableSelListener(tableRef.get()));
    }

    protected void sort() {
        DefaultRowSorter sorter = (DefaultRowSorter) tableRef.get().getRowSorter();
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }

    public void setORF(Set<ORF> orfs) {
        setORF(new ArrayList<ORF>(orfs));
    }

    public void setORF(List<ORF> orfs) {
        ResultTableModel model = (ResultTableModel) tableRef.get().getModel();
        model.setOrfs(orfs);
        model.fireTableDataChanged();
    }    
}
