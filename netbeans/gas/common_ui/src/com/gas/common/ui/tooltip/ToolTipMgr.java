/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.gas.common.ui.tooltip;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.Timer;
import org.openide.windows.WindowManager;

public class ToolTipMgr extends MouseAdapter implements MouseMotionListener, PropertyChangeListener {

    private Timer exitTimer;
    private Timer enterTimer;
    private WeakReference<ITTContentProvider> providerRef;
    JDialog popup;
    private int initialDelay = 1000;
    private int stopDelay = 1000;
    private static ToolTipMgr shared = new ToolTipMgr();
    private PopupMouseAdapter popupMouseAdapter = new PopupMouseAdapter();

    public ToolTipMgr() {
    }

    public static ToolTipMgr sharedInstance() {
        return shared;
    }

    public JDialog getPopup() {
        return popup;
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public int getStopDelay() {
        return stopDelay;
    }

    public void setStopDelay(int stopDelay) {
        this.stopDelay = stopDelay;
    }

    public void registerComponent(JComponent component, boolean mouseEnter, boolean propertyChange) {
        if (mouseEnter) {
            component.removeMouseListener(this);
            component.addMouseListener(this);
        }
        if (propertyChange) {
            component.addPropertyChangeListener("mouseIn", this);
        }
    }

    public void unregisterComponent(JComponent component) {
        component.removeMouseListener(this);
        component.removePropertyChangeListener(this);
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        ITTContentProvider provider = (ITTContentProvider) event.getSource();
        initiateToolTip(provider, event.getLocationOnScreen());
    }

    protected synchronized void initiateToolTip(ITTContentProvider contentProvider, final Point locOnScreen) {
        if (providerRef == null || providerRef.get() == null) {
            providerRef = new WeakReference<ITTContentProvider>(contentProvider);
        }
        if (contentProvider != providerRef.get()) {
            if (providerRef.get() != null) {
                hideImmediately();
            }
            providerRef = new WeakReference<ITTContentProvider>(contentProvider);
            if (exitTimer != null) {
                if (exitTimer.isRunning()) {
                    exitTimer.stop();
                }
            }
        }
        enterTimer = new Timer(initialDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showImmediately(locOnScreen, providerRef.get());
            }
        });
        enterTimer.setRepeats(false);

        if (!enterTimer.isRunning()) {
            enterTimer.start();
        }

    }

    public boolean isShowing() {
        boolean ret = true;
        if (popup == null) {
            ret = false;
        } else {
            ret = popup.isShowing();
        }
        return ret;
    }

    public Rectangle getBoundsOnScreen() {
        Rectangle ret = null;
        if (popup != null && popup.isShowing()) {
            Point loc = popup.getLocationOnScreen();
            Dimension size = popup.getSize();
            ret = new Rectangle(loc, size);
        }
        return ret;
    }

    synchronized void showImmediately(Point locOnScreen, ITTContentProvider provider) {
        if (provider == null) {
            return;
        }
        JComponent comp = (JComponent) provider;
        hideImmediately();
        popup = new JDialog(WindowManager.getDefault().getMainWindow(), ModalityType.MODELESS);
        popup.setFocusable(false);
        popup.setUndecorated(true);
        popup.addMouseListener(popupMouseAdapter);

        Container content = provider.getToolTipContent();

        popup.setContentPane(content);

        //popup.setSize(content.getPreferredSize());

        popup.pack();

        Point loc = getPopupLocOnScreen(locOnScreen, comp, content);

        if (loc != null) {
            popup.setLocation(loc);

            popup.setVisible(true);
        } else {
            popup.setLocation(locOnScreen);
            popup.setVisible(true);
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ITTContentProvider src = (ITTContentProvider) evt.getSource();
        String name = evt.getPropertyName();
        Boolean v = (Boolean) evt.getNewValue();
        if (!name.equals("mouseIn")) {
            return;
        }

        if (v) {
            Point pt = MouseInfo.getPointerInfo().getLocation();
            initiateToolTip(src, pt);
        } else {
            handleMouseExit(MouseInfo.getPointerInfo().getLocation());
        }
    }

    private class PopupMouseAdapter extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent event) {
            processEvent(event);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            processEvent(event);
        }

        private synchronized void processEvent(MouseEvent event) {
            if (!isOnParent(event) && !isOnPopup(event.getLocationOnScreen())) {

                if (enterTimer != null) {
                    enterTimer.stop();
                }
                if (exitTimer == null) {
                    exitTimer = new Timer(stopDelay, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            hideImmediately();
                        }
                    });
                    exitTimer.setRepeats(false);

                    exitTimer.start();

                } else if (!exitTimer.isRunning()) {
                    exitTimer.start();
                }
            } else {
            }
        }
    }

    private Point getPopupLocOnScreen(Point ptOnScreen, JComponent parent, Container content) {
        Point ret = new Point();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Point locOnScreen = UIUtil.getLocOnScreen(parent);
        if (locOnScreen == null) {
            return null;
        }
        int x = locOnScreen.x;
        int y = locOnScreen.y;
        Dimension parentSize = parent.getSize();

        Dimension contentSize = content.getPreferredSize();

        if (x + contentSize.width > screenSize.width) {
            x = x - Math.abs(contentSize.width - parentSize.width);
            if (x + parentSize.width > screenSize.width) {
                x = x - Math.abs(x + contentSize.width - screenSize.width);
            }
        } else if (x < 0) { // off the screen
            x = MathUtil.round(ptOnScreen.getX());
        }
        if (y + contentSize.height > screenSize.height) {
            y -= contentSize.height;
        } else {
            y += parentSize.height;
        }

        ret.setLocation(x, y);
        return ret;
    }

    @Override
    public void mouseExited(MouseEvent event) {
        handleMouseExit(event.getLocationOnScreen());
    }

    synchronized void handleMouseExit(Point locOnScreen) {
        if (!isOnPopup(locOnScreen)) {
            if (enterTimer != null && enterTimer.isRunning()) {
                enterTimer.stop();
            }

            if (exitTimer == null) {
                exitTimer = new Timer(stopDelay, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hideImmediately();
                    }
                });
                exitTimer.setRepeats(false);
            }
            if (!exitTimer.isRunning()) {
                exitTimer.start();
            }
        }
    }

    private boolean isOnPopup(Point locOnScreen) {
        boolean ret = false;

        int x = locOnScreen.x;
        int y = locOnScreen.y;

        if (popup != null && popup.isShowing()) {
            Point l = popup.getLocationOnScreen();
            Rectangle rect = new Rectangle(l, popup.getSize());
            ret = rect.contains(x, y);
        }

        return ret;
    }

    private boolean isOnParent(MouseEvent event) {
        boolean ret = false;

        int x = event.getXOnScreen();
        int y = event.getYOnScreen();


        if (providerRef != null && providerRef.get() != null) {
            JComponent comp = (JComponent) providerRef.get();
            Point ptOnScreen = UIUtil.getLocOnScreen(comp);
            if (ptOnScreen != null) {
                Rectangle rect = new Rectangle(ptOnScreen, comp.getSize());
                ret = rect.contains(x, y);
            }
        }
        return ret;
    }

    public synchronized void hideImmediately() {
        if (popup != null) {
            popup.setVisible(false);
            popup = null;
        }
    }
}
