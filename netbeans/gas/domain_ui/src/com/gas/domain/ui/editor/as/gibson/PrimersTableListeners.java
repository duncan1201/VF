/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.OverlapPrimer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dq
 */
class PrimersTableListeners {
    
    static class TblListener implements ListSelectionListener {
        
        private PrimersTable primersTbl;
        
        TblListener(PrimersTable tbl){
            this.primersTbl = tbl;
        }
        
        @Override
        public void valueChanged(ListSelectionEvent e){
            boolean adjusting = e.getValueIsAdjusting();
            if(adjusting){
                return;
            }
            ListSelectionModel mdl = (ListSelectionModel)e.getSource();
            int index = mdl.getLeadSelectionIndex();
            if(index < 0){
                return;
            }      
            GibsonAssemblyPanel panel = UIUtil.getParent(primersTbl, GibsonAssemblyPanel.class);
            if(panel == null){
                return;
            }
                        
            OverlapPrimer primer = this.primersTbl.getSelectedOverlapPrimer();            
            if(primer != null){
                panel.primersPreview.setSelectedPrimer(primer.getName(), primer.isForward());
                panel.primerDetailsPanel.setOverlapPrimer(primer);
                panel.showCenterPanel(GibsonAssemblyPanel.PRIMERS_DETAILS_PANEL);
            }            
        }
    }
}
