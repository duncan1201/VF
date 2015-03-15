/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.domain.core.primer3.OligoElement;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author dq
 */
class OEPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            OEPanel src = (OEPanel) evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("oligoElement") || name.equals("oligo")) {
                if(src.oligo == null || src.oligoElement == null){
                    return;
                }
                NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                NumberFormat nfPercent = NumberFormat.getPercentInstance(Locale.ENGLISH);
                nfPercent.setMaximumFractionDigits(2);
                nfPercent.setMinimumFractionDigits(2);

                OligoElement oe = (OligoElement) v;
                String nameSeq = null;
                if (oe.getName().equalsIgnoreCase("internal")) {
                    nameSeq = String.format("DNA Probe %d(%d-%d)", oe.getNo() + 1, oe.calculateStart(), oe.calculateEnd());
                } else if (oe.getName().equalsIgnoreCase("left")) {
                    nameSeq = String.format("Forward Primer %d(%d-%d)", oe.getNo() + 1, oe.calculateStart(), oe.calculateEnd());
                } else if (oe.getName().equalsIgnoreCase("right")) {
                    nameSeq = String.format("Reverse Primer %d(%d-%d)", oe.getNo() + 1, oe.calculateStart(), oe.calculateEnd());
                }
                src.nameLabelRef.get().setText(nameSeq);
                if (oe.getTail() != null && !oe.getTail().isEmpty()) {
                    src.seqLabelRef.get().setText(String.format("%s-%s", oe.getTail(), oe.getSeq()));
                } else {
                    src.seqLabelRef.get().setText(String.format("%s", oe.getSeq()));
                }
                src.tmLabelRef.get().setText(String.format("TM:%s", nf.format(oe.getTm())));
                src.gcLabelRef.get().setText(String.format("GC:%s", nfPercent.format(oe.getGc() / 100)));
                src.selfAnyLabelRef.get().setText(String.format("Self Complementarity:%s", nf.format(oe.getSelfAny())));
                src.selfEndLabelRef.get().setText(String.format("3' Self Complementarity:%s", nf.format(oe.getSelfAny())));
                if(src.oligo.getComplAny() != null){
                    src.complAnyLabelRef.get().setText(String.format("Pair Complementarity:%s", nf.format(src.oligo.getComplAny())));
                }else{
                    src.complAnyLabelRef.get().setText("");
                }
                if(src.oligo.getComplEnd() != null){
                    src.complEndLabelRef.get().setText(String.format("3' Pair Complementarity:%s", nf.format(src.oligo.getComplEnd())));
                }else{
                    src.complEndLabelRef.get().setText("");
                }                
                src.hairpinLabelRef.get().setText(String.format("Hairpin:%s", nf.format(oe.getHairpin())));
            }
        }
    }
}
