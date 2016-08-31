/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gas.msa.common;

import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Dunqiang
 */
@ServiceProvider(service=IExecutableService.class)
public class ExecutableServiceImpl implements IExecutableService{
    public boolean isExecutablePresent(String a){
        if (Utilities.isWindows()) {
            
        } else if (Utilities.isMac()) {
        
        }
        
        boolean ret = false;
        return ret;
    }
}
