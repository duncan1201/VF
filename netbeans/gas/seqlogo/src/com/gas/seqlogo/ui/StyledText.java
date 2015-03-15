/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.seqlogo.ui;

import com.gas.common.ui.util.FontUtil;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author dq
 */
public class StyledText {
    private TextLayout textLayout;
    private Float x;
    private Float y;
    private Float width;
    private Float height;
    private String text;
    private Color color;
    
    public StyledText(){
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }
    
    public void paint(Graphics2D g){
        if(x == null || y == null || width == null || height == null || text == null || text.isEmpty()){
            return ;
        }                
        //test();
        textLayout = createTextLayout();        
        
        Shape shape = textLayout.getOutline(null);        
        Rectangle2D rect = shape.getBounds2D();
        double sx = width / rect.getWidth();
        double sy = height / rect.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(sx, sy);        
        Shape scaledShape = at.createTransformedShape(shape);       
        Rectangle2D scaledRect = scaledShape.getBounds2D();
        
        double tx = x - scaledRect.getX();
        double ty = y + scaledRect.getHeight();
        AffineTransform at2 = AffineTransform.getTranslateInstance(tx, ty);        
        Shape tShape = at2.createTransformedShape(scaledShape);       
        
        g.setColor(Color.BLACK);
        g.fill(tShape);
    }
    
    private void test(Graphics2D g){
        if(text.equalsIgnoreCase("A")){
            g.setColor(Color.GREEN);
        }else if(text.equalsIgnoreCase("C")){
            g.setColor(Color.DARK_GRAY);
        }if(text.equalsIgnoreCase("T")){
            g.setColor(Color.RED);
        }if(text.equalsIgnoreCase("G")){
            g.setColor(Color.YELLOW);
        }
        g.drawRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));        
        
    }
    
    private TextLayout createTextLayout(){
        
        TextLayout ret = new TextLayout(text, new Font("Helvetica", Font.PLAIN, 16), new FontRenderContext(null, true, false));
        return ret;
    }
    
    public int getTextLength(){
        return text == null ? 0 : text.length();
    }
        
}
