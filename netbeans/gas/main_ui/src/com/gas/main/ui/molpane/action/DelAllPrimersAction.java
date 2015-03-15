/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.main.ui.molpane.MolPane;
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
    
    public class DelAllPrimersAction extends AbstractAction {

        private WeakReference<MolPane> molPaneRef;
        
        public DelAllPrimersAction(WeakReference<MolPane> molPaneRef){
            super("Delete all primer binds", ImageHelper.createImageIcon(ImageNames.PRIMER_DELETE_16));
            this.molPaneRef = molPaneRef;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String msg = "Are you sure you want to delete all primer binds?";
            String title = "Delete primer binds";
            
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);            
            Integer answer = (Integer) DialogDisplayer.getDefault().notify(c);
            if(answer.equals(JOptionPane.OK_OPTION)){
                MolPane molPane = molPaneRef.get();
                AnnotatedSeq as = molPane.getAs();
                as.getP3output().clear();
                molPane.refresh();
            }
        }
    }