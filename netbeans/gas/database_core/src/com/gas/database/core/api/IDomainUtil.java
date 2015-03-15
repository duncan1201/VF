/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.api;

import com.gas.common.ui.core.StringList;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.filesystem.Folder;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IDomainUtil {

    Object reloadFullyFromDB(IFolderElement o);

    Object reloadFromDB(IFolderElement o, boolean full);

    StringList getNames(List<IFolderElement> objs);

    void closeEditors(List<IFolderElement> objs);

    void closeEditor(IFolderElement obj);

    /**
     * Update the corresponding relationship. If the toFolder is the recycle bin,
     * deactivate the related relationship. If move from the recycle bin,
     * @param toFolder
     */
    void move(IFolderElement o, Folder toFolder);

    /**
     * If the destination folder is recycle bin, warn the user that the relationship will be irreversibly destroyed
     * If duplicate and with descendants, warn the user
     * @param ret the folder elements need to be check
     * @param toFolder 
     */
    IFolderElementList checkOverwrites(IFolderElementList ret, Folder toFolder, boolean skipIfSameFolder);

    void checkDesendants(IFolderElementList fes, String title);
    
    void checkDesendants(IFolderElementList fes);
    
    /**
     * overwrite if any duplicates
     */
    void copy(IFolderElement o, Folder toFolder);

    void merge(Object o);

    void persist(Collection<IFolderElement> co);

    void persist(IFolderElement o);

    void udpateCurrentFolders();
    
    void openEditor(Object obj);
}
