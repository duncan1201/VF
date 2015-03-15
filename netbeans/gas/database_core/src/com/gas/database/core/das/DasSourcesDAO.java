package com.gas.database.core.das;

import java.util.List;
import java.util.Set;

import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import com.gas.das.core.command.registry.DasSources;
import com.gas.database.core.HibernateUtil;

public class DasSourcesDAO {

    private Configuration config;

    public DasSourcesDAO(Configuration config) {
        this.config = config;
    }

    private void createAndStoreDasSource(DasSources sources) {

        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        Set<DasSources.Source> srcs = sources.getSources();
        for (DasSources.Source src : srcs) {
            session.save(src);
        }

        session.save(sources);
        session.getTransaction().commit();
    }

    private void list() {

        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from DasSources").list();
        System.out.println(result.size());
        for (DasSources event : (List<DasSources>) result) {
            System.out.println("DasSources.sources.size (" + event.getSources().size() + ") : ");
        }
        session.getTransaction().commit();
        //session.close();

    }
}
