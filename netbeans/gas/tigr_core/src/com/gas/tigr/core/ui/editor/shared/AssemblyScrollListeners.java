/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.caret.CaretMoveEvent;
import com.gas.common.ui.caret.CaretParentAdapter;
import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.editor.TigrPtPanel;
import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class AssemblyScrollListeners {

    static class CaretAdpt extends CaretParentAdapter {

        @Override
        public void onMove(CaretMoveEvent event) {
            Object src = event.getSource();
            Point pos = event.getPos();
            CaretMoveEvent.DIR dir = event.getDir();

        }
    }

    static class ScrollBarListener implements ChangeListener {

        WeakReference<AssemblyScroll> ref;

        public ScrollBarListener(WeakReference<AssemblyScroll> ref) {
            this.ref = ref;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            TigrPtPanel panel = UIUtil.getParent(ref.get(), TigrPtPanel.class);
            if (panel == null) {
                return;
            }
            BoundedRangeModel model = (BoundedRangeModel) e.getSource();
            float start = 1.0f * model.getValue() / model.getMaximum();
            float width = 1.0f * model.getExtent() / model.getMaximum();
            panel.setScrollValueAdjusting(true);
            panel.getMiniMap().setOverlayStart(start);
            panel.getMiniMap().setOverlayWidth(width);
            panel.setScrollValueAdjusting(false);
        }
    }

    static class AwtListener implements AWTEventListener {

        AssemblyScroll ref;

        AwtListener(AssemblyScroll ref) {
            this.ref = ref;
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if(!ref.isShowing()){
                return;
            }
            if (event instanceof MouseEvent) {
                MouseEvent me = (MouseEvent) event;
                if (me.getID() == MouseEvent.MOUSE_PRESSED) {
                    Point ptOnScreen = me.getLocationOnScreen();

                    Boolean isWithin = UIUtil.isWithin(ref, ptOnScreen);
                    if (isWithin != null && isWithin) {
                        ref.getMainComp().requestFocusInWindow();
                        ICaretParent caretParent = ref.getCaretParent(ptOnScreen);
                        if (caretParent != null) {
                            ref.setCaretEnablement(caretParent);
                        }
                    }
                }
            }
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AssemblyScroll src = (AssemblyScroll) evt.getSource();
            String pName = evt.getPropertyName();
            if (pName.equals("scale")) {
                int desiredWidth = src.getColumnHeaderView().getDesiredWidth();
                UIUtil.setPreferredWidth(src.getColumnHeaderView(), desiredWidth);
                UIUtil.setPreferredWidth(src.mainComp, desiredWidth);
            }
        }
    }
    
    static class CompAdpt extends ComponentAdapter {
        
        @Override
        public void componentResized(ComponentEvent e) {
            AssemblyScroll src = (AssemblyScroll)e.getSource();
            src.getHorizontalScrollBar().setValue(0);            
            src.getMainComp().revalidate();
        }
    }
}
