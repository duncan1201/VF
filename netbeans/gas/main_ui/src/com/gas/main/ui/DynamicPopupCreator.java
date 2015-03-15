/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.domain.ui.dynamicTable.DynamicTable;
import com.gas.domain.ui.dynamicTable.IDynamicPopup;
import com.gas.domain.ui.dynamicTable.IDynamicPopupCreator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IDynamicPopupCreator.class)
public class DynamicPopupCreator implements IDynamicPopupCreator{

    @Override
    public IDynamicPopup create(DynamicTable table) {
        IDynamicPopup ret = new DynamicPopup(table);
        return ret;
    }
    
}
