/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.combo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.*;

/**
 *
 * @author dq
 */
public class BorderComboRenderer extends JPanel implements ListCellRenderer {

    public BorderComboRenderer() {
        LayoutManager layout = null;
        layout = new BorderLayout();
        setLayout(layout);
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        removeAll();
        String str = null;
        if (value instanceof Item) {
            Item item = (Item) value;
            if (item.getBottomSeparator() && !isSelected) {
                add(new JSeparator(), BorderLayout.SOUTH);
            }
            if (item.getTopSeparator() && !isSelected) {
                add(new JSeparator(), BorderLayout.NORTH);
            }
            str = item.getData().toString();
        } else {
            str = value.toString();
        }

        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setFont(list.getFont());
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setText(str);

        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            label.setBackground(list.getBackground());
            label.setForeground(list.getForeground());
        }

        add(label, BorderLayout.CENTER);

        return this;
    }

    public static class Item {

        private Object data;
        private boolean topSeparator;
        private boolean bottomSeparator;

        public Item() {
        }

        public Item(Object data) {
            this(data, false, false);
        }

        public Item(Object data, boolean topSeparator, boolean bottomSeparator) {
            this.data = data;
            this.topSeparator = topSeparator;
            this.bottomSeparator = bottomSeparator;
        }

        public void setTopSeparator(boolean topSeparator) {
            this.topSeparator = topSeparator;
        }

        public void setBottomSeparator(boolean bottomSeparator) {
            this.bottomSeparator = bottomSeparator;
        }

        public boolean getTopSeparator() {
            return topSeparator;
        }

        public boolean getBottomSeparator() {
            return bottomSeparator;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
