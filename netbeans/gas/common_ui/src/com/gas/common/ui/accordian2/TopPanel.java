/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
class TopPanel extends JPanel {

    String barName;
    WeakReference<JScrollPane> scrollPaneRef;

    TopPanel() {
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneRef = new WeakReference<JScrollPane>(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
        hookupListeners();
    }

    void hookupListeners() {
        addPropertyChangeListener(new TopPanelListeners.PtyListener());
    }

    void setBarName(String n) {
        String old = this.barName;
        this.barName = n;
        firePropertyChange("barName", old, barName);
    }

    String getBarName() {
        return barName;
    }

    void setCenterContent(Component comp) {
        add(comp, BorderLayout.CENTER);
    }
}
