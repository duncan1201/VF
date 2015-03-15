/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.ui.banner.BannerTC;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class SearchPanelListeners {
    static class PtyChangeLisener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object src = evt.getSource();
            IFolderPanel current = BannerTC.getInstance().getFolderPanel();
            
            String pName = evt.getPropertyName();
            Object newValue = evt.getNewValue();
            if(pName.equals("busy") && current == src){
                Boolean busy = (Boolean)newValue;
                if(busy){
                    BannerTC.getInstance().setIcon(ImageHelper.createImageIcon(ImageNames.CIRCLE_BALL_16));
                }else{
                    BannerTC.getInstance().setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                }
            }else if(pName.equals("statusLine") && current == src){
                String statusLine = (String)newValue;
                BannerTC.getInstance().setHeaderText(statusLine);
            }
        }
    }
}
