/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.pdb;

import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.pdb.PDBDoc;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 *
 * @author dq
 */
public class PDBDAO {

    private Configuration configuration;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void persist(PDBDoc doc) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.persist(doc);

        session.getTransaction().commit();
    }

    public PDBDoc merge(PDBDoc doc) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        PDBDoc ret = (PDBDoc)session.merge(doc);

        session.getTransaction().commit();        
        
        return ret;
    }

    public PDBDoc getFullByPdbId(String pdbId) {
        PDBDoc ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        String q = "select doc from PDBDoc as doc inner join doc.header where doc.header.idCode = ? and doc.modified = ?";
        List list = session.createQuery(q).setString(0, pdbId).setBoolean(1, false).list();
        if(list.size() > 0){
            ret = (PDBDoc) list.get(0);
        }
        if (ret != null) {
            ret.touchAll();
        }
        session.getTransaction().commit();

        return ret;
    }
    
    public PDBDoc getFullByHibernateId(String hibernateId, boolean full) {
        PDBDoc ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Object obj = session.createQuery("select doc from PDBDoc as doc where doc.hibernateId = ? ").setString(0, hibernateId).uniqueResult();
        ret = (PDBDoc) obj;
        if (ret != null && full) {
            ret.touchAll();
        }
        session.getTransaction().commit();

        return ret;
    }    
}
