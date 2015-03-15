package com.gas.database.core.pubmed;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.pubmed.PubmedArticle;
import java.util.List;
import org.hibernate.Query;

public class PubmedDAO {

    private Configuration configuration;

    public PubmedDAO(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void persist(PubmedArticle article) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.persist(article);

        session.getTransaction().commit();
    }

    public PubmedArticle getbyPMID(String id) {
        PubmedArticle ret = null;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();
        final Query query = session.createQuery("from PubmedArticle as p where p.pmid = ?").setString(0, id);
        query.setCacheable(true);
        Object obj = query.uniqueResult();
        ret = (PubmedArticle) obj;
        session.getTransaction().commit();
        return ret;
    }

    public PubmedArticle getFullbyPMID(String id) {
        PubmedArticle ret = null;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        List list = session.createQuery("from PubmedArticle as p where p.pmid = ?").setString(0, id).list();
        if (list.size() > 0) {
            ret = (PubmedArticle) list.get(0);
        }
        ret.getAbstractTxt();
        session.getTransaction().commit();

        return ret;
    }

    
    public PubmedArticle getByHibernateId(String hId, boolean full) {
        PubmedArticle ret = null;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Object obj = session.createQuery("from PubmedArticle as p where p.hibernateId = ?").setString(0, hId).uniqueResult();
        ret = (PubmedArticle) obj;
        if(full){
            ret.getAbstractTxt();
        }
        session.getTransaction().commit();

        return ret;
    }

    public boolean isPresent(String pmid) {
        boolean ret = false;
        Long count;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Object obj = session.createQuery("select count(*) from PubmedArticle as p where p.pmid = ?").setString(0, pmid).uniqueResult();
        count = (Long) obj;
        ret = count > 0;
        session.getTransaction().commit();
        return ret;
    }

    public PubmedArticle merge(PubmedArticle article) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        PubmedArticle merged = (PubmedArticle)session.merge(article);

        session.getTransaction().commit();
                 
        return merged;
    }

    public void delete(PubmedArticle article) {
        if (article == null) {
            throw new IllegalArgumentException("article cannot be null");
        }

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();
        session.delete(article);
        session.getTransaction().commit();
    }
}
