/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.pdb.core.ui;

import com.gas.domain.ui.editor.pdb.api.IPDBEditor;
import com.gas.domain.ui.editor.pdb.api.IPDBEditorFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IPDBEditorFactory.class)
public class PDBEditorFactory implements IPDBEditorFactory{

    @Override
    public IPDBEditor create() {
        IPDBEditor ret = new PDBEditor();
        return ret;
    }
    
}
