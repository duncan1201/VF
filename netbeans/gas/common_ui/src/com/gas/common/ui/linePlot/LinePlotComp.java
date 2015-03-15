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
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class LinePlotComp extends JComponent {

    private LinePlot linePlot = new LinePlot();

    @Override
    public void paintComponent(Graphics g) {
        final Dimension size = getSize();
        if (size.width <= 0 || size.height <= 0) {
            return;
        }
        super.paintComponent(g);
        Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight());
        Graphics2D g2d = (Graphics2D) g;
        IVisibleLocProvider provider = UIUtil.getParent(this, IVisibleLocProvider.class);
        Loc visibleLoc = provider.calculateVisibleLoc();
        linePlot.paint(g2d, rect, visibleLoc.getStart(), visibleLoc.getEnd());
    }

    public void clear() {
        linePlot.clear();
    }

    public void addPlot(String name, FloatList list, Color c, boolean display) {
        linePlot.add(name, list, c, display);
    }

    public boolean isEmpty() {
        return linePlot.isEmpty();
    }

    public void createSampleData(int size) {
        FloatList floatList = new FloatList();
        for (int i = 0; i < size; i++) {
            floatList.add((float) Math.random());
        }
        linePlot.add("Sample", floatList, Color.red, true);
    }
}
