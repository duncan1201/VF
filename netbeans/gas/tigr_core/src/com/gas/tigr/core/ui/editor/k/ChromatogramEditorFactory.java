/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.domain.ui.editor.kromatogram.api.IChromatogramEditorFactory;
import com.gas.domain.ui.editor.kromatogram.api.IChromatogramEditor;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IChromatogramEditorFactory.class)
public class ChromatogramEditorFactory implements IChromatogramEditorFactory {

    @Override
    public IChromatogramEditor create() {
        IChromatogramEditor ret = new ChromatogramEditor();
        return ret;
    }
    
}
