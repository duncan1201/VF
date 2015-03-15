/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui;

import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.domain.ui.editor.msa.api.IMSAEditorFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IMSAEditorFactory.class)
public class MSAEditorFactory implements IMSAEditorFactory {

    @Override
    public IMSAEditor create() {
        IMSAEditor ret = new MSAEditor();
        return ret;
    }
    
}
