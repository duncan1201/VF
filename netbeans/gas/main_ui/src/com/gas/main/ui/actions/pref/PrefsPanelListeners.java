/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.pref;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public class PrefsPanelListeners {
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PrefsPanel src = (PrefsPanel)evt.getSource();
            String name = evt.getPropertyName();
            Object newValue = evt.getNewValue();
            if(name.equals("dialogDescriptor")){
                if(newValue != null){                    
                    DialogDescriptor dd = (DialogDescriptor)newValue;
                    NotificationLineSupport nls = dd.createNotificationLineSupport();
                    src.getProxyConfigPanel().setDialogDescriptor(dd);
                    src.getProxyConfigPanel().setNotificationLineSupport(nls);
                }
            }
        }
    }
}
