/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.gas.common.ui.test2;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.beans.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import java.util.EventListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import sun.swing.SwingUtilities2;

/**
 * A default implementation of Caret.  The caret is rendered as
 * a vertical line in the color specified by the CaretColor property 
 * of the associated JTextComponent.  It can blink at the rate specified
 * by the BlinkRate property.
 * <p>
 * This implementation expects two sources of asynchronous notification.
 * The timer thread fires asynchronously, and causes the caret to simply
 * repaint the most recent bounding box.  The caret also tracks change
 * as the document is modified.  Typically this will happen on the
 * event dispatch thread as a result of some mouse or keyboard event.
 * The caret behavior on both synchronous and asynchronous documents updates
 * is controlled by <code>UpdatePolicy</code> property. The repaint of the
 * new caret location will occur on the event thread in any case, as calls to
 * <code>modelToView</code> are only safe on the event thread.
 * <p>
 * The caret acts as a mouse and focus listener on the text component
 * it has been installed in, and defines the caret semantics based upon
 * those events.  The listener methods can be reimplemented to change the 
 * semantics.
 * By default, the first mouse button will be used to set focus and caret
 * position.  Dragging the mouse pointer with the first mouse button will
 * sweep out a selection that is contiguous in the model.  If the associated
 * text component is editable, the caret will become visible when focus
 * is gained, and invisible when focus is lost.
 * <p>
 * The Highlighter bound to the associated text component is used to 
 * render the selection by default.  
 * Selection appearance can be customized by supplying a
 * painter to use for the highlights.  By default a painter is used that
 * will render a solid color as specified in the associated text component
 * in the <code>SelectionColor</code> property.  This can easily be changed
 * by reimplementing the 
 * <a href="#getSelectionHighlighter">getSelectionHighlighter</a>
 * method.
 * <p>
 * A customized caret appearance can be achieved by reimplementing
 * the paint method.  If the paint method is changed, the damage method
 * should also be reimplemented to cause a repaint for the area needed
 * to render the caret.  The caret extends the Rectangle class which
 * is used to hold the bounding box for where the caret was last rendered.
 * This enables the caret to repaint in a thread-safe manner when the
 * caret moves without making a call to modelToView which is unstable
 * between model updates and view repair (i.e. the order of delivery 
 * to DocumentListeners is not guaranteed).
 * <p>
 * The magic caret position is set to null when the caret position changes.
 * A timer is used to determine the new location (after the caret change).
 * When the timer fires, if the magic caret position is still null it is
 * reset to the current caret position. Any actions that change
 * the caret position and want the magic caret position to remain the
 * same, must remember the magic caret position, change the cursor, and
 * then set the magic caret position to its original value. This has the
 * benefit that only actions that want the magic caret position to persist
 * (such as open/down) need to know about it.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author  Timothy Prinzing
 * @version %I% %G%
 * @see     Caret
 */
public class MyDefaultCaret extends DefaultCaret {

    /**
     * Renders the caret as a vertical line.  If this is reimplemented
     * the damage method should also be reimplemented as it assumes the
     * shape of the caret is a vertical line.  Sets the caret color to
     * the value returned by getCaretColor().
     * <p>
     * If there are multiple text directions present in the associated
     * document, a flag indicating the caret bias will be rendered.
     * This will occur only if the associated document is a subclass
     * of AbstractDocument and there are multiple bidi levels present
     * in the bidi element structure (i.e. the text has multiple
     * directions associated with it).
     *
     * @param g the graphics context
     * @see #damage
     */
    public void paint(Graphics g) {
        if (isVisible()) {
            try {
                JTextComponent component = getComponent();
                TextUI mapper = component.getUI();
                int dot = getDot();
                Position.Bias dotBias = getDotBias();
                Rectangle r = mapper.modelToView(component, dot, dotBias);

                if ((r == null) || ((r.width == 0) && (r.height == 0))) {
                    return;
                }
                if (width > 0 && height > 0
                        && !this._contains(r.x, r.y, r.width, r.height)) {
                    // We seem to have gotten out of sync and no longer
                    // contain the right location, adjust accordingly.
                    Rectangle clip = g.getClipBounds();

                    if (clip != null && !clip.contains(this)) {
                        // Clip doesn't contain the old location, force it
                        // to be repainted lest we leave a caret around.
                        repaint();
                    }
                    // This will potentially cause a repaint of something
                    // we're already repainting, but without changing the
                    // semantics of damage we can't really get around this.
                    damage(r);
                }
                g.setColor(component.getCaretColor());
                //int paintWidth = getCaretWidth(r.height);
                int paintWidth = 1;
                r.x -= paintWidth >> 1;
                g.fillRect(r.x, r.y, paintWidth, r.height);

                // see if we should paint a flag to indicate the bias
                // of the caret.  
                // PENDING(prinz) this should be done through
                // protected methods so that alternative LAF 
                // will show bidi information.
                Document doc = component.getDocument();
                /*
                if (doc instanceof AbstractDocument) {
                    Element bidi = ((AbstractDocument) doc).getBidiRootElement();
                    if ((bidi != null) && (bidi.getElementCount() > 1)) {
                        // there are multiple directions present.
                        flagXPoints[0] = r.x + ((dotLTR) ? paintWidth : 0);
                        flagYPoints[0] = r.y;
                        flagXPoints[1] = flagXPoints[0];
                        flagYPoints[1] = flagYPoints[0] + 4;
                        flagXPoints[2] = flagXPoints[0] + ((dotLTR) ? 4 : -4);
                        flagYPoints[2] = flagYPoints[0];
                        g.fillPolygon(flagXPoints, flagYPoints, 3);
                    }
                }
                 */
                 
            } catch (BadLocationException e) {
                // can't render I guess
                //System.err.println("Can't render cursor");
            }
        }
    }

    // Rectangle.contains returns false if passed a rect with a w or h == 0,
    // this won't (assuming X,Y are contained with this rectangle).
    private boolean _contains(int X, int Y, int W, int H) {
        int w = this.width;
        int h = this.height;
        if ((w | h | W | H) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if any dimension is zero, tests below must return false...
        int x = this.x;
        int y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        if (W > 0) {
            w += x;
            W += X;
            if (W <= X) {
                // X+W overflowed or W was zero, return false if...
                // either original w or W was zero or
                // x+w did not overflow or
                // the overflowed x+w is smaller than the overflowed X+W
                if (w >= x || W > w) {
                    return false;
                }
            } else {
                // X+W did not overflow and W was not zero, return false if...
                // original w was zero or
                // x+w did not overflow and x+w is smaller than X+W
                if (w >= x && W > w) {
                    return false;
                }
            }
        } else if ((x + w) < X) {
            return false;
        }
        if (H > 0) {
            h += y;
            H += Y;
            if (H <= Y) {
                if (h >= y || H > h) {
                    return false;
                }
            } else {
                if (h >= y && H > h) {
                    return false;
                }
            }
        } else if ((y + h) < Y) {
            return false;
        }
        return true;
    }
}
