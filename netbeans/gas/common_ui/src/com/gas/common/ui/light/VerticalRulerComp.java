/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.linePlot.LinePlotCompMap;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class VerticalRulerComp extends JComponent {

    private VerticalRuler verticalRuler = new VerticalRuler();

    @Override
    public void paintComponent(Graphics g) {
        final Dimension size = getSize();
        if (size.width <= 0 || size.height <= 0) {
            return;
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        verticalRuler.paint(g2d, new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight()));
    }

    protected void setVerticalRuler(VerticalRuler verticalRuler) {
        this.verticalRuler = verticalRuler;
    }

    public static VerticalRulerComp createPredefined(String name) {
        VerticalRulerComp ret = new VerticalRulerComp();
        if (name.equals(LinePlotCompMap.GC)) {
            VerticalRuler vRuler = VerticalRuler.createPredefined(name);
            ret.setVerticalRuler(vRuler);
        }
        return ret;
    }
}
