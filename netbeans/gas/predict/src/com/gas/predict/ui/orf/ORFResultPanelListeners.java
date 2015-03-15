/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.orf.api.ORF;
import com.gas.domain.ui.editor.IMolPane;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dq
 */
class ORFResultPanelListeners {

    static class TableSelListener implements ListSelectionListener {

        WeakReference<JTable> tableRef;
        WeakReference<IMolPane> molPaneRef;

        TableSelListener(JTable table) {
            tableRef = new WeakReference<JTable>(table);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean adjusting = e.getValueIsAdjusting();
            if (adjusting) {
                return;
            }
            DefaultListSelectionModel selectionModel = (DefaultListSelectionModel)e.getSource();
            int index = selectionModel.getLeadSelectionIndex();            
            if(index < 0){
                return;
            }
            int indexModel = tableRef.get().convertRowIndexToModel(index);
            ResultTableModel model = (ResultTableModel) tableRef.get().getModel();
            ORF orf = model.getRow(indexModel);
            IMolPane molPane = getMolPane();
            List data = molPane.getShapeData(FetureKeyCnst.ORF, orf.getStartPos(), orf.getEndPos());
            if(!data.isEmpty()){
                molPane.setSelectedFeture((Feture)data.get(0));
            }
        }

        IMolPane getMolPane() {
            if (molPaneRef == null || molPaneRef.get() == null) {
                IMolPane molPane = UIUtil.getParent(tableRef.get(), IMolPane.class);
                molPaneRef = new WeakReference<IMolPane>(molPane);
            }
            return molPaneRef.get();
        }
    }
}
