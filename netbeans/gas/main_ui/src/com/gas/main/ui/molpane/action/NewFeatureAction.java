/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.main.ui.editor.as.ASEditor;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.sitepanel.annotation.AnnPanel;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 *
 * @author dq
 */

    public class NewFeatureAction extends AbstractAction {

        private WeakReference<MolPane> molPaneRef;

        public NewFeatureAction(WeakReference<MolPane> molPaneRef) {
            super("New...", ImageHelper.createImageIcon(ImageNames.FEATURE_ADD_16));
            this.molPaneRef = molPaneRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final MolPane molPane = molPaneRef.get();
            final AnnotatedSeq as = molPane.getAs();
            Loc loc = null;
            LocList selections = molPane.getSelections();
            if (selections != null && !selections.isEmpty()) {
                loc = selections.get(0);
            }
            Feture feture = new Feture();
            Lucation luc = null;
            if (loc != null) {
                luc = new Lucation(loc.getStart(), loc.getEnd(), true);
            }
            if (luc != null) {
                feture.setLucation(luc);
            }
            final AnnPanel newAnnPanel = new AnnPanel(feture, as.isCircular(), false);
            newAnnPanel.setMolPaneRef(new WeakReference<MolPane>(molPane));
            final DialogDescriptor dialogDescriptor = new DialogDescriptor(newAnnPanel, "New Annotation");
            newAnnPanel.setDialogDescriptor(dialogDescriptor);
            newAnnPanel.validateInput();

            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dialogDescriptor);

            if (answer == JOptionPane.OK_OPTION) {
                newAnnPanel.ui2Feture();
                Feture f = newAnnPanel.getFeture();
                as.getFetureSet().add(f);
                molPane.refresh();
                setCanSave();
            }
        }

        private void setCanSave() {
            ASEditor asEditor = (ASEditor) UIUtil.getEditorMode().getSelectedTopComponent();

            if (asEditor != null) {
                asEditor.setCanSave();
            }
        }
    }
