/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.caret;

import java.util.EventListener;

/**
 *
 * @author dq
 */
public interface ICaretParentListener extends EventListener {

    void onDelete(CaretParentEvent event);

    void onInsert(CaretParentEvent event);

    void onReplace(CaretParentEvent event);

    void onMove(CaretMoveEvent event);
}
