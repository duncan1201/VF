/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class PolygonList extends ArrayList<Polygon> {

    public void fill(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            Polygon polygon = get(i);
            g.fill(polygon);
        }
    }
}
