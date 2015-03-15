/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.ckpanel.ChromatogramComp2;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Map;
import javax.swing.JLayeredPane;

/**
 *
 * @author dq
 */
class MainCompListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            MainComp src = (MainComp) evt.getSource();
            if (pName.equals("reads")) {
                UIUtil.removeComponentsInLayer(src.layeredPane, JLayeredPane.DEFAULT_LAYER);
                src.setSelection(null);
                src.comp2s.clear();
                //getAssemblySPane().getHorizontalScrollBar().setValue(0);
                Collections.sort(src.reads, new ChromatogramComp2.Sorter());
                for (ChromatogramComp2.Read read : src.reads) {

                    ChromatogramComp2 p = new ChromatogramComp2();

                    p.setRead(read);
                    p.setGraphVisible(true);
                    src.comp2s.add(p);
                    src.layeredPane.add(p);
                    src.layeredPane.setLayer(p, JLayeredPane.DEFAULT_LAYER);
                }
                src.layeredPane.revalidate();
                src.requestFocusInWindow();
            } else if (pName.equals("baseFont")) {
                for (ChromatogramComp2 p : src.comp2s) {
                    p.setBaseFont(src.baseFont);
                }
                src.revalidate();
            } else if (pName.equals("variantMapMdl")) {
                Map<String, VariantMapMdl.Read> readsMap = src.variantMapMdl.getReadMap();

                for (ChromatogramComp2 p : src.comp2s) {
                    String name = p.getRead().getName();
                    VariantMapMdl.Read variantRead = readsMap.get(name);
                    p.setVariants(variantRead);
                }
            } else if (pName.equals("selection")) {
                MainComp.Layout layout = (MainComp.Layout) src.getLayout();
                layout.layoutOverlay(src);
            } else if (pName.equals("layoutState")) {
                if(src.getLayoutState() == CNST.LAYOUT.ENDING){
                    AssemblyScroll scroll = UIUtil.getParent(src, AssemblyScroll.class);                    
                    RowHeaderView rowHeaderView = scroll.getRowHeaderView();
                    UIUtil.setPreferredHeight(rowHeaderView, src.getPreferredSize().height);
                    rowHeaderView.repaint();
                }
            }
        }
    }

    static class CompListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            MainComp src = (MainComp) e.getSource();
            src.layeredPane.setBounds(src.getBounds());
        }
    }
}
