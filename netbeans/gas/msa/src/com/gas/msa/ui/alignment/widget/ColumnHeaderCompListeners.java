/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class ColumnHeaderCompListeners {

    static class PtyListener implements PropertyChangeListener {

        WeakReference<ColumnHeaderUI> columnHeaderUIRef;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ColumnHeaderComp src = (ColumnHeaderComp) evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("colorProvider")) {
                src.getConsensusList().setColorProvider((IColorProvider) v);
                src.getSeqLogoUI().setColorProvider((IColorProvider) v);
                src.repaint();
            } else if (name.equals("spotLight")) {
                src.getOverlei().setSelection(src.getSpotLight());
                src.getLinearRuler().setSelection(src.getSpotLight());
                src.repaint();
            } else if (name.equals("selection")) {
                if (v != null) {
                    Loc2D loc2d = (Loc2D) v;
                    src.getConsensusList().setSelection(loc2d.toLoc());
                } else {
                    src.getConsensusList().setSelection(null);
                }
                src.repaint();
            } else if (name.equals("paintState")) {
                CNST.PAINT state = (CNST.PAINT) v;
                if (state == CNST.PAINT.ENDING) {

                    JCaret caret = getColumnHeaderUI(src).getCaret();
                    if (caret.getPos() != null) {
                        Point newLoc = src.getConsensusList().getCaretLoc(caret.getPos().x);
                        caret.setLocation(newLoc);
                    }
                }
                MSAScroll msaScroll = UIUtil.getParent(src, MSAScroll.class);
                msaScroll.getCornerUI().repaint();
            } else if (name.equals("paintVisibleOnly")) {
                src.repaint();
            }
        }

        ColumnHeaderUI getColumnHeaderUI(ColumnHeaderComp src) {
            if (columnHeaderUIRef == null || columnHeaderUIRef.get() == null) {
                ColumnHeaderUI columnHeaderUI = UIUtil.getParent(src, ColumnHeaderUI.class);
                columnHeaderUIRef = new WeakReference<ColumnHeaderUI>(columnHeaderUI);
            }
            return columnHeaderUIRef.get();
        }
    }
}
