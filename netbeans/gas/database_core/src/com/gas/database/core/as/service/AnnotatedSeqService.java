package com.gas.database.core.as.service;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.core.StringList;
import com.gas.database.core.HibernateUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.FolderDAO;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.service.api.IHibernateConfigService;
import java.util.List;

import org.hibernate.cfg.Configuration;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.filesystem.Folder;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IAnnotatedSeqService.class)
public class AnnotatedSeqService implements IAnnotatedSeqService {

    private IHibernateConfigService configService = Lookup.getDefault().lookup(IHibernateConfigService.class);
    private Configuration config;

    public AnnotatedSeqService() {
        config = configService.getDefaultConfiguration();
    }

    public AnnotatedSeqService(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }

    private void validate() {
        if (config == null) {
            throw new IllegalArgumentException("Config cannnot be null");
        }
    }

    @Override
    public void setConfig(Configuration config) {
        this.config = config;
    }

    public List<AnnotatedSeq> getAll() {
        validate();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        List<AnnotatedSeq> all = dao.getAll();
        return all;
    }

    @Override
    public AnnotatedSeq getByAccession(String accession) {
        validate();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        return dao.findByAccession(accession);
    }

    @Override
    public void rename(AnnotatedSeq as, String newName) {
       
        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        Folder folder = folderService.loadWithParents(as.getFolder().getHibernateId());
        
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        session.beginTransaction();
        String folderPath = folder.getAbsolutePath();
        String oldAsPath = folderPath + "\\" + as.getName();
        String newAsPath = folderPath + "\\" + newName;
        dao.merge(as, session, false);
        dao.replaceAsFullPath(oldAsPath, newAsPath, session, true);
    }

    @Override
    public AnnotatedSeq merge(AnnotatedSeq as) {
        validate();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        return dao.merge(as, null, true);
    }

    @Override
    public void move(AnnotatedSeq as, Folder toFolder) {
        Folder fromFolder = as.getFolder();
        
        FolderDAO daoFolder = new FolderDAO(config);
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        fromFolder = daoFolder.getByHibernateId(fromFolder.getHibernateId(), false, false, true, session, false);
                        
        if (fromFolder.isNCBIFolder()) {
            fromFolder.removeObject(as);
        }
        toFolder = daoFolder.getByHibernateId(toFolder.getHibernateId(), false, false, true, session, false);
        final String oldAsPath = String.format("%s\\%s", fromFolder.getAbsolutePath(), as.getName());
        final String newAsPath = String.format("%s\\%s", toFolder.getAbsolutePath(), as.getName());
        if (toFolder.isRecycleBin()) {            
            dao.replaceAsFullPath(oldAsPath, newAsPath, false, session, false);
        } else if (!toFolder.isRecycleBin() && !fromFolder.isRecycleBin() && !fromFolder.isNCBIFolder()) {
            dao.replaceAsFullPath(oldAsPath, newAsPath, true, session, false);
        }
        as.setPrevFolderPath(fromFolder.getAbsolutePath());
        as.setFolder(toFolder);
        dao.merge(as, session, true);
    }

    @Override
    public void mergeAsPref(AnnotatedSeq as) {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        AnnotatedSeq ret = null;
        session.merge(as.getAsPref());
        session.getTransaction().commit();
    }

    @Override
    public AnnotatedSeq getFullByHibernateId(String hId) {
        return getFullByHibernateId(hId, null);
    }

    @Override
    public <T> T getDescendants(AnnotatedSeq as, Class<T> clazz) {        
        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        String folderPath = folderService.loadWithParents(as.getFolder().getHibernateId()).getAbsolutePath();
        String asPath = String.format("%s\\%s", folderPath, as.getName());
        AnnotatedSeqDAO daoAs = new AnnotatedSeqDAO(config);

        return daoAs.getDescendants(asPath, null, clazz);
    }

    @Override
    public AnnotatedSeq getFullByHibernateId(String hId, Folder rootFolder) {
        AnnotatedSeq ret = getByHibernateId(hId, true);
        if (ret.getOperation() != null) {
            buildFamilyTree(ret, rootFolder);
        }
        return ret;
    }

    /**
     * @param rootFolder increase the performance
     */
    void buildFamilyTree(AnnotatedSeq as, Folder rootFolder) {
        if (as == null) {
            return;
        }
        Operation ope = as.getOperation();
        if (ope == null) {
            return;
        }
        Set<Operation.Participant> parts = ope.getParticipants();
        Iterator<Operation.Participant> itr = parts.iterator();
        while (itr.hasNext()) {
            Operation.Participant part = itr.next();
            AnnotatedSeq asPart = getByAbsolutePath(part.getAbsolutePath(), rootFolder);
            if (asPart == null) {
                return;
            }
            Operation ope2 = asPart.getOperation();
            if (ope2 != null) {
                part.setOperation(ope2);
                buildFamilyTree(asPart, rootFolder);
            }
        }
    }

    /**
     * @param absolutePath
     * @return null if not found
     */
    @Override
    public AnnotatedSeq getFullByAbsolutePath(String absolutePath) {
        return get(absolutePath, true, null);
    }

    /**
     * @param absolutePath
     * @return null if not found
     */
    AnnotatedSeq getByAbsolutePath(String absolutePath, Folder rootFolder) {
        return get(absolutePath, false, rootFolder);
    }

    private AnnotatedSeq get(String absolutePath, boolean full, Folder rootFolder) {
        AnnotatedSeq ret = null;
        final int index = absolutePath.lastIndexOf("\\");
        final String folderPath = absolutePath.substring(0, index);
        final String nameMolecule = absolutePath.substring(index + 1);
        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        Folder folder;
        if (rootFolder == null) {
            folder = folderService.loadWithData(folderPath);
        } else {
            folder = rootFolder.getFolder(folderPath);
            System.out.println();
        }
        if (folder != null) {
            ret = folder.getElement(nameMolecule, AnnotatedSeq.class);
            buildFamilyTree(ret, rootFolder);
            if (ret != null && full) {
                ret = getFullByHibernateId(ret.getHibernateId());
            }
        }

        return ret;
    }

    @Override
    public AnnotatedSeq getByHibernateId(String hId, boolean full) {
        validate();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        return dao.findByHibernateId(hId, full);
    }

    @Override
    public void persist(AnnotatedSeq as) {
        validate();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        dao.persist(as);
    }

    /**
     * @return not in ( <id>, <id>, <id>, ...)
     */
    private String createNotInCondition(IntList idList) {
        StringBuilder ret = new StringBuilder();
        Iterator<Integer> itr = idList.iterator();
        if (!idList.isEmpty()) {
            ret.append("not in(");
        }
        while (itr.hasNext()) {
            Integer id = itr.next();
            ret.append(id);
            if (itr.hasNext()) {
                ret.append(',');
            }
        }
        if (!idList.isEmpty()) {
            ret.append(")");
        }
        return ret.toString();
    }

    @Override
    public String save(AnnotatedSeq as) {
        validate();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        return (String) dao.save(as);
    }

    @Override
    public List<AnnotatedSeq> getFull(List<AnnotatedSeq> as) {
        validate();
        AnnotatedSeqDAO dao = new AnnotatedSeqDAO(config);
        return dao.getFull(as);
    }

    @Override
    public List<AnnotatedSeq> getByFetureSource(Integer hId) {
        return null;
    }
}
