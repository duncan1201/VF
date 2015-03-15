/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class TCHeader extends JPanel {

    private JLabel rightLabel;
    private JLabel centerLabel;
    private JLabel leftLabel;

    public TCHeader() {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        setOpaque(true);
        setBackground(new Color(223, 234, 246));

        //setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        GridBagConstraints c = null;

        leftLabel = new JLabel("", ImageHelper.createImageIcon(ImageNames.EMPTY_16), JLabel.CENTER);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        leftLabel.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        add(leftLabel, c);

        Component comp = Box.createRigidArea(new Dimension(1, 1));
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        add(comp, c);


        centerLabel = new JLabel("", ImageHelper.createImageIcon(ImageNames.EMPTY_16), JLabel.CENTER);
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        add(centerLabel, c);

        comp = Box.createRigidArea(new Dimension(1, 1));
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        add(comp, c);

        rightLabel = new JLabel("", ImageHelper.createImageIcon(ImageNames.EMPTY_16), JLabel.RIGHT);
        //label.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        add(rightLabel, c);
    }

    public void setText(String name, int hAlignment) {
        if (hAlignment == SwingConstants.LEFT) {
            leftLabel.setText(name);
        } else if (hAlignment == SwingConstants.CENTER) {
            centerLabel.setText(name);
        } else if (hAlignment == SwingConstants.RIGHT) {
            rightLabel.setText(name);
        } else {
            throw new IllegalArgumentException("Only the following values allowed for hAlignment: JLabel.LEFT/RIGHT/CENTER");
        }
    }

    public void setText(String name) {
        setText(name, SwingConstants.LEFT);
    }

    public void setIcon(ImageIcon icon) {
        setIcon(icon, SwingConstants.LEFT);
    }

    public void setIcon(ImageIcon icon, int hAlignment) {
        if (hAlignment == SwingConstants.LEFT) {
            leftLabel.setIcon(icon);
        } else if (hAlignment == SwingConstants.CENTER) {
            centerLabel.setIcon(icon);
        } else if (hAlignment == SwingConstants.RIGHT) {
            rightLabel.setIcon(icon);
        } else {
            throw new IllegalArgumentException("Only the following values allowed for hAlignment: JLabel.LEFT/RIGHT/CENTER");
        }
        this.leftLabel.setIcon(icon);
    }
}
