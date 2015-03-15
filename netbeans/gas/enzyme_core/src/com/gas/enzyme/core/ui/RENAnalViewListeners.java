/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui;

import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.enzyme.core.ui.actions.DigestAction;
import java.awt.Color;
import java.lang.ref.WeakReference;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dq
 */
class RENAnalViewListeners {
    static class TableSelectListener implements ListSelectionListener{
        
        private WeakReference<RENAnalView> renAnalysisViewRef;
        private final String COLOR = '#' + ColorUtil.toHex(Color.DARK_GRAY);
        private String NO_SELECTION_FORMAT = "<html><font color=%s>Digest (no enzymes selected)</font></html>";
        private String FORMAT = "<html>Digest <font color=%s>using %s</font></html>";
        
        protected TableSelectListener(RENAnalView renAnalysisView){
            this.renAnalysisViewRef = new WeakReference<RENAnalView>(renAnalysisView);
        }
        
        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean adjusting = e.getValueIsAdjusting();
            if(!adjusting){
                RMap.EntryList entryList = renAnalysisViewRef.get().getSitesFoundPanel().getSelectedRMapEntries();               
                JButton btn = renAnalysisViewRef.get().getDigestButton();
                btn.setEnabled(!entryList.isEmpty());
                final DigestAction digestAction = (DigestAction)btn.getAction();
                IMolPane molPane = UIUtil.getParent(renAnalysisViewRef.get(), IMolPane.class);
                digestAction.setAs(molPane.getAs());
                digestAction.setSelectedEntries(entryList);
                if(entryList.isEmpty()){
                    digestAction.putValue(Action.NAME, String.format(NO_SELECTION_FORMAT, COLOR));
                }else{                
                    digestAction.putValue(Action.NAME, String.format(FORMAT, COLOR, entryList.getDisplayNames(true)));
                }
            }
        }
    }
}
