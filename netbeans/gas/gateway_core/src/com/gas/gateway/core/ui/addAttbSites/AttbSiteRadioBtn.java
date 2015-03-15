/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.radiobtn.RadioBtn;
import com.gas.gateway.core.service.api.PrimerAdapter;
import com.gas.gateway.core.ui.api.IAttBSiteUIServices;
import javax.swing.Icon;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class AttbSiteRadioBtn extends RadioBtn {

    private IAttBSiteUIServices service = Lookup.getDefault().lookup(IAttBSiteUIServices.class);
    private PrimerAdapter left;
    private PrimerAdapter right;

    public AttbSiteRadioBtn(Icon icon, boolean selected){
        super(icon, selected);
    }
      
    public PrimerAdapter getLeft() {
        return left;
    }

    public PrimerAdapter getRight() {
        return right;
    }

    public void setLeft(PrimerAdapter left) {
        this.left = left;
    }

    public void setRight(PrimerAdapter right) {
        this.right = right;
    }    
}
