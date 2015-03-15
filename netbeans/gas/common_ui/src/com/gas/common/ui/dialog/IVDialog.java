/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.dialog;

import java.util.List;

/**
 *
 * @author dq
 */
public interface IVDialog {

    boolean validateInput();

    List<String> getErrorMessage();

    int getClosedButton();

    void setClosedButton(int closedButton);
}
