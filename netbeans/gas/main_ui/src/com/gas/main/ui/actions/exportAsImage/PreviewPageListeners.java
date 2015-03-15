/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class PreviewPageListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PreviewPage src = (PreviewPage)evt.getSource();
            String name = evt.getPropertyName();
            if(name.equals("scale") || name.equals("image")){
                src.revalidate();
                src.repaint();
            }
        }
    }
}
