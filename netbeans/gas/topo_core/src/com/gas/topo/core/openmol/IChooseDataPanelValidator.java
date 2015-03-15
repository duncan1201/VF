/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.openmol;

import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
public interface IChooseDataPanelValidator {
    void setRef(WeakReference<ChooseDataPanel> ref);
    boolean validate();
    String getFriendlyMessage();
}
