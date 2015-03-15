/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import com.gas.common.ui.util.RectHelper;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author dunqiang
 */
public class RectList extends ArrayList<Rectangle> {

    public boolean intersects(Rectangle rect) {
        return intersects(rect.x, rect.y, rect.width, rect.height);
    }

    public boolean intersects(double x, double y, double w, double h) {
        boolean ret = false;
        for (int i = 0; i < size(); i++) {
            Rectangle r = get(i);
            ret = RectHelper.intersects(r, x, y, w, h);
            if (ret) {
                break;
            }
        }
        return ret;
    }
}
