/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.primer;

import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.primer3.UserInput;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 *
 * @author dq
 */
public class UserInputDAO {

    private Configuration configuration;

    public UserInputDAO(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void create(UserInput userInput) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.persist(userInput);

        session.getTransaction().commit();
    }

    public void merge(List<UserInput> userInputs) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        for (UserInput userInput : userInputs) {
            session.merge(userInput);
        }

        session.getTransaction().commit();
    }

    public UserInput merge(UserInput userInput) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        UserInput ret = (UserInput) session.merge(userInput);

        session.getTransaction().commit();

        return ret;
    }

    public List<UserInput> getAll(boolean full) {        
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from UserInput");

        List<UserInput> ret = query.list();
        if(full){
            for(UserInput ui: ret){
                ui.touchAll();
            }
        }

        session.getTransaction().commit();

        return ret;
    }

    public UserInput getByName(String name) {
        UserInput ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from UserInput as ui where ui.name = ?");
        query.setString(0, name);
        query.list();
        ret = (UserInput) query.uniqueResult();

        session.getTransaction().commit();

        return ret;
    }

    public void setFavorite(UserInput userInput) {

        if(userInput.isFavorite()){
            return;
        }
        
        List<UserInput> favorites = getFavorite();
        for (UserInput favorite : favorites) {
            favorite.setFavorite(false);
        }
        userInput.setFavorite(true);

        List<UserInput> tmp = new ArrayList<UserInput>();
        tmp.addAll(favorites);
        tmp.add(userInput);

        merge(tmp);
    }

    public Long countByName(String name) {
        Long ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("select count(*) from UserInput as ui where ui.name = ?");
        query.setString(0, name);
        ret = (Long) query.uniqueResult();

        session.getTransaction().commit();

        return ret;
    }

    public void delete(UserInput userInput) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.delete(userInput);

        session.getTransaction().commit();
    }

    public List<UserInput> getFavorite() {

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from UserInput as ui where ui.favorite = ?");
        query.setBoolean(0, true);
        List<UserInput> ret = query.list();
        for (UserInput ui : ret) {
            ui.touchAll();
        }

        session.getTransaction().commit();

        return ret;
    }

    public UserInput getFullByHibernateId(Integer hibernateId) {
        UserInput ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from UserInput as ui where ui.hibernateId = ?");
        query.setInteger(0, hibernateId);
        ret = (UserInput) query.uniqueResult();
        ret.touchAll();
        session.getTransaction().commit();

        return ret;
    }

    public UserInput getFullByName(String name) {
        UserInput ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from UserInput as ui where ui.name = ?");
        query.setString(0, name);
        ret = (UserInput) query.uniqueResult();
        ret.touchAll();
        session.getTransaction().commit();

        return ret;
    }
}
