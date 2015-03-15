/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.editor;

import com.gas.domain.core.ren.REN;
import java.beans.PropertyChangeSupport;
import javax.swing.RowFilter;

/**
 *
 * @author dq
 */
public class RENListTableFilter extends RowFilter<RENListTableModel, Integer>{

    private boolean includeBlunt = true;
    private boolean include5 = true;
    private boolean include3 = true;
    private boolean palindromesOnly = false;
    
    public RENListTableFilter(){
    }

    @Override
    public boolean include(Entry<? extends RENListTableModel, ? extends Integer> entry) {
        int id = entry.getIdentifier();
        RENListTableModel model = entry.getModel();
        RENListTableModel.Row row = model.getRow(id);
        REN ren = row.getRen();
        int endType = ren.getDownstreamEndType();
        boolean ret = true;
        if(!includeBlunt && endType == REN.BLUNT){
            ret = false;
        }else if(!include5 && endType == REN.OVERHANG_5PRIME){
            ret = false;
        }else if(!include3 && endType == REN.OVERHANG_3PRIME){
            ret =  false;
        }
        
        if(palindromesOnly){
            ret = ret && ren.isPalindromic();
        }
        
        return ret;
    }

    public boolean isInclude3() {
        return include3;
    }

    public void setInclude3(boolean include3) {
        this.include3 = include3;
    }

    public boolean isInclude5() {
        return include5;
    }

    public void setInclude5(boolean include5) {
        this.include5 = include5;
    }

    public boolean isIncludeBlunt() {
        return includeBlunt;
    }

    public void setIncludeBlunt(boolean includeBlunt) {
        this.includeBlunt = includeBlunt;
    }

    public boolean isPalindromesOnly() {
        return palindromesOnly;
    }

    public void setPalindromesOnly(boolean palindromesOnly) {
        this.palindromesOnly = palindromesOnly;
    }
    
}
