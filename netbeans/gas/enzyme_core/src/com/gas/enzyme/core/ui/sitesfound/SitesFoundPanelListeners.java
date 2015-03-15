/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.sitesfound;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.ISequenceUI;
import com.gas.domain.ui.editor.as.IASEditor;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dq
 */
class SitesFoundPanelListeners {

    static class SelectionListener implements ListSelectionListener {

        private IMolPane molPane;
        private JTable table;

        SelectionListener(JTable table) {
            this.table = table;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (molPane == null) {
                molPane = UIUtil.getParent(table, IMolPane.class);
            }
            boolean adjusting = e.getValueIsAdjusting();
            if (adjusting) {
                return;
            }
            SitesFoundTableModel model = (SitesFoundTableModel) table.getModel();
            int[] rows = table.getSelectedRows();           
            if (rows.length > 0) {
                int[] modelIndexes = UIUtil.convertRowIndexToModel(table, rows);
                SitesFoundTableModel.RowList data = model.getRows(modelIndexes);
                if (data != null) {
                    Loc loc = data.get(0).getPosition();
                    molPane.setSelectedSite(data.getEntries());
                    molPane.setSelection(loc);
                    molPane.center(loc);
                }
            }
        }
    }
}
