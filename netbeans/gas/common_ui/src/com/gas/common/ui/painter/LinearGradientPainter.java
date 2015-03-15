/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author dunqiang
 */
public class LinearGradientPainter implements Painter<JComponent> {

    private LinearGradientPaint paint;
    private boolean vertical = true;
    private float[] fractions;
    private Color[] colors;
    private CycleMethod cycleMethod;
    private GeneralPath generalPath;

    public LinearGradientPainter() {
        this(null, null);
    }

    public LinearGradientPainter(float[] fractions, Color[] colors) {
        this(true, fractions, colors, CycleMethod.NO_CYCLE);
    }

    public LinearGradientPainter(boolean vertical, float[] fractions, Color[] colors) {
        this(vertical, fractions, colors, CycleMethod.NO_CYCLE);
    }

    public LinearGradientPainter(float[] fractions, Color[] colors,
            CycleMethod cycleMethod) {
        this(true, fractions, colors, cycleMethod);
    }

    public LinearGradientPainter(boolean vertical, float[] fractions, Color[] colors,
            CycleMethod cycleMethod) {
        this(vertical, fractions, colors, cycleMethod, null);
    }

    public LinearGradientPainter(boolean vertical, float[] fractions, Color[] colors,
            CycleMethod cycleMethod, GeneralPath generalPath) {
        this.vertical = vertical;
        this.fractions = fractions;
        this.colors = colors;
        this.cycleMethod = cycleMethod;
        this.generalPath = generalPath;
    }

    public void setColors(Color... colors) {
        this.colors = colors;
    }

    public void setCycleMethod(CycleMethod cycleMethod) {
        this.cycleMethod = cycleMethod;
    }

    public void setFractions(float... fractions) {
        this.fractions = fractions;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public void setGeneralPath(GeneralPath generalPath) {
        this.generalPath = generalPath;
    }

    @Override
    public void paint(Graphics2D g, JComponent object, int width, int height) {
        paint(g, object, width, height, true);
    }
    
    
    public void paint(Graphics2D g, JComponent object, int width, int height, boolean fill) {
        if (paint == null) {
            if (vertical) {
                paint = new LinearGradientPaint(0, 0, 0, height, fractions, colors, cycleMethod);
            } else {
                paint = new LinearGradientPaint(0, 0, width, 0, fractions, colors, cycleMethod);
            }
        }
        g.setPaint(paint);
        if (generalPath == null) {
            if(fill){
                g.fillRect(0, 0, width, height);                
            }else{
                g.drawRect(0, 0, width, height);
            }
        } else {
            if(fill){
                g.fill(generalPath);
            }else{
                g.draw(generalPath);
            }
        }
    }
}