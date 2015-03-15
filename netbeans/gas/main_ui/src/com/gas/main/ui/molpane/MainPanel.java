/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.tabbedpanel.TabbedPanel;
import com.gas.common.ui.util.Pref;
import com.gas.domain.ui.editor.zoom.ZoomPanel;
import com.gas.main.ui.molpane.sitepanel.SidePanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class MainPanel extends JPanel {
    
    WeakReference<TabbedPanel> tabbedPanelRef;
    WeakReference<ZoomPanel> zoomPanelRef;

    MainPanel() {
        setLayout(new BorderLayout());
        TabbedPanel tabbPanel = new TabbedPanel(SwingConstants.BOTTOM);
        tabbedPanelRef = new WeakReference<TabbedPanel>(tabbPanel);
        ZoomPanel zoomPanel = new ZoomPanel(null);
        zoomPanelRef = new WeakReference<ZoomPanel>(zoomPanel);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;        
        tabbPanel.setDecorator(zoomPanel, c);
        add(tabbPanel, BorderLayout.CENTER);
    }
    
    ZoomPanel getZoomPanel(){
        return zoomPanelRef.get();
    }

    Component getShowingComponent(){
        return tabbedPanelRef.get().getShowingComponent();
    }
    
    void addComponent(String title, JComponent comp) {
        tabbedPanelRef.get().addTab(title, null, comp);
    }
    
    void insertComponent(String title, JComponent comp, int index){
        tabbedPanelRef.get().insertTab(title, null, comp, index);
    }

    void addSidePanel(SidePanel sidePanel) {
        add(sidePanel, BorderLayout.EAST);
    }
}

