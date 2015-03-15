/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import org.netbeans.spi.actions.AbstractSavable;

/**
 *
 * @author dq
 */
public interface ISavableEditor {

    void setCanSave();

    Class<? extends AbstractSavable> getMySavableClass();

    void setMySavable(AbstractSavable savable);
}
