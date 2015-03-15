/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author dq
 */
public class BusyLabel extends JLabel {

    private boolean busy;
    private static Icon emptyIcon = ImageHelper.createImageIcon(ImageNames.EMPTY_16);
    private static Icon busyIcon = ImageHelper.createImageIcon(ImageNames.CIRCLE_BALL_16);

    public BusyLabel() {
        setIcon(emptyIcon);

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String pName = evt.getPropertyName();
                if (pName.equals("busy")) {
                    boolean newValue = (Boolean) evt.getNewValue();
                    if (newValue) {
                        setIcon(busyIcon);
                    } else {
                        setIcon(emptyIcon);
                    }
                    revalidate();
                }
            }
        });
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        boolean old = this.busy;
        this.busy = busy;
        firePropertyChange("busy", old, this.busy);
    }
}
