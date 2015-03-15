/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class CircularRulerLabelList extends ArrayList<CircularRulerLabel> {
    
    public boolean intersects(Polygon polygon){
        boolean ret = false;
        Iterator<CircularRulerLabel> itr = iterator();
        while(itr.hasNext()){
            CircularRulerLabel label = itr.next();
            ret = label.intersects(polygon);
            if(ret){
                return ret;
            }
        }
        return ret;
    }
    
    public void removeOverlappedLabels(){
        for(int i = 0; i < size(); i++){
            CircularRulerLabel label = get(i);
            for(int j = i + 1; j < size(); j++){
                CircularRulerLabel labelJ = get(j);
                if(label.intersects(labelJ)){
                    remove(j);
                    j--;
                }
            }
        }
    }
    
    public void remove(Integer pos){
        Iterator<CircularRulerLabel> itr = iterator();
        while(itr.hasNext()){
            CircularRulerLabel label = itr.next();
            Integer p = (Integer)label.getData();
            if(p.intValue() == pos.intValue()){
                itr.remove();
            }
        }
    }
    
    public void remove(Polygon pt){
        Iterator<CircularRulerLabel> itr = iterator();
        while(itr.hasNext()){
            CircularRulerLabel label = itr.next();           
            if(label.intersects(pt)){
                itr.remove();
            }
        }
    }        
    
    public CircularRulerLabel last(){
        CircularRulerLabel ret = null;
        if(!isEmpty()){
            ret = get(size() - 1);
        }
        return ret;
    }
    
    public void paint(Graphics2D g2d){
        Iterator<CircularRulerLabel> itr = iterator();
        while(itr.hasNext()){
            CircularRulerLabel label = itr.next();
            label.paint(g2d);
        }
    }
}
