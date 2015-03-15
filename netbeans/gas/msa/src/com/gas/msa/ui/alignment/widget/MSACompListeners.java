/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class MSACompListeners {

    static class CaretPtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            JCaret src = (JCaret) evt.getSource();
            MSAComp parent = (MSAComp) src.getCaretParent();

            String name = evt.getPropertyName();
            if (name.equals("pos")) {
                final Point pos = src.getPos();
                if(pos != null){                    
                    final String rowName = parent.getRowName(pos.y);
                    StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setCaretSeqName(rowName);
                }                
            } else if (name.equals("state")) {
                if (src.getState() == JCaret.STATE.ON || src.getState() == JCaret.STATE.BLINK) {
                    final String rowName = parent.getRowName(src.getPos().y);
                    StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setCaretSeqName(rowName);
                }
            }
        }
    }

    static class PtyListener implements PropertyChangeListener {

        MSAScroll msaScroll;
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            MSAComp src = (MSAComp) evt.getSource();
            if (name.equals("colorProvider")) {
                src.alignmentList.setColorProvider((IColorProvider) v);
                src.repaint();
            } else if (name.equals("editingAllowed")) {
                JCaret.STATE oldState = src.getCaret().getState();
                if (oldState != JCaret.STATE.OFF) {
                    src.getCaret().setState(src.isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
                }
            } else if (name.equals("msa")) {
                MSA msa = (MSA)v;
                src.setZoom(msa.getMsaSetting().getZoom());
                src.createUIObjects();
                src.repaint();
            } else if (name.equals("preferredSize")) {
                Dimension s = (Dimension) v;
                UIUtil.setHeight(src.getPosIndicator(), s.height);
                UIUtil.setHeight(src.getMovingIndicator(), s.height);

                UIUtil.setPreferredWidth(src.getViewUI(), s.width);
                src.revalidate();
            } else if (name.equals("scaleUpSeqLogo")) {
            } else if (name.equals("seqLogoHeight")) {
            } else if (name.equals("zoom")) {
                Integer width = src.calculatePreferredWidth();
                if(width != null){
                    UIUtil.setPreferredWidth(src, width);
                    UIUtil.setWidth(src, width);
                    src.revalidate();
                }else{
                    src.revalidate();
                }
            } else if (name.equals("spotLight")) {
                src.overlei.setSelection(src.spotLight);
                src.repaint();
            } else if (name.equals("selection")) {
                src.alignmentList.setSelection(src.selection);
                src.repaint();
            } else if (name.equals("paintVisibleOnly")) {
                src.repaint();
            } else if (name.equals("paintState")) {
                CNST.PAINT state = (CNST.PAINT) v;
                if (state == CNST.PAINT.ENDING) {

                    JCaret caret = src.getCaret();
                    if (caret.getPos() != null) {
                        Point newLoc = src.getCaretLocationByPos(caret.getPos());
                        caret.setLocation(newLoc);
                    }
                    if(msaScroll == null){
                        msaScroll = UIUtil.getParent(caret, MSAScroll.class);
                    }
                    if(!msaScroll.getRowHeaderUI().isRectSet()){
                        msaScroll.getRowHeaderUI().repaint();
                    }
                }
            }
        }
    }
}
