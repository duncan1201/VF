/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 *
 * @author dq
 */
public class MSACompEdit implements UndoableEdit {

    @Override
    public void undo() throws CannotUndoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canUndo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void redo() throws CannotRedoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canRedo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void die() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return "getPresentationName";
    }

    @Override
    public String getUndoPresentationName() {
        return "getUndoPresentationName";
    }

    @Override
    public String getRedoPresentationName() {
        return "getRedoPresentationName";
    }
    
}
