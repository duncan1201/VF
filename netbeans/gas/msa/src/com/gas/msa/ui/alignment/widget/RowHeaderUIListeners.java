/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class RowHeaderUIListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RowHeaderUI src = (RowHeaderUI) evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("msa")) {
                src.createUIObjects();
                src.layoutUIObjects();
                src.repaint();
            }else if(pName.equals("selectedRow")){
                src.createUIObjects();
                src.repaint();
            }
        }
    }
}
