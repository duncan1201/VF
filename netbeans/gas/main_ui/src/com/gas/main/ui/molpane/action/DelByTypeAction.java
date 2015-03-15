/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.main.ui.editor.as.ASEditor;
import com.gas.main.ui.molpane.MolPane;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

 public class DelByTypeAction extends AbstractAction {

        private String featureType;
        private boolean restrictionSites;
        private WeakReference<MolPane> molPaneRef;

        public DelByTypeAction(WeakReference<MolPane> molPaneRef) {
            super("Delete features of this type", ImageHelper.createImageIcon(ImageNames.FEATURE_DELETE_16));
            this.molPaneRef = molPaneRef;
        }

        public void setRestrictionSites(boolean restrictionSites) {
            this.restrictionSites = restrictionSites;
        }

        public String getFeatureType() {
            return featureType;
        }

        public void setFeatureType(String featureType) {
            this.featureType = featureType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String title;
            String msg;
            if (featureType != null) {
                title = "Delete Annotations";
                msg = String.format("<html>Are you sure you want to delete all \"%s\" annotations?</html>", getFeatureType());
            } else {
                title = "Delete all restriction sites";
                msg = "<html>Are you sure you want to delete all restriction sites?</html>";
            }
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);            
            Integer answer = (Integer) DialogDisplayer.getDefault().notify(c);
            if (answer == JOptionPane.OK_OPTION) {
                ASEditor asEditor = (ASEditor) UIUtil.getEditorMode().getSelectedTopComponent();
                MolPane molPane = molPaneRef.get();
                String fetureType = getFeatureType();
                AnnotatedSeq as = molPane.getAs();
                if (restrictionSites) {
                    as.getRmap().getEntries().clear();
                    molPane.refresh();
                } else if(FetureKeyCnst.OVERLAPPING_PRIMER.equalsIgnoreCase(fetureType)){
                    if(as.getGbOutput() != null){
                        as.getGbOutput().clear();
                        molPane.refresh();
                    }
                } else if (FetureKeyCnst.OVERHANG.equalsIgnoreCase(fetureType)) {
                    as.getOverhangs().clear();
                    molPane.refresh();
                    UIUtil.setTopCompIcon(asEditor, ImageHelper.createImage(ImageNames.NUCLEOTIDE_16));
                } else if (FetureKeyCnst.ORF.equalsIgnoreCase(fetureType)) {
                    as.getOrfResult().getOrfSet().clear();
                    molPane.refresh();
                } else {
                    AsHelper.removeFeaturesByType(as, getFeatureType());
                    molPane.refresh();
                }
                molPane.revalidate();

                if (asEditor != null) {
                    asEditor.setCanSave();
                }
            }
        }
    }
