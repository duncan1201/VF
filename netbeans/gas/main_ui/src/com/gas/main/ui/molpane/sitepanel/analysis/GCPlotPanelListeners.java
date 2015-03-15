/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.analysis;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.gc.api.GCResult;
import com.gas.domain.core.gc.api.IGCService;
import com.gas.common.ui.util.Pref;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.main.ui.molpane.MolPane;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;

/**

 @author dq
 */
class GCPlotPanelListeners {

    private static IGCService gcService = Lookup.getDefault().lookup(IGCService.class);

    private static GCPlotPanel getGCPlotPanel(JComponent src) {
        GCPlotPanel panel = UIUtil.getParent(src, GCPlotPanel.class);
        return panel;
    }

    private static MolPane getMolPane(JComponent src) {
        MolPane molPane = UIUtil.getParent(src, MolPane.class);
        return molPane;
    }

    private static void calculateAndDraw(JComponent src) {
        GCPlotPanel gcPlotPanel = getGCPlotPanel(src);
        if (gcPlotPanel == null) {
            return;
        }
        MolPane molPane = getMolPane(src);
        if (molPane == null) {
            return;
        }
        boolean selected = gcPlotPanel.getVisibleBox().isSelected();

        final AnnotatedSeq as = molPane.getAs();
        if (selected) {
            if (as == null) {
                System.out.print("");
            }
            final String seq = as.getSiquence().getData();
            Integer windowSize = (Integer) gcPlotPanel.getWindowSizeSpinner().getValue();
            GCResult gcResult = gcService.calculate(seq, windowSize, as.isCircular());
            as.setGcResult(gcResult);
            molPane.setGCResult(gcResult);
        } else {
            as.setGcResult(null);
            molPane.setGCResult(null);
        }
    }
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            GCPlotPanel src = (GCPlotPanel)evt.getSource();
            String name = evt.getPropertyName();
            if(name.equals("gcResult")){
                src.populateUI();
            }
        }
    
    }

    static class VisibleListener implements ItemListener {

        WeakReference<GCPlotPanel> panelRef;
        
        VisibleListener(GCPlotPanel panel){
            panelRef = new WeakReference<GCPlotPanel>(panel);
        }
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(panelRef.get().populatingUI){
                return;
            }
            JCheckBox src = (JCheckBox) e.getSource();            
            calculateAndDraw(src);
            IASEditor editor = UIUtil.getParent(src, IASEditor.class);
            if(editor != null){
                editor.setCanSave();
            }
        }
    }

    static class HeightListener implements ChangeListener {

        private WeakReference<GCPlotPanel> panelRef;
        private WeakReference<MolPane> molPaneRef;

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner) e.getSource();
            if (panelRef == null || panelRef.get() == null) {
                GCPlotPanel panel = UIUtil.getParent(src, GCPlotPanel.class);
                panelRef = new WeakReference<GCPlotPanel>(panel);
            }
            if (panelRef.get() == null || panelRef.get().populatingUI) {
                return;
            }
            if (molPaneRef == null || molPaneRef.get() == null) {
                MolPane molPane = UIUtil.getParent(src, MolPane.class);
                molPaneRef = new WeakReference<MolPane>(molPane);
            }
            if (molPaneRef.get() == null) {
                return;
            }
            Integer v = (Integer) src.getValue();
            Pref.CommonPtyPrefs.getInstance().setGCHeight(v);
            final AnnotatedSeq as = molPaneRef.get().getAs();
            if (as != null && as.isCircular()) {
                molPaneRef.get().getRingPane().getRingGraphPanel().revalidate();
            } else if (as != null && !as.isCircular()) {
                molPaneRef.get().getGraphPane().getGraphPanel().revalidate();
            }

        }
    }

    static class WindowSizeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {            
            JSpinner src = (JSpinner) e.getSource();
            GCPlotPanel gcPlotPanel = UIUtil.getParent(src, GCPlotPanel.class);
            if(gcPlotPanel.populatingUI){
                return;
            }
            Integer windowSize = (Integer) src.getValue();
            final MolPane molPane = getMolPane(src);
            final Integer length = molPane.getAs().getLength();
            if ((windowSize - 2) > length) {
                src.setValue(windowSize - 2);
            } else {
                calculateAndDraw(src);
            }
            IASEditor editor = UIUtil.getParent(src, IASEditor.class);
            if(editor != null){
                editor.setCanSave();
            }            
        }
    }
}
