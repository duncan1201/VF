/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.domain.ui.predict.api.IPredictPanel;
import com.gas.domain.ui.predict.api.IPredictPanelCreator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IPredictPanelCreator.class)
public class PreditPanelCreator implements IPredictPanelCreator {

    @Override
    public IPredictPanel create() {
        IPredictPanel ret = new PredictPanel();
        return ret;
    }
    
}
