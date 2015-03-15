/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
class EntityShapeList extends ArrayList<EntityShape> {

    public void paint(Graphics2D g, ImageObserver observer) {
        Iterator<EntityShape> itr = iterator();
        while (itr.hasNext()) {
            final EntityShape shape = itr.next();
            shape.paint(g, observer);
        }
    }

    public EntityShape get(Point pt) {
        Iterator<EntityShape> itr = iterator();
        while (itr.hasNext()) {
            final EntityShape shape = itr.next();
            if (shape.contains(pt)) {
                return shape;
            }
        }
        return null;
    }

    public boolean contains(Point pt) {
        Iterator<EntityShape> itr = iterator();
        while (itr.hasNext()) {
            final EntityShape shape = itr.next();
            if (shape.contains(pt)) {
                return true;
            }
        }
        return false;
    }
}
