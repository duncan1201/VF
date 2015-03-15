/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.PathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

/**

 @author dq
 */
public class RingPathCreator {

    /**
     @param startAngle counterclockwise, starts from 3 o'clock
     @param endAngle counterclockwise, starts from 3 o'clock
     @param extent     clockwise
     */
    public static RingPath create(Rectangle outterBounds, float ringWidth, Boolean forward, float startAngle, float extent, boolean fuzzyStart, boolean fuzzyEnd) {
        startAngle = MathUtil.normalizeDegree(startAngle).floatValue();
        RingPath ret = new RingPath();
        Rectangle middleBounds = UIUtil.shrink(outterBounds, MathUtil.round(ringWidth * 0.5f - 1), MathUtil.round(ringWidth * 0.5f - 1));
        Rectangle innerBounds = UIUtil.shrink(outterBounds, ringWidth, ringWidth);

        final Point.Double center = new Point.Double(outterBounds.getWidth() * 0.5 + outterBounds.getX(), outterBounds.getHeight() * 0.5 + outterBounds.getY());
        final float outerRadius = outterBounds.width / 2.0f;
        final float middleRadius = middleBounds.width / 2.0f;

        ret.setRadius(middleRadius);
        ret.setCenter(center);

        float tipDgr = MathUtil.getAngle((int) Math.round(ringWidth * .5), outerRadius, false).floatValue();
        GeneralPath path = new GeneralPath();
        if (Math.abs(extent) < tipDgr) {
            extent = extent > 0 ? tipDgr : -tipDgr;
        }

        Arc2D.Float outterArc = null;
        Arc2D.Float innerArc = null;

        if (forward == null) {
            outterArc = new Arc2D.Float(outterBounds, startAngle, -extent, Arc2D.OPEN);
            innerArc = new Arc2D.Float(innerBounds, startAngle + -extent, extent, Arc2D.OPEN);
        } else if (forward) {
            outterArc = new Arc2D.Float(outterBounds, startAngle, -extent + tipDgr, Arc2D.OPEN);
            innerArc = new Arc2D.Float(innerBounds, startAngle + -extent + tipDgr, extent - tipDgr, Arc2D.OPEN);
        } else if (!forward) {
            outterArc = new Arc2D.Float(outterBounds, startAngle - tipDgr, -extent + tipDgr, Arc2D.OPEN);
            innerArc = new Arc2D.Float(innerBounds, startAngle + -extent, extent - tipDgr, Arc2D.OPEN);
        }

        path.append(outterArc, false);
        Boolean clockwise = null;
        CNST.CAP cap = null;
        Boolean head = null;
        if (forward == null || !forward) {
            clockwise = false;
            head = false;
            cap = fuzzyEnd ? CNST.CAP.TWO_TIP : CNST.CAP.ROUNDED;
        } else if (forward) {
            clockwise = true;
            cap = fuzzyEnd ? CNST.CAP.TWO_TIP : CNST.CAP.ONE_TIP;
            head = true;
        }
        GeneralPath inwardsTip = createTip(outterBounds, ringWidth, startAngle, extent, fuzzyEnd ? tipDgr * 0.6f : tipDgr, clockwise, true, cap, head);
        path.append(inwardsTip, true);

        path.append(innerArc, true);
        if (forward == null || forward) {
            head = false;
            cap = fuzzyStart ? CNST.CAP.TWO_TIP : CNST.CAP.ROUNDED;
            clockwise = true;
        } else if (!forward) {
            head = true;
            cap = fuzzyStart ? CNST.CAP.TWO_TIP : CNST.CAP.ONE_TIP;
            clockwise = false;
        }
        GeneralPath outwardsTip = createTip(outterBounds, ringWidth, startAngle, extent, fuzzyStart ? tipDgr * 0.6f : tipDgr, clockwise, false, cap, head);
        path.append(outwardsTip, true);

        ret.setPath(path);
        ret.setStartAngle(startAngle);
        ret.setExtent(extent);
        return ret;
    }

    /**
     @param clockwise the direction of the ring tip
     @param head      whether it's head or tail of a ring
     */
    private static GeneralPath createTip(Rectangle outerBounds, float ringWidth, float startAngle, float extent, float tipDegree, boolean clockwise, boolean inwards, CNST.CAP cap, boolean head) {     
        GeneralPath ret = new GeneralPath();
        Arc2D.Float outerArc = null;
        Arc2D.Float upperMiddleArc = null;
        Arc2D.Float middleArc = null;
        Arc2D.Float lowerMiddleArc = null;
        Arc2D.Float innerArc = null;
        final Rectangle upperBounds = UIUtil.shrink(outerBounds, ringWidth * 0.25, ringWidth * 0.25);
        final Rectangle middleBounds = UIUtil.shrink(outerBounds, ringWidth * 0.5, ringWidth * 0.5);
        final Rectangle lowerBounds = UIUtil.shrink(outerBounds, ringWidth * 0.75, ringWidth * 0.75);
        final Rectangle innerBounds = UIUtil.shrink(outerBounds, ringWidth, ringWidth);

        if (clockwise) {
            if (inwards) {
                if (head) {
                    outerArc = new Arc2D.Float(outerBounds, startAngle, -extent + tipDegree, Arc2D.OPEN);
                    if (cap == CNST.CAP.ONE_TIP) {
                        middleArc = new Arc2D.Float(middleBounds, startAngle, -extent, Arc2D.OPEN);
                    } else if (cap == CNST.CAP.TWO_TIP) {
                        upperMiddleArc = new Arc2D.Float(upperBounds, startAngle, -extent, Arc2D.OPEN);
                        middleArc = new Arc2D.Float(middleBounds, startAngle, -extent + tipDegree, Arc2D.OPEN);
                        lowerMiddleArc = new Arc2D.Float(lowerBounds, startAngle, -extent, Arc2D.OPEN);
                    }
                    innerArc = new Arc2D.Float(innerBounds, startAngle - extent + tipDegree, extent - tipDegree, Arc2D.OPEN);

                    if (cap == CNST.CAP.ONE_TIP) {
                        ret.moveTo(outerArc.getEndPoint().getX(), outerArc.getEndPoint().getY());
                        ret.lineTo(middleArc.getEndPoint().getX(), middleArc.getEndPoint().getY());
                        ret.lineTo(innerArc.getStartPoint().getX(), innerArc.getStartPoint().getY());
                    } else if (cap == CNST.CAP.TWO_TIP) {
                        ret.moveTo(outerArc.getEndPoint().getX(), outerArc.getEndPoint().getY());
                        ret.lineTo(upperMiddleArc.getEndPoint().getX(), upperMiddleArc.getEndPoint().getY());
                        ret.lineTo(middleArc.getEndPoint().getX(), middleArc.getEndPoint().getY());
                        ret.lineTo(lowerMiddleArc.getEndPoint().getX(), lowerMiddleArc.getEndPoint().getY());
                        ret.lineTo(innerArc.getStartPoint().getX(), innerArc.getStartPoint().getY());
                    } else if (cap == CNST.CAP.ROUNDED) {
                        ret.moveTo(outerArc.getEndPoint().getX(), outerArc.getEndPoint().getY());
                        ret.lineTo(innerArc.getStartPoint().getX(), innerArc.getStartPoint().getY());
                    }
                } else {
                    ret = createTip(outerBounds, ringWidth, startAngle, extent, tipDegree, clockwise, inwards, cap, !head);
                    double anchorx = outerBounds.getCenterX();
                    double anchory = outerBounds.getCenterY();
                    ret = PathUtil.transform(AffineTransform.getRotateInstance(Math.toRadians(-extent + tipDegree), anchorx, anchory), ret);
                }
            } else {
                ret = createTip(outerBounds, ringWidth, startAngle, extent, tipDegree, clockwise, !inwards, cap, head);
                ret = PathUtil.reverse(ret);
            }
        } else {
            if (inwards) {
                if (head) {
                    outerArc = new Arc2D.Float(outerBounds, startAngle - tipDegree, -extent + tipDegree, Arc2D.OPEN);
                    if (cap == CNST.CAP.ONE_TIP) {
                        middleArc = new Arc2D.Float(middleBounds, startAngle, -extent, Arc2D.OPEN);
                    } else if (cap == CNST.CAP.TWO_TIP) {
                        upperMiddleArc = new Arc2D.Float(upperBounds, startAngle, -extent, Arc2D.OPEN);
                        middleArc = new Arc2D.Float(middleBounds, startAngle - tipDegree, -extent + tipDegree, Arc2D.OPEN);
                        lowerMiddleArc = new Arc2D.Float(lowerBounds, startAngle, -extent, Arc2D.OPEN);
                    }
                    innerArc = new Arc2D.Float(innerBounds, startAngle - extent, extent - tipDegree, Arc2D.OPEN);


                    if (cap == CNST.CAP.ONE_TIP) {
                        ret.moveTo(outerArc.getStartPoint().getX(), outerArc.getStartPoint().getY());
                        ret.lineTo(middleArc.getStartPoint().getX(), middleArc.getStartPoint().getY());
                        ret.lineTo(innerArc.getEndPoint().getX(), innerArc.getEndPoint().getY());
                    } else if (cap == CNST.CAP.TWO_TIP) {
                        ret.moveTo(outerArc.getStartPoint().getX(), outerArc.getStartPoint().getY());
                        ret.lineTo(upperMiddleArc.getStartPoint().getX(), upperMiddleArc.getStartPoint().getY());
                        ret.lineTo(middleArc.getStartPoint().getX(), middleArc.getStartPoint().getY());
                        ret.lineTo(lowerMiddleArc.getStartPoint().getX(), lowerMiddleArc.getStartPoint().getY());
                        ret.lineTo(innerArc.getEndPoint().getX(), innerArc.getEndPoint().getY());
                    } else if (cap == CNST.CAP.ROUNDED) {
                        ret.moveTo(outerArc.getStartPoint().getX(), outerArc.getStartPoint().getY());
                        ret.lineTo(innerArc.getEndPoint().getX(), innerArc.getEndPoint().getY());
                    }
                } else {
                    ret = createTip(outerBounds, ringWidth, startAngle, extent, tipDegree, clockwise, inwards, cap, !head);
                    double anchorx = outerBounds.getCenterX();
                    double anchory = outerBounds.getCenterY();
                    ret = PathUtil.transform(AffineTransform.getRotateInstance(Math.toRadians(extent - tipDegree), anchorx, anchory), ret);
                }

            } else {
                ret = createTip(outerBounds, ringWidth, startAngle, extent, tipDegree, clockwise, !inwards, cap, head);
                ret = PathUtil.reverse(ret);
            }
        }
        return ret;
    }
}
