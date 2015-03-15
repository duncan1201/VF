/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.translation;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.ISequenceUI;
import com.gas.domain.ui.editor.as.IASEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import javax.swing.JCheckBox;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class TranslationPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            TranslationPanel src = (TranslationPanel) evt.getSource();
            final String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("translationResults") && v != null) {
                src.populateUI();
            }
        }
    }

    static class ColorComboListener implements ActionListener {

        TranslationPanel translationPanel;
        ISequenceUI editor;

        public ColorComboListener(TranslationPanel translationPanel) {
            this.translationPanel = translationPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            IColorProvider colorProvider = (IColorProvider) translationPanel.getColorCombo().getSelectedItem();
            if (editor == null) {
                editor = UIUtil.getParent(translationPanel, ISequenceUI.class);
            }
            editor.setTranslationColorProvider(colorProvider);

            Pref.ColorProviderPrefs pref = Pref.ColorProviderPrefs.getInstance();
            pref.setColorProviderName(Pref.ColorProviderPrefs.KEY.TRANSLATION, colorProvider.getName());
        }
    }

    static void translate(IMolPane molPane, TranslationPanel translationPanel) {
        if (translationPanel.populatingUI) {
            return;
        }
        IGeneticCodeTableService tableService = Lookup.getDefault().lookup(IGeneticCodeTableService.class);

        IColorProvider colorProvider = (IColorProvider) translationPanel.colorCombo.getSelectedItem();

        if (molPane != null) {
            molPane.setTranslationColorProvider(colorProvider);
        }

        GeneticCodeTable table = (GeneticCodeTable) translationPanel.geneticCodeBox.getSelectedItem();

        String seq = molPane.getSequence();
        int[] selectedFrames = translationPanel.getSelectedFrames();
        List<TranslationResult> result = tableService.translate(seq, table.getName(), selectedFrames);

        molPane.getAs().setTranslationResults(new HashSet<TranslationResult>(result));
        molPane.setTranslationResults(molPane.getAs().getTranslationResults());

        IASEditor editor = UIUtil.getParent(translationPanel, IASEditor.class);
        if (editor != null) {
            editor.setCanSave();
        }
    }

    static class FrameListener implements ItemListener {

        WeakReference<IMolPane> molPaneRef;
        WeakReference<TranslationPanel> panelRef;

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();
            if (molPaneRef == null || molPaneRef.get() == null) {
                IMolPane molPane = UIUtil.getParent(src, IMolPane.class);
                molPaneRef = new WeakReference<IMolPane>(molPane);
            }
            if (panelRef == null || panelRef.get() == null) {
                TranslationPanel panel = UIUtil.getParent(src, TranslationPanel.class);
                panelRef = new WeakReference<TranslationPanel>(panel);
            }
            if (molPaneRef.get() == null || panelRef.get() == null) {
                return;
            }
            translate(molPaneRef.get(), panelRef.get());
        }
    }

    static class GeneticCodeTableListener implements ActionListener {

        IMolPane molPane;
        IGeneticCodeTableService tableService = Lookup.getDefault().lookup(IGeneticCodeTableService.class);
        private TranslationPanel translationPanel;

        public GeneticCodeTableListener(TranslationPanel translationPanel) {
            this.translationPanel = translationPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (molPane == null) {
                molPane = UIUtil.getParent(translationPanel, IMolPane.class);
            }
            translate(molPane, translationPanel);
        }
    }
}
