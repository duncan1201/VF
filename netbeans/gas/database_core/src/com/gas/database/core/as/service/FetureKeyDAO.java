/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.as.service;

import com.gas.common.ui.core.StringComparator;
import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.as.FetureKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 *
 * @author dq
 */
public class FetureKeyDAO {

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    private Configuration configuration;

    public void create(FetureKey fk) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannnot be null");
        }
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.save(fk);
        session.getTransaction().commit();

    }

    public long getCount(String name) {
        Long ret;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("select count(*) from FetureKey where name = ?").setString(0, name);

        ret = (Long) query.uniqueResult();

        session.getTransaction().commit();
        return ret;
    }
    
    public List<String> getAllNamesWithQualifiers(){
        List<String> ret = new ArrayList<String>();
        List<FetureKey> fks = getAllWithQualifiers();
        for(FetureKey fk: fks){
            ret.add(fk.getName());
        }
        return ret;
    }
    
    public List<FetureKey> getAllWithQualifiers(){
        List<FetureKey> ret = new ArrayList<FetureKey>();

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from FetureKey");

        List<FetureKey> all = query.list();
        for(FetureKey fk: all){
            if(!fk.getQualifiers().isEmpty()){
                ret.add(fk);
            }
        }
        session.getTransaction().commit();

        Collections.sort(ret, new FetureKey.NameComparator());
        return ret;
    }

    public List<FetureKey> getAll() {

        List<FetureKey> ret = new ArrayList<FetureKey>();

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from FetureKey");

        ret = query.list();

        session.getTransaction().commit();

        Collections.sort(ret, new FetureKey.NameComparator());

        return ret;
    }

    public List<String> getAllNames() {

        List<String> ret = new ArrayList<String>();

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("select fk.name from FetureKey fk");

        ret = query.list();

        session.getTransaction().commit();

        Collections.sort(ret, new StringComparator());

        return ret;
    }

    public FetureKey getFullByName(String name) {
        FetureKey ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from FetureKey where name = ?").setString(0, name);

        ret = (FetureKey) query.uniqueResult();

        Iterator<String> itr = ret.getQualifiers().iterator();
        while (itr.hasNext()) {
            itr.next();
        }


        session.getTransaction().commit();


        return ret;
    }

    public List<String> getAllQualifiers() {

        List<String> ret = new ArrayList<String>();
        Set<String> retSet = new HashSet<String>();
        List<FetureKey> fkList = new ArrayList<FetureKey>();

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from FetureKey");

        fkList = query.list();
        for (FetureKey fk : fkList) {
            retSet.addAll(fk.getQualifiers());
        }

        session.getTransaction().commit();

        ret.addAll(retSet);
        Collections.sort(ret, new StringComparator());

        return ret;
    }
}
