/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.TasmUtil;
import com.gas.tigr.core.ui.ctrl.contigs.ContigsTable;
import com.gas.tigr.core.ui.editor.TigrPtPanel;
import com.gas.tigr.core.ui.editor.shared.AssemblyScroll;
import com.gas.tigr.core.ui.editor.shared.MiniMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dq
 */
class CtrlPanelListeners {

    static class ContigsTblListener implements ListSelectionListener {

        TigrPtPanel tigrPtPanel;
        CtrlPanel ctrlPanel;
        MiniMap miniMap;
        AssemblyScroll assemblySPane;
        ContigsTable tbl;

        ContigsTblListener(ContigsTable tbl) {
            this.tbl = tbl;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean adjusting = e.getValueIsAdjusting();
            if (!adjusting) {
                DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) e.getSource();
                int leadSelectionIndex = selectionModel.getLeadSelectionIndex();
                int indexModel = tbl.convertRowIndexToModel(leadSelectionIndex);
                Object obj = this.tbl.getModel().getRow(indexModel);
                if (obj == null) {
                    return;
                }
                initIfNecessary();
                // need to clear selection first
                ctrlPanel.getContigsPanel().getVariantsPanel().getVariantsTable().clearSelection();
                // then set the scroll bar value
                assemblySPane.getHorizontalScrollBar().setValue(0);
                tigrPtPanel.getAssemblyPanel().setCondig((Condig) obj);

                VariantMapMdl variantMapMdl = TasmUtil.toVariantMapMdl((Condig) obj);
                miniMap.setVariantMapMdl(variantMapMdl);
                tigrPtPanel.getContigsPanel().setVariantMapMdl(variantMapMdl);

                assemblySPane.setVariantMapMdl(variantMapMdl);
            }
        }

        private void initIfNecessary() {
            if (tigrPtPanel == null) {
                tigrPtPanel = UIUtil.getParent(this.tbl, TigrPtPanel.class);
            }
            if (miniMap == null) {
                miniMap = UIUtil.getChild(tigrPtPanel, MiniMap.class);
            }
            if (assemblySPane == null) {
                assemblySPane = UIUtil.getChild(tigrPtPanel, AssemblyScroll.class);
            }
            if (ctrlPanel == null) {
                ctrlPanel = tigrPtPanel.getCtrlPanel();
            }

        }
    }
}
