/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ckpanel;

import com.gas.common.ui.caret.CaretParentAdapter;
import com.gas.common.ui.caret.CaretParentEvent;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.Trace;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 *
 * @author dq
 */
class ChromatogramCompListeners2 {
    
    static class CaretAdpt extends CaretParentAdapter{
        
            WeakReference<ChromatogramComp2> ref;
        
            CaretAdpt(WeakReference<ChromatogramComp2> ref){
                this.ref = ref;
            }
        
            @Override
            public void onDelete(CaretParentEvent event) {
                ref.get().centerGraph.delete(event.getPos(), event.getData().length());
            }

            @Override
            public void onInsert(CaretParentEvent event) {
                System.out.println("onInsert...");
            }

            @Override
            public void onReplace(CaretParentEvent event) {
                // do nothing
            }        
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ChromatogramComp2 src = (ChromatogramComp2) evt.getSource();
            String pName = evt.getPropertyName();
            if (pName.equals("visibleA")
                    || pName.equals("visibleT")
                    || pName.equals("visibleC")
                    || pName.equals("visibleG")
                    || pName.equals("visibleQv")
                    || pName.equals("offsetVisible")) {
                src.getCenterGraph().repaint();
            } else if (pName.equals("graphVisible")) {
                src.revalidate();
            } else if (pName.equals("read")) {
                src.brickComp.setData(src.read.getBases());

                Kromatogram k = src.read.getKromatogram();
                if (k != null) {
                    src.centerGraph.setQvs(k.getQualityValues());
                    src.centerGraph.setOffsets(k.getOffsets());
                    Iterator<Trace> itr = k.getTraces().values().iterator();
                    while (itr.hasNext()) {
                        Trace trace = itr.next();
                        src.centerGraph.getIntegerLists().add(trace.toIntegerList());
                    }
                }
            } else if (pName.equals("baseFont")) {
                src.brickComp.setFont(src.baseFont);

                src.revalidate();
            } else if (pName.equals("variants")) {
                src.brickComp.setVariants(src.variants);
            }
        }
    }
}
