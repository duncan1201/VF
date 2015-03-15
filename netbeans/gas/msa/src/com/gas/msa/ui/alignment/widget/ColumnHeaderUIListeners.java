/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class ColumnHeaderUIListeners {

    static class CaretPtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            JCaret src = (JCaret) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("pos")) {
                StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setCaretSeqName("consensus");
            } else if (src.getState() == JCaret.STATE.ON || src.getState() == JCaret.STATE.BLINK) {
                StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setCaretSeqName("consensus");
            }
        }
    }

    static class CompAdptr extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            ColumnHeaderUI src = (ColumnHeaderUI) e.getSource();
            if (src.getColumnHeaderComp() != null && src.getColumnHeaderComp().getRulerHeight() != null) {
                int height = src.getHeight() - src.getColumnHeaderComp().getRulerHeight();
                UIUtil.setHeight(src.getPosIndicator(), height);
                UIUtil.setHeight(src.getMovingIndicator(), height);
            }
        }
    }

    static class CompPtyListener implements PropertyChangeListener {

        WeakReference<ColumnHeaderUI> ref;

        CompPtyListener(WeakReference<ColumnHeaderUI> ref) {
            this.ref = ref;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("preferredSize")) {
                Dimension size = (Dimension) v;
                UIUtil.setPreferredHeight(ref.get(), size.height);
                ref.get().revalidate();
            }
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ColumnHeaderUI src = (ColumnHeaderUI) evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("colorProvider")) {
                ColumnHeaderComp comp = src.getColumnHeaderComp();
                comp.setColorProvider((IColorProvider) v);
            } else if (pName.equals("editingAllowed")) {
                JCaret.STATE oldState = src.getCaret().getState();
                if (oldState != JCaret.STATE.OFF) {
                    src.getCaret().setState(src.isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
                }
            } else if (pName.equals("msa")) {
                ColumnHeaderComp comp = src.getColumnHeaderComp();
                comp.setMsa((MSA) v);
            }
        }
    }
}
