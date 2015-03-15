/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import org.netbeans.api.actions.Savable;

/**
 *
 * @author dq
 */
public interface IMySavable extends Savable {

    void handleCloseWithoutSaving();
}
