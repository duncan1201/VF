/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class CircularBrickList extends ArrayList<CircularBrick> {

    private Integer maxPos;
    private Integer minPos;
    private Loc selection;

    public CircularBrick get(Point p) {
        CircularBrick ret = null;
        for (int i = 0; i < size(); i++) {
            CircularBrick c = get(i);
            if (c.contains(p)) {
                ret = c;
                break;
            }
        }
        return ret;
    }

    @Override
    public boolean add(CircularBrick brick) {
        boolean ret = super.add(brick);
        updateMeta(brick, true);
        return ret;
    }

    public Loc getSelection() {
        return selection;
    }

    public void setSelection(Loc selection) {
        this.selection = selection;
    }

    public Integer getMaxPos() {
        return maxPos;
    }

    public Integer getMinPos() {
        return minPos;
    }

    private void updateMeta(CircularBrick brick, boolean add) {
        if (maxPos == null || maxPos < brick.getPos()) {
            maxPos = brick.getPos();
        }
        if (minPos == null || minPos > brick.getPos()) {
            minPos = brick.getPos();
        }
    }

    /**
     * @see MathUtil#getAngleInDegrees(java.awt.geom.Point2D,
     * java.awt.geom.Point2D)
     */
    public CircularBrick getByAngle(double angle) {
        CircularBrick ret = null;
        for (int i = 0; i < size(); i++) {
            CircularBrick brick = get(i);
            Boolean contains = brick.containsAngle(angle);

            if (contains != null && contains) {
                ret = brick;
                break;
            }
        }
        return ret;
    }

    public Integer getCaretPos(Double angleDgr) {
        if (angleDgr == null) {
            return null;
        }
        angleDgr = MathUtil.normalizeDegree(angleDgr);
        Integer ret = null;
        CircularBrick c = getByAngle(angleDgr);
        if (c != null) {
            double middleAngle = c.getMiddleAngle(true);
            if (angleDgr >= middleAngle) { // left half
                ret = c.getPos();
            } else { // right half
                if (c.getPos() + 1 <= maxPos) {
                    ret = c.getPos() + 1;
                } else {
                    ret = minPos;
                }
            }
        }
        return ret;
    }

    public Point2D getCaretLocation(Integer pos) {
        CircularBrick brick = getByPos(pos);
        return brick.getCaretLocation();
    }

    public Double getCaretAngle(Integer pos) {
        Double ret = null;
        CircularBrick brick = getByPos(pos);
        if (brick != null) {
            ret = brick.getCaretAngle();
        }
        return ret;
    }

    public CircularBrick getByPos(Integer pos) {
        CircularBrick ret = null;
        for (int i = 0; i < size(); i++) {
            CircularBrick brick = get(i);
            if (brick.getPos().intValue() == pos.intValue()) {
                ret = brick;
                break;
            }
        }
        return ret;
    }

    public void deleteByPos(Integer pos) {
        for (int i = 0; i < size(); i++) {
            CircularBrick brick = get(i);
            if (brick.getPos().intValue() == pos.intValue()) {
                remove(i);
            }
        }
        for (int i = 0; i < size(); i++) {
            CircularBrick brick = get(i);
            if (brick.getPos().intValue() > pos.intValue()) {
                brick.setPos(brick.getPos() - 1);
            }
        }
    }

    public Point2D getCaretLocation(double angle) {
        Point2D ret = null;
        CircularBrick c = getByAngle(angle);
        if (c != null) {
            ret = c.getCaretLocation(angle);
        }
        return ret;
    }

    public boolean containsAngle(double angle) {
        boolean ret = false;
        for (int i = 0; i < size(); i++) {
            boolean contains = get(i).containsAngle(angle);
            if (contains) {
                ret = contains;
                break;
            }
        }
        return ret;
    }

    public void paint(Graphics2D g2d) {
        if (g2d == null) {
            return;
        }
        for (int i = 0; i < size(); i++) {
            CircularBrick circularBrick = get(i);
            if (selection != null && selection.contains(circularBrick.getPos())) {
                circularBrick.setSelected(true);
            } else {
                circularBrick.setSelected(false);
            }
            circularBrick.paint(g2d);
        }
    }
}
