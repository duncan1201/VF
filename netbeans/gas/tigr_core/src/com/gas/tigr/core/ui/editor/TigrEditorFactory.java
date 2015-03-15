/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor;

import com.gas.domain.ui.editor.tigr.api.ITigrEditorFactory;
import com.gas.domain.ui.editor.tigr.api.ITigrPtEditor;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=ITigrEditorFactory.class)
public class TigrEditorFactory implements ITigrEditorFactory{

    @Override
    public ITigrPtEditor create() {
        ITigrPtEditor ret = new TigrPtEditor();
        return ret;
    }
    
}
