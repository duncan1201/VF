/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import com.gas.common.ui.misc.Loc;

/**
 *
 * @author dq
 */
public interface IVisibleLocProvider {

    Loc calculateVisibleLoc();

    Loc getTotalLoc();

    boolean isPaintVisibleOnly();
}
