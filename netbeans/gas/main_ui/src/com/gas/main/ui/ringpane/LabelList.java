/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.domain.core.as.Feture;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class LabelList extends ArrayList<Label> {
    
    public void paint(Graphics2D g){
        for(int i = 0; i < size(); i++){
            Label label = get(i);
            label.paint2(g);
        }
    }
    
    public <T> List<T> getData(Class<T> clazz){
        List<T> ret = new ArrayList<T>();
        for(int i = 0; i < size(); i++){
            Label label = get(i);
            Object data = label.getData();
            ret.add((T)data);
        }
        return ret;
    }
    
    public void setFontSize(Float size){
        for(int i = 0; i < size(); i++){
            Label label = get(i);
            Font font = label.getTextFont();
            Font newFont = font.deriveFont(size);
            label.setTextFont(newFont);
        }        
    }
    
    public Label updateSelection(Feture feture){
        Label ret = null;
        Iterator<Label> itr = iterator();
        while(itr.hasNext()){
            Label label = itr.next();
            Ring ring = (Ring)label.getData();
            Feture data = (Feture)ring.getData();
            if(data == feture){
                ret = label;
            }
            label.setSelected(data == feture);
        }
        return ret;
    }
    
    public LabelList getByText(String text){
        LabelList ret = new LabelList();
        for(Label label: this){
            if(label.getText().equalsIgnoreCase(text)){
                ret.add(label);
            }
        }
        return ret;
    }
    
    public Label updateSelection(Point pt){
        Label ret = null;
        for(int i = 0; i < size(); i++){
            Label label = get(i);
            Rectangle bounds = label.getBounds();
            boolean contains = bounds.contains(pt);
            label.setSelected(contains);
            if(contains){
                ret = label;
            }
        }
        return ret;
    }
    
    public Label getSelected(){
        Label ret = null;
        Iterator<Label> itr = iterator();
        while(itr.hasNext()){
            Label label = itr.next();
            if(label.isSelected()){
                ret = label;
                break;
            }
        }
        return ret;
    }
   
    public Label getLabel(Ring ring){
        Label ret = null;
        Iterator<Label> itr = iterator();
        while(itr.hasNext()){
            Label label = itr.next();
            Object data = label.getData();
            if(ring == data){
                ret = label;
                break;
            }
        }
        return ret;
    }
    
    public Label getLabel(Point pt){
        Label ret = null;
        for(int i = 0; i < size(); i++){
            Label label = get(i);
            Rectangle b = label.getBounds();
            if(b.contains(pt)){
                ret = label;
                break;
            }
        }
        return ret;
    }
    
    protected void setSelected(Label label){
        Iterator<Label> itr = iterator();
        while (itr.hasNext()) {
            Label l = itr.next();
            l.setSelected(l == label);
        }        
    }
}
