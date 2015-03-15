/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author dunqiang
 */
public class IconButton extends JButton {

    private boolean chosen;

    public IconButton(Icon icon, int borderWidth) {
        setIcon(icon, borderWidth);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String pName = evt.getPropertyName();
                if (pName.equals("chosen")) {
                    repaint();
                }
            }
        });
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        boolean old = this.chosen;
        this.chosen = chosen;
        firePropertyChange("chosen", old, this.chosen);
    }

    public IconButton(Icon icon) {
        this(icon, 1);

    }

    public void setIcon(Icon icon, int borderWidth) {
        setBorder(new BorderAA(Color.BLACK, borderWidth));
        super.setIcon(icon);
        resetPreferredSize();
    }

    private void resetPreferredSize() {
        Insets insets = getInsets();
        Icon icon = getIcon();
        setPreferredSize(new Dimension(icon.getIconWidth() + insets.left + insets.right, icon.getIconHeight() + insets.top + insets.bottom));
        setBackground(Color.WHITE);
    }
}
