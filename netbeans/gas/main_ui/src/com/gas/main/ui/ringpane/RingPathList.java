/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**

 @author dq
 */
public class RingPathList extends ArrayList<RingPath> {

    /**
     It expects the start angle of RingPath in clockwise direction

     @see RingPath
     */
    public GeneralPathList createConnectors(boolean join) {
        GeneralPathList ret = new GeneralPathList();
        Iterator<RingPath> itr = iterator();
        RingPath prev = null;
        while (itr.hasNext()) {
            RingPath cur = itr.next();
            if (prev != null) {
                Point2D center = cur.getCenter();
                double curStart = cur.getStartAngle();
                double radius = cur.getRadius();

                double prevStart = prev.getStartAngle();
                double prevExtent = prev.getExtent();

                double startAngle = MathUtil.normalizeDegree(prevStart - prevExtent);
                double endAngle = MathUtil.normalizeDegree(curStart);                
                double distance = MathUtil.getDistance(MathUtil.round(startAngle, 4), MathUtil.round(endAngle, 4), false);

                GeneralPath conPath = new GeneralPath();
                if (join) {
                    Arc2D.Float con = new Arc2D.Float();
                    con.setArcByCenter(center.getX(), center.getY(), radius, startAngle, -(distance), Arc2D.OPEN);
                    conPath.append(con, false);
                } else {
                    List<Arc2D> arcs = UIUtil.createArcsByCenter(center.getX(), center.getY(), radius, startAngle, -(distance - 1), Arc2D.OPEN);
                    for (Arc2D arc : arcs) {
                        conPath.append(arc, false);
                    }
                }
                ret.add(conPath);
            }
            prev = cur;
        }

        return ret;
    }

    public GeneralPathList getRings() {
        GeneralPathList ret = new GeneralPathList();
        Iterator<RingPath> itr = iterator();
        while (itr.hasNext()) {
            RingPath cur = itr.next();
            ret.add(cur.getPath());
        }

        return ret;
    }
}
