/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.statusline;

import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = StatusLineElementProvider.class, position = 100)
public class StatusLineCompProvider implements StatusLineElementProvider {

    private StatusLinePanel statusLinePanel = null;

    @Override
    public StatusLinePanel getStatusLineElement() {
        if (statusLinePanel == null) {
            statusLinePanel = new StatusLinePanel();
        }
        return statusLinePanel;
    }
}
