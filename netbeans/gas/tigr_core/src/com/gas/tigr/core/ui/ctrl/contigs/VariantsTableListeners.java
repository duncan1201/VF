/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.editor.TigrPtPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author dq
 */
class VariantsTableListeners {

    static class SelectionListener implements ListSelectionListener {

        TigrPtPanel tigrPtPanel;
        VariantsTable variantsTable;

        SelectionListener(VariantsTable variantsTable){
            this.variantsTable = variantsTable;
        }
        
        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean adjusting = e.getValueIsAdjusting();
            if (adjusting) {
                return;
            }
            if (tigrPtPanel == null) {
                tigrPtPanel = UIUtil.getParent(variantsTable, TigrPtPanel.class);
            }
            DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) e.getSource();
            int rowIndex = selectionModel.getLeadSelectionIndex();
            if (rowIndex < 0) {
                return;
            }
            TableRowSorter trs = (TableRowSorter) variantsTable.getRowSorter();
            int modelIndex = trs.convertRowIndexToModel(rowIndex);

            VariantsTableModel.Row row = variantsTable.getModel().getRow(modelIndex);
            Integer pos = null;
            if (row != null) {
                pos = row.getPos();
            }
            if (pos != null) {
                tigrPtPanel.getAssemblyPanel().getAssemblySPane().setSelection(new Loc(pos, pos));
            }

        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        VariantsTable variantsTable;

        public PtyChangeListener(VariantsTable variantsTable) {
            this.variantsTable = variantsTable;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            if (pName.equals("variantMapMdl")) {
                variantsTable.clearSelection();

                Iterator<VariantMapMdl.Variant> itr = variantsTable.getVariantMapMdl().getSortedVariants().iterator();

                variantsTable.getModel().getRows().clear();
                int i = 1;
                while (itr.hasNext()) {
                    VariantMapMdl.Variant variant = itr.next();
                    VariantsTableModel.Row row = new VariantsTableModel.Row();
                    row.setNo(i++);
                    row.setPos(variant.getPos() + variant.getReadStart() - 1);
                    row.setBase(String.format("%s/%s", variant.getFromBase().toString().toUpperCase(), variant.getToBase().toString().toUpperCase()));
                    variantsTable.getModel().getRows().add(row);
                }
                variantsTable.getModel().fireTableDataChanged();
            }
        }
    }
}
