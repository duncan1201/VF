/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author dunqiang
 */
public class RadialGradientPainter implements Painter<JComponent> {

    private RadialGradientPaint paint;
    private float[] fractions;
    private Color[] colors;
    private CycleMethod cycleMethod;
    private GeneralPath generalPath;

    public RadialGradientPainter() {
        this(null, null);
    }

    public RadialGradientPainter(float[] fractions, Color[] colors) {
        this(true, fractions, colors, CycleMethod.NO_CYCLE);
    }

    public RadialGradientPainter(boolean vertical, float[] fractions, Color[] colors) {
        this(vertical, fractions, colors, CycleMethod.NO_CYCLE);
    }

    public RadialGradientPainter(float[] fractions, Color[] colors,
            CycleMethod cycleMethod) {
        this(true, fractions, colors, cycleMethod);
    }

    public RadialGradientPainter(boolean vertical, float[] fractions, Color[] colors,
            CycleMethod cycleMethod) {
        this(vertical, fractions, colors, cycleMethod, null);
    }

    public RadialGradientPainter(boolean vertical, float[] fractions, Color[] colors,
            CycleMethod cycleMethod, GeneralPath generalPath) {
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

    public void setGeneralPath(GeneralPath generalPath) {
        this.generalPath = generalPath;
    }

    @Override
    public void paint(Graphics2D g, JComponent object, int width, int height) {
        if (paint == null) {
            //paint = new RadialGradientPaint(0, 0, 0, height, fractions, colors, cycleMethod);         
        }
        g.setPaint(paint);
        if (generalPath == null) {
            g.fillRect(0, 0, width, height);
        } else {
            g.fill(generalPath);
        }
    }
}