/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.tigr.service;

import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.database.core.tigr.TigrPtDAO;
import com.gas.database.core.tigr.service.api.ITigrPtService;
import com.gas.domain.core.tigr.TigrProject;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=ITigrPtService.class)
public class TigrPtService  implements ITigrPtService{
    
    IHibernateConfigService cService = Lookup.getDefault().lookup(IHibernateConfigService.class);
    private Configuration config;
    
    public TigrPtService(){
        config = cService.getDefaultConfiguration();
    }
    
    @Override
    public void persist(TigrProject project){
        TigrPtDAO dao = new TigrPtDAO();
        dao.setConfig(config);
        dao.persist(project);
    }
    
    @Override
    public List<TigrProject> getAll(){
        TigrPtDAO dao = new TigrPtDAO();
        dao.setConfig(config);
        return dao.getAll();
    }
    
    @Override
    public TigrProject getFullByHibernateId(String hId){
        return getByHibernateId(hId, true);
    }
    
    @Override
    public TigrProject getByHibernateId(String hId, boolean full){
        TigrPtDAO dao = new TigrPtDAO();
        dao.setConfig(config);
        
        return dao.getFullByHibernateId(hId, full);
    }

    @Override
    public TigrProject merge(TigrProject p) {
        TigrPtDAO dao = new TigrPtDAO();
        dao.setConfig(config);
        
        return dao.merge(p);
    }
}
