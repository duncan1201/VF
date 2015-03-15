/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;

/**
 *
 * @author dq
 */
class LabelListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object obj = evt.getNewValue();
            Label label = (Label) evt.getSource();
            final String pName = evt.getPropertyName();
            if (pName.equals("borderColor")) {
                Color c = (Color) obj;
                label.setBorder(BorderFactory.createLineBorder(c));
            } else if (pName.equals("selected")) {
                Font font = label.getTextFont();
                Font newFont ;
                if (label.isSelected()) {                  
                    newFont = font.deriveFont(Font.BOLD);
                } else {                    
                    newFont = font.deriveFont(Font.PLAIN);
                }
                label.setTextFont(newFont);                
            }
        }
    }
}
