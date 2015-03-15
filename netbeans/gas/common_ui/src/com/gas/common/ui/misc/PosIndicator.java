/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class PosIndicator extends JComponent {

    private Color color = new Color(100, 100, 100, 188);

    public PosIndicator() {
    }

    @Override
    public void paintComponent(Graphics g) {

        Dimension size = getSize();
        g.setColor(color);
        g.fillRect(0, 0, size.width, size.height);

    }
}
