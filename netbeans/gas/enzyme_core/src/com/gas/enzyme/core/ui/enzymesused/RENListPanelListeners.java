/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.enzymesused;

import com.gas.domain.core.ren.REN;
import com.gas.enzyme.core.ui.editor.RENListTableFilter;
import com.gas.enzyme.core.ui.editor.RENListTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultRowSorter;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author dq
 */
class RENListPanelListeners {

    static class AddBtnListener implements ActionListener {

        private RENListPanel newRENListPanel;

        public AddBtnListener(RENListPanel newRENListPanel) {
            this.newRENListPanel = newRENListPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RENListTableModel allTableModel = (RENListTableModel) newRENListPanel.allTable.getModel();
            RENListTableModel newTableModel = (RENListTableModel) newRENListPanel.newTable.getModel();

            int[] indices = newRENListPanel.allTable.getSelectedRows();
            List<REN> toBeRemoved = new ArrayList<REN>();
            for (int index : indices) {
                int modelIndex = newRENListPanel.allTable.convertRowIndexToModel(index);

                RENListTableModel.Row row = allTableModel.getRow(modelIndex);                
                REN ren = row.getRen();                
                toBeRemoved.add(ren);

                newTableModel.add(new REN(ren));
            }
            allTableModel.remove(toBeRemoved);
            
            TableRowSorter rowSorter = (TableRowSorter) newRENListPanel.newTable.getRowSorter();
            rowSorter.sort();
            newRENListPanel.validateInput();
        }
    }

    static class RemoveBtnListener implements ActionListener {

        private RENListPanel newRENListPanel;

        RemoveBtnListener(RENListPanel newRENListPanel) {
            this.newRENListPanel = newRENListPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RENListTableModel allTableModel = (RENListTableModel) newRENListPanel.allTable.getModel();
            RENListTableModel newTableModel = (RENListTableModel) newRENListPanel.newTable.getModel();

            int[] indices = newRENListPanel.newTable.getSelectedRows();
            for (int index : indices) {
                int modelIndex = newRENListPanel.newTable.convertRowIndexToModel(index);

                RENListTableModel.Row row = newTableModel.getRow(modelIndex);
                REN ren = row.getRen();
                newTableModel.remove(ren);

                allTableModel.add(ren);
            }
            TableRowSorter rowSorter = (TableRowSorter) newRENListPanel.allTable.getRowSorter();
            rowSorter.sort();
            newRENListPanel.validateInput();
        }
    }

    static class FiltersBtnListener implements ItemListener {

        private JTable table;

        FiltersBtnListener(JTable table) {
            this.table = table;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();
            String cmd = src.getActionCommand();
            if (cmd.equalsIgnoreCase("blunt")) {
                DefaultRowSorter rowSorter = (DefaultRowSorter) table.getRowSorter();
                RENListTableFilter filter = (RENListTableFilter) rowSorter.getRowFilter();

                filter.setIncludeBlunt(e.getStateChange() == ItemEvent.SELECTED);
                rowSorter.sort();
            } else if (cmd.equalsIgnoreCase("palindromic")) {
                DefaultRowSorter rowSorter = (DefaultRowSorter) table.getRowSorter();
                RENListTableFilter filter = (RENListTableFilter) rowSorter.getRowFilter();

                filter.setPalindromesOnly(e.getStateChange() == ItemEvent.SELECTED);
                rowSorter.sort();                
            }
        }
    }

    static class FiveBtnListener implements ItemListener {

        private JTable table;

        FiveBtnListener(JTable table) {
            this.table = table;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            TableRowSorter rowSorter = (TableRowSorter) table.getRowSorter();
            RENListTableFilter filter = (RENListTableFilter) rowSorter.getRowFilter();
            filter.setInclude5(e.getStateChange() == ItemEvent.SELECTED);
            rowSorter.sort();
        }
    }

    static class DocListener implements DocumentListener {

        private RENListPanel newRENListPanel;

        DocListener(RENListPanel newRENListPanel) {
            this.newRENListPanel = newRENListPanel;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            newRENListPanel.validateInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            newRENListPanel.validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            newRENListPanel.validateInput();
        }
    }

    static class ThreeBtnListener implements ItemListener {

        private JTable table;

        ThreeBtnListener(JTable table) {
            this.table = table;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            TableRowSorter rowSorter = (TableRowSorter) table.getRowSorter();
            RENListTableFilter filter = (RENListTableFilter) rowSorter.getRowFilter();
            filter.setInclude3(e.getStateChange() == ItemEvent.SELECTED);
            rowSorter.sort();
        }
    }
}
