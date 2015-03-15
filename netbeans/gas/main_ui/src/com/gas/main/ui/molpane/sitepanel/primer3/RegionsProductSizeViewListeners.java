/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.main.ui.molpane.MolPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.ref.WeakReference;
import javax.swing.JButton;
import javax.swing.JCheckBox;

/**
 *
 * @author dq
 */
class RegionsProductSizeViewListeners {

    static class CopyLocListener implements ActionListener{

        WeakReference<RegionsProductSizeView> panelRef;
        
        CopyLocListener(RegionsProductSizeView view){
            this.panelRef = new WeakReference<RegionsProductSizeView>(view);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton)e.getSource();
            final String cmd = src.getActionCommand();
            MolPane molPane = UIUtil.getParent(src, MolPane.class);
            final LocList locList =molPane.getSelections();
            if(locList.isEmpty()){
                return;
            }
            Loc loc = locList.get(0);            
            if(cmd.equalsIgnoreCase("target")){
                panelRef.get().sequenceTargetRegionFrom.setValue(loc.getStart());
                panelRef.get().sequenceTargetRegionTo.setValue(loc.getEnd());
            }else if(cmd.equalsIgnoreCase("included")){
                panelRef.get().includedRegionFrom.setValue(loc.getStart());
                panelRef.get().includedRegionTo.setValue(loc.getEnd());            
            }
        }
    }
    
    static class CheckBoxListener implements ItemListener {

        private WeakReference<RegionsProductSizeView> ref;

        protected CheckBoxListener(RegionsProductSizeView view) {
            ref = new WeakReference<RegionsProductSizeView>(view);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();
            final String cmd = src.getActionCommand();
            if (cmd.equalsIgnoreCase("productSize")) {
                ref.get().getPRIMER_PRODUCT_SIZE_RANGE_MIN().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                ref.get().getPRIMER_PRODUCT_SIZE_RANGE_MAX().setEnabled(e.getStateChange() == ItemEvent.SELECTED);                
            } else if (cmd.equalsIgnoreCase("productOptSize")) {
                ref.get().getPRIMER_PRODUCT_OPT_SIZE().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            } else if (cmd.equalsIgnoreCase("includedRegion")) {
                ref.get().getIncludedRegionFrom().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                ref.get().getIncludedRegionTo().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                ref.get().copyIncludedBtn.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            } else if (cmd.equalsIgnoreCase("targetRegion")) {
                ref.get().getSequenceTargetRegionFrom().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                ref.get().getSequenceTargetRegionTo().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                ref.get().copyTargetBtn.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        }
    }
}
