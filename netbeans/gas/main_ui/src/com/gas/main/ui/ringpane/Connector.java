/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class Connector extends JComponent {

    private Line2D line;
    private Object data;
    private boolean selected;

    public Connector() {
        hookupListener();
    }

    private void hookupListener() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Line2D getLine() {
        return line;
    }

    public void setLine(Line2D line) {
        this.line = line;
        int width = MathUtil.round(Math.abs(line.getP1().getX() - line.getP2().getX()) + 1);
        int height = MathUtil.round(Math.abs(line.getP1().getY() - line.getP2().getY()) + 1);
        setSize(width, height);
        int x = MathUtil.round(Math.min(line.getP1().getX(), line.getP2().getX()));
        int y = MathUtil.round(Math.min(line.getP1().getY(), line.getP2().getY()));
        setLocation(x, y);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        paint2((Graphics2D) g);
    }

    protected void paint2(Graphics2D g2d) {
        final Dimension size = getSize();
        if (size.width < 0 || size.height < 0) {
            return;
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke old = g2d.getStroke();

        final double x = line.getP1().getX();
        final double y = line.getP1().getY();
        final double x1 = line.getP2().getX();
        final double y1 = line.getP2().getY();


        // paint the border first
        g2d.setStroke(new BasicStroke(selected?4:3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        //GeneralPath borderPath = createBorder(new Line2D.Double(x, y, x1, y1), selected);
        g2d.setColor(Color.WHITE);
        //g2d.draw(borderPath);
        g2d.drawLine(MathUtil.round(x), MathUtil.round(y), MathUtil.round(x1), MathUtil.round(y1));
        
        // then the line because they might overlap
        if (selected) {
            BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2d.setStroke(stroke);
        } else {
            BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2d.setStroke(stroke);
        }

        g2d.setColor(getForeground());
        g2d.drawLine(MathUtil.round(x), MathUtil.round(y), MathUtil.round(x1), MathUtil.round(y1));

        g2d.setStroke(old);        
    }
}
