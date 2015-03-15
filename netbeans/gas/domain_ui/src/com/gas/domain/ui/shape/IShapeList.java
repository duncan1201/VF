/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class IShapeList extends ArrayList<IShape> {

    public IShapeList getByLocation(int start, int end) {
        IShapeList ret = new IShapeList();
        Iterator<IShape> itr = iterator();
        while (itr.hasNext()) {
            IShape arrow = itr.next();
            Loc loc = arrow.getLoc();
            if (Math.min(start, end) == loc.getMin() && Math.max(start, end) == loc.getMax()) {
                ret.add(arrow);
            }
        }
        return ret;
    }

    public IShapeList getByDisplayTexts(String... displayTexts) {
        StringList texts = new StringList(displayTexts);
        IShapeList ret = new IShapeList();
        Iterator<IShape> itr = iterator();
        while (itr.hasNext()) {
            IShape arrow = itr.next();
            String t = arrow.getDisplayText();
            if (texts.containsIgnoreCase(t)) {
                ret.add(arrow);
            }
        }
        return ret;
    }

    List<Object> getAllData() {
        List<Object> ret = new ArrayList<Object>();
        Iterator<IShape> itr = iterator();
        while (itr.hasNext()) {
            IShape arrow = itr.next();
            ret.add(arrow.getData());
        }
        return ret;
    }
}