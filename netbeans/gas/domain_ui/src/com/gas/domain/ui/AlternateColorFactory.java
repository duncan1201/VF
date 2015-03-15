/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui;

import java.awt.Color;

/**
 *
 * @author dunqiang
 */
public class AlternateColorFactory {

    private int counter = 0;
    private Color oddColor = null;
    private Color evenColor = null;

    public AlternateColorFactory() {
        this(null, null);
    }

    public AlternateColorFactory(Color odd, Color even) {
        this.oddColor = odd;
        this.evenColor = even;
    }

    public Color getEvenColor() {
        return evenColor;
    }

    public void resetCounter() {
        counter = 0;
    }

    public int getCounter() {
        return counter;
    }

    public void setEvenColor(Color evenColor) {
        this.evenColor = evenColor;
    }

    public Color getOddColor() {
        return oddColor;
    }

    public void setOddColor(Color oddColor) {
        this.oddColor = oddColor;
    }

    public Color getColor() {
        if (oddColor == null || evenColor == null) {
            throw new IllegalArgumentException("Must set oddColor and evenColor");
        }
        Color ret = null;
        if (counter % 2 == 0) {
            ret = oddColor;
        } else {
            ret = evenColor;
        }
        counter++;
        return ret;
    }
}
