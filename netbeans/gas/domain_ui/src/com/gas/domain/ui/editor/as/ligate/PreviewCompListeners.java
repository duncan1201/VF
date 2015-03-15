/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.UIUtil;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author dq
 */
class PreviewCompListeners {
    static class MouseAdpt extends MouseAdapter {
        
        @Override
        public void mousePressed(MouseEvent e) {
            PreviewComp previewComp = (PreviewComp)e.getSource();
            EndsView endsView = previewComp.endsViewList.getEndsView(e.getPoint());
            LigatePanel ligatePanel = UIUtil.getParent(previewComp, LigatePanel.class);
            if(endsView != null){
                previewComp.setSelectedName(endsView.getAs().getName()); 
                ligatePanel.getLigateTable().setSelected(endsView.getAs().getName());
            }else{
                previewComp.setSelectedName(null);
                ligatePanel.getLigateTable().clearSelection();
            }
            previewComp.repaint();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            PreviewComp previewComp = (PreviewComp)e.getSource();
            EndsView endsView = previewComp.endsViewList.getEndsView(e.getPoint());
            if(endsView != null){
                previewComp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }else{
                previewComp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
}
