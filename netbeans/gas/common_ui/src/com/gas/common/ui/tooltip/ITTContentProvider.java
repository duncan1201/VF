/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tooltip;

import java.awt.Container;
import java.awt.Point;
import javax.swing.JComponent;

/**
 *
 * @author dunqiang
 */
public interface ITTContentProvider {

    Container getToolTipContent();
    
    void setRichToolTip(JComponent t, boolean mouseEnter, boolean propertyChange);
    
    Object getData();

    JComponent createRichToolTip();

    JComponent getRichToolTip();

}
