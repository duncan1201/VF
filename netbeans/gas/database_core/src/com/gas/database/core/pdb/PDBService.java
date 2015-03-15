/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.pdb;

import com.gas.database.core.pdb.api.IPDBService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.pdb.PDBDoc;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IPDBService.class)
public class PDBService implements IPDBService {

    private Configuration config;
    private final static IHibernateConfigService hibernateConfigService = Lookup.getDefault().lookup(IHibernateConfigService.class);

    public PDBService() {
        config = hibernateConfigService.getDefaultConfiguration();
    }

    @Override
    public Configuration getConfig() {
        return config;
    }

    @Override
    public void setConfig(Configuration config) {
        this.config = config;
    }

    @Override
    public PDBDoc getFullByPdbId(String pdbId) {
        PDBDoc ret = null;
        PDBDAO dao = new PDBDAO();
        dao.setConfiguration(config);
        ret = dao.getFullByPdbId(pdbId);
        return ret;
    }

    @Override
    public PDBDoc getFullByHibernateId(String hibernateId) {
        return getByHibernateId(hibernateId, true);
    }

    @Override
    public PDBDoc getByHibernateId(String hibernateId, boolean full) {
        PDBDoc ret = null;
        PDBDAO dao = new PDBDAO();
        dao.setConfiguration(config);
        ret = dao.getFullByHibernateId(hibernateId, full);
        return ret;
    }

    @Override
    public PDBDoc merge(PDBDoc doc) {
        PDBDoc ret = null;
        PDBDAO dao = new PDBDAO();
        dao.setConfiguration(config);
        ret = dao.merge(doc);
        return ret;
    }

    @Override
    public void persist(PDBDoc doc) {
        PDBDAO dao = new PDBDAO();
        dao.setConfiguration(config);
        dao.persist(doc);
    }
}
