/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.editor.pubmed;

import com.gas.domain.ui.editor.pubmed.IPubmedEditor;
import com.gas.domain.ui.editor.pubmed.IPubmedEditorFactory;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IPubmedEditorFactory.class)
public class PubmedEditorFactory implements IPubmedEditorFactory{
    @Override
    public IPubmedEditor create(){
        IPubmedEditor ret = new PubmedEditor();
        return ret;
    }
}
