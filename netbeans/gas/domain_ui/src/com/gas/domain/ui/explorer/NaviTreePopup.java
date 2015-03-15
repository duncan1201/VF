/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.domain.ui.banner.IImportActionsFactory;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class NaviTreePopup extends JPopupMenu {

    static IImportActionsFactory factory = Lookup.getDefault().lookup(IImportActionsFactory.class);
    
    NaviTreeActions.NewAction newAction;
    NaviTreeActions.RenameAction renameAction;
    NaviTreeActions.DelAction delAction;
    NaviTreeActions.PasteAction pasteAction;
    NaviTreeActions.RestoreAllAction restoreAllAction;
    NaviTreeActions.EmptyRecycleBinAction emptyAction;
    Action importFromFile ;

    NaviTreePopup() {
        newAction = new NaviTreeActions.NewAction();
        add(newAction);

        renameAction = new NaviTreeActions.RenameAction();
        add(renameAction);

        delAction = new NaviTreeActions.DelAction();
        add(delAction);

        pasteAction = new NaviTreeActions.PasteAction();
        add(pasteAction);       

        restoreAllAction = new NaviTreeActions.RestoreAllAction();
        add(restoreAllAction);

        emptyAction = new NaviTreeActions.EmptyRecycleBinAction();
        add(emptyAction);
        
        importFromFile = factory.createImportFromFileAction();
        add(importFromFile);        
    }    

    public void updateEnablement(boolean myDataRoot, boolean recycleBin) {
        newAction.setEnabled(!recycleBin);
        renameAction.setEnabled(!recycleBin && !myDataRoot);        
        delAction.setEnabled(!recycleBin && !myDataRoot);
        pasteAction.setEnabled(!recycleBin);
        
        importFromFile.setEnabled(!recycleBin);
        
        restoreAllAction.setEnabled(recycleBin);
        emptyAction.setEnabled(recycleBin);
    }

    public void setNaviTreePanel(NaviTreePanel panel) {
        newAction.setNaviTreePanel(panel);
        renameAction.setNaviTreePanel(panel);
        delAction.setTree(panel.getTree());
        emptyAction.setTree(panel.getTree());
    }
}
