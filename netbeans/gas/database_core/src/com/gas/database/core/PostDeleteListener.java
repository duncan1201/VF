/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core;

import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class PostDeleteListener implements PostDeleteEventListener{

    IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    
    
    @Override
    public void onPostDelete(PostDeleteEvent pde) {
    }
    
}
