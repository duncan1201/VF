/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.misc;

import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.VfParameter;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author dq
 */
public class VfParameterDAO {

    private Configuration configuration;

    public VfParameterDAO(Configuration configuration) {
        this.configuration = configuration;
    }

    public void merge(VfParameter param) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        session.merge(param);

        session.getTransaction().commit();
    }
}
