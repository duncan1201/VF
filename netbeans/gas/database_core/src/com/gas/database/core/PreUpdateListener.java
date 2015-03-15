/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core;

import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.primer3.UserInput;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;

/**
 *
 * @author dq
 */
public class PreUpdateListener implements PreUpdateEventListener{

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        Object entity = event.getEntity();
        if(entity instanceof UserInput){
            return onPreUpdate((UserInput)entity);
        }else{
            return false;
        }
    }
    
    private boolean onPreUpdate(){
        return false;
    }
    
    private boolean onPreUpdate(UserInput entity){
        entity.removeSequenceTemplate();        
        return false;
    }
    
}
