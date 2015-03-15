package com.gas.database.core.filesystem;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.gas.domain.core.filesystem.Folder;
import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.FolderNames;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;

public class FolderDAO {

    private Configuration configuration;

    public FolderDAO(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private Session persist(Folder folder, Session session) {
        if (session == null) {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        }
        Iterator<Folder> itr = folder.getChildrenItr();
        while (itr.hasNext()) {
            Folder child = itr.next();
            persist(child, session);
        }
        session.persist(folder);

        return session;
    }

    public void persist(Folder folder) {
        Session session = persist(folder, null);
        session.getTransaction().commit();;
    }

    public Folder getFolderTree() {
        return getFolderTree(true);
    }

    /**
     * @param absolutePath
     * @return null if the folder does not exist
     */
    public Folder getFolder(String absolutePath, boolean touchData) {
        Folder ret;
        //absolutePath
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Folder root = (Folder) session.createQuery("from Folder as f where f.root = true AND f.name = '" + FolderNames.ROOT + "'").uniqueResult();
        ret = root.getFolder(absolutePath);
        if (touchData) {
            touchData(ret);
        }
        session.getTransaction().commit();
        return ret;
    }

    public Folder getFolderTree(boolean touchData) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("from Folder as f where f.root = true AND f.name = '" + FolderNames.ROOT + "'");          
        Folder ret = (Folder)query.uniqueResult();
        if (ret != null && touchData) {
            touchData(ret);
        }
        session.getTransaction().commit();

        return ret;
    }

    private void touchData(Folder folder) {
        if (folder == null) {
            return;
        }
        Iterator<Folder> itr = folder.getChildrenItr();
        while (itr.hasNext()) {
            touchData(itr.next());
        }
        folder.touchData();
    }

    public Folder getByHibernateId(Integer id, boolean touchData, boolean touchRecursively, boolean touchParents) {
        return getByHibernateId(id, touchData, touchRecursively, touchParents, null, true);
    }

    public Folder getByHibernateId(Integer id, boolean touchData, boolean touchRecursively, boolean touchParents, Session _session, boolean commit) {
        Folder ret = null;
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();
        if (_session == null) {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        } else {
            session = _session;
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
        }

        Object obj = session.createQuery("from Folder as f where f.hibernateId = ?").setInteger(0, id).uniqueResult();
        if(obj == null){
            return null;
        }
        ret = (Folder) obj;
        ret.getAbsolutePath();
        if (touchData) {
            if (touchRecursively) {
                touchData(ret);
            } else {
                ret.touchData();                
            }
        }
        if (touchParents) {
            ret.getAbsolutePath();
        }
        if (commit) {
            session.getTransaction().commit();
        }
        return ret;
    }

    public void merge(Folder folder){
        merge(folder, false);
    }
    
    public void merge(Folder folder, boolean ncbiFolderAllowed) {
        merge(folder, null, true, ncbiFolderAllowed);
    }

    public Session merge(Folder folder, Session _session, boolean commit) {
        return merge(folder, _session, commit, false);
    }
    
    public Session merge(Folder folder, Session _session, boolean commit, boolean ncbiFolderAllowed) {
        Session session = mergeDown(folder, _session, ncbiFolderAllowed);
        if (commit) {
            session.getTransaction().commit();            
        }
        return session;
    }

    /**
     * will NOT commit
     *
     * @param session
     */
    private Session mergeDown(Folder folder, Session session, boolean ncbiFolderAllowed) {
        if (session == null) {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        } else {
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
        }

        Iterator<Folder> itr = folder.getChildrenItr();
        while (itr.hasNext()) {
            Folder child = itr.next();
            mergeDown(child, session, ncbiFolderAllowed);
        }
        if (folder.getHibernateId() == null) {
            session.persist(folder);
        } else {
            if(folder.isNCBIFolder()){
                if(ncbiFolderAllowed){
                    session.merge(folder);
                }
            }else{
                session.merge(folder);
            }
        }
        return session;
    }    

    public void deleteElements(Folder folder, Session _session, boolean commit, IFolderElement... fes) {
        Session session;
        if (_session == null) {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        } else {
            session = _session;
            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }
        }

        for (IFolderElement fe : fes) {
            folder.removeObject(fe);
        }
        mergeDown(folder, session, false);
        if (commit) {
            session.getTransaction().commit();
        }
    }

    public void delete(Folder folder, Session _session, boolean commit) {
        if (folder == null) {
            throw new IllegalArgumentException("Folder cannot be null");
        }

        Session session;
        if (_session == null) {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        } else {
            session = _session;
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
        }

        session.delete(folder);
        if (commit) {
            session.getTransaction().commit();
        }

    }
}
