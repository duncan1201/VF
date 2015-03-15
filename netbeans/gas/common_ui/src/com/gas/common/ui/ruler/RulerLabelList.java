/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import com.gas.common.ui.core.RectList;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class RulerLabelList extends ArrayList<RulerLabel> {

    public void paint(Graphics2D g) {
        for (int i = 0; i < size(); i++) {
            get(i).paint(g);
        }
    }

    public RectList getRectList() {
        RectList ret = new RectList();
        for (int i = 0; i < size(); i++) {
            Rectangle b = get(i).getBounds();
            if (b != null) {
                ret.add(b);
            }
        }
        return ret;
    }

    public void remove(Rectangle rect) {
        Iterator<RulerLabel> itr = iterator();
        while (itr.hasNext()) {
            RulerLabel label = itr.next();
            Rectangle bounds = label.getBounds();
            if (bounds.intersects(rect)) {
                itr.remove();
                break;
            }
        }
    }

    public void remove(RectList rectList) {
        if (rectList.isEmpty()) {
            return;
        }
        Iterator<RulerLabel> itr = iterator();
        while (itr.hasNext()) {
            Rectangle bounds = itr.next().getBounds();
            if (rectList.intersects(bounds)) {
                itr.remove();
            }
        }
    }
}
