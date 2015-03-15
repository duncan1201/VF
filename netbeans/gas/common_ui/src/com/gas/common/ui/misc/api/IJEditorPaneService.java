/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc.api;

import javax.swing.JEditorPane;

/**
 *
 * @author dq
 */
public interface IJEditorPaneService {

    /**
     * @param contentType possible values: text/plain, text/html and text/rtf
     */
    JEditorPane create(Boolean editable, String contentType);

    JEditorPane create(Boolean editable);

    JEditorPane create();
}
