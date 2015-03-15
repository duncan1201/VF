/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.PrimerAdapter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author dq
 */
class SensePanelListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        private WeakReference<SensePanel> ref;

        protected PtyChangeListener(WeakReference<SensePanel> ref) {
            this.ref = ref;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final String pName = evt.getPropertyName();
            Object newValue = evt.getNewValue();
            if (pName.equals("sdIncluded")) {
                ref.get().sdBtn.setSelected((Boolean) newValue);
            } else if (pName.equals("kozakIncluded")) {
                ref.get().kozakBtn.setSelected((Boolean) newValue);
            } else if (pName.equals("startCodonIncluded")) {
                ref.get().startBtn.setSelected((Boolean) newValue);
            }
        }
    }

    static class StartCodonItemListener implements ItemListener {

        private WeakReference<SensePanel> ref;
        private WeakReference<AddAttbSitesPanel> parentRef;

        protected StartCodonItemListener(WeakReference<SensePanel> ref) {
            this.ref = ref;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            ref.get().startCodonIncluded = state == ItemEvent.SELECTED;

            parentRef = getParentRef(e);
            if (parentRef == null) {
                return;
            }

            parentRef.get().updatePreview();
        }

        private WeakReference<AddAttbSitesPanel> getParentRef(ItemEvent e) {
            if (parentRef == null) {
                Object src = e.getSource();
                AddAttbSitesPanel panel = UIUtil.getParent((Component) src, AddAttbSitesPanel.class);
                if (panel != null) {
                    parentRef = new WeakReference<AddAttbSitesPanel>(panel);
                }
            }
            return parentRef;
        }
    }

    static class SpacerListener implements DocumentListener {

        JTextField textField;

        SpacerListener(JTextField comp) {
            this.textField = comp;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            process(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            process(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            process(e);
        }

        void process(DocumentEvent e) {
            AddAttbSitesPanel addAttSitesPanel = UIUtil.getParent(textField, AddAttbSitesPanel.class);
            final String text = textField.getText();
            PrimerAdapter primerAdapter = addAttSitesPanel.getLeftPrimerAdapter();
            StringList forbidden = new StringList(primerAdapter.getForbidden());
            if (BioUtil.areNonambiguousDNAs(text) && !forbidden.contains(text)) {
                primerAdapter.setPostfix(text.toUpperCase(Locale.ENGLISH));
                addAttSitesPanel.updatePreview();                                
            } else {
                final String postfix = primerAdapter.getPostfix();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textField.setText(postfix);
                    }
                });
            }
        }
    }

    static class FuseListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComponent comp = (JComponent) e.getSource();
            boolean selected = e.getStateChange() == ItemEvent.SELECTED;
            SensePanel sensePanel = UIUtil.getParent(comp, SensePanel.class);
            sensePanel.spacerField.setEnabled(selected);

            AddAttbSitesPanel addAttbSitesPanel = UIUtil.getParent(sensePanel, AddAttbSitesPanel.class);
            PrimerAdapter adapter = addAttbSitesPanel.getLeftPrimerAdapter();
            if (selected) {            
                adapter.setPostfix(addAttbSitesPanel.getSensePanel().spacerField.getText());
            }
            addAttbSitesPanel.updatePreview();

        }
    }

    static class KozakBtnItemListener implements ItemListener {

        private WeakReference<SensePanel> ref;

        protected KozakBtnItemListener(WeakReference<SensePanel> ref) {
            this.ref = ref;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            ref.get().kozakIncluded = state == ItemEvent.SELECTED;

            AddAttbSitesPanel panel = UIUtil.getParent((Component) e.getSource(), AddAttbSitesPanel.class);
            if (panel != null) {
                panel.updatePreview();
            }
        }
    }

    static class SDItemListener implements ItemListener {

        private WeakReference<SensePanel> ref;

        protected SDItemListener(WeakReference<SensePanel> ref) {
            this.ref = ref;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            ref.get().sdIncluded = state == ItemEvent.SELECTED;

            AddAttbSitesPanel panel = UIUtil.getParent((Component) e.getSource(), AddAttbSitesPanel.class);
            if (panel != null) {
                panel.updatePreview();
            }
        }
    }
}
