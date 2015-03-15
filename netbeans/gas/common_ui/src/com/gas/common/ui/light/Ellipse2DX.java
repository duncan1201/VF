/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author dq
 */
public class Ellipse2DX {

    public static class Float extends Ellipse2D.Float {

        private boolean selected = false;
        private boolean visible = true;
        private Object data;
        private Color color;

        public Float() {
            super();
        }

        public Float(float x, float y, float w, float h) {
            super(x, y, w, h);
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void paint(Graphics2D g2d) {
            if (!visible) {
                return;
            }
            if (color == null) {
                g2d.setColor(Color.BLACK);
            } else {
                g2d.setColor(color);
            }
            if (selected) {
                g2d.fill(this);
            } else {
                g2d.draw(this);
            }

        }
    }

    public class Double extends Ellipse2D.Double {
    }
}
