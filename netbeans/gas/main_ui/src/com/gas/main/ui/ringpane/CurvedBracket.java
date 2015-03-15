/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.IVisibleLocProvider;
import com.gas.common.ui.light.PolygonList;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RMap;
import com.gas.common.ui.util.Pref;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class CurvedBracket extends JComponent {

    private Float startOffset = 0f;
    private Float startAngle;
    private Float extent;
    private String txt;
    private Integer thickness;
    private Integer startPos;
    private Integer endPos;
    private Integer totalPos;
    private Object data;
    private IVisibleLocProvider visibleLocProvider;
    private final float widthMultiplier = 1.2f;
    private Color selectedColor = Color.BLUE;
    private Color color = Color.GRAY;
    private boolean selected;
    private Float ringUpperRadius;
    private Float ringLowerRadius;
    private GeneralPath boundsPath;

    public CurvedBracket() {
        setFont(FontUtil.getDefaultMSFont());
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new CurvedBracketListeners.PtyChangeListener());
        addComponentListener(new CurvedBracketListeners.CompAdpt());
    }

    public void setRingUpperRadius(Float ringUpperRadius) {
        this.ringUpperRadius = ringUpperRadius;
    }

    public void setRingLowerRadius(Float ringLowerRadius) {
        this.ringLowerRadius = ringLowerRadius;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    protected void paint2(Graphics g) {
        if (extent == null || startAngle == null || txt == null) {
            return;
        }
        if (visibleLocProvider == null) {
            visibleLocProvider = UIUtil.getParent(this, IVisibleLocProvider.class);
        }
        if (visibleLocProvider != null) {
            Loc visibleLoc = visibleLocProvider.calculateVisibleLoc();
            if (visibleLoc == null) {
                return;
            }
            boolean visible = visibleLoc.isOverlapped(startPos, endPos);
            if (!visible) {
                return;
            }
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(selected ? selectedColor : color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final Dimension size = getSize();
        if (size.width != size.height) {
            throw new IllegalArgumentException("The width of the bounds is not the same as its height");
        }

        GeneralPath path = createBracketPath();

        g2d.draw(path);

        PolygonList polygonList = createArrows();
        polygonList.fill(g2d);

        drawText(g2d);

    }

    @Override
    public void paintComponent(Graphics g) {
        paint2(g);
    }

    public void setBoundsPath(GeneralPath boundsPath) {
        this.boundsPath = boundsPath;
    }

    private GeneralPath getBoundsPath() {
        if (boundsPath == null) {
            boundsPath = createBoundsPath();
        }
        return boundsPath;
    }

    /*
     * @param pt: relative to its parent
     */
    public boolean isWithin(Point pt) {
        if (getBoundsPath() == null) {
            return false;
        } else {
            return boundsPath.contains(pt);
        }
    }

    private void drawText(Graphics2D g2d) {
        if (txt == null || txt.isEmpty() || !hasEnoughRoom()) {
            return;
        }

        final Dimension size = getSize();
        AffineTransform old = g2d.getTransform();
        Rectangle drawingBounds = new Rectangle(0, 0, size.width, size.height);
        Point.Double center = new Point.Double(drawingBounds.getCenterX(), drawingBounds.getCenterY());

        FontMetrics fm = FontUtil.getFontMetrics(this);
        int radius = (int) Math.round(drawingBounds.width * .5f - fm.getAscent());
        int charWidth = fm.charWidth('A');

        final double angIncrement = Math.asin(charWidth * widthMultiplier * 1.0 / radius);
        final double theta = Math.toRadians(-startOffset + startAngle + extent / 2) - txt.length() * angIncrement / 2;
        AffineTransform tx = null;
        for (int i = 0; i < txt.length(); i++) {
            Character c = txt.charAt(i);

            if (i == 0) {
                tx = AffineTransform.getTranslateInstance(center.getX(), center.getY());
                tx.rotate(theta);
            } else {
                tx = new AffineTransform();
                tx.rotate(angIncrement);
            }
            g2d.transform(tx);

            g2d.drawString(c.toString(), Math.round(charWidth * 0.5), -radius);
        }
        g2d.setTransform(old);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getEndPos() {
        return endPos;
    }

    public void setEndPos(Integer endPos) {
        Integer old = this.endPos;
        this.endPos = endPos;
        firePropertyChange("endPos", old, this.endPos);
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        Integer old = this.startPos;
        this.startPos = startPos;
        firePropertyChange("startPos", old, this.startPos);
    }

    public Integer getTotalPos() {
        return totalPos;
    }

    public void setTotalPos(Integer totalPos) {
        Integer old = this.totalPos;
        this.totalPos = totalPos;
        firePropertyChange("totalPos", old, this.totalPos);
    }

    protected GeneralPath createBoundsPath() {
        GeneralPath ret = null;
        if (startOffset != null && startAngle != null && extent != null) {
            final Dimension size = getSize();
            final Point loc = getLocation();
            Rectangle drawRect = new Rectangle(0, 0, size.width, size.height);
            Rectangle innerBounds = UIUtil.shrink(drawRect, getTxtHeight(), getTxtHeight());

            Arc2D.Float outerArc = new Arc2D.Float(drawRect, startOffset + 90 - startAngle, -extent, Arc2D.OPEN);
            Arc2D.Float innerArc = new Arc2D.Float(innerBounds, startOffset + 90 - startAngle, -extent, Arc2D.OPEN);

            ret = new GeneralPath();
            ret.moveTo(outerArc.getStartPoint().getX() + loc.getX(), outerArc.getStartPoint().getY() + loc.getY());
            ret.lineTo(outerArc.getEndPoint().getX() + loc.getX(), outerArc.getEndPoint().getY() + loc.getY());
            ret.lineTo(innerArc.getEndPoint().getX() + loc.getX(), innerArc.getEndPoint().getY() + loc.getY());
            ret.lineTo(innerArc.getStartPoint().getX() + loc.getX(), innerArc.getStartPoint().getY() + loc.getY());
            ret.closePath();
        }
        return ret;
    }

    private Rectangle getDrawingBounds() {
        Dimension size = getSize();
        return new Rectangle(0, 0, size.width, size.height);
    }

    private GeneralPath createBracketPath() {
        Rectangle drawingBounds = getDrawingBounds();
        Rectangle pathOutterBounds = UIUtil.shrink(drawingBounds, getTxtHeight(), getTxtHeight());
        Rectangle innerBounds = UIUtil.shrink(drawingBounds, getThickness(), getThickness());

        GeneralPath ret = new GeneralPath();

        Arc2D.Float outterArc = null;
        Arc2D.Float innerArc = null;



        if (hasEnoughRoom()) {
            pathOutterBounds = UIUtil.shrink(drawingBounds, getTxtHeight(), getTxtHeight());
            innerBounds = UIUtil.shrink(drawingBounds, getThickness(), getThickness());
            outterArc = new Arc2D.Float(pathOutterBounds, startOffset + 90 - startAngle, -extent, Arc2D.OPEN);
            innerArc = new Arc2D.Float(innerBounds, startOffset + 90 - startAngle, -extent, Arc2D.OPEN);

            ret.moveTo(innerArc.getStartPoint().getX(), innerArc.getStartPoint().getY());
            ret.lineTo(outterArc.getStartPoint().getX(), outterArc.getStartPoint().getY());
            ret.lineTo(outterArc.getEndPoint().getX(), outterArc.getEndPoint().getY());
            ret.lineTo(innerArc.getEndPoint().getX(), innerArc.getEndPoint().getY());
        } else {
            pathOutterBounds = UIUtil.shrink(drawingBounds, getTxtHeight(), getTxtHeight());
            innerBounds = UIUtil.shrink(pathOutterBounds, 2, 2);

            outterArc = new Arc2D.Float(pathOutterBounds, startOffset + 90 - startAngle, -extent, Arc2D.OPEN);
            innerArc = new Arc2D.Float(innerBounds, startOffset + 90 - startAngle, -extent, Arc2D.OPEN);

            ret.moveTo(innerArc.getStartPoint().getX(), innerArc.getStartPoint().getY());
            ret.lineTo(outterArc.getStartPoint().getX(), outterArc.getStartPoint().getY());
            ret.lineTo(outterArc.getEndPoint().getX(), outterArc.getEndPoint().getY());
            ret.lineTo(innerArc.getEndPoint().getX(), innerArc.getEndPoint().getY());
        }
        return ret;
    }

    private boolean hasEnoughRoom() {
        double radius = getDrawingBounds().width / 2.0;
        double arcLength = MathUtil.getArcLength(radius, extent, false);
        final FontMetrics fm = FontUtil.getFontMetrics(this);
        int strWidth = fm.stringWidth(txt);

        return arcLength > Math.round(strWidth * 0.65f);
    }

    static int calculateTriangleHeight() {
        final FontMetrics fm = FontUtil.getFontMetrics(FontUtil.getDefaultMSFont());
        int fontHeight = fm.getHeight();
        final int triangleHeight = MathUtil.nextOdd(fontHeight * 0.5f);
        return triangleHeight;
    }

    private PolygonList createArrows() {
        PolygonList ret = new PolygonList();

        if (!hasEnoughRoom()) {
            return ret;
        }
        final Dimension size = getSize();
        final FontMetrics fm = FontUtil.getFontMetrics(FontUtil.getDefaultMSFont());
        int fontHeight = fm.getHeight();
        final int triangleHeight = calculateTriangleHeight();

        if (ringUpperRadius != null || ringLowerRadius != null) {


            RMap.Entry entry = (RMap.Entry) data;

            int[] downCutPos = entry.getDownstreamCutPos();
            int cutType = entry.getDownstreamCutType();

            // upper arrow first
            Polygon polygon = new Polygon();
            polygon.addPoint(Math.round(size.width * 0.5f + ringUpperRadius), Math.round(size.height * 0.5f));
            polygon.addPoint(Math.round(size.width * 0.5f + ringUpperRadius + triangleHeight), Math.round(size.height * 0.5f + MathUtil.nextOdd(fontHeight * 0.3f)));
            polygon.addPoint(Math.round(size.width * 0.5f + ringUpperRadius + triangleHeight), Math.round(size.height * 0.5f - MathUtil.nextOdd(fontHeight * 0.3f)));

            Float delta;
            if (cutType == REN.OVERHANG_3PRIME) {
                delta = Math.max(downCutPos[0], downCutPos[1]) * 360f / totalPos;
            } else {
                delta = Math.min(downCutPos[0], downCutPos[1]) * 360f / totalPos;
            }
            AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(-(startOffset + 90 - startAngle - delta)), size.width * 0.5, size.height * 0.5);
            polygon = UIUtil.transform(at, polygon);
            ret.add(polygon);

            polygon = new Polygon();
            polygon.addPoint(Math.round(size.width * 0.5f + ringLowerRadius), Math.round(size.height * 0.5f));
            polygon.addPoint(Math.round(size.width * 0.5f + ringLowerRadius - triangleHeight), Math.round(size.height * 0.5f + MathUtil.nextOdd(fontHeight * 0.3f)));
            polygon.addPoint(Math.round(size.width * 0.5f + ringLowerRadius - triangleHeight), Math.round(size.height * 0.5f - MathUtil.nextOdd(fontHeight * 0.3f)));

            if (cutType == REN.OVERHANG_3PRIME) {
                delta = Math.min(downCutPos[0], downCutPos[1]) * 360f / totalPos;
            } else {
                delta = Math.max(downCutPos[0], downCutPos[1]) * 360f / totalPos;
            }
            at = AffineTransform.getRotateInstance(Math.toRadians(-(startOffset + 90 - startAngle - delta)), size.width * 0.5, size.height * 0.5);
            polygon = UIUtil.transform(at, polygon);
            ret.add(polygon);

        }
        return ret;
    }

    protected Integer getThickness() {
        if (thickness == null) {
            thickness = calculateThickness();
        }
        return thickness;
    }

    protected Integer calculateThickness() {
        FontMetrics fm = FontUtil.getFontMetrics(this);
        Integer ret = Math.round(fm.getHeight() * 1.2f);
        return ret;
    }

    protected Integer getTxtHeight() {
        FontMetrics fm = FontUtil.getFontMetrics(this);
        return fm.getHeight();
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Float getExtent() {
        return extent;
    }

    public void setExtent(Float extent) {
        Float old = this.extent;
        this.extent = extent;
        firePropertyChange("extent", old, this.extent);
    }

    public Float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(Float startAngle) {
        Float old = this.startAngle;
        this.startAngle = startAngle;
        firePropertyChange("startAngle", old, this.startAngle);
    }

    public Float getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Float startOffset) {
        Float old = this.startOffset;
        this.startOffset = startOffset;
        firePropertyChange("startOffset", old, this.startOffset);
    }

    public static CurvedBracketList compact(List<CurvedBracket> brackets) {
        CurvedBracketList ret = new CurvedBracketList();
        List<CurvedBracket> tmp = new ArrayList<CurvedBracket>(brackets);
        Collections.sort(tmp, new StartPosComparator());
        LocList locList = new LocList();
        for (int i = 0; !tmp.isEmpty();) {
            CurvedBracket b = tmp.get(i);
            boolean intersect = locList.intersect(b.getStartPos(), b.getEndPos()) != null;
            if (!intersect) {
                ret.add(b);
                tmp.remove(i);
                locList.add(new Loc(b.getStartPos(), b.getEndPos()));
            } else {
                i++;
            }

            if (i > tmp.size() - 1) {
                locList.clear();
                i = 0;
            }
        }


        return ret;
    }

    protected static class StartPosComparator implements Comparator<CurvedBracket> {

        @Override
        public int compare(CurvedBracket o1, CurvedBracket o2) {
            return o1.getStartPos().compareTo(o2.getStartPos());
        }
    }
}
