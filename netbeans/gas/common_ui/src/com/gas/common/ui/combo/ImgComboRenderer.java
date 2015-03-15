/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.combo;

import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author dq
 */
public class ImgComboRenderer extends JLabel implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)
        IComboItem item = (IComboItem) value;

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        if (item == null) {
            return this;
        }

        //Set the icon and text.  If icon was null, say so.
        ImageIcon icon = item.getImageIcon();
        String displayName = item.getDisplayName();
        if (icon != null) {
            setIcon(icon);
        }
        setText(displayName);

        return this;
    }

    public static interface IComboItem {

        ImageIcon getImageIcon();

        String getDisplayName();
    }
}
