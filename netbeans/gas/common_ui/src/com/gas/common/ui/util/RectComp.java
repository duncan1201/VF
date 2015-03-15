/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class RectComp extends JComponent {

    @Override
    protected void printComponent(Graphics g) {
        g.setColor(Color.BLACK);
        Dimension size = getSize();
        g.fillRect(0, 0, size.width, size.height);
    }
}
