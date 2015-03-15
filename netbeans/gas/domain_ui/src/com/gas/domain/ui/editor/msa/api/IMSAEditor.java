/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.msa.api;

import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.ISavableEditor;

/**
 *
 * @author dq
 */
public interface IMSAEditor extends ISavableEditor, IPrintEditor {

    MSA getMsa();

    void setMsa(MSA msa);

    void refreshUI();
}
