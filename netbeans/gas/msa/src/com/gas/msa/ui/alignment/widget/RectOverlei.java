/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.light.IPaintable;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author dq
 */
public class RectOverlei implements IPaintable{

    private Rectangle2D.Double rect;
    private Integer totalPos;
    private Loc selection;
    private float whiteness = 0.8f;
    private float alpha = 0.3f;
    private Color color = new Color(whiteness, whiteness, whiteness, alpha);
    private Color borderColor = new Color(whiteness * 0.8f,whiteness * 0.8f,whiteness * 0.8f);

    public void setTotalPos(Integer totalPos) {
        this.totalPos = totalPos;
    }

    public void setSelection(Loc selection) {
        this.selection = selection;
    }    
    
    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect) {
        this.paint(g, rect, 1, totalPos);
    }
    
    @Override
    public void paint(Graphics2D g, int startPos, int endPos){
        paint(g, this.rect, startPos, endPos);
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect, int startPos, int endPos) {
        if(totalPos == null || selection == null || selection.getEnd() == null || selection.getStart() == null){
            return ;
        }
        if(rect == null){
            throw new IllegalArgumentException();
        }
        this.rect = rect;
        final double unitWidth = rect.getWidth() / totalPos ;
        
        Color oldColor = g.getColor();
                
        g.setColor(color);
        g.fill(new Rectangle2D.Double(rect.getX(), rect.getY(), unitWidth * (selection.getStart() - 1), rect.getHeight()));        
        final double x = rect.getX() + unitWidth * (selection.getEnd());
        g.fill(new Rectangle2D.Double(x , rect.getY(), rect.getWidth() - x, rect.getHeight()));
        
        // draw the borders                
        g.setColor(borderColor);
        int xBorder1 = MathUtil.round(rect.getX() + unitWidth * (selection.getStart() - 1));
        g.drawLine(xBorder1, (int)rect.getY(), xBorder1, (int)(rect.getY() + rect.getHeight()));
        int xBorder2 = MathUtil.round(rect.getX() + unitWidth * selection.getEnd());
        g.drawLine(xBorder2, (int)rect.getY(), xBorder2, (int)(rect.getY() + rect.getHeight()));
        g.setColor(oldColor);
    }

    @Override
    public Rectangle2D.Double getRect() {
        return rect;
    }

    @Override
    public void setRect(Rectangle2D.Double rect) {
        this.rect = rect;
    }
    
}
