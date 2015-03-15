/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import java.awt.Graphics2D;

/**
 *
 * @author dq
 */
public class Connector {

    int x;
    int y;
    int width;
    int height;

    public void paint(Graphics2D g) {
        g.drawLine(x, y, x + width, y);
        g.drawLine(x, y, x, y + height);
        g.drawLine(x, y + height, x + width, y + height);
    }
}
