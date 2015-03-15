/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.domain.ui.primer3.api.IPrimer3Panel;
import com.gas.domain.ui.primer3.api.IPrimer3PanelFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IPrimer3PanelFactory.class)
public class Primer3PanelFactory implements IPrimer3PanelFactory{

    @Override
    public IPrimer3Panel create() {
        IPrimer3Panel ret = new Primer3Panel();
        return ret;
    }
    
}
