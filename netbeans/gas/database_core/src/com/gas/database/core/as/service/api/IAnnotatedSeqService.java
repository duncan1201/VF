/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.as.service.api;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import java.util.List;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author dunqiang
 */
public interface IAnnotatedSeqService {

    AnnotatedSeq getByAccession(String accession);

    AnnotatedSeq getFullByHibernateId(String hId);

    /**
     * @param rootFolder: the root folder that will increase the performance
     */
    AnnotatedSeq getFullByHibernateId(String hId, Folder rootFolder);

    /**
     *@param clazz: StringList.class or AnnotatedSeqList.class
     */
    <T> T getDescendants(AnnotatedSeq as, Class<T> clazz);

    AnnotatedSeq getFullByAbsolutePath(String absolutePath);

    List<AnnotatedSeq> getFull(List<AnnotatedSeq> as);

    List<AnnotatedSeq> getByFetureSource(Integer hId);

    AnnotatedSeq getByHibernateId(String Id, boolean full);

    void setConfig(Configuration config);

    /**
     * Rename the sequence and update the corresponding operations
     */
    void rename(AnnotatedSeq as, String newName);

    /**
     * <ol>
     *<li> if the destination folder is the recycle bin, deactivate the operations
     *<li> if the destination folder and the source folder is NOT the recycle bin, activate the operations     
     * </ol>
     */
    void move(AnnotatedSeq as, Folder toFolder);
    
    AnnotatedSeq merge(AnnotatedSeq as);

    void mergeAsPref(AnnotatedSeq as);

    void persist(AnnotatedSeq as);

    String save(AnnotatedSeq as);
}
