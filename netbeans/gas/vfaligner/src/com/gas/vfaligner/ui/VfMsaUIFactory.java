/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.vfaligner.ui;

import com.gas.domain.core.msa.vfmsa.IVfMsaUI;
import com.gas.domain.core.msa.vfmsa.IVfMsaUIFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IVfMsaUIFactory.class)
public class VfMsaUIFactory implements IVfMsaUIFactory{
    
    @Override
    public IVfMsaUI create(){
        IVfMsaUI ret = new VfMsaUI();
        return ret;
    }
}
