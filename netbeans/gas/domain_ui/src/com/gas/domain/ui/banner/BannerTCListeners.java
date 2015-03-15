/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.domain.core.filesystem.Folder;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class BannerTCListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object src = evt.getSource();
            String pName = evt.getPropertyName();

        }
    }
}
