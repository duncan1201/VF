/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.editor;

import com.gas.domain.ui.editor.renlist.api.IRENListEditor;
import com.gas.domain.ui.editor.renlist.api.IRENListEditorFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IRENListEditorFactory.class)
public class RENListFactory implements IRENListEditorFactory {

    @Override
    public IRENListEditor create() {
        IRENListEditor ret = new RENListEditor();
        return ret;
    }
    
}
