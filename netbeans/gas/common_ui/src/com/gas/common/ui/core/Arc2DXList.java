/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class Arc2DXList extends ArrayList<Arc2DX.Float> {

    public void paint(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            Arc2DX.Float arc2d = get(i);
            arc2d.paint(g);
        }
    }
}
