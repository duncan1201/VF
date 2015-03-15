/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import com.gas.common.ui.core.RectList;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class RulerLabelListMap extends HashMap<Integer, RulerLabelList> {

    public void paint(Graphics2D g) {
        Iterator<RulerLabelList> itr = values().iterator();
        while (itr.hasNext()) {
            RulerLabelList l = itr.next();
            l.paint(g);
        }
    }

    public void remove(RectList rectList) {
        Iterator<RulerLabelList> itr = values().iterator();
        while (itr.hasNext()) {
            RulerLabelList l = itr.next();
            l.remove(rectList);
        }
    }
}
