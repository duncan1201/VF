/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.histogram;

import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import org.jdesktop.swingx.color.ColorUtil;

/**
 *
 * @author dq
 */
public class Histogram extends JComponent {

    private Color from = ColorCnst.MAROON;
    private Color to = ColorCnst.AO;
    private List<Integer> data = new ArrayList<Integer>();

    public Histogram() {
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String pName = evt.getPropertyName();
                if (pName.equals("data")) {
                    repaint();
                } else if (pName.equals("fill") || pName.equals("fillColor")) {
                    repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        if (data.isEmpty()) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();
        Insets insets = getInsets();

        final Integer MAX = MathUtil.max(data);

        int totalPos = data.size();
        int totalDisplayWidth = size.width - insets.left - insets.right;
        int totalDisplayHeight = size.height - insets.bottom - insets.top;
        for (int i = 0; i < data.size(); i++) {
            Integer d = data.get(i);
            if (d == null) {               
                continue;
            }
            float x = UIUtil.toScreen(totalPos, i + 1, totalDisplayWidth, insets.left, SwingConstants.LEFT);
            float xNext = UIUtil.toScreen(totalPos, i + 2, totalDisplayWidth, insets.left, SwingConstants.LEFT);

            float y = UIUtil.toScreen(MAX, d, totalDisplayHeight, insets.top, SwingConstants.CENTER);
            y = Math.min(totalDisplayHeight - 1, y);
            
            Rectangle2D r = new Rectangle2D.Float(x, y, xNext - x, totalDisplayHeight - y);            
            Color iColor = ColorUtil.interpolate(from, to, d * 1.0f /MAX);
            g2d.setColor(iColor);
            g2d.fill(r);
        }
    }

    public void setData(List<Integer> data) {
        List<Integer> old = this.data;
        this.data = data;
        firePropertyChange("data", old, this.data);
    }

    public int getDesiredHeight() {
        return 40;
    }
}
