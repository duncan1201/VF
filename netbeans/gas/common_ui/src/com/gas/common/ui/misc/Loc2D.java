/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

/**
 *
 * @author dq
 */
public class Loc2D {

    public int x1, x2, y1, y2, totalPos;

    public Loc2D() {
    }

    public Loc2D(Loc loc) {
        this(loc.getStart(), 1, loc.getEnd(), 1);
    }

    public Loc2D(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Loc toLoc() {
        return new Loc(x1, x2);
    }

    public Loc2D(Loc2D another) {
        this.x1 = another.x1;
        this.x2 = another.x2;
        this.y1 = another.y1;
        this.y2 = another.y2;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getMinX() {
        return Math.min(x1, x2);
    }

    public int getMaxX() {
        return Math.max(x1, x2);
    }
    
    public void setMaxX(int maxX){
        if(x1 > x2){
            x1 = maxX;
        }else{
            x2 = maxX;
        }
    }
    
    public void setMinX(int minX){
        if(x1 <= x2){
            x1 = minX;
        } else {
            x2 = minX;
        }
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getMinY() {
        return Math.min(y1, y2);
    }
    
    public void setMinY(int minY){
        if(y1 < y2){
            y1 = minY;
        }else{
            y2 = minY;
        }
    }
    
    public void setMaxY(int maxY){
        if(y1 > y2){
            y1 = maxY;
        }else{
            y2 = maxY;
        }
    }

    public int getMaxY() {
        return Math.max(y1, y2);
    }

    public int width() {
        return Math.abs(x1 - x2) + 1;
    }

    public int height() {
        return Math.abs(y1 - y2) + 1;
    }

    public void translate(int dx, int dy) {
        x1 = x1 + dx;
        x2 = x2 + dx;
        y1 = y1 + dy;
        y2 = y2 + dy;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(x1);
        ret.append(',');
        ret.append(y1);
        ret.append(';');
        ret.append(x2);
        ret.append(',');
        ret.append(y2);
        return ret.toString();
    }
}
