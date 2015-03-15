/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core;

import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class PostUpdateListener implements PostUpdateEventListener {

    IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    
    @Override
    public void onPostUpdate(PostUpdateEvent pue) {
    }
    
}
