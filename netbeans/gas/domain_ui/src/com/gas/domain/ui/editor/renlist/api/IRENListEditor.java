/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.renlist.api;

import com.gas.domain.core.ren.RENList;
import com.gas.domain.ui.editor.ISavableEditor;
import javax.swing.JTable;

/**
 *
 * @author dq
 */
public interface IRENListEditor extends ISavableEditor {

    RENList getRENList();

    void setRENList(RENList renList);

    JTable getRenTable();
}
