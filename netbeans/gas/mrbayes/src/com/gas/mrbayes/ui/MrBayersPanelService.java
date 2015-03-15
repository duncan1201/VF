/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.mrbayes.ui;

import com.gas.phylo.mrbayers.api.IMrBayesPanel;
import com.gas.phylo.mrbayers.api.IMrBayesPanelService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMrBayesPanelService.class)
public class MrBayersPanelService implements IMrBayesPanelService{

    @Override
    public IMrBayesPanel create() {
        IMrBayesPanel ret = new MrBayesPanel();
        return ret;        
    }
    
}
