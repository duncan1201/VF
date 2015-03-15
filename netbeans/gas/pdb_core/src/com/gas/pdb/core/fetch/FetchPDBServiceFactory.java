/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.pdb.core.fetch;

import com.gas.pdb.core.fetch.api.IFetchPDBService;
import com.gas.pdb.core.fetch.api.IFetchPDBServiceFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IFetchPDBServiceFactory.class)
public class FetchPDBServiceFactory implements IFetchPDBServiceFactory{
    @Override
    public IFetchPDBService create(){
        IFetchPDBService ret = new FetchPDBService();
        return ret;
    }
}
