/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.imports;

import com.gas.domain.ui.editor.IExportActionFactory;
import javax.swing.Action;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IExportActionFactory.class)
public class ExportActionFactory implements IExportActionFactory{

    @Override
    public Action create(String text) {
        ExportCurrentEditorAction ret = new ExportCurrentEditorAction(text);
        return ret;
    }
    
}
