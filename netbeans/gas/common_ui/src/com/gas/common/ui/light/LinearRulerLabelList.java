/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class LinearRulerLabelList extends ArrayList<LinearRulerLabel> {

    public void paint(Graphics2D g2d) {
        for (int i = 0; i < size(); i++) {
            get(i).paint(g2d);
        }
    }

    public void removeIntersection(LinearRulerLabelList list) {
        for (int i = 0; i < list.size(); i++) {
            LinearRulerLabel label = list.get(i);
            removeByRect(label.getBounds());
        }
    }

    public void removeIntersection() {
        for (int i = 0; i < size(); i++) {
            LinearRulerLabel label = get(i);
            for (int j = i + 1; j < size(); j++) {
                LinearRulerLabel other = get(j);
                Boolean intersects = label.intersects(other);
                if (intersects != null && intersects) {
                    remove(j);
                    j--;
                }
            }
        }
    }

    public void removeByRect(Rectangle rect) {
        if (rect == null) {
            return;
        }
        for (int i = 0; i < size(); i++) {
            LinearRulerLabel label = get(i);
            Boolean intersects = label.intersects(rect);
            if (intersects != null && intersects) {
                this.remove(i);
                i--;
            }
        }
    }
}
