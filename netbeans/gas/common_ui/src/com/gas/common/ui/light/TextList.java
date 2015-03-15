/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.MathUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class TextList extends ArrayList<Text> {

    private Rectangle2D.Double rect;
    private boolean vertical;
    private Color bgColor = ColorCnst.GRAY_250;
    private Color fgColor = Color.BLACK;

    public void paint(Graphics2D g) {
        paint(g, SwingConstants.CENTER);
    }
    
    public Integer getMinX(){
        Integer ret = null;
        for(Text t: this){
            if (ret == null || ret > t.getX()){
                ret = t.getX();
            }
        }
        return ret;
    }
    
    public Integer getMinY(){
        Integer ret = null;
        for(Text t: this){
            if (ret == null || ret > t.getY()){
                ret = t.getY();
            }
        }
        return ret;        
    }
    
    public Integer getMaxY(){
        Integer ret = null;
        for(Text t: this){
            if (ret == null || ret < t.getY() + t.getHeight()){
                ret = t.getY() + t.getHeight();
            }
        }
        return ret;        
    }    
    
    public Integer getMaxX(){
        Integer ret = null;
        for(Text t: this){
            int tmp = t.getX() + t.getWidth();
            if(ret == null || ret < tmp){
                ret = tmp;
            }
        }
        return ret;
    }
    
    public void translate(int deltaX, int deltaY){
        for(Text t: this){
            t.setX(t.getX() + deltaX);
            t.setY(t.getY() + deltaY);
        }
    }
    
    public Integer getHeight(){
        int minY = getMinY();
        int maxY = getMaxY();
        return maxY - minY;
    }
    
    public Integer getWidth(){
        int minX = getMinX();
        int maxX = getMaxX();
        int ret = maxX - minX;
        return ret;
    }
    
    public Rectangle calculateBounds(){
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        
        Rectangle ret = new Rectangle(minX, minY, maxX - minX, maxY - minY);        
        return ret;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }
    
    public String getLongest(){
        String ret = null;
        for(Text t: this){
            String s = t.getStr();
            if(ret == null || ret.length() < s.length()){
                ret = s;
            }
        }
        return ret;
    }
    
    public void paint(Graphics2D g, int horizontalAlignment) {
        this.paint(g, horizontalAlignment, true);
    }
    
    public void paint(Graphics2D g, int horizontalAlignment, boolean autosetColor) {
        for (int i = 0; i < size(); i++) {
            Text text = get(i);
            if(autosetColor){
                text.setBgColor(text.isSelected() ? ColorCnst.SELECTED_TEXT_BG : bgColor);
                if(text.isSelected()){
                    text.setFgColor(Color.WHITE);
                } else {
                    if(fgColor != null){
                        text.setFgColor(fgColor);
                    }else{
                        text.setFgColor(Color.BLACK);
                    }
                }
            }
            text.paint(g, horizontalAlignment);
        }
    }
    
    public void setFont(Font font){
        for(Text text: this){
            text.setFont(font);
        }
    }

    public StringList getStrs() {
        StringList ret = new StringList();
        for (Text text : this) {
            ret.add(text.getStr());
        }
        return ret;
    }

    /**
     * @param rowName: the selected rowName; could be null
     */
    public void setSelected(String rowName) {
        for (Text text : this) {
            text.setSelected(text.getStr().equals(rowName));
        }
    }

    public void setSelected(Text t) {
        for (Text text : this) {
            text.setSelected(text == t);
        }
    }

    /**
     * @param pt: relative to its hosting parent
     */
    public Text getText(Point pt) {
        Text ret = null;
        for (Text text : this) {
            if (text.getRect().contains(pt)) {
                ret = text;
                break;
            }
        }
        return ret;
    }

    public Rectangle2D.Double getRect() {
        return rect;
    }

    public void setRect(Rectangle2D.Double rect) {
        this.rect = rect;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public void layout() {
        if (vertical) {
            layoutVertical();
        } else {
            layoutHorizontal();
        }
    }
    
    public boolean isRectSet(){
        return rect != null;
    }

    void layoutVertical() {        
        double unitHeight = rect.height / size();
        int y = 0;
        for (int i = 0; i < size(); i++) {
            Text text = get(i);
            text.setX(0);
            text.setWidth(MathUtil.round(rect.getWidth()));
            text.setY(y);
            text.setHeight(MathUtil.round(unitHeight));
            y += unitHeight;
        }
    }

    void layoutHorizontal() {
    }
}
