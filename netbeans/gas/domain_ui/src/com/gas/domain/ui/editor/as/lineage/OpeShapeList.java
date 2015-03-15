/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
class OpeShapeList extends ArrayList<OpeShape> {

    public void paint(Graphics2D g) {
        Iterator<OpeShape> itr = iterator();
        while (itr.hasNext()) {
            OpeShape opeShape = itr.next();
            opeShape.paint(g);
        }
    }
}
