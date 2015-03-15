/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.linePlot;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.light.IPaintable;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class CirLinePlot implements IPaintable {

    private Rectangle2D.Double rect;
    private Map<String, FloatList> plotData = new HashMap<String, FloatList>();
    private Map<String, Color> colors = new HashMap<String, Color>();
    private Integer ringWidth;
    private float startOffset;

    public void addPlot(String name, FloatList data, Color color) {
        plotData.put(name, data);
        colors.put(name, color);
    }

    @Override
    public void paint(Graphics2D g, Double rect) {
        if (plotData.isEmpty()) {
            return;
        }
        final FloatList floatList = plotData.values().iterator().next();
        this.paint(g, rect, 1, floatList.size());
    }

    /**
     * @param startOffset starts from 12 o'clock, clockwise
     */
    public void setStartOffset(float startOffset) {
        this.startOffset = startOffset;
    }

    public boolean isEmpty() {
        return plotData.isEmpty();
    }

    public void clear() {
        plotData.clear();
        colors.clear();
    }

    private int getSize() {
        int ret = 0;
        if (!isEmpty()) {
            FloatList floatList = plotData.values().iterator().next();
            ret = floatList.size();
        }
        return ret;
    }

    private double getUnitWidth(Point2D center, double radius) {
        int totalPos = getSize();
        double startDeg = UIUtil.toDegree(1, totalPos, SwingConstants.LEFT);
        double endDeg = UIUtil.toDegree(1, totalPos, SwingConstants.RIGHT);
        Point2D ptStart = MathUtil.getCoordsDeg(center, radius, 90 - startDeg + startOffset);
        Point2D ptEnd = MathUtil.getCoordsDeg(center, radius, 90 - endDeg + startOffset);
        double unitWidth = Math.abs(ptEnd.getX() - ptStart.getX());
        return unitWidth;
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect, final int startPos, final int endPos) {
        if (isEmpty() || ringWidth == null) {
            return;
        }
        final Point2D.Double center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());

        final double RADIUS = rect.getWidth() * 0.5;

        double unitWidth = getUnitWidth(center, RADIUS);

        if (unitWidth >= 1.5) {
            _paintIfRoom(g, rect, center, RADIUS, startPos, endPos);
        } else {
            _paintIfNoRoom(g, rect, center, RADIUS, startPos, endPos);
        }
    }

    private void _paintIfNoRoom(Graphics2D g, Rectangle2D.Double rect, Point2D.Double center, double RADIUS, int startPos, int endPos) {
        Rectangle2D middle = UIUtil.shrink(rect, ringWidth * 0.5, ringWidth * 0.5);
        g.setColor(Color.GRAY);
        g.drawOval(MathUtil.round(middle.getX()), MathUtil.round(middle.getY()), MathUtil.round(middle.getWidth()), MathUtil.round(middle.getHeight()));

        final int totalPos = getSize();
        Iterator<String> keyItr = plotData.keySet().iterator();
        while (keyItr.hasNext()) {
            final String key = keyItr.next();
            final FloatList floatList = plotData.get(key);
            GeneralPath path = new GeneralPath();
            for (int pos = startPos; LocUtil.contains(startPos, endPos, pos);) {
                Float flt = floatList.get(pos - 1);
                if (flt == null) {
                    continue;
                }
                double radius = RADIUS - ringWidth * (1 - flt);

                double startDeg = UIUtil.toDegree(pos, totalPos, SwingConstants.LEFT);
                Point2D ptStart = MathUtil.getCoordsDeg(center, radius, 90 - startDeg + startOffset);

                if (path.getCurrentPoint() == null) {
                    path.moveTo(ptStart.getX(), ptStart.getY());
                } else {
                    path.lineTo(ptStart.getX(), ptStart.getY());
                }

                if (startPos <= endPos) {
                    pos++;
                } else {
                    if (pos == totalPos) {
                        pos = 1;
                    } else {
                        pos++;
                    }
                }
            }
            g.setColor(colors.get(key));
            g.draw(path);
        }
    }

    private void _paintIfRoom(Graphics2D g, Rectangle2D.Double rect, Point2D.Double center, double RADIUS, int startPos, int endPos) {
        Rectangle2D middle = UIUtil.shrink(rect, ringWidth * 0.5, ringWidth * 0.5);
        g.setColor(Color.GRAY);
        g.drawOval(MathUtil.round(middle.getX()), MathUtil.round(middle.getY()), MathUtil.round(middle.getWidth()), MathUtil.round(middle.getHeight()));

        final int totalPos = getSize();
        Iterator<String> keyItr = plotData.keySet().iterator();
        while (keyItr.hasNext()) {
            final String key = keyItr.next();
            final FloatList floatList = plotData.get(key);
            GeneralPath path = new GeneralPath();
            for (int pos = startPos; LocUtil.contains(startPos, endPos, pos);) {
                Float flt = floatList.get(pos - 1);
                if (flt == null) {
                    continue;
                }
                double radius = RADIUS - ringWidth * (1 - flt);

                double startDeg = UIUtil.toDegree(pos, totalPos, SwingConstants.LEFT);
                double endDeg = UIUtil.toDegree(pos, totalPos, SwingConstants.RIGHT);
                Point2D ptStart = MathUtil.getCoordsDeg(center, radius, 90 - startDeg + startOffset);
                Point2D ptEnd = MathUtil.getCoordsDeg(center, radius, 90 - endDeg + startOffset);

                if (path.getCurrentPoint() == null) {
                    path.moveTo(ptStart.getX(), ptStart.getY());
                } else {
                    path.lineTo(ptStart.getX(), ptStart.getY());
                }
                path.lineTo(ptEnd.getX(), ptEnd.getY());

                if (startPos <= endPos) {
                    pos++;
                } else {
                    if (pos == totalPos) {
                        pos = 1;
                    } else {
                        pos++;
                    }
                }
            }
            g.setColor(colors.get(key));
            g.draw(path);
        }
    }

    @Override
    public void paint(Graphics2D g, int startPos, int endPos) {
        this.paint(g, rect, startPos, endPos);
    }

    @Override
    public Rectangle2D.Double getRect() {
        return this.rect;
    }

    public void setPlotData(Map<String, FloatList> plotData) {
        this.plotData = plotData;
    }

    public void setRingWidth(Integer ringWidth) {
        this.ringWidth = ringWidth;
    }

    protected Integer getRingWidth() {
        return this.ringWidth;
    }

    @Override
    public void setRect(Rectangle2D.Double rect) {
        this.rect = rect;
    }
}
