/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.main.ui.editor.as.ASEditor;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.sitepanel.primer3.edit.EditPrimerPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.edit.IEditPrimerUI;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class EditPrimerAction extends AbstractAction {

    private WeakReference<MolPane> molPaneRef;
    private Feture feture;

    public EditPrimerAction(WeakReference<MolPane> molPaneRef) {
        super("Edit Primer...", ImageHelper.createImageIcon(ImageNames.PRIMER_EDIT_16));
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
        IEditPrimerUI editPrimerPanel = new EditPrimerPanel(feture);
        DialogDescriptor dd = new DialogDescriptor(editPrimerPanel, String.format("Edit Primer %s", ""));
        editPrimerPanel.setDialogDescriptor(dd);
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            String extension = editPrimerPanel.getExtension();
            String seq = editPrimerPanel.getPrimerSeq();
            OligoElement oe = (OligoElement) feture.getData();
            if(oe.isForwardPrimer() || oe.isInternal()){
                oe.setSeq(seq);
            }else if(oe.isReversePrimer()){
                oe.setSeq(seq);
            }
            
            oe.setTail(extension);
            
            molPane.refresh();
            molPane.revalidate();
            setCanSave();
            System.out.println();
        }
    }

    private void setCanSave() {
        ASEditor asEditor = (ASEditor) UIUtil.getEditorMode().getSelectedTopComponent();
        if (asEditor != null) {
            asEditor.setCanSave();
        }
    }
}
