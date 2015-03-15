/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.actions;

import com.gas.domain.ui.editor.as.IDigestAction;
import com.gas.domain.ui.editor.as.IDigestActionFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IDigestActionFactory.class)
public class DigestActionFactory implements IDigestActionFactory{
    
    @Override
    public IDigestAction create(){
        return new DigestAction();
    }
}
