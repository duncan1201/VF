/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXBusyLabel;

/**
 *
 * @author dunqiang
 */
public class BusyPanel extends JPanel {

    private JXBusyLabel busyLabel;

    public BusyPanel() {
        super(new GridBagLayout());

        busyLabel = new JXBusyLabel();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(busyLabel, c);
    }

    public void setBusy(boolean busy) {
        busyLabel.setBusy(busy);
    }

    public boolean isBusy() {
        return busyLabel.isBusy();
    }

    public void setText(String txt) {
        busyLabel.setText(txt);
    }
}
