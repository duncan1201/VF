/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor;

import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tigr.TigrProject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Set;

/**

 @author dq
 */
class TigrPtEditorListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            TigrPtEditor editor = (TigrPtEditor) evt.getSource();
            String pName = evt.getPropertyName();
            if (pName.equals("tigrPt")) {
                TigrProject tigrPt = editor.getTigrPt();
                Set<Condig> condigs = tigrPt.getUnmodifiableCondigs();
                editor.getTigrPtPanel().getContigsPanel().setCondigs(new ArrayList(condigs));
                editor.getTigrPtPanel().getSettingsPanel().setTigrSettings(tigrPt.getSettings());
                
                int rowCount = editor.getTigrPtPanel().getContigsPanel().getContigsTable().getRowCount();
                if(rowCount > 0){
                    editor.getTigrPtPanel().getContigsPanel().getContigsTable().getSelectionModel().setSelectionInterval(0, 0);
                }
            }
        }
    }
}
