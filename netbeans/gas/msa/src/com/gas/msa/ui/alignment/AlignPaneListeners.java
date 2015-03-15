/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment;

import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.Pref;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.pref.MSAPref;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class AlignPaneListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            AlignPane src = (AlignPane) evt.getSource();
            if (pName.equals("msa")) {
                MSA msa = (MSA) v;  
                src.getMsaScroll().setMsa(msa);
                src.getCtrlPane().setMsa(msa);                  
                boolean dna = msa.isDnaByGuess();
                MSAPref.KEY key = dna ? MSAPref.KEY.DNA : MSAPref.KEY.PROTEIN;
                String name = MSAPref.getInstance().getColorProviderName(key);
                IColorProvider p = ColorProviderFetcher.getColorProvider(name);
                src.setColorProvider(p);
            } else if (pName.equals("colorProvider")) {
                src.getMsaScroll().setColorProvider((IColorProvider) v);
            } else if (pName.equals("zoom")) {
                Integer z = (Integer) v;                
                src.getMsaScroll().getViewUI().getMsaComp().setZoom(z);
            }
        }
    }

    static class PrefListener implements PropertyChangeListener {

        private WeakReference<AlignPane> ref;

        public PrefListener(AlignPane ref) {
            this.ref = new WeakReference<AlignPane>(ref);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("editable")) {
                Boolean editable = Pref.CommonPtyPrefs.getInstance().getEditable();
                ref.get().getMsaScroll().getColumnHeaderUI().setEditingAllowed(editable);
                ref.get().getMsaScroll().getViewUI().getMsaComp().setEditingAllowed(editable);
            }
        }
    }

    static class MSAPrefListener implements PropertyChangeListener {

        private AlignPane alignpane;

        public MSAPrefListener(AlignPane ref) {
            this.alignpane = ref;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            Object v = evt.getNewValue();
            String pName = evt.getPropertyName();
            if (pName.equals("colorProviderName")) {
                String name = (String) v;
                IColorProvider p = ColorProviderFetcher.getColorProvider(name);
                alignpane.setColorProvider(p);
            }
        }
    }
}
