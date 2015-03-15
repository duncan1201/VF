/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.border;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.border.EtchedBorder;
import static javax.swing.border.EtchedBorder.LOWERED;

/**
 *
 * @author dq
 */
public class EtchedBorder2 extends EtchedBorder {

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        int w = width;

        g.translate(x, y);

        g.setColor(getShadowColor(c));
        g.drawLine(0, 0, w - 1, 0);

        g.translate(-x, -y);
    }
}
