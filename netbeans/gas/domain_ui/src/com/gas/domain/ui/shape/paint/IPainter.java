/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.paint;

import com.gas.domain.ui.shape.IShape;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author dq
 */
public interface IPainter {
    String getFetureType();
    boolean isDefault();
    void paint(IShape shape, Graphics2D g2d);
}
