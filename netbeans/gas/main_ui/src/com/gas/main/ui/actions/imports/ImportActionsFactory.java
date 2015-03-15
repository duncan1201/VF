/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.imports;

import com.gas.domain.ui.banner.IImportActionsFactory;
import javax.swing.Action;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IImportActionsFactory.class)
public class ImportActionsFactory implements IImportActionsFactory {
    public Action createImportFromFileAction(){
        Action ret = new ImportFromFileAction();
        return ret;
    }
}
