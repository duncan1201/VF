/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.clustalw.core.ui.msa.ClustalWUI;
import com.gas.domain.core.msa.clustalw.IClustalWUI;
import com.gas.domain.core.msa.clustalw.IClustalWUIFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IClustalWUIFactory.class)
public class ClustalWUIFactory implements IClustalWUIFactory {

    @Override
    public IClustalWUI create(boolean vertical, String profile1, String profile2) {
        IClustalWUI ret = new ClustalWUI(vertical, profile1, profile2);
        return ret;
    }
    
}
