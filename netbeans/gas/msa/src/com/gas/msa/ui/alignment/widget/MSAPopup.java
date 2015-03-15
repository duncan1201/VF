/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc2D;
import com.gas.domain.ui.editor.IExportActionFactory;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class MSAPopup extends JPopupMenu {
    
    MSAActions.EditNameAction editNameAction;
    MSAActions.ExtractRegionAction extractRegionAction;
    static IExportActionFactory factory = Lookup.getDefault().lookup(IExportActionFactory.class);
    
    MSAPopup() {
        Action exportAction = factory.create("Export...");
        editNameAction = new MSAActions.EditNameAction();
        extractRegionAction = new MSAActions.ExtractRegionAction();
        
        add(exportAction);
        add(editNameAction);
        add(extractRegionAction);
    }
    
    void setRowName(String rowName) {
        editNameAction.setInitTextInput(rowName);
        extractRegionAction.setInitName(rowName);
    }
    
    void setRowNames(StringList rowNames) {
        editNameAction.setExistingNames(rowNames);
    }

    /**
     * @param loc2d: 1-based, starts from the concensus
     */
    void setSelection(Loc2D loc2d) {
        extractRegionAction.setSelection(loc2d);
    }
    
    void setComp(JComponent comp) {
        editNameAction.setComp(comp);
        extractRegionAction.setComp(comp);
    }
}
