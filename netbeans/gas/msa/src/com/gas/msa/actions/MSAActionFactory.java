/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.actions;

import com.gas.domain.ui.banner.IMSAActionFactory;
import javax.swing.Action;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMSAActionFactory.class)
public class MSAActionFactory implements IMSAActionFactory{

    @Override
    public Action createMSAAction() {
        Action action = new MSAAction();
        return action;
    }
    
}
