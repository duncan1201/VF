/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EFetch;

import com.gas.domain.ui.util.api.IEFetchService;
import com.gas.domain.ui.util.api.IEFetchServiceFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IEFetchServiceFactory.class)
public class EFetchServiceFactory implements IEFetchServiceFactory{

    @Override
    public IEFetchService create() {
        IEFetchService ret = new EFetchService();
        return ret;
    }

}
