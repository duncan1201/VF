/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.awt.Component;
import java.awt.Point;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class CompLocComparator implements Comparator<Component> {

    private boolean vertical = true;

    public CompLocComparator() {
        this(true);
    }

    public CompLocComparator(boolean vertical) {
        this.vertical = vertical;
    }

    @Override
    public int compare(Component o1, Component o2) {
        if (!o1.isShowing() || !o2.isShowing()) {
            return 0;
        }
        Point p1 = o1.getLocationOnScreen();
        Point p2 = o2.getLocationOnScreen();
        if (vertical) {
            return p1.y - p2.y;
        } else {
            return p1.x - p2.x;
        }
    }
}
