/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.tigr;

import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.ace.ACE.Contig;
import com.gas.domain.core.ace.ACE.Read;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.Rid;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TIGRSettings;
import com.gas.domain.core.tigr.TigrProject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 *
 * @author dq
 */
public class TigrPtDAO {

    private Configuration config;

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void persist(TigrProject p) {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        session.persist(p);

        session.getTransaction().commit();
    }

    public TigrProject merge(TigrProject tigrProject) {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        TigrProject ret = (TigrProject) session.merge(tigrProject);

        session.getTransaction().commit();

        return ret;
    }

    public List<TigrProject> getAll() {
        List<TigrProject> ret = new ArrayList<TigrProject>();
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        ret.addAll(session.createQuery("from TigrProject ").list());

        session.getTransaction().commit();
        return ret;
    }

    public TigrProject getFullByHibernateId(String hId, boolean full) {

        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        TigrProject ret = (TigrProject) session.createQuery("from TigrProject t where t.hibernateId = ?").setString(0, hId).uniqueResult();
        if (full) {
            // must assign the TIGRSettings to a variable
            TIGRSettings settings = ret.getSettings();
            settings.touchAll();
            Iterator<Condig> itr = ret.getUnmodifiableCondigs().iterator();
            while (itr.hasNext()) {
                Condig contig = itr.next();
                contig.getQualities().iterator();
                Iterator<Rid> rItr = contig.getRids().iterator();
                while (rItr.hasNext()) {
                    Rid rid = rItr.next();
                    Kromatogram k = rid.getKromatogram();
                    k.touchAll();                   
                    rid.getKromatogram().touchAll();
                }
            }

            Iterator<Kromatogram> kItr = ret.getKromatogramItr();
            while (kItr.hasNext()) {
                Kromatogram k = kItr.next();
                k.touchAll();
            }
        }
        session.getTransaction().commit();
        return ret;
    }
}
