/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.ren;

import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 *
 * @author dunqiang
 */
public class RENListDAO {

    private Configuration configuration;

    public RENListDAO() {
    }

    public RENListDAO(Configuration config) {
        this.configuration = config;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void persist(RENList list) {
        if (list == null) {
            return;
        }
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.persist(list);

        session.getTransaction().commit();
    }

    public RENList merge(RENList renList) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        RENList ret = (RENList)session.merge(renList);

        session.getTransaction().commit();
        
        return ret;
    }

    public RENList getRENList(String name) {
        RENList ret = null;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Object obj = session.createQuery("from RENList as r where r.name = ?").setString(0, name).uniqueResult();
        ret = (RENList) obj;
        session.getTransaction().commit();
        return ret;
    }

    public RENList getByHibernateId(String hId, boolean full) {
        RENList ret = null;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Object obj = session.createQuery("from RENList as r where r.hibernateId = ?").setString(0, hId).uniqueResult();
        ret = (RENList) obj;
        if (full) {
            Iterator<REN> itr = ret.getIterator();
            while (itr.hasNext()) {
                REN ren = itr.next();
                ren.getUpstreamCutPos();
                ren.getDownstreamCutPos();
            }
        }
        session.getTransaction().commit();
        return ret;
    }

    public List<RENList> getRENLists() {
        List<RENList> ret = new ArrayList<RENList>();
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        ret = session.createQuery("from RENList ").list();

        session.getTransaction().commit();
        return ret;
    }

    public List<RENList> getRENLists(boolean deletable) {
        List<RENList> ret = new ArrayList<RENList>();
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        ret = session.createQuery("from RENList as r where r.deletable = ?").setBoolean(0, deletable).list();

        session.getTransaction().commit();
        return ret;
    }

    public List<String> getAllNames() {
        List<String> ret = new ArrayList<String>();
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        ret = session.createQuery("select r.name from RENList as r").list();

        session.getTransaction().commit();
        return ret;
    }
}
