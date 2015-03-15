package com.gas.database.core.das;

import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import com.gas.das.core.command.entrypts.EntryPoints;
import com.gas.das.core.command.entrypts.EntryPoints.Segment;
import com.gas.database.core.HibernateUtil;

public class EntryPointsDAO {

    private Configuration config;

    public EntryPointsDAO(Configuration config) {
        this.config = config;
    }

    private void createAndStoreEvent(String href) {

        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        EntryPoints pts = new EntryPoints();
        EntryPoints.Segment seg = new EntryPoints.Segment();
        seg.setType("child");
        pts.getSegments().add(seg);
        pts.setHref(href);
        session.save(seg);
        session.save(pts);
        session.getTransaction().commit();
    }

    private void createAndStoreSegment(String href) {

        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        EntryPoints.Segment pts = new EntryPoints.Segment();
        pts.setType(href);
        session.save(pts);
        session.getTransaction().commit();
    }

    private void list() {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from EntryPoints").list();
        System.out.println(result.size());
        for (EntryPoints event : (List<EntryPoints>) result) {
            System.out.println("EntryPoints (" + event.getHref() + ") : ");
            System.out.println("number of segments=" + event.getSegments().size());
        }
        session.getTransaction().commit();
        //session.close();

    }

    private void listSegment() {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from EntryPoints$Segment").list();
        System.out.println(result.size());
        for (Segment event : (List<EntryPoints.Segment>) result) {
            System.out.println("EntryPoints.Type (" + event.getType() + ") : ");
        }
        session.getTransaction().commit();
        //session.close();

    }
}
