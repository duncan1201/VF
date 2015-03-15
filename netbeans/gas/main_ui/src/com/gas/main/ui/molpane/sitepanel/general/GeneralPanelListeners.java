/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.general;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class GeneralPanelListeners {
    static class PtyChangeListener implements PropertyChangeListener{

        PtyChangeListener(){
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            GeneralPanel generalPanel = (GeneralPanel)evt.getSource();
            String pName = evt.getPropertyName();
            if(pName.equals("forProtein")){
                Boolean forProtein = generalPanel.getForProtein();
                if(forProtein){
                    generalPanel.removeTranslationPanel();
                }
                generalPanel.getPropertiesPanel().setForProtein(forProtein);
            }
        }
    }
}
