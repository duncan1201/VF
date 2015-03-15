/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.pubmed.api;

import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.pubmed.PubmedArticle;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author dunqiang
 */
public interface IPubmedDBService {
    Configuration getConfig();
    void setConfig(Configuration config);
    boolean isPresent(String pmid);
    PubmedArticle getFullByPmid(String pmid);
    PubmedArticle getFullByHibernateId(String hId);
    PubmedArticle getByHibernateId(String hId, boolean full);
    PubmedArticle merge(PubmedArticle pubmedArticle);
    void persist(PubmedArticle article);
}
