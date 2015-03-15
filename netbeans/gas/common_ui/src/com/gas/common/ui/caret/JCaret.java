/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.caret;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Exceptions;

/**
 *
 * @author dunqiang
 */
public class JCaret extends JComponent implements IReclaimable {
    /*
     * <pre>
     focus & editable--->state
     X        X    --->on
     X        Y    --->blink
     Y        X    --->on
     Y        Y    --->blink
     </pre>
     */

    public static enum STATE {

        BLINK, ON, OFF
    };
    public static int DEFAULT_CARET_WIDTH = 2;
    public static int DEFAULT_BLINK_RATE = 700;
    private final static Color OFF_COLOR = new Color(1, 1, 1, 0);
    private final static Color ON_COLOR = Color.BLACK;
    private int caretWidth = DEFAULT_CARET_WIDTH;
    private int blinkRate = DEFAULT_BLINK_RATE;
    private boolean insertMode = false;
    private Character data;
    private Double angdeg;
    private Point pos;
    // for internal use only
    //JComponent comp;
    ICaretParent caretParent;
    private Timer flasher;
    private Color color = OFF_COLOR;
    private STATE state = STATE.OFF;

    public JCaret() {
        getFlasher();
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new JCaretListeners.PtyChangeListener());

        Toolkit.getDefaultToolkit().addAWTEventListener(
                new JCaretListeners.AWTListener(this), AWTEvent.MOUSE_EVENT_MASK);
        //KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new JCaretListeners.PtyListener2());

    }

    public Double getAngdeg() {
        return this.angdeg;
    }

    /**
     */
    public void setAngdeg(Double angdeg) {
        this.angdeg = angdeg;
    }

    public void setPos(Point pos) {
        setPos(pos, false);
    }

    public void setPos(Point pos, Boolean forcedEvent) {
        Point old = this.pos;
        this.pos = pos;
        if (forcedEvent) {
            firePropertyChange("pos", null, this.pos);
        } else {
            firePropertyChange("pos", old, this.pos);
        }
    }

    public Point getPos() {
        return pos;
    }

    public Point getCenter() {
        Point ret = new Point(getX() + Math.round(getWidth() * 0.5f), getY() + Math.round(getHeight() * 0.5f));
        return ret;
    }

    @Override
    public void cleanup() {
        if (flasher != null) {
            ActionListener[] als = flasher.getActionListeners();
            for (int i = 0; i < als.length; i++) {
                flasher.removeActionListener(als[i]);
            }
            flasher.stop();
        }
        flasher = null;
        caretParent = null;
    }

    public STATE getState() {
        return state;
    }

    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);
    }

    public void setState(STATE state) {
        STATE old = this.state;
        this.state = state;
        firePropertyChange("state", old, this.state);
    }

    public boolean isBlinking() {
        return state == STATE.BLINK;
    }

    public boolean isOn() {
        return state == STATE.ON;
    }

    public ICaretParent getCaretParent() {
        return caretParent;
    }

    public Character getData() {
        return data;
    }

    public void setData(Character data) {
        this.data = data;
    }

    public boolean isInsertMode() {
        return insertMode;
    }

    public void setInsertMode(boolean insertMode) {
        boolean old = this.insertMode;
        this.insertMode = insertMode;
        firePropertyChange("insertMode", old, this.insertMode);
    }

    public int getBlinkRate() {
        return blinkRate;
    }

    public void setBlinkRate(int rate) {
        this.blinkRate = rate;
    }

    public int getCaretWidth() {
        if (isInsertMode()) {
            return 4;
        } else {
            return caretWidth;
        }
    }

    public void setCaretWidth(int caretWidth) {
        this.caretWidth = caretWidth;
    }

    protected final Timer getFlasher() {
        if (flasher == null) {
            flasher = new Timer(blinkRate, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                }
            });
        }
        return flasher;
    }

    public void install(ICaretParent c) {
        if (this.caretParent != null) {
            throw new IllegalStateException("The caret cannot be install on more than once");
        }
        this.caretParent = c;
        ((Component) caretParent).setFocusable(true);
        ((Component) caretParent).addKeyListener(new JCaretListeners.K1Listener());
    }

    public void deinstall(JTextComponent c) {
    }

    @Override
    public void paintComponent(Graphics g) {
        final Dimension size = getSize();
        final Dimension parentSize = ((Component) caretParent).getSize();
        if (size.width <= 0 || size.height <= 0 || (caretParent.isCircular() && angdeg == null)) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (state == STATE.ON) {
            color = ON_COLOR;
        } else if (state == STATE.OFF) {
            color = OFF_COLOR;
        } else if (state == STATE.BLINK) {
            if (color == ON_COLOR) {
                color = OFF_COLOR;
            } else if (color == OFF_COLOR) {
                color = ON_COLOR;
            }
        }
        g2d.setColor(color);

        if (!caretParent.isCircular()) {
            g2d.fillRect(0, 0, size.width, size.height);
        } else {
            setBounds(0, 0, parentSize.width, parentSize.height);
            g2d.setStroke(new BasicStroke(caretParent.getCaretWidth()));

            if (angdeg != null) {
                Line2D line = createLine();
                if (line != null) {
                    g2d.draw(line);
                }
            } else {
                throw new UnsupportedOperationException("JCaret angdeg is null");
            }
        }
        g2d.setTransform(old);
    }

    public Line2D createLine() {
        Line2D ret = null;
        if (angdeg != null) {
            final Dimension parentSize = ((Component) caretParent).getSize();

            Point2D.Double centerLocal = new Point2D.Double(parentSize.width * 0.5, parentSize.height * .5);

            int caretRadius = caretParent.getCaretRadius();
            int caretHeight = caretParent.getCaretHeight();
            Point2D pt1 = MathUtil.getCoordsDeg(centerLocal, caretRadius - 1, angdeg);
            Point2D pt2 = MathUtil.getCoordsDeg(centerLocal, caretRadius - caretHeight, angdeg);

            ret = new Line2D.Double(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());

        }
        return ret;
    }

    protected void handleMouseEvent(MouseEvent e) {
        boolean s = ((JComponent) getCaretParent()).requestFocusInWindow();
        if (caretParent.isCircular()) {
            Point loc = caretParent.getCaretLocation(e.getLocationOnScreen());
            if (loc == null) {
                return;
            }

            Point pt = caretParent.getCaretPos(e.getLocationOnScreen());
            setPos(pt);
        } else {
            Point pt = caretParent.getCaretPos(e.getLocationOnScreen());
            setPos(pt);
        }
    }

    @Override
    public void setLocation(Point pt) {
        Point old = getLocation();
        if (pt != null) {
            super.setLocation(pt);
        }
        firePropertyChange("location", old, pt);
    }

    protected boolean isWithinBoundary(MouseEvent e) {
        Point locOnScreen = e.getLocationOnScreen();
        Rectangle rect = ((JComponent) caretParent).getVisibleRect();

        rect = UIUtil.convertRectToScreen(rect, (JComponent) caretParent);

        boolean ret = rect.contains(locOnScreen);

        return ret;
    }

    public static void displayInfo() {
        JLabel label = new JLabel("Please enable the edit mode ");
        label.setIcon(ImageHelper.createImageIcon(ImageNames.EDIT_16));
        label.setHorizontalTextPosition(SwingConstants.LEFT);

        DialogDescriptor.Message dd = new DialogDescriptor.Message(label, DialogDescriptor.INFORMATION_MESSAGE);
        dd.setTitle("Edit Mode Required");
        DialogDisplayer.getDefault().notify(dd);
    }

    private boolean checkMinimumLength() {
        Integer totalPos = caretParent.getTotalPos();
        if (totalPos < 2) {
            String msg = String.format(CNST.ERROR_FORMAT, "Cannot delete", "The sequence cannot be empty");
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle("Edit Sequence");
            DialogDisplayer.getDefault().notify(m);
            return false;
        } else {
            return true;
        }
    }

    protected void handleKeyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        Character keyChar = e.getKeyChar();

        if (state == STATE.OFF || pos == null) {
            return;
        }

        final Point maxPos = caretParent.getCaretMaxPos();
        if (keyCode == KeyEvent.VK_HOME) {
            if (caretParent.isCircular()) {
                return;
            }
            Point newPt = new Point(1, pos.y);
            if (newPt != null) {
                setPos(newPt);
            }
            caretParent.caretMoved(SwingConstants.WEST, e.isShiftDown(), true);
        } else if (keyCode == KeyEvent.VK_END) {
            if (caretParent.isCircular()) {
                return;
            }
            Point cur = getPos();
            setPos(new Point(maxPos.x + 1, cur.y));

            caretParent.caretMoved(SwingConstants.EAST, e.isShiftDown(), true);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            boolean success;
            Point newPt = null;
            if (caretParent.isCircular()) {
                success = true;
                if (pos.x < maxPos.x) {
                    newPt = new Point(pos.x + 1, pos.y);
                } else if (pos.x == maxPos.x) {
                    newPt = new Point(1, pos.y);
                }
            } else {
                if (pos.x <= maxPos.x) {
                    newPt = new Point(pos.x + 1, pos.y);
                    success = true;
                } else {
                    success = false;
                }
            }
            if (newPt != null) {
                setPos(newPt);
            }
            caretParent.caretMoved(SwingConstants.EAST, e.isShiftDown(), success);
        } else if (keyCode == KeyEvent.VK_LEFT) {
            boolean success;
            Point newPt = null;
            if (caretParent.isCircular()) {
                success = true;
                newPt = new Point(pos);
                if (pos.x == 1) {
                    newPt.x = maxPos.x;
                } else {
                    newPt.x = pos.x - 1;
                }
            } else {
                newPt = new Point(pos);
                if (pos.x > 1) {
                    newPt.x = pos.x - 1;
                    success = true;
                } else {
                    success = false;
                }
            }
            if (newPt != null) {
                setPos(newPt);
            }
            caretParent.caretMoved(SwingConstants.WEST, e.isShiftDown(), success);
        } else if (keyCode == KeyEvent.VK_UP) {
            boolean success;
            if (pos.y > 1) {
                setPos(new Point(pos.x, pos.y - 1));
                success = true;
            } else {
                success = false;
            }
            caretParent.caretMoved(SwingConstants.NORTH, e.isShiftDown(), success);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            boolean success;
            if (pos.y < maxPos.y) {
                setPos(new Point(pos.x, pos.y + 1));
                success = true;
            } else {
                success = false;
            }
            caretParent.caretMoved(SwingConstants.SOUTH, e.isShiftDown(), success);

        } else if (keyCode == KeyEvent.VK_DELETE) { // "Delete" btn in Windows; "fn+Delete" btn in Mac
            if (!caretParent.isEditingAllowed()) {
                displayInfo();
                return;
            }
            Loc2D selection = caretParent.getSelection();
            if (selection != null) {
                caretParent.replace(selection, "");
            } else {
                Point insertIndex = caretParent.getCaretPos();
                boolean valid = checkMinimumLength();
                if (!valid) {
                    return;
                }
                if (insertIndex != null && insertIndex.x > 0 && insertIndex.x <= maxPos.x) {
                    caretParent.delete(caretParent.getCaretPos(), true);
                }
            }
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) { // "backspace" btn in Windows; "Delete" btn in Mac
            if (!caretParent.isEditingAllowed()) {
                displayInfo();
                return;
            }
            Loc2D selection = caretParent.getSelection();
            if (selection != null) {
                caretParent.replace(selection, "");
            } else {
                boolean valid = checkMinimumLength();
                if (!valid) {
                    return;
                }
                Point insertIndex = caretParent.getCaretPos();
                if (caretParent.isCircular()) {
                    if (insertIndex != null && insertIndex.x > 0) {
                        caretParent.delete(caretParent.getCaretPos(), false);
                    }
                } else {
                    if (insertIndex != null && insertIndex.x > 1) {
                        caretParent.delete(caretParent.getCaretPos(), false);
                    }
                }
            }
        } else if (keyCode == KeyEvent.VK_INSERT) {
            setInsertMode(!isInsertMode());
        } else {
            if (!e.isControlDown() && !e.isShiftDown() && !caretParent.isEditingAllowed()) {
                displayInfo();
                return;
            }
            if (state != STATE.BLINK) {
                return;
            }

            if (isInsertMode()) {
                throw new UnsupportedOperationException();
                //caretParent.replace(loc2D, keyChar.toString().toUpperCase(Locale.ENGLISH));
            } else {
                Loc2D selection = caretParent.getSelection();
                if (selection != null) {
                    caretParent.replace(selection, keyChar.toString().toUpperCase(Locale.ENGLISH));
                } else {
                    if (!e.isControlDown() && !e.isShiftDown()) {        
                        boolean valid = caretParent.isInputValid(e);
                        if (!valid) {
                            return;
                        }
                        caretParent.insert(caretParent.getCaretPos(), keyChar.toString().toUpperCase(Locale.ENGLISH).charAt(0));
                    }
                }
            }
        }
    }
}