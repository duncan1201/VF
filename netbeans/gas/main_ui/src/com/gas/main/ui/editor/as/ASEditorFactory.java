/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.editor.as;

import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.domain.ui.editor.as.IASEditorFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IASEditorFactory.class)
public class ASEditorFactory implements IASEditorFactory {

    @Override
    public IASEditor create() {
        IASEditor ret = new ASEditor();
        return ret;
    }
    
}
