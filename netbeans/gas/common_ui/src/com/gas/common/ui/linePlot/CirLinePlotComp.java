/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.linePlot;

import com.gas.common.ui.IVisibleLocProvider;
import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class CirLinePlotComp extends JComponent {

    private CirLinePlot cirLinePlot = new CirLinePlot();

    @Override
    public void paintComponent(Graphics g) {
        final Dimension size = getSize();
        if (size.getWidth() <= 0 || size.getHeight() <= 0) {
            return;
        }
        IVisibleLocProvider provider = UIUtil.getParent(this, IVisibleLocProvider.class);
        Loc visibleLoc = provider.calculateVisibleLoc();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        cirLinePlot.paint(g2d, new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight()), visibleLoc.getStart(), visibleLoc.getEnd());

    }

    public void addPlot(String name, FloatList data, Color color) {
        cirLinePlot.addPlot(name, data, color);
    }

    public void clear() {
        cirLinePlot.clear();
    }
}
