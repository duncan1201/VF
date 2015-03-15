/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.main.ui.molpane.Events.GraphPanelPosChangedEvent;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
 *
 * @author dunqiang
 */
class InternalListeners {

    static class LocationListener implements AWTEventListener {

        private JComponent graphPanel;
        private int totalPos;
        private Rectangle drawingRect;

        LocationListener(JComponent graphPanel, int totalPos, Rectangle rect) {
            this.graphPanel = graphPanel;
            this.totalPos = totalPos;
            this.drawingRect = rect;
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if (!graphPanel.isShowing()) {
                return;
            }
            if (event instanceof MouseEvent) {
                MouseEvent mEvent = (MouseEvent) event;

                Point loc = UIUtil.getLocOnScreen(graphPanel);
                Dimension size = graphPanel.getSize();
                Rectangle bounds = new Rectangle(loc, size);
                Point mouseOnScreenLoc = mEvent.getLocationOnScreen();


                if (bounds.contains(mouseOnScreenLoc)) {
                    // translate the relative coordinate
                    Point mouseLoc = new Point(mouseOnScreenLoc);
                    mouseLoc.translate(-loc.x, -loc.y);


                    int pixel = mouseLoc.x - drawingRect.x;

                    Integer pos = MathUtil.round(pixel * totalPos * 1.0 / drawingRect.width, Integer.class);
                    //pos = Math.max(1, pos);
                    //pos = Math.min(pos, totalPos);
                    GraphPanelPosChangedEvent e = new GraphPanelPosChangedEvent(graphPanel, pos);
                    graphPanel.firePropertyChange("sdfaa", 1, 2);
                } else {
                }
            }
        }
    }
}
