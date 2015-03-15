/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import javax.swing.JPopupMenu;

/**
 *
 * @author dq
 */
class CornerUIPopup extends JPopupMenu {
    
    MSAActions.ExtractRegionAction extractAction;
    
    CornerUIPopup(){
        extractAction = new MSAActions.ExtractRegionAction();
        add(extractAction);
    }
    
    
}
