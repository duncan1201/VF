/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.IVisibleLocProvider;
import com.gas.common.ui.core.BooleanList;
import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.tooltip.JTTComponent;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 *
 * @author dq
 */
public class Ring extends JTTComponent {

    private static final int START_ADJUSTMENT = 90;
    private BooleanList forwards;
    private FloatList startAngles;
    protected FloatList extents;
    private BooleanList fuzzyStarts;
    private BooleanList fuzzyEnds;
    private Integer ringThickness;
    private Color seedColor;
    private boolean selected;
    private Object data;
    // for internal use only
    private boolean mouseIn = false;
    private GeneralPathList path;
    private float startOffset = 0;
    private final float widthMultiplier = 1.2f;
    private IVisibleLocProvider visibleLocProvider;

    public Ring() {
        hookupListeners();
    }

    private void hookupListeners() {
    }

    @Override
    public Font getFont() {
        Font ret = super.getFont();
        if (ret == null) {
            ret = FontUtil.getDefaultSansSerifFont();
        }
        return ret;
    }

    public boolean isMouseIn() {
        return mouseIn;
    }

    public void setMouseIn(boolean mouseIn) {
        boolean old = this.mouseIn;
        this.mouseIn = mouseIn;
        firePropertyChange("mouseIn", old, this.mouseIn);
    }

    /*
     * return 0-90: NORTH_EAST; 90-180: SOUTH_EAST; 180-270: SOUTH_WEST; 270-360: NORTH_WEST    
     */
    public Float getCenterAngle() {
        final Float startAngle = getStartAngles().first();
        final Float endAngle = getStartAngles().last() + getExtents().last();
        Float ret = UIUtil.getCenterAngle(startAngle, endAngle).floatValue() + getStartOffset();
        return ret;
    }

    public Point getStartPt() {
        Rectangle bounds = getBounds();
        final Point center = new Point(Math.round(bounds.width * 0.5f), Math.round(bounds.height * 0.5f));
        final Float angle = getCenterAngle();
        final AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle), center.getX(), center.getY());
        Integer _ringThickness = getRingThickness();
        Point p = new Point(center.x, Math.round(_ringThickness * 0.25f));
        Point newP = UIUtil.transform(tx, p);
        newP.translate(bounds.x, bounds.y);
        return newP;
    }

    public float getStartOffset() {
        return startOffset;
    }

    /**
     * @param startOffset starts from 12 o'clock, clockwise
     */
    public void setStartOffset(float startOffset) {
        float old = this.startOffset;
        this.startOffset = startOffset;
        firePropertyChange("startOffset", old, this.startOffset);
    }

    public Color getSeedColor() {
        return seedColor;
    }

    public void setSeedColor(Color seedColor) {
        this.seedColor = seedColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    protected boolean isWithin(Point point) {
        //if (boundsPath == null) {
        final Point location = getLocation();
        final Dimension size = getSize();
        final double radius = size.width * 0.5;
        final Point2D center = new Point2D.Double(size.width * 0.5 + location.getX(), size.height * 0.5 + location.getY());

        // test the distance to the center first
        double distance = center.distance(point);
        Integer _ringThickness = getRingThickness();
        boolean ret = radius - _ringThickness < distance && distance < radius;
        if (ret) {
            // test the angle now
            Float startAngle = startAngles.first();
            Float endAngle = startAngles.last() + extents.last();
            // spans the full circle
            if (endAngle - startAngle == 360) {
                ret = true;
            } else {
                Double angle = MathUtil.getAngleInDegrees(point, center);
                angle = MathUtil.normalizeDegree(90 - angle);
                Float startAngleWithOffset = startAngle + startOffset;
                startAngleWithOffset = MathUtil.normalizeDegree(startAngleWithOffset).floatValue();
                Float endAngleWithOffset = endAngle + startOffset;
                endAngleWithOffset = MathUtil.normalizeDegree(endAngleWithOffset).floatValue();
                ret = LocUtil.contains(startAngleWithOffset, endAngleWithOffset, angle);
            }
        }
        //}
        return ret;
    }

    public Integer getRingThickness() {
        if (ringThickness == null) {
            Integer tmp = calculateRingThickness();
            setRingThickness(tmp);
        }
        return ringThickness;
    }

    private Integer calculateRingThickness() {
        Integer ret = null;
        FontMetrics fm = FontUtil.getFontMetrics(this);
        ret = Math.round(fm.getHeight() * 0.7f);
        return ret;
    }

    public void setRingThickness(Integer ringWidth) {
        Integer old = this.ringThickness;
        this.ringThickness = ringWidth;
        firePropertyChange("ringThickness", old, this.ringThickness);
    }

    public FloatList getExtents() {
        return extents;
    }

    /**
     * @param extents clockwise
     */
    public void setExtents(FloatList extents) {
        FloatList old = this.extents;
        this.extents = extents;
        firePropertyChange("extents", old, this.extents);
    }

    public FloatList getStartAngles() {
        return startAngles;
    }

    public BooleanList getFuzzyStarts() {
        return fuzzyStarts;
    }

    public void setFuzzyStarts(BooleanList fuzzyStarts) {
        this.fuzzyStarts = fuzzyStarts;
    }

    public BooleanList getFuzzyEnds() {
        return fuzzyEnds;
    }

    public void setFuzzyEnds(BooleanList fuzzyEnds) {
        this.fuzzyEnds = fuzzyEnds;
    }

    /**
     * @return startAngle starts from 12-o'clock, clockwise, normalized
     */
    public void setStartAngles(FloatList startAngle) {
        FloatList old = this.startAngles;
        this.startAngles = startAngle;
        firePropertyChange("startAngles", old, this.startAngles);
    }

    public BooleanList getForwards() {
        return forwards;
    }

    public void setForwards(BooleanList forwards) {
        BooleanList old = this.forwards;
        this.forwards = forwards;
        firePropertyChange("forwards", old, this.forwards);
    }

    protected void paint2(Graphics2D g2d) {
        if (visibleLocProvider == null) {
            visibleLocProvider = UIUtil.getParent(this, IVisibleLocProvider.class);
        }
        if (visibleLocProvider != null) {
            Loc visibleLoc = visibleLocProvider.calculateVisibleLoc();
            if (visibleLoc == null) {
                return;
            }
            Feture feture = (Feture) data;
            int min = feture.getLucation().getStart();
            int max = feture.getLucation().getEnd();
            boolean overlapped = visibleLoc.isOverlapped(min, max);
            if (!overlapped) {
                return;
            }
        }
        Rectangle drawingBounds = getBounds();
        if (drawingBounds.width <= 0 || drawingBounds.height <= 0) {
            return;
        }

        AffineTransform old = g2d.getTransform();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawingBounds.height = drawingBounds.width = Math.min(drawingBounds.width, drawingBounds.height);

        if (selected) {
            drawSelectedBackground(g2d, drawingBounds);
        }

        RingPathList ringPathList = createPath(drawingBounds);
        path = ringPathList.getRings();

        initPaint(g2d);
        //g2d.fill(path);
        path.setFill(true);
        path.paint(g2d);

        GeneralPathList connectors = ringPathList.createConnectors(true);
        connectors.setFill(false);
        connectors.paint(g2d);

        g2d.setTransform(old); // restore old tranform   
    }

    private void drawSelectedBackground(Graphics2D g2d, Rectangle drawingBounds) {
        Color darker = ColorUtil.changeSB(seedColor, 1.4f, 0.7f);
        Color ligher = ColorUtil.changeSB(seedColor, 1.0f, 1.3f);
        GeneralPath path = createBoundsPath(drawingBounds);
        g2d.setColor(ligher);
        g2d.fill(path);
        g2d.setColor(darker);
        g2d.draw(path);
    }

    private void initPaint(Graphics2D g2d) {
        Rectangle outterBounds = getBounds();
        float radius = MathUtil.round(outterBounds.width / 2.0f);
        Point2D center = new Point2D.Double(radius + outterBounds.getX(), radius + outterBounds.getX());
        float distCenter = 1 - getRingThickness() * 0.5f / radius;
        if (distCenter < 0) {
            distCenter = 0.5f;
        }
        float[] dist = {0.0f, distCenter, 1.0f};


        Color c2 = null;
        Color[] colors = new Color[3];

        c2 = ColorUtil.changeBrightness(seedColor, 0.6f);
        colors[1] = seedColor;

        colors[0] = colors[2] = c2;

        RadialGradientPaint p =
                new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(p);
    }

    private GeneralPath createBoundsPath(Rectangle outerBounds) {
        Float startAngle = startAngles.first();
        startAngle = MathUtil.normalizeDegree(startAngle).floatValue();
        Float endAngle = startAngles.last() + extents.last();
        endAngle = MathUtil.normalizeDegree(endAngle).floatValue();

        Float pathStartAngle = -startAngle + START_ADJUSTMENT - startOffset;
        pathStartAngle = MathUtil.normalizeDegree(pathStartAngle).floatValue();

        final Float distance = MathUtil.getDistance(startAngle, endAngle, true).floatValue();
        RingPath ringPath = RingPathCreator.create(outerBounds, getRingThickness(), null, pathStartAngle, distance, false, false);
        GeneralPath ret = ringPath.getPath();
        return ret;
    }

    private RingPathList createPath(Rectangle outerBounds) {
        RingPathList ringPathList = new RingPathList();
        for (int i = 0; i < startAngles.size(); i++) {
            RingPath ringPath = RingPathCreator.create(outerBounds, getRingThickness(), forwards.get(i), -startAngles.get(i) + START_ADJUSTMENT - startOffset, extents.get(i), fuzzyStarts.get(i), fuzzyEnds.get(i));
            ringPathList.add(ringPath);
        }
        return ringPathList;
    }
}
