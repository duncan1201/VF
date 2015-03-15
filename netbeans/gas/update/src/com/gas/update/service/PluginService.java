/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update.service;

import com.gas.update.service.api.IPluginService;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IPluginService.class)
public class PluginService implements IPluginService{

    JSONObject aaa;
    private UpdateUnitProvider getUpdateUnitProvider() {
        UpdateUnitProvider ret = null;
        List<UpdateUnitProvider> providers = UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true);
        if (providers.isEmpty()) {
            return ret;
        }
        ret = providers.get(0);
        return ret;
    }
    
    public List<UpdateElement> aaa(){
        List<UpdateElement> ret = new ArrayList<UpdateElement>();
        UpdateUnitProvider provider = getUpdateUnitProvider();
        List<UpdateUnit> units = provider.getUpdateUnits();
        for(UpdateUnit unit: units){
            UpdateElement updateElement = unit.getInstalled();
            if(updateElement == null){
                
            }
        }
        return ret;
    }
}
