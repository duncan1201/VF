/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.main.ui.editor.as.ASEditor;
import com.gas.main.ui.molpane.MolPane;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author dq
 */
    public class DelCurFeatureAction extends AbstractAction {

        private Feture feture;
        private WeakReference<MolPane> molPaneRef;

        public DelCurFeatureAction(WeakReference<MolPane> molPaneRef) {
            super("Delete current feature", ImageHelper.createImageIcon(ImageNames.FEATURE_DELETE_16));
            this.molPaneRef = molPaneRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DialogDescriptor.Confirmation cf = new DialogDescriptor.Confirmation(String.format("Are you sure you want to delete the selected annotation \"%s\"?", feture.getDisplayName()), String.format("Delete Annotation \"%s\"", feture.getDisplayName()), NotifyDescriptor.YES_NO_OPTION);
            Integer answer = (Integer) DialogDisplayer.getDefault().notify(cf);
            if (answer == JOptionPane.OK_OPTION) {
                molPaneRef.get().deleteFeature(feture);
                ASEditor asEditor = (ASEditor) UIUtil.getEditorMode().getSelectedTopComponent();
                if (asEditor != null) {
                    asEditor.setCanSave();
                }
            }
        }

        public Feture getFeture() {
            return feture;
        }

        public void setFeture(Feture feture) {
            this.feture = feture;
        }
    }
