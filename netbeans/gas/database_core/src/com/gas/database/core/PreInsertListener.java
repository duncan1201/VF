/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core;

import com.gas.domain.core.primer3.UserInput;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;

/**
 *
 * @author dq
 */
public class PreInsertListener  implements PreInsertEventListener{

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        Object entity = event.getEntity();
        if(!(entity instanceof UserInput)){
            return false;
        }
        UserInput userInput = (UserInput)entity;
        userInput.removeSequenceTemplate();
        return false;        
    }
    
}
