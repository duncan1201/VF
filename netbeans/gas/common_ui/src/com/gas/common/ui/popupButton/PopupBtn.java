/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.popupButton;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author dq
 */
public class PopupBtn extends JButton {

    private int direction;
    private Popup popup;
    private Rectangle popupBoundsOnScreen;
    private boolean showingPopup;
    private IPopupBtnContent popupContent;
    private final static Icon downIcon = ImageHelper.createImageIcon(ImageNames.BR_DOWN_16);
    private final static Icon upIcon = ImageHelper.createImageIcon(ImageNames.BR_UP_16);
    private final static Icon leftIcon = ImageHelper.createImageIcon(ImageNames.BR_PREV_16);
    private final static Icon rightIcon = ImageHelper.createImageIcon(ImageNames.BR_NEXT_16);
    private Timer hideTimer;

    public PopupBtn(String text) {
        this(text, SwingConstants.BOTTOM);
    }

    public IPopupBtnContent getPopupContent() {
        return popupContent;
    }

    public void setPopupContent(IPopupBtnContent popupContent) {
        this.popupContent = popupContent;
    }

    void hidePopup() {
        if (popup != null && showingPopup) {
            popupContent.hiding();
            popup.hide();
            popupBoundsOnScreen = null;
            showingPopup = false;
            popupContent.hidden();
        }
    }

    private void initIconText() {
        if (direction == SwingConstants.BOTTOM) {
            setIcon(downIcon);
        } else if (direction == SwingConstants.TOP) {
            setIcon(upIcon);
        } else if (direction == SwingConstants.LEFT) {
            setIcon(leftIcon);
        } else if (direction == SwingConstants.RIGHT) {
            setIcon(rightIcon);
        } else {
            throw new UnsupportedOperationException(String.format("Not recognized direction:%d", this.direction));
        }
    }

    private Point calculateLocation() {
        Rectangle bounds = PopupBtn.this.getBounds();
        Dimension pSize = ((Component) popupContent).getPreferredSize();
        Point ret = new Point(bounds.width, bounds.height);
        ret.x -= pSize.width;
        SwingUtilities.convertPointToScreen(ret, PopupBtn.this);
        return ret;
    }

    /**
     * @param direction: possible values are:
     * SwingConstants.LEFT/RIGHT/TOP/BOTTOM
     */
    public PopupBtn(String text, int direction) {
        setText(text);
        this.direction = direction;
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.LEFT);
        initIconText();
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (popupContent != null) {
                    Dimension pSize = ((Component) popupContent).getPreferredSize();
                    final Point point = calculateLocation();
                    if (popup == null || showingPopup == false) {
                        popup = PopupFactory.getSharedInstance().getPopup(PopupBtn.this, (Component) popupContent, point.x, point.y);
                        popupContent.showing();
                        popup.show();
                        showingPopup = true;
                        popupContent.shown();
                        Point locOnScreen = UIUtil.getLocOnScreen((Component) popupContent);
                        if (locOnScreen != null) {
                            popupBoundsOnScreen = new Rectangle(locOnScreen, pSize);
                        }
                    } else {
                        hidePopup();
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Timer timer = getHideTimer();
                if (timer.isRunning()) {
                    timer.stop();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Timer timer = getHideTimer();
                if (!timer.isRunning()) {
                    timer.restart();
                }
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                MouseEvent me = (MouseEvent) event;
                int id = me.getID();
                if (id == MouseEvent.MOUSE_MOVED) {
                    int xx = 1;
                }
                Point p = me.getLocationOnScreen();
                if (popupBoundsOnScreen != null) {
                    if (popupBoundsOnScreen.contains(p) && hideTimer != null && hideTimer.isRunning()) {
                        hideTimer.stop();
                    }
                    Rectangle boundsOnScreen = getBoundsOnScreen();

                    if (!popupBoundsOnScreen.contains(p) && !boundsOnScreen.contains(p)) {
                        Timer timer = getHideTimer();
                        if (!timer.isRunning()) {
                            timer.restart();
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK);

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();

            }
        });
    }

    private Rectangle getBoundsOnScreen() {
        Rectangle ret = null;
        Point locOnScreen = UIUtil.getLocOnScreen(this);
        if (locOnScreen != null) {
            Dimension pSize = getPreferredSize();
            ret = new Rectangle(locOnScreen, pSize);
        }
        return ret;
    }

    private Timer getHideTimer() {
        if (hideTimer == null) {
            hideTimer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hidePopup();
                }
            });
            hideTimer.setRepeats(false);
        }
        return hideTimer;
    }
}
