/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.caret;

import java.awt.Point;
import java.util.EventObject;

/**
 *
 * @author dq
 */
public class CaretMoveEvent extends EventObject {

    public enum DIR {

        UP, DOWN, LEFT, RIGHT
    };
    private Point pos;
    private DIR dir;

    public CaretMoveEvent(Object src) {
        super(src);
    }

    public CaretMoveEvent(Object src, DIR dir, Point pos) {
        super(src);
        this.dir = dir;
        this.pos = pos;
    }

    public DIR getDir() {
        return dir;
    }

    public void setDir(DIR dir) {
        this.dir = dir;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }
}
