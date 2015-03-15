/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.predict.api;

import javax.swing.ImageIcon;

/**
 *
 * @author dq
 */
public interface IPredictPanel {

    ImageIcon getImageIcon();

    String getToolTipText();

    void refresh();
}
