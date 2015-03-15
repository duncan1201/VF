/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.as.CloseASEditorAction;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class AncestorsCompListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AncestorsComp src = (AncestorsComp) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("as")) {
                if (src.as != null) {
                    src.repaint();
                }
            }
        }
    }

    static class MouseAdpt extends MouseAdapter {

        IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);

        @Override
        public void mouseClicked(MouseEvent e) {
            final AncestorsComp src = (AncestorsComp) e.getSource();
            final EntityShape eShape = src.entityShapeList.get(e.getPoint());
            if (eShape != null && eShape.absolutePath != null && eShape.active) {
                AnnotatedSeq asEntity = asService.getFullByAbsolutePath(eShape.absolutePath);
                if (asEntity == null) {
                    final String msgError = String.format("Nucleotide \"%s\" not found", eShape.absolutePath);

                    final String msg = String.format(CNST.ERROR_FORMAT, msgError, instruction());
                    String title = "Nucleotide not found";
                    DialogDescriptor.Confirmation m = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_CANCEL_OPTION);
                    Object answer = DialogDisplayer.getDefault().notify(m);
                    if (answer.equals(DialogDescriptor.YES_OPTION)) {
                        IMolPane molPane = UIUtil.getParent(src, IMolPane.class);
                        AnnotatedSeq as = molPane.getAs();
                        CloseASEditorAction closeAction = new CloseASEditorAction(as);
                        closeAction.actionPerformed(null);

                        as = asService.getFullByHibernateId(as.getHibernateId());
                        OpenASEditorAction openAction = new OpenASEditorAction(as);
                        openAction.actionPerformed(null);
                    } else if (answer.equals(DialogDescriptor.NO_OPTION)) {
                    }
                    return;
                }
                OpenASEditorAction action = new OpenASEditorAction(asEntity);
                action.actionPerformed(null);
            }
        }

        private String instruction() {
            String ret = "Click <b>Yes</b> to reopen the editor<br/><br/>Click <b>No</b> to deactive the link(cannot be undo)";
            return ret.toString();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            final AncestorsComp src = (AncestorsComp) e.getSource();
            final EntityShape eShape = src.entityShapeList.get(e.getPoint());
            if (eShape != null && eShape.absolutePath != null && eShape.active) {
                src.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                src.setToolTipText("Click to open " + eShape.name);
                Action toolTipAction = src.getActionMap().get("postTip");
                if (toolTipAction != null) {
                    ActionEvent postTip = new ActionEvent(src, ActionEvent.ACTION_PERFORMED, "");
                    toolTipAction.actionPerformed(postTip);
                }
            } else {
                src.setCursor(Cursor.getDefaultCursor());
                src.setToolTipText(null);                
            }
        }
    }
}
