/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.light.Text;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
class MSAScrollListeners {

    static class ViewUICompAptr extends ComponentAdapter {

        private WeakReference<MSAScroll> msaScrollRef;

        ViewUICompAptr(MSAScroll msaScroll) {
            this.msaScrollRef = new WeakReference<MSAScroll>(msaScroll);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            msaScrollRef.get().getRowHeaderUI().repaint();
        }
    }

    static class ColumnHeaderCompAptr extends ComponentAdapter {

        private WeakReference<MSAScroll> msaScrollRef;

        ColumnHeaderCompAptr(MSAScroll msaScroll) {
            this.msaScrollRef = new WeakReference<MSAScroll>(msaScroll);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            this.msaScrollRef.get().getCornerUI().repaint();
        }
    }

    static class CaretPtyListener implements PropertyChangeListener {

        private WeakReference<MSAScroll> ref;

        public CaretPtyListener(WeakReference<MSAScroll> ref) {
            this.ref = ref;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object v = evt.getNewValue();
            String pName = evt.getPropertyName();
            JCaret caret = (JCaret) evt.getSource();
            if (pName.equals("location")) {
                if (caret.isEnabled() && caret.isBlinking()) {
                    if (v != null) {
                        Point p = (Point) v;
                        ref.get().getColumnHeaderUI().setPosIndicatorLoc(p.x);
                        UIUtil.setX(ref.get().getViewUI().getMsaComp().getPosIndicator(), p.x);
                    }
                }
            }
        }
    }

    static class MsaCompPtyListener implements PropertyChangeListener {

        private WeakReference<MSAScroll> ref;

        MsaCompPtyListener(WeakReference<MSAScroll> ref) {
            this.ref = ref;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("preferredSize")) {
                Dimension size = (Dimension) v;
                UIUtil.setPreferredHeight(ref.get().getRowHeaderUI(), size.height);
                UIUtil.setPreferredWidth(ref.get().getColumnHeaderUI(), size.width);
                ref.get().getColumnHeaderUI().revalidate();
            }
        }
    }

    static class MouseAptr extends MouseAdapter {

        private boolean dragged;
        private Point selectStart;
        private Point selectEnd;

        @Override
        public void mouseMoved(MouseEvent e) {
            updateMovingIndicator(e);
        }

        private void updateMovingIndicator(MouseEvent e) {
            MSAScroll src = (MSAScroll) e.getSource();
            ColumnHeaderUI columnUI = src.getColumnHeaderUI();
            Point caretLoc = columnUI.getCaretLocation(e.getLocationOnScreen());
            if (caretLoc != null) {
                columnUI.setMovingIndicatorLoc(caretLoc.x);
                src.getViewUI().getMsaComp().getMovingIndicator().setLocation(caretLoc.x, 0);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            dragged = true;
            updateMovingIndicator(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {

            MSAScroll src = (MSAScroll) e.getSource();
            boolean succeed = src.requestFocusInWindow();
            Point pt = e.getPoint();
            ColumnHeaderUI columnUI = src.getColumnHeaderUI();
            ViewUI viewUI = src.getViewUI();

            Rectangle columnUIRect = SwingUtilities.convertRectangle(columnUI, columnUI.getVisibleRect(), src);
            Rectangle viewUIRect = SwingUtilities.convertRectangle(src.getViewUI(), src.getViewUI().getVisibleRect(), src);
            Rectangle rowHeaderUIRect = SwingUtilities.convertRectangle(src.rowHeaderUI, src.rowHeaderUI.getVisibleRect(), src);
            Rectangle cornerUIRect = SwingUtilities.convertRectangle(src.cornerUI, src.cornerUI.getVisibleRect(), src);

            if (columnUIRect.contains(pt) || viewUIRect.contains(pt)) {
                selectStart = new Point();
                selectStart.x = columnUI.getInsertPos(e.getXOnScreen());
                if (columnUIRect.contains(pt)) {
                    selectStart.y = 1;
                } else {
                    Integer rowNo = viewUI.getMsaComp().getRowNo(e.getYOnScreen());
                    if (rowNo != null) {
                        selectStart.y = rowNo + 1;
                    }else{
                        selectStart.y = src.msaRef.get().getEntriesCount() + 1;
                    }
                }
            } else if (rowHeaderUIRect.contains(pt)) {
                Point ptConverted = SwingUtilities.convertPoint(src, pt, src.rowHeaderUI);
                Text text = src.rowHeaderUI.textList.getText(ptConverted);
                if (text != null) {                    
                    src.setSelectedRow(text.getStr());
                }
            } else if(cornerUIRect.contains(pt)){
                src.setSelectedRow("consensus");
            }
        }

        private void triggerPopup(MouseEvent e) {
            MSAScroll src = (MSAScroll) e.getSource();
            final Rectangle viewUIRect = UIUtil.convertRect(src.viewUI, src.viewUI.getVisibleRect(), src);
            final Rectangle cornerUIRect = UIUtil.convertRect(src.cornerUI, src.cornerUI.getVisibleRect(), src);
            final Rectangle columnHeaderUIRect = UIUtil.convertRect(src.columnHeaderUI, src.columnHeaderUI.getVisibleRect(), src);
            final Rectangle rowHeaderUIRect = UIUtil.convertRect(src.rowHeaderUI, src.rowHeaderUI.getVisibleRect(), src);

            final Point pt = e.getPoint();
            MSAPopup msaPopup = src.getViewUI().getMsaComp().getPopupMenu();
            String rowName = src.getRowName(pt);
            msaPopup.setRowName(rowName);
            if (viewUIRect.contains(pt)) {
            } else if (columnHeaderUIRect.contains(pt)) {
            } else if (cornerUIRect.contains(pt)) {                
            } else if (rowHeaderUIRect.contains(pt)) {
                src.rowHeaderUI.setSelectedRow(rowName);
                src.setSelectedRow(rowName);
            }

            StringList list = src.msaRef.get().getEntriesNames();
            msaPopup.setRowNames(list);
            Loc2D loc2d = src.getSelection();
            msaPopup.setSelection(loc2d);
            msaPopup.setComp(src);

            UIUtil.showPopupMenu(msaPopup, src, pt.x, pt.y);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                triggerPopup(e);
                return;
            }
            MSAScroll src = (MSAScroll) e.getSource();
            final Point releasedPt = e.getPoint();
            ColumnHeaderUI columnUI = src.getColumnHeaderUI();
            ViewUI viewUI = src.getViewUI();

            final Rectangle columnVisibleRect = SwingUtilities.convertRectangle(columnUI, columnUI.getVisibleRect(), src);
            final Rectangle viewUIRect = SwingUtilities.convertRectangle(viewUI, viewUI.getVisibleRect(), src);
            if(!viewUIRect.contains(releasedPt) && !columnVisibleRect.contains(releasedPt)){
                return;
            }
            if (dragged) {
                if (columnVisibleRect.contains(releasedPt) || viewUIRect.contains(releasedPt)) {
                    selectEnd = new Point();
                    selectEnd.x = columnUI.getInsertPos(e.getXOnScreen());

                    if (columnVisibleRect.contains(releasedPt)) {
                        selectEnd.y = 1;
                    } else {
                        final Integer rowNo = viewUI.getMsaComp().getRowNo(e.getYOnScreen());
                        if(rowNo != null){
                            selectEnd.y = rowNo + 1;
                        }else{
                            selectEnd.y = src.msaRef.get().getEntriesCount() + 1;
                        }
                    }
                    int x = Math.min(selectStart.x, selectEnd.x);
                    int x2 = Math.max(selectStart.x, selectEnd.x) - 1;
                    int y = Math.min(selectStart.y, selectEnd.y);
                    int y2 = Math.max(selectStart.y, selectEnd.y);

                    src.setSelection(new Loc2D(x, y, x2, y2));
                }
            } else {
                src.setSelection(null);
            }
            // update carets
            updateCarets(e);
            reset();
        }

        private void updateCarets(MouseEvent e) {
            MSAScroll src = (MSAScroll) e.getSource();
            final Point releasedPt = e.getPoint();
            ColumnHeaderUI columnUI = src.getColumnHeaderUI();
            ViewUI viewUI = src.getViewUI();

            Rectangle columnVisibleRect = columnUI.getVisibleRect();
            columnVisibleRect = SwingUtilities.convertRectangle(columnUI, columnVisibleRect, src);
            Rectangle viewUIRect = viewUI.getVisibleRect();
            viewUIRect = SwingUtilities.convertRectangle(viewUI, viewUIRect, src);
            if (columnVisibleRect.contains(releasedPt)) {
                Point caretLoc = columnUI.getCaretLocation(e.getLocationOnScreen());
                viewUI.getMsaComp().getCaret().setState(JCaret.STATE.OFF);
                columnUI.getCaret().setState(columnUI.isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
                columnUI.getCaret().setLocation(caretLoc);

            } else if (viewUIRect.contains(releasedPt)) {
                Point caretLoc = viewUI.getMsaComp().getCaretLocation(e.getLocationOnScreen());
                columnUI.getCaret().setState(JCaret.STATE.OFF);
                viewUI.getMsaComp().getCaret().setState(viewUI.getMsaComp().isEditingAllowed() ? JCaret.STATE.BLINK : JCaret.STATE.ON);
                viewUI.getMsaComp().getCaret().setLocation(caretLoc);
            }
        }

        private void reset() {
            dragged = false;
            selectStart = null;
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            MSAScroll src = (MSAScroll) evt.getSource();
            Object v = evt.getNewValue();
            if (pName.equals("msa")) {
                src.getViewUI().setMsa((MSA) v);
                src.getRowHeaderUI().setMsa((MSA) v);
                src.getColumnHeaderUI().setMsa((MSA) v);
                src.getCornerUI().resetPreferredWidth();
            } else if (pName.equals("colorProvider")) {
                src.getViewUI().setColorProvider((IColorProvider) v);
                src.getColumnHeaderUI().setColorProvider((IColorProvider) v);
            } else if (pName.equals("selection")) {
                Loc2D selection = src.getSelection();
                if (selection != null) {
                    int x = selection.x1;
                    int x2 = selection.x2;
                    Loc spotLight = new Loc(Math.min(x, x2), Math.max(x, x2));
                    src.getColumnHeaderUI().getColumnHeaderComp().setSpotLight(spotLight);
                    src.getViewUI().getMsaComp().setSpotLight(spotLight);

                    if (selection.y1 == 1) {
                        StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setStartSeqName("consensus");
                        src.getColumnHeaderUI().getColumnHeaderComp().setSelection(selection);
                    } else {
                        final String rowName = src.getViewUI().getMsaComp().getRowName(selection.y1 - 1);
                        StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setStartSeqName(rowName);
                        
                        src.getColumnHeaderUI().getColumnHeaderComp().setSelection(null);
                    }

                    if (selection.y2 > 1) {
                        Loc2D sel = new Loc2D(selection);
                        sel.translate(0, -1);
                        src.getViewUI().getMsaComp().setSelection(sel);
                        
                        final String rowName = src.getViewUI().getMsaComp().getRowName(selection.y2 - 1);
                        StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setEndSeqName(rowName);                        
                    } else {
                        src.getViewUI().getMsaComp().setSelection(null);
                        StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setEndSeqName("consensus");
                    }
                    StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setStart(x);
                    StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setEnd(x2);
                    StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setSelectLength(x2 - x + 1);
                    
                } else {
                    src.getColumnHeaderUI().getColumnHeaderComp().setSelection(null);
                    src.getViewUI().getMsaComp().setSelection(null);
                    src.getViewUI().getMsaComp().setSpotLight(null);
                    src.getColumnHeaderUI().getColumnHeaderComp().setSpotLight(null);                 
                    StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().clearSelection();
                }
                src.getCornerUI().selectedConsensus(false);
                src.getRowHeaderUI().setSelectedRow(null);
                
            }
        }
    }
}
