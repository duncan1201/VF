/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.proxysetting.api;

import com.gas.common.ui.proxysetting.api.ProxySetting;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public interface IProxyConfigPanel {
    ProxySetting getProxySetting();
    void validateInput();
    void setDialogDescriptor(DialogDescriptor dialogDescriptor);
    void setNotificationLineSupport(NotificationLineSupport notificationLineSupport);
}
