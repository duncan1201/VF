/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
public class TabbedPanel extends JPanel {

    WeakReference<TabArea> tabAreaRef;
    WeakReference<JPanel> contentPaneRef;
    List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    /**
     * @param tabPlacement possible values are:
     * SwingConstants.BOTTOM/TOP/LEFT/RIGHT
     */
    public TabbedPanel(int tabPlacement) {
        setLayout(new BorderLayout());
        TabArea tabArea = new TabArea(tabPlacement);
        tabAreaRef = new WeakReference<TabArea>(tabArea);
        if (tabPlacement == SwingConstants.BOTTOM) {
            add(tabArea, BorderLayout.SOUTH);
        }

        JPanel contentPane = new JPanel();
        contentPaneRef = new WeakReference<JPanel>(contentPane);
        contentPane.setLayout(new CardLayout());
        add(contentPane, BorderLayout.CENTER);
    }

    /**
     * @param text case insensitive
     */
    public void setSelectedTab(String text) {
        Tab tab = tabAreaRef.get().tabContainer.getTabByText(text);
        if (tab != null) {
            tabAreaRef.get().tabContainer.setSelected(tab);
        }
    }

    public void addChangeListener(ChangeListener cl) {
        changeListeners.add(cl);
    }

    void fireChangeEvents() {
        for (ChangeListener cl : changeListeners) {
            try {
                cl.stateChanged(new ChangeEvent(this));
            } catch (Exception e) {
            }
        }
    }

    /**
     * @param index 0-based
     */
    public void setSelectedTab(int index) {
        if (index > -1 && index < tabAreaRef.get().tabContainer.tabs.size()) {
            Tab tab = tabAreaRef.get().tabContainer.tabs.get(index);
            tabAreaRef.get().tabContainer.setSelected(tab);
        }
    }

    public String getSelected() {
        Tab tab = tabAreaRef.get().tabContainer.getSelected();
        if (tab != null) {
            return tab.text;
        }
        return null;
    }

    /**
     * @param index 0-based
     */
    public void insertTab(String text, Image image, JComponent comp, int index) {
        String id = UUID.randomUUID().toString();
        tabAreaRef.get().addTab(text, image, index, id);
        contentPaneRef.get().add(comp, id);
        CardLayout layout = (CardLayout) contentPaneRef.get().getLayout();
        layout.show(contentPaneRef.get(), id);
    }

    public void addTab(String text, Image image, JComponent comp) {
        insertTab(text, image, comp, -1);
    }

    public void setDecorator(JComponent comp, GridBagConstraints c) {
        tabAreaRef.get().contentPane.add(comp, c);
    }

    public Component getShowingComponent() {
        Component ret = null;
        int count = contentPaneRef.get().getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = contentPaneRef.get().getComponent(i);
            if (comp.isShowing()) {
                ret = comp;
            }
        }
        return ret;
    }
}
