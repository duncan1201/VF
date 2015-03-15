/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.tigr.service.api;

import com.gas.domain.core.tigr.TigrProject;
import java.util.List;

/**
 *
 * @author dq
 */
public interface ITigrPtService {
    
    void persist(TigrProject project);    

    List<TigrProject> getAll();

    TigrProject getFullByHibernateId(String hId);
    
    TigrProject getByHibernateId(String hId, boolean full);
    
    TigrProject merge(TigrProject p);
}
