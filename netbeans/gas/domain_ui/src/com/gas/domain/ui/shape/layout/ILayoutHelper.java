/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.layout;

import com.gas.domain.ui.shape.Arrow;
import com.gas.domain.ui.shape.IShape;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

/**
 *
 * @author liaodu
 */
public interface ILayoutHelper {
    Line2D.Float getDesiredVerticalLine(Arrow arrow, IShape.TEXT_LOC txtLoc, Rectangle drawingRect, Integer totalPos);
    Integer getDesiredStartPixel(IShape arrow, IShape.TEXT_LOC txtLoc, Rectangle drawingRect, Integer totalPos);
    int getDesiredWidth(IShape arrow, IShape.TEXT_LOC txtLoc, Rectangle drawingRect, Integer totalPos);
    Rectangle getTextDrawingRectangle(IShape arrow);
    Rectangle getShapeDrawingRectangle(IShape arrow);
    int getMinimumHeight(IShape arrow);
    Dimension getMinTextDimension(IShape arrow);
    
    int getDesiredHeight(IShape arrow);
    
    String getFetureType();
    boolean isDefault();
    
    ILayoutHelper newInstance();
}
