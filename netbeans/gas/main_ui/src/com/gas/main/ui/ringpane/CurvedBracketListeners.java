/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.util.UIUtil;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class CurvedBracketListeners {

    static class CompAdpt extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            CurvedBracket src = (CurvedBracket) e.getSource();
            if (src.getStartAngle() != null && src.getExtent() != null && src.getStartOffset() != null) {
                GeneralPath path = src.createBoundsPath();
                src.setBoundsPath(path);
            }
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            CurvedBracket b = (CurvedBracket) evt.getSource();
            String pName = evt.getPropertyName();
            if (pName.equals("startPos") || pName.equals("endPos") || pName.equals("totalPos")) {
                if (b.getStartPos() != null && b.getEndPos() != null && b.getTotalPos() != null) {
                    Integer pos = null;
                    if (b.getEndPos() > b.getStartPos()) {
                        pos = b.getEndPos() - b.getStartPos() + 1;
                    } else {
                        pos = b.getTotalPos() - b.getStartPos() + b.getEndPos() + 1;
                    }
                    Float extent = UIUtil.toScreenWidth(360, b.getTotalPos(), pos, Float.class);
                    b.setExtent(extent);
                    Float startAngle = UIUtil.toScreenWidth(360, b.getTotalPos(), b.getStartPos() - 1, Float.class);
                    b.setStartAngle(startAngle);
                }
            } else if (pName.equals("extent") || pName.equals("startOffset") || pName.equals("startAngle") || pName.equals("selected")) {
                b.repaint();
                if (b.getStartAngle() != null && b.getExtent() != null && b.getStartOffset() != null) {
                    GeneralPath path = b.createBoundsPath();
                    b.setBoundsPath(path);
                }
            }
        }
    }
}
