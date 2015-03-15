/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import java.awt.Rectangle;
import java.util.Comparator;
import javax.swing.SwingConstants;

/**
 *
 * @author dunqiang
 */
public class RectangleComparator implements Comparator<Rectangle> {

    private int orientation;
    
    public RectangleComparator(){
        this(SwingConstants.HORIZONTAL);
    }
    
    /**
     * SwingConstants.HORIZONTAL or SwingConstants.VERTICAL
     */
    public RectangleComparator(int orientation){
        this.orientation = orientation;
    }
    
    @Override
    public int compare(Rectangle o1, Rectangle o2) {
        int ret = 0;
        Integer x1 = o1.x;
        Integer y1 = o1.y;
        
        Integer x2 = o2.x;
        Integer y2 = o2.y;
        
        if(orientation == SwingConstants.HORIZONTAL){
            ret = x1.compareTo(x2);
        }else if(orientation == SwingConstants.VERTICAL){
            ret = y1.compareTo(y2);
        }
        
        return ret;
    }
}
