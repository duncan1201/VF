/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.common.ui.button.WideComboBox;
import com.gas.domain.core.geneticCode.api.CodonList;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.orf.api.ORFParam;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class ORFPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            ORFPanel src = (ORFPanel) evt.getSource();            
            if (name.equals("orfResult")) {
                ORFParam orfParam = src.orfResult.getOrfParams();
                if(orfParam.getFrames().isEmpty()){
                    orfParam.setFrames(new int[]{-1,-2,-3, 1, 2, 3});
                }
                src.populateUI(orfParam);
            }
        }
    }

    static class GeneticCodeComboListener implements ActionListener {

        private WeakReference<ORFPanel> orfPanelRef;

        public GeneticCodeComboListener(ORFPanel orfPanel) {
            this.orfPanelRef = new WeakReference<ORFPanel>(orfPanel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            WideComboBox src = (WideComboBox) e.getSource();
            GeneticCodeTable table = (GeneticCodeTable) src.getSelectedItem();
            CodonList startCodons = table.getStartCodons();
            CodonList stopCodons = table.getStopCodons();
            orfPanelRef.get().getStartCodonsField().setText(startCodons.getBases().toString(' '));
            orfPanelRef.get().getStopCodonsField().setText(stopCodons.getBases().toString(' '));
            orfPanelRef.get().save();
        }
    }
}
