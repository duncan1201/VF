/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.misc;

import com.gas.database.core.misc.api.VF_PARAMS;
import com.gas.database.core.misc.api.IVfParamService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.VfParameter;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IVfParamService.class)
public class VfParamService implements IVfParamService {

    private IHibernateConfigService configService = Lookup.getDefault().lookup(IHibernateConfigService.class);
    private Configuration config;
    

    public VfParamService() {
        config = configService.getDefaultConfiguration();
    }
    
    
    @Override
    public void merge(String name, String value){
        VfParameterDAO dao = new VfParameterDAO(config);
        VfParameter p = new VfParameter();
        p.setName(name);
        p.setValue(value);
        dao.merge(p);
    }
}
