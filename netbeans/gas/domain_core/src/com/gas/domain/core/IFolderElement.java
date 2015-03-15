/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core;

import com.gas.domain.core.filesystem.Folder;
import java.util.Date;

/**
 *
 * @author dq
 */
public interface IFolderElement {

    public Folder getFolder();

    public void setFolder(Folder folder);

    String getPrevFolderPath();

    void setPrevFolderPath(String p);

    String getHibernateId();

    boolean isRead();

    void setRead(boolean read);

    public String getName();

    public void setName(String name);
    
    public void setDesc(String desc);
    
    public String getDesc();
    
    void setLastModifiedDate(Date lastModifiedDate);
    Date getLastModifiedDate();
}
