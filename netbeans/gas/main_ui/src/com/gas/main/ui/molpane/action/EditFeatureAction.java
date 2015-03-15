/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
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
public class EditFeatureAction extends AbstractAction {

    private Feture feture;
    private WeakReference<MolPane> molPaneRef;

    public EditFeatureAction(WeakReference<MolPane> molPaneRef) {
        super("Edit...", ImageHelper.createImageIcon(ImageNames.FEATURE_EDIT_16));
        this.molPaneRef = molPaneRef;
    }

    public Feture getFeture() {
        return feture;
    }

    public void setFeture(Feture feture) {
        this.feture = feture;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final MolPane molPane = molPaneRef.get();
        final AnnotatedSeq as = molPane.getAs();
        final AnnPanel annPanel = new AnnPanel(feture, as.isCircular(), true);
        annPanel.setMolPaneRef(new WeakReference<MolPane>(molPane));
        final DialogDescriptor dialogDescriptor = new DialogDescriptor(annPanel, String.format("Edit Annotation \"%s\"", feture.getDisplayName()));
        annPanel.setDialogDescriptor(dialogDescriptor);
        annPanel.validateInput();

        Integer answer = (Integer) DialogDisplayer.getDefault().notify(dialogDescriptor);
        if (answer.equals(JOptionPane.OK_OPTION)) {
            annPanel.ui2Feture();
            molPane.refresh();
            molPane.revalidate();
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
