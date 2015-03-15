/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.core.Arc2DX;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.RectHelper;
import com.gas.common.ui.util.ColorCnst;
import java.awt.*;
import java.awt.geom.*;
import java.beans.PropertyChangeSupport;
import org.jdesktop.swingx.color.ColorUtil;

/**
 *
 * @author dq
 */
public class CircularBrick {

    private GeneralPath path;
    private Arc2DX.Float outerArc;
    private Arc2DX.Float innerArc;
    private Color bgColor;
    private Color fgColor;
    private boolean fill = true;
    private String text;
    private Integer pos;
    private boolean selected;
    private PropertyChangeSupport propertyChangeSupport;

    public CircularBrick() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        hookupListeners();
    }

    private void hookupListeners() {
        propertyChangeSupport.addPropertyChangeListener(new CircularBrickListeners.PtyListener());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    /**
     * @param bgColor: background color
     */
    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * @param fgColor: foreground color
     */
    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    protected void setPath(GeneralPath p) {
        this.path = p;
    }

    public Arc2DX.Float getInnerArc() {
        return innerArc;
    }

    public void setInnerArc(Arc2DX.Float innerArc) {
        Arc2D old = this.innerArc;
        this.innerArc = innerArc;
        propertyChangeSupport.firePropertyChange("innerArc", old, this.innerArc);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public Arc2DX.Float getOuterArc() {
        return outerArc;
    }

    public void setOuterArc(Arc2DX.Float outerArc) {
        Arc2D old = this.outerArc;
        this.outerArc = outerArc;
        propertyChangeSupport.firePropertyChange("outerArc", old, this.outerArc);
    }

    public void paint(Graphics2D g2d) {
        if (path == null) {
            return;
        }

        Color old = g2d.getColor();
        AffineTransform oldAt = g2d.getTransform();
        final FontMetrics fm = g2d.getFontMetrics();
        if (selected) {
            g2d.setColor(ColorCnst.SELECTED_TEXT_BG);
        } else {
            g2d.setColor(bgColor);
        }

        if (fill) {
            g2d.fill(path);
        } else {
            g2d.draw(path);
        }
        if (text != null) {
            Rectangle2D outerFrame = outerArc.getFrame();
            Rectangle2D innerFrame = innerArc.getFrame();
            double middelDegree = outerArc.getMiddleAngle(true);
            double theta = MathUtil.normalizeDegree(90 - middelDegree);
            g2d.rotate(Math.toRadians(theta), outerFrame.getCenterX(), outerFrame.getCenterY());
            if (selected) {
                g2d.setColor(ColorCnst.SELECTED_TEXT_FG);
            } else if (fgColor != null) {
                g2d.setColor(fgColor);
            } else {
                g2d.setColor(ColorUtil.computeForeground(g2d.getColor()));
            }
            int x = MathUtil.round(outerFrame.getCenterX() - fm.stringWidth(text) * 0.5);
            double y = innerFrame.getY() - ((innerFrame.getY() - outerFrame.getY()) - fm.getHeight()) * 0.5 - fm.getDescent() * 0.5;
            g2d.drawString(text, x, MathUtil.round(y));
        }
        g2d.setColor(old);
        g2d.setTransform(oldAt);
    }

    public Boolean contains(Point p) {
        Boolean ret = null;
        if (path != null) {
            ret = path.contains(p);
        }
        return ret;
    }

    public Double getMiddleAngle(boolean normalize) {
        Double ret = null;
        if (outerArc != null) {
            ret = outerArc.getMiddleAngle(normalize);
        }
        return ret;
    }

    public Point getCenter() {
        Point ret = null;
        if (outerArc != null) {
            ret = new Point();
            ret.setLocation(outerArc.getCenterX(), outerArc.getCenterY());
        }
        return ret;
    }

    /*
     * @return angle extent in degrees
     */
    public Double getAngleExtent() {
        Double ret = null;
        if (ret != null) {
            ret = outerArc.getAngleExtent();
        }
        return ret;
    }

    public String getAngles() {
        StringBuilder ret = new StringBuilder();
        if (outerArc != null) {
            ret.append(getPos());
            ret.append('(');
            ret.append(Math.min(outerArc.getAngleStart(), outerArc.getAngleStart() + outerArc.getAngleExtent()));
            ret.append(',');
            ret.append(Math.max(outerArc.getAngleStart(), outerArc.getAngleStart() + outerArc.getAngleExtent()));
            ret.append(')');
        }
        return ret.toString();
    }

    public Boolean containsAngle(double angleDegree) {
        Boolean ret = null;
        angleDegree = MathUtil.round(angleDegree, 2);
        if (outerArc != null) {
            ret = outerArc.containsAngle(angleDegree);
        } else if (innerArc != null) {
            ret = innerArc.containsAngle(angleDegree);
        }
        return ret;
    }

    /*
     * @param angle: in degrees
     */
    public void getCaretRect(double angle) {
        Rectangle ret = null;
        Boolean contains = containsAngle(angle);
        Point2D pt;
        if (contains != null && contains) {
            double middelAngle = MathUtil.normalizeDegree(outerArc.getMiddleAngle());
            if (angle >= middelAngle) {
                if (outerArc.getAngleExtent() >= 0) {
                    pt = outerArc.getEndPoint();
                } else {
                    pt = outerArc.getStartPoint();
                }
            } else {
                if (outerArc.getAngleExtent() >= 0) {
                    pt = outerArc.getStartPoint();
                } else {
                    pt = outerArc.getEndPoint();
                }
            }
            Rectangle2D frame = outerArc.getFrame();
            Point center = RectHelper.getCenter(frame);
            if (pt.getX() > center.x) {
                ret = new Rectangle();
            } else if (pt.getX() == center.x) {
                ret = new Rectangle();
            } else {
                ret = new Rectangle();
                ret.setLocation(MathUtil.round(pt.getX()), MathUtil.round(pt.getY()));
            }
        }
    }

    public Point2D getCaretLocation() {
        Point2D startPoint = outerArc.getStartPoint();
        Point2D endPoint = outerArc.getEndPoint();
        Point2D ret = null;
        if (startPoint.getX() > endPoint.getX()) {
            ret = endPoint;
        } else {
            ret = startPoint;
        }
        return ret;
    }

    /**
     * @return in degrees
     */
    public double getCaretAngle() {
        double ret = 0;
        double angleStart = outerArc.getAngleStart();
        double angleExtent = outerArc.getAngleExtent();
        if (angleExtent > 0) {
            ret = angleStart + angleExtent;
        } else {
            ret = angleStart;
        }
        return MathUtil.normalizeDegree(ret);
    }

    /*
     * @param angle: in degres (0-360)
     */
    public Point2D getCaretLocation(double angle) {
        Point2D ret = null;
        Boolean contains = containsAngle(angle);
        if (contains != null && contains) {
            double middelAngle = MathUtil.normalizeDegree(outerArc.getMiddleAngle());
            if (angle >= middelAngle) {
                if (outerArc.getAngleExtent() >= 0) {
                    ret = outerArc.getEndPoint();
                } else {
                    ret = outerArc.getStartPoint();
                }
            } else {
                if (outerArc.getAngleExtent() >= 0) {
                    ret = outerArc.getStartPoint();
                } else {
                    ret = outerArc.getEndPoint();
                }
            }
        }
        return ret;
    }
}
