/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.pdb.api;

import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.pdb.PDBDoc;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author dq
 */
public interface IPDBService {

    Configuration getConfig();

    void setConfig(Configuration config);

    PDBDoc getFullByPdbId(String pdbId);
    
    PDBDoc getFullByHibernateId(String hibernateId);
    
    PDBDoc getByHibernateId(String hibernateId, boolean full);
    
    PDBDoc merge(PDBDoc doc);
    
    void persist(PDBDoc doc);
        
}
