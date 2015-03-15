/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.actions;

import com.gas.topo.core.blunt.BluntTOPOCloningAction;
import com.gas.domain.ui.banner.ITOPOCloningActionFactory;
import javax.swing.Action;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider( service = ITOPOCloningActionFactory.class)
public class TOPOCloningActionFactory implements ITOPOCloningActionFactory {

    @Override
    public Action createTOPOCloningAction() {
        return new BluntTOPOCloningAction();
    }

    @Override
    public Action createTOPOTACloningAction() {
        return new TOPOTACloningAction();
    }

    @Override
    public Action createDirectionalTOPOCloningAction() {
        return new DirTOPOCloningAction();
    }

    @Override
    public Action createTAInsertAction() {
        return new CreateTAInsertAction();
    }

    @Override
    public Action createDirectionalTOPOInsertAction() {
        return new CreateDirTOPOInsertAction();
    }
    
}
