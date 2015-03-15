/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.util.MathUtil;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**

 @author dq
 */
public class RingPath {    
    private Point2D center;
    private GeneralPath path;
    private Float radius;
    private double startAngle;
    private double extent;

    public double getStartAngle() {
        return startAngle;
    }

    /**
    @param startAngle counterclockwise, starts from 3 o'clock, 0-360
    */
    public void setStartAngle(double startAngle) {
        MathUtil.normalizeDegree(startAngle);
        this.startAngle = startAngle;
    }

    public double getExtent() {
        return extent;
    }

    /**
    @param extent clockwise
    */
    public void setExtent(double extent) {
        this.extent = extent;
    }

    public GeneralPath getPath() {
        return path;
    }        

    public void setPath(GeneralPath path) {
        this.path = path;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }   

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }
    
}
