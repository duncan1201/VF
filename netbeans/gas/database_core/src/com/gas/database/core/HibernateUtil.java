package com.gas.database.core;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.gas.database.core.service.api.IHibernateConfigService;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEventListener;
import org.openide.util.Lookup;

public class HibernateUtil {

    public static SessionFactory getSessionFactory(Configuration config) {
        try {
            return getSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    
    
    private static final SessionFactory sessionFactory = buildSessionFactory();
 

    private static SessionFactory buildSessionFactory() {
        try {
            IHibernateConfigService configService = Lookup.getDefault().lookup(IHibernateConfigService.class);
            Configuration config = configService.getDefaultConfiguration();

            PreInsertEventListener[] preInsertEventListeners = {new PreInsertListener()};
            config.getEventListeners().setPreInsertEventListeners(preInsertEventListeners);
            
            PreUpdateEventListener[] preUpdateEventListeners = {new PreUpdateListener()};                        
            config.getEventListeners().setPreUpdateEventListeners(preUpdateEventListeners);
            
            PostDeleteEventListener[] postDeleteEventListeners = {new PostDeleteListener()};
            config.getEventListeners().setPostDeleteEventListeners(postDeleteEventListeners);
            
            PostInsertEventListener[] postInsertEventListeners = {new PostInsertListener()};
            config.getEventListeners().setPostCommitInsertEventListeners(postInsertEventListeners);
            
            PostUpdateEventListener[] postUpdateEventListeners = {new PostUpdateListener()};
            config.getEventListeners().setPostCommitUpdateEventListeners(postUpdateEventListeners);
                                    
            return config.buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }    
}
