/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.tigr.service;

import com.gas.database.core.HibernateUtil;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.database.core.tigr.service.api.IKromatogramService;
import com.gas.domain.core.tigr.Kromatogram;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IKromatogramService.class)
public class KromatogramService implements IKromatogramService {

    IHibernateConfigService service = Lookup.getDefault().lookup(IHibernateConfigService.class);
    private Configuration config;

    public KromatogramService() {
        config = service.getDefaultConfiguration();
    }

    @Override
    public Kromatogram merge(Kromatogram kromatogram) {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        Kromatogram ret = (Kromatogram) session.merge("Kromatogram", kromatogram);

        session.getTransaction().commit();

        return ret;
    }

    @Override
    public Kromatogram getFullByHibernateId(String hibernateId) {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        Kromatogram ret = (Kromatogram) session.createQuery("from Kromatogram t where t.hibernateId = ?").setString(0, hibernateId).uniqueResult();
        ret.touchAll();

        session.getTransaction().commit();
        return ret;
    }

    @Override
    public void persist(Kromatogram kromatogram) {

        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        session.persist("Kromatogram", kromatogram);

        session.getTransaction().commit();
    }
}
