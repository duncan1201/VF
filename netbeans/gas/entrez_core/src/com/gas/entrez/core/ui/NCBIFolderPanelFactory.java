/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui;

import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.ui.INCBIFolderPanelFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = INCBIFolderPanelFactory.class)
public class NCBIFolderPanelFactory implements INCBIFolderPanelFactory{

    @Override
    public IFolderPanel create() {
        IFolderPanel ret = new SearchPanel();
        return ret;
    }
    
}
