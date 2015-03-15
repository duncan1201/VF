/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core;

import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import org.hibernate.HibernateException;
import org.hibernate.event.FlushEntityEvent;
import org.hibernate.event.FlushEntityEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class PostInsertListener implements PostInsertEventListener {
    
    @Override
    public void onPostInsert(PostInsertEvent pie) {
    }

}
