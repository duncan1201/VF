/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.general;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.ISequenceUI;
import com.gas.common.ui.util.Pref;
import com.gas.main.ui.editor.as.ASEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
class PropertiesPanelListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PropertiesPanel propertiesPanel = (PropertiesPanel) evt.getSource();
            String pName = evt.getPropertyName();
            if (pName.equals("forProtein")) {
                Boolean forProtein = propertiesPanel.getForProtein();

                propertiesPanel.doubleStrandedCheckBox.setEnabled(!forProtein);

                propertiesPanel.reinitColorCombo();
            }
        }
    }

    static class DoubleStrandedItemListener implements ItemListener {

        private PropertiesPanel p;

        DoubleStrandedItemListener(PropertiesPanel p) {
            this.p = p;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComponent src = (JComponent) e.getSource();
            int state = e.getStateChange();

            ASEditor editor = getASEditor(src);
            if (editor == null) {
                return;
            }
            editor.getAnnotatedSeq().getAsPref().setDoubleStranded(state == ItemEvent.SELECTED);

        }
    }

    private static ASEditor getASEditor(JComponent src) {
        return UIUtil.getParent(src, ASEditor.class);
    }

    static class MinimapItemListener implements ItemListener {

        private PropertiesPanel p;

        MinimapItemListener(PropertiesPanel p) {
            this.p = p;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComponent src = (JComponent) e.getSource();
            int state = e.getStateChange();
            ASEditor editor = getASEditor(src);
            if (editor == null) {
                return;
            }
            editor.getAnnotatedSeq().getAsPref().setMinimapShown(state == ItemEvent.SELECTED);
        }
    }

    static class BaseNumberItemListener implements ItemListener {

        private PropertiesPanel p;

        BaseNumberItemListener(PropertiesPanel p) {
            this.p = p;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComponent src = (JComponent)e.getSource();
            int state = e.getStateChange();

            ASEditor editor = getASEditor(src);
            if(editor == null){
                return;
            }
            editor.getAnnotatedSeq().getAsPref().setBaseNumberShown(state == ItemEvent.SELECTED);
        }
    }

    static class ColorComboListener implements ActionListener {

        private WeakReference<PropertiesPanel> ref;
        private Pref.ColorProviderPrefs pref = Pref.ColorProviderPrefs.getInstance();
        private ISequenceUI editor;

        ColorComboListener(PropertiesPanel p) {
            this.ref = new WeakReference<PropertiesPanel>(p);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (editor == null) {
                editor = UIUtil.getParent(ref.get(), ISequenceUI.class);
            }
            IColorProvider colorProvider = (IColorProvider) ref.get().getColorCombo().getSelectedItem();
            editor.setPrimarySeqColorProvider(colorProvider);
        }
    }
}
