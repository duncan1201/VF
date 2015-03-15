/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class VerticalRulerListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            VerticalRuler src = (VerticalRuler) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("selected")) {
                if (src.isSelected()) {
                    src.setBgColor(ColorCnst.SELECTED_TEXT_BG);
                    src.labelColor = ColorCnst.SELECTED_TEXT_FG;
                } else {
                    src.labelColor = Color.BLACK;
                    src.setBgColor(null);
                }
            }
        }
    }
}
