/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.filesystem.service.api;

import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import java.util.List;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author dunqiang
 */
public interface IFolderService {

   Folder getFolderTree();      
         
   Folder loadWithData(String absolutePath);
   
   Folder loadWithData(Integer hId);
   
   Folder loadWithDataAndChildren(Integer hId);
   
   Folder loadWithDataAndParents(Integer hId);
   
   Folder loadWithDataAndParentAndChildren(Integer hId);
   
   /**
    * Renames the folder and corresponding absolute path used in operations
    */
   void renameFolder(Folder folder, String newName);     
   
   Folder loadWithRecursiveDataAndParentAndChildren(Integer hId);
   
   Folder loadWithChildren(Integer hId);
   
   void createNCBIChildFolderIfNeeded(String folderName);
   
   Folder loadWithParents(Integer hId);
   
   Folder loadWithParentAndChildren(Integer hId);

   void importInitialData();
   
   void create(Folder folder);

   Folder initFolderTreeIfNecessary();

   void merge(Folder folder);
   
   /**
    * Delete the data and will deactivate the corresponding relationship,if any.
    */
   void deleteData(Folder folder, IFolderElement... objs);
      
   /**
    * Delete the folder and its contents. Make the corresponding relationship inactive 
    */
   void delete(Folder folder);

   void setConfig(Configuration config);
 
}
