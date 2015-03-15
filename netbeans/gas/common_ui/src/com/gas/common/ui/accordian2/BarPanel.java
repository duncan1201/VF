/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.painter.LinearGradientPainter;
import com.gas.common.ui.painter.LinearGradientPainterFactory;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class BarPanel extends JPanel {

    Icon icon;
    String text;
    JLabel label;
    JLabel labelIcon;
    boolean expanded;
    boolean selected;

    BarPanel(String text, Icon icon, boolean expanded, boolean selected) {
        this.icon = icon;
        this.text = text;
        this.expanded = expanded;
        this.selected = selected;
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        initComponents();
        hookupListeners();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintBorder(g);
        final Dimension size = getSize();
        final Insets insets = getInsets();
        LinearGradientPainter painter;

        if (selected) {
            painter = LinearGradientPainterFactory.create(getOutlookPane().selectedColor, 0.9f);
        } else {
            painter = LinearGradientPainterFactory.create(getOutlookPane().color, 0.9f);
        }

        painter.paint((Graphics2D) g, this, size.width, size.height);
    }

    OutlookPane getOutlookPane() {
        OutlookPane ret = UIUtil.getParent(this, OutlookPane.class);
        return ret;
    }

    void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        if (selected) {
            label = new JLabel(String.format("<html><b>%s</b></html>", text), icon, SwingConstants.CENTER);
        } else {
            label = new JLabel(text, icon, SwingConstants.CENTER);
        }
        add(label, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createHorizontalGlue();
        add(filler, c);

        c = new GridBagConstraints();
        c.gridy = 0;

        if (expanded || selected) {
            labelIcon = new JLabel(ImageHelper.createImageIcon(ImageNames.CHECK_16));
        } else {
            labelIcon = new JLabel(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        }
        add(labelIcon, c);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    void hookupListeners() {
        addPropertyChangeListener(new BarPanelListeners.PtyListener());
        addMouseListener(new BarPanelListeners.MouseAdpt());
    }

    boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    Icon getIcon() {
        return icon;
    }

    String getText() {
        return text;
    }

    boolean isExpanded() {
        return expanded;
    }

    void setExpanded(boolean expanded) {
        boolean old = this.expanded;
        this.expanded = expanded;
        firePropertyChange("expanded", old, this.expanded);
    }
}
