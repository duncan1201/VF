/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.misc.Loc;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class BrickRingListeners {

    static class PtyChangeListener implements PropertyChangeListener {


        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            BrickRing src = (BrickRing) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("doubleStranded")) {
                src.repaint();
            } else if (name.equals("rotateOffset")) {
                src.repaint();
            } else if (name.equals("colorProvider")) {
                src.repaint();
            } else if (name.equals("bases")) {
                src.repaint();
            } else if (name.equals("endPos") || name.equals("startPos") || name.equals("totalPos")) {
                if (src.getEndPos() != null && src.getStartPos() != null) {
                    src.setTotalPos(src.getEndPos() - src.getStartPos() + 1);
                }
            } else if(name.equals("selection")){
                src.repaint();
            }
        }
    }
}
