/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeSupport;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class TransLabel {

    private Float x;
    private Float y;
    private Float width;
    private Float height;
    private Integer horizontalAlignment;
    private Integer verticalAlignment;
    private Object data;
    private Point2D anchor;
    private java.lang.Double theta;
    private PropertyChangeSupport propertyChangeSupport;
    protected Polygon _bounds;
    private Color color;

    public TransLabel() {
        this(null, null);
    }

    public TransLabel(Integer hAlign, Integer vAlign) {
        this.horizontalAlignment = hAlign;
        this.verticalAlignment = vAlign;
        propertyChangeSupport = new PropertyChangeSupport(this);
        hookupListeners();
    }

    private void hookupListeners() {
        propertyChangeSupport.addPropertyChangeListener(new TransLabelListeners.PtyListener());
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean contains(double x, double y) {
        boolean ret;
        if (_bounds == null) {
            ret = false;
        } else {
            ret = _bounds.contains(x, y);
        }
        return ret;
    }

    public void setTheta(java.lang.Double theta) {
        java.lang.Double old = this.theta;
        this.theta = theta;
        propertyChangeSupport.firePropertyChange("theta", old, theta);
    }

    public void setAnchor(Point2D anchor) {
        Point2D old = this.anchor;
        this.anchor = anchor;
        propertyChangeSupport.firePropertyChange("anchor", old, this.anchor);
    }

    public void setAnchor(double x, double y) {
        setAnchor(new Point2D.Double(x, y));
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public void setX(Float x) {
        Float old = this.x;
        this.x = x;
        propertyChangeSupport.firePropertyChange("x", old, this.x);
    }

    public Float getX() {
        return x;
    }

    public Integer getMinX() {
        Integer ret = null;
        if (_bounds != null) {
            for (int i = 0; i < _bounds.npoints; i++) {
                int _x = _bounds.xpoints[i];
                if (ret == null || ret > _x) {
                    ret = _x;
                    break;
                }
            }
        }
        return ret;
    }

    public Integer getMaxX() {
        Integer ret = null;
        if (_bounds != null) {
            for (int i = 0; i < _bounds.npoints; i++) {
                int _x = _bounds.xpoints[i];
                if (ret == null || ret < _x) {
                    ret = _x;
                    break;
                }
            }
        }
        return ret;
    }

    public Integer getMinY() {
        Integer ret = null;
        if (_bounds != null) {
            for (int i = 0; i < _bounds.npoints; i++) {
                int _y = _bounds.ypoints[i];
                if (ret == null || ret > _y) {
                    ret = _y;
                    break;
                }
            }
        }
        return ret;
    }

    public Integer getMaxY() {
        Integer ret = null;
        if (_bounds != null) {
            for (int i = 0; i < _bounds.npoints; i++) {
                int _y = _bounds.ypoints[i];
                if (ret == null || ret < _y) {
                    ret = _y;
                    break;
                }
            }
        }
        return ret;
    }

    public void setY(Float y) {
        Float old = this.y;
        this.y = y;
        propertyChangeSupport.firePropertyChange("y", old, this.y);
    }

    public Float getY() {
        return y;
    }

    public void setWidth(Float width) {
        Float old = this.width;
        this.width = width;
        propertyChangeSupport.firePropertyChange("width", old, this.width);
    }

    public Float getWidth() {
        return width;
    }

    public void setHeight(Float height) {
        Float old = this.height;
        this.height = height;
        propertyChangeSupport.firePropertyChange("height", old, this.height);
    }

    public Float getHeight() {
        return height;
    }

    public void paint(Graphics2D g2d) {
        if (data == null || x == null || y == null || width == null || height == null) {
            return;
        }
        AffineTransform old = g2d.getTransform();
        if (theta != null && anchor != null) {
            AffineTransform transform = AffineTransform.getRotateInstance(theta, anchor.getX(), anchor.getY());
            //g2d.setTransform(transform);
            g2d.rotate(theta, anchor.getX(), anchor.getY());
            _bounds = transform(transform);
        } else {
            _bounds = toPolygon();
        }
        Font font = g2d.getFont();
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int strWidth = fm.stringWidth(data.toString());
        final int ascent = fm.getAscent();
        final int descent = fm.getDescent();
        float _x = 0;
        float _y = 0;
        if (horizontalAlignment == null || horizontalAlignment.intValue() == SwingConstants.LEFT) {
            _x = this.x;
        } else if (horizontalAlignment.intValue() == SwingConstants.CENTER) {
            _x = this.x + (this.width - strWidth) * 0.5f;
        } else if (horizontalAlignment.intValue() == SwingConstants.RIGHT) {
            _x = this.x + (this.width - strWidth);
        }

        if (verticalAlignment == null) {
            _y = this.y;
        } else if (verticalAlignment.intValue() == SwingConstants.CENTER) {
            _y = this.y + this.height * 0.5f + (ascent - descent) * 0.5f;
        } else if (verticalAlignment.intValue() == SwingConstants.TOP) {
            _y = this.y + ascent;
        } else if (verticalAlignment.intValue() == SwingConstants.BOTTOM) {
            _y = this.y + ascent + descent;
        }
        Color oldColor = g2d.getColor();
        if (color != null) {
            g2d.setColor(color);
        }
        g2d.drawString(data.toString(), Math.round(_x), Math.round(_y));

        // restore the transform       
        g2d.setTransform(old);

        g2d.setColor(oldColor);
    }

    protected void calculateBounds() {
        if (x != null && y != null && width != null && height != null) {
            if (theta != null && anchor != null) {
                AffineTransform at = AffineTransform.getRotateInstance(theta, anchor.getX(), anchor.getY());
                _bounds = transform(at);
            } else {
                _bounds = toPolygon();
            }
        }
    }

    public void translateByAdd(float _x, float _y) {
        if (anchor != null) {
        } else {
            x = x + _x;
            y = y + _y;
        }
        calculateBounds();
    }

    public void translateByMul(float _x, float _y) {
        if (anchor != null) {
            anchor.setLocation(anchor.getX() * _x, anchor.getY() * _y);
            x = x * _x;
            y = (float) anchor.getY() - getHeight() * 0.5f;
        } else {
            x = x * _x;
            y = y * _y;
        }
        calculateBounds();
    }

    private Polygon toPolygon() {
        Polygon ret = new Polygon();
        ret.addPoint(Math.round(x), Math.round(y));
        ret.addPoint(Math.round(x + width), Math.round(y));
        ret.addPoint(Math.round(x + width), Math.round(y + height));
        ret.addPoint(Math.round(x), Math.round(y + height));
        return ret;
    }

    private Polygon transform(AffineTransform at) {
        Polygon p = toPolygon();
        return UIUtil.transform(at, p);
    }
}
