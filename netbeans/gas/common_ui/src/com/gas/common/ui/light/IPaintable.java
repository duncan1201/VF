/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author dq
 */
public interface IPaintable {

    void paint(Graphics2D g, Rectangle2D.Double rect);

    void paint(Graphics2D g, Rectangle2D.Double rect, int startPos, int endPos);

    void paint(Graphics2D g, int startPos, int endPos);

    Rectangle2D.Double getRect();

    void setRect(Rectangle2D.Double rect);
}
