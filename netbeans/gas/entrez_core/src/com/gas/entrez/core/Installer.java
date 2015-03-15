/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core;

import com.gas.entrez.core.EInfo.api.IEInfoCmd;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class Installer extends ModuleInstall {
    
    private final static Logger logger = Logger.getLogger(Installer.class.getName());
    

    @Override
    public void restored() {
        logger.info("com.gas.entrez.core.Installer.restored()");
        IEInfoCmd infoCmd = Lookup.getDefault().lookup(IEInfoCmd.class);
        infoCmd.getPreloaded("protein");
        infoCmd.getPreloaded("pubmed");
        infoCmd.getPreloaded("nucleotide");
        infoCmd.getPreloaded("structure");
    }
}
