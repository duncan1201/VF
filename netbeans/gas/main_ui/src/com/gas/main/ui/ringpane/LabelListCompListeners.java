/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class LabelListCompListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            LabelListComp src = (LabelListComp) evt.getSource();
            if (name.equals("labelList")) {
                Float fontSize = src.getFontSize();
                if(fontSize != null){
                    src.getLabelList().setFontSize(fontSize);
                }
                src.repaint();
            } else if (name.equals("fontSize")) {
                Float size = (Float) v;
                src.getLabelList().setFontSize(size);
                src.repaint();
            }
        }
    }
}
