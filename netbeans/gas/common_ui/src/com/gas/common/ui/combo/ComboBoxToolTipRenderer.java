/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.combo;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

/**
 *
 * @author dq
 */
public class ComboBoxToolTipRenderer extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {

        JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);

        if (-1 < index && null != value) {
                    list.setToolTipText(value.toString());
                }
        return comp;
    }
}
