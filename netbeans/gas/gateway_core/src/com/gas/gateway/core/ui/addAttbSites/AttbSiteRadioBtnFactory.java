/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.gateway.core.service.api.PrimerAdapter;
import com.gas.gateway.core.ui.api.IAttBSiteUIServices;
import javax.swing.Icon;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class AttbSiteRadioBtnFactory {
    
    static IAttBSiteUIServices services = Lookup.getDefault().lookup(IAttBSiteUIServices.class);
    
    public static AttbSiteRadioBtn create(PrimerAdapter left, PrimerAdapter right, boolean leftDir, boolean rightDir){
        return create(left, right, leftDir, rightDir, false);
    }
    
    public static AttbSiteRadioBtn create(PrimerAdapter left, PrimerAdapter right, boolean leftDir, boolean rightDir,  boolean selected){
        Icon icon = services.createIcon(left.getAttSite(), right.getAttSite(), leftDir, rightDir);
        AttbSiteRadioBtn ret = new AttbSiteRadioBtn(icon, selected);
        ret.setLeft(left);
        ret.setRight(right);
        return ret;
    }
}
