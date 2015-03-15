/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.pubmed;

import com.gas.database.core.pubmed.api.IPubmedDBService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.pubmed.PubmedArticle;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dunqiang
 */
@ServiceProvider(service = IPubmedDBService.class)
public class PubmedDBService implements IPubmedDBService {
    
    private Configuration config;
    IHibernateConfigService hs = Lookup.getDefault().lookup(IHibernateConfigService.class);
    
    public PubmedDBService() {
        config = hs.getDefaultConfiguration();
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
    public boolean isPresent(String pmid) {
        PubmedDAO dao = new PubmedDAO(config);
        return dao.isPresent(pmid);
    }
    
    @Override
    public PubmedArticle getFullByPmid(String pmid) {
        PubmedDAO dao = new PubmedDAO(config);
        return dao.getFullbyPMID(pmid);
    }
    
    @Override
    public PubmedArticle getFullByHibernateId(String hId) {
        return getByHibernateId(hId, true);
    }
    
    @Override
    public PubmedArticle getByHibernateId(String hId, boolean full) {
        PubmedDAO dao = new PubmedDAO(config);
        PubmedArticle ret = dao.getByHibernateId(hId, full);
        return ret;
    }
    
    @Override
    public PubmedArticle merge(PubmedArticle pubmedArticle) {
        PubmedDAO dao = new PubmedDAO(config);
        PubmedArticle ret = dao.merge(pubmedArticle);
        return ret;
    }
    
    @Override
    public void persist(PubmedArticle article) {
        PubmedDAO dao = new PubmedDAO(config);
        dao.persist(article);
    }
}
