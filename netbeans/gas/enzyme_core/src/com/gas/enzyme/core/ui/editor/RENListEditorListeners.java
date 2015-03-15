/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.editor;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dq
 */
class RENListEditorListeners {
    
    static class TableSelectListener implements ListSelectionListener{
        
        private RENListEditor editor;
        
        TableSelectListener(RENListEditor editor){
            this.editor = editor;
        }
        
        @Override
        public void valueChanged(ListSelectionEvent e) {            
            boolean adjusting = e.getValueIsAdjusting();
            ListSelectionModel src = (ListSelectionModel)e.getSource();
            if(adjusting){
                return;
            }            
            this.editor.controlPanel.removeBtn.setEnabled(src.getMinSelectionIndex() > -1);
        }
    }
}
