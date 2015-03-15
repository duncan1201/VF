/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import com.gas.common.ui.util.UIUtil;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class GeneralPathList extends ArrayList<GeneralPath> {

    private boolean fill;

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public GeneralPathList createConnectors() {
        GeneralPathList ret = new GeneralPathList();
        Collections.sort(this, new StartComparator());
        for (int i = 0; i < size(); i++) {
            GeneralPath cur = get(i);
            GeneralPath next = null;
            if (i + 1 < size()) {
                next = get(i + 1);
            }
            if (next != null) {
                Point2D start = UIUtil.getEastMiddle(cur);
                Point2D end = UIUtil.getWestMiddle(next);
                GeneralPath connector = new GeneralPath();
                connector.moveTo(start.getX(), start.getY());
                connector.lineTo(end.getX(), end.getY());
                ret.add(connector);
            }
        }

        return ret;
    }

    public GeneralPath getByLocation(Point point) {
        GeneralPath ret = null;
        for (int i = 0; i < size(); i++) {
            GeneralPath p = get(i);
            if (p.contains(point)) {
                ret = p;
                break;
            }
        }

        return ret;
    }

    public boolean contains(Point point) {
        boolean ret = false;
        for (int i = 0; i < size(); i++) {
            GeneralPath path = get(i);
            if (path.contains(point)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public void paint(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            if (fill) {
                g.fill(get(i));
            } else {
                g.draw(get(i));
            }
        }
    }

    private static class StartComparator implements Comparator<GeneralPath> {

        @Override
        public int compare(GeneralPath o1, GeneralPath o2) {
            Rectangle r1 = o1.getBounds();
            Rectangle r2 = o2.getBounds();
            return r1.x - r2.x;
        }
    }
}
