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
public class Filler extends JComponent {

    private Color color;

    public Filler(Color color) {
        this.color = color;
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        g.setColor(color);
        g.fillRect(0, 0, size.width, size.height);
    }
}
