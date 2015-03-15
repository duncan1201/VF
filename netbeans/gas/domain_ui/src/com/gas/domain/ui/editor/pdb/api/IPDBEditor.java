/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.pdb.api;

import com.gas.domain.core.pdb.PDBDoc;

/**
 *
 * @author dq
 */
public interface IPDBEditor {

    void setPdbDoc(PDBDoc pdbDoc);

    PDBDoc getPdbDoc();
}
