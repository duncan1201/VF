/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.linePlot;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.light.IPaintable;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class LinePlot implements IPaintable {

    private Map<String, Color> colors = new HashMap<String, Color>();
    private Map<String, FloatList> plotData = new HashMap<String, FloatList>();
    private Map<String, Boolean> labelDisplays = new HashMap<String, Boolean>();
    private Rectangle2D.Double rect;
    private Color background;

    public void add(String name, FloatList list, Color c, boolean display) {
        plotData.put(name, list);
        colors.put(name, c);
        labelDisplays.put(name, display);
    }

    @Override
    public Rectangle2D.Double getRect() {
        return rect;
    }

    public void setRect(double x, double y, double width, double height) {
        Rectangle2D.Double rec = new Rectangle2D.Double(x, y, width, height);
        setRect(rec);
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    @Override
    public void setRect(Rectangle2D.Double rect) {
        this.rect = rect;
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect) {
        paint(g, rect, 1, plotData.size());
    }

    @Override
    public void paint(Graphics2D g, int startPos, int endPos) {
        paint(g, this.rect, startPos, endPos);
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect, int startPos, int endPos) {
        if (rect == null || plotData.isEmpty() || colors.isEmpty() || labelDisplays.isEmpty() || rect.getWidth() <= 0 || rect.getHeight() <= 0) {
            return;
        }

        if (background != null) {
            g.setColor(background);
            g.fillRect(0, 0, (int) Math.round(rect.getWidth()), (int) Math.round(rect.getHeight()));
        }

        g.setColor(Color.LIGHT_GRAY);
        g.drawLine((int) Math.round(rect.getX()), (int) Math.round(rect.getCenterY()), (int) Math.round(rect.getWidth()), (int) Math.round(rect.getCenterY()));

        this.rect = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        Iterator<String> itr = plotData.keySet().iterator();
        while (itr.hasNext()) {
            String name = itr.next();

            FloatList fList = plotData.get(name);
            Color color = colors.get(name);

            GeneralPath path = _createPath(fList, rect, startPos, endPos);
            g.setColor(color);
            g.draw(path);
        }
    }

    protected boolean isEmpty() {
        return plotData.isEmpty();
    }

    protected void clear() {
        plotData.clear();
        colors.clear();
        labelDisplays.clear();
    }

    private GeneralPath _createPath(FloatList list, Rectangle2D rect, int startPos, int endPos) {
        final double unitSpace = rect.getWidth() / list.size();
        GeneralPath ret;
        if (unitSpace > 1) {
            ret = _createPathIfRoom(list, rect, startPos, endPos);
        } else {
            ret = _createPathIfNoRoom(list, rect, startPos, endPos);
        }
        return ret;
    }

    private GeneralPath _createPathIfNoRoom(FloatList list, Rectangle2D rect, int startPos, int endPos) {
        GeneralPath ret = new GeneralPath();
        Float startX = UIUtil.toScreen(list.size(), startPos, Math.round(rect.getWidth()), 0, SwingConstants.LEFT);
        Float endX = UIUtil.toScreen(list.size(), endPos, Math.round(rect.getWidth()), 0, SwingConstants.LEFT);
        int totalPos = list.size();
        for (int x = startX.intValue(); x <= endX; x++) {
            int pos = UIUtil.toPos(x, rect.getWidth(), totalPos);
            pos = Math.min(pos, endPos);
            Float flt = list.get(pos - 1);
            if (flt != null) {
                double y = rect.getHeight() - flt * rect.getHeight();

                if (ret.getCurrentPoint() == null) {
                    ret.moveTo(x, y);
                } else {
                    ret.lineTo(x, y);
                }
            }
        }
        return ret;
    }

    private GeneralPath _createPathIfRoom(FloatList list, Rectangle2D rect, int startPos, int endPos) {
        final double unitSpace = rect.getWidth() / list.size();
        GeneralPath ret = new GeneralPath();
        for (int pos = startPos; pos < Math.min(list.size(), endPos); pos++) {
            Float f = list.get(pos - 1);
            if (f == null) {
                continue;
            }
            double x1 = (pos - 1) * unitSpace + rect.getX();
            double y = rect.getHeight() - f * rect.getHeight();
            y = Math.min(rect.getHeight() - 1, y);
            double x2 = pos * unitSpace + rect.getX();

            Line2D line = new Line2D.Double(x1, y, x2, y);
            if (ret.getCurrentPoint() == null) {
                ret.moveTo(line.getX1(), line.getY1());
            }
            ret.append(line, true);
        }
        return ret;
    }
}
