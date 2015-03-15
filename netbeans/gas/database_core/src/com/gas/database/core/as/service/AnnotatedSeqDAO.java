package com.gas.database.core.as.service;

import com.gas.common.ui.core.StringList;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.database.core.HibernateUtil;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.Operation.Participant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Should not be visible outside the package
 *
 */
public class AnnotatedSeqDAO {

    private Configuration configuration;
    private Logger logger = Logger.getLogger(AnnotatedSeqDAO.class.getName());

    public AnnotatedSeqDAO(Configuration configuration) {
        this.configuration = configuration;
    }

    Configuration getConfiguration() {
        return configuration;
    }

    void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * @param _session could be null
     * @return absolute paths of descendants
     */
    <T> T getDescendants(String asPath, Session _session, Class<T> retType) {
        T ret;

        if (!retType.isAssignableFrom(StringList.class) && !retType.isAssignableFrom(AnnotatedSeqList.class)) {
            throw new IllegalArgumentException(String.format("Class %s not supported", retType.getName()));
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
        StringList descendantsPaths = new StringList();
        AnnotatedSeqList asList = new AnnotatedSeqList();
        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.operations.size > 0");
        List<AnnotatedSeq> seqs = query.list();
        for (AnnotatedSeq seq : seqs) {
            Iterator<Operation.Participant> itr = seq.getOperation().getParticipants().iterator();
            while (itr.hasNext()) {
                Participant part = itr.next();
                if (part.getAbsolutePath().equals(asPath)) {
                    descendantsPaths.add(String.format("%s\\%s", seq.getFolder().getAbsolutePath(), seq.getName()));
                    asList.add(seq);
                }
            }
        }

        session.getTransaction().commit();

        if (retType.isAssignableFrom(StringList.class)) {
            ret = (T) descendantsPaths;
        } else {
            ret = (T) asList;
        }
        return ret;
    }

    public int deactiveAsPath(StringList asPaths, Session _session, boolean commit) {
        Session session;
        if (_session == null) {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        } else {
            session = _session;
            if(!session.getTransaction().isActive()){
                session.getTransaction().begin();
            }
        }

        Set<AnnotatedSeq> toBeUpdated = new HashSet<AnnotatedSeq>();
        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.operations.size > 0");
        List<AnnotatedSeq> list = query.list();
        for (AnnotatedSeq seq : list) {
            Iterator<Operation.Participant> itr = seq.getOperation().getParticipants().iterator();
            while (itr.hasNext()) {
                Operation.Participant part = itr.next();
                if (asPaths.contains(part.getAbsolutePath())) {
                    part.setActive(false);
                    toBeUpdated.add(seq);
                }
            }
        }

        for (AnnotatedSeq seq : toBeUpdated) {
            session.merge(seq);
        }
        if (commit) {
            session.getTransaction().commit();
        }
        return toBeUpdated.size();
    }

    void deactiveFolderPath(String folderPath, Session _session) {
        Session session;
        if (_session == null) {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        } else {
            session = _session;
        }

        Set<AnnotatedSeq> toBeUpdated = new HashSet<AnnotatedSeq>();
        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.operations.size > 0");
        List<AnnotatedSeq> list = query.list();
        for (AnnotatedSeq seq : list) {
            Set<Operation.Participant> parts = seq.getOperation().getParticipants();
            Iterator<Operation.Participant> itr = parts.iterator();
            while (itr.hasNext()) {
                Operation.Participant part = itr.next();
                if (part.getAbsolutePath().startsWith(folderPath + "\\")) {
                    part.setActive(false);
                    toBeUpdated.add(seq);
                }
            }
        }

        for (AnnotatedSeq seq : toBeUpdated) {
            session.merge(seq);
        }
        session.getTransaction().commit();
    }

    void replaceAsFullPath(String oldFullPath, String newFullPath, Session _session, boolean commit) {    
        replaceAsFullPath(oldFullPath, newFullPath, null, _session, commit);
    }
    
    /**
     * Replaces the AS full path and make it active
     */
    void replaceAsFullPath(String oldFullPath, String newFullPath, Boolean activate, Session _session, boolean commit) {
        Session session;
        if (_session != null) {
            session = _session;
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
        } else {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        }
        Set<AnnotatedSeq> toBeUpdated = new HashSet<AnnotatedSeq>();

        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.operations.size > 0");
        List<AnnotatedSeq> seqs = query.list();
        for (AnnotatedSeq seq : seqs) {
            Operation operation = seq.getOperation();
            Iterator<Operation.Participant> itr = operation.getParticipants().iterator();
            while (itr.hasNext()) {
                Participant part = itr.next();
                if (part.getAbsolutePath().equals(oldFullPath)) {
                    part.setAbsolutePath(newFullPath);
                    if(activate != null){
                        part.setActive(activate);
                    }
                    toBeUpdated.add(seq);
                }
            }
        }

        for (AnnotatedSeq seq : toBeUpdated) {
            session.merge(seq);
        }

        if (commit) {
            session.getTransaction().commit();
        }
    }

    /**
     * @param oldFolderPath
     * @param _session if null, create a new session and begin a transaction if
     * necessary
     * @param commit whether to commit the transaction or not at the end
     * @return the number of records updated
     */
    public int replaceFolderPath(String oldFolderPath, String newFolderPath, Session _session, boolean commit) {
        Session session;
        if (_session != null) {
            session = _session;
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
        } else {
            session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
            session.beginTransaction();
        }


        Set<AnnotatedSeq> toBeUpdated = new HashSet<AnnotatedSeq>();
        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.operations.size > 0");
        List<AnnotatedSeq> list = query.list();
        for (AnnotatedSeq seq : list) {
            Set<Operation.Participant> parts = seq.getOperation().getParticipants();
            Iterator<Operation.Participant> itr = parts.iterator();
            while (itr.hasNext()) {
                Operation.Participant part = itr.next();
                if (part.getAbsolutePath().startsWith(oldFolderPath + "\\")) {
                    String newAbsolutePath = part.getAbsolutePath().replace(oldFolderPath, newFolderPath);
                    part.setAbsolutePath(newAbsolutePath);
                    toBeUpdated.add(seq);
                }
            }
        }

        for (AnnotatedSeq seq : toBeUpdated) {
            session.merge(seq);
        }
        if (commit) {
            session.getTransaction().commit();
        }
        return toBeUpdated.size();
    }

    void persist(AnnotatedSeq as) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannnot be null");
        }
        if (as.getName() == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.persist(as);

        session.getTransaction().commit();
    }

    Serializable save(AnnotatedSeq as) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannnot be null");
        }
        if (as.getName() == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        Serializable ret = session.save(as);

        session.getTransaction().commit();
        return ret;
    }

    void delete(AnnotatedSeq as) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        session.delete(as);

        session.getTransaction().commit();
    }

    AnnotatedSeq findByName(String name) {

        List<AnnotatedSeq> seqs = findAllByName(name);

        if (seqs.size() > 0) {
            return seqs.get(0);
        } else {
            return null;
        }
    }

    AnnotatedSeq merge(AnnotatedSeq seq, Session _session, boolean commit) {
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


        AnnotatedSeq ret = null;
        ret = (AnnotatedSeq) session.merge(seq);
        if (commit) {
            session.getTransaction().commit();
        }
        return ret;
    }

    List<AnnotatedSeq> findAllByName(String name) {
        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        session.beginTransaction();

        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.name = ?");
        query.setString(0, name);

        ret = query.list();

        session.getTransaction().commit();

        return ret;
    }

    AnnotatedSeq findByAccession(String accession) {
        AnnotatedSeq ret = null;
        //FlexRichSequence rs = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.accession = ?");
        query.setString(0, accession);

        List list = query.list();

        if (list.size() > 0) {
            ret = (AnnotatedSeq) list.get(0);
        }
        session.getTransaction().commit();

        session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();
        session.beginTransaction();

        session.getTransaction().commit();

        return ret;
    }

    AnnotatedSeq findByHibernateId(String hId) {
        return findByHibernateId(hId, true);
    }

    List<AnnotatedSeq> getFull(List<AnnotatedSeq> as) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq _as : as) {
            AnnotatedSeq full = findByHibernateId(_as.getHibernateId());
            ret.add(full);
        }
        return ret;
    }

    AnnotatedSeq findByHibernateId(String hId, boolean full) {
        AnnotatedSeq ret = null;

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from AnnotatedSeq as aseq where aseq.hibernateId = ?");
        query.setString(0, hId);

        Object obj = query.uniqueResult();

        ret = (AnnotatedSeq) obj;
        if (full) {
            AsHelper.touchAll(ret);
        }
        session.getTransaction().commit();

        return ret;
    }

    List<AnnotatedSeq> getAll() {

        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();

        Session session = HibernateUtil.getSessionFactory(configuration).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery("from AnnotatedSeq");

        ret = query.list();

        session.getTransaction().commit();

        return ret;
    }
}
