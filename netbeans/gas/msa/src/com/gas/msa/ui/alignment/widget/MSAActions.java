/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.InputPanel;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.database.core.api.IDomainUtil;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class MSAActions {
    
    static class ExtractRegionAction extends AbstractAction{
        
        private Loc2D loc2d;
        String initName;
        JComponent comp;
        
        ExtractRegionAction(){
            super("Extract Region...");
        }
        
        void setComp(JComponent comp){
            this.comp = comp;
        }
        
        /**
         * @param loc2d: 1-based. starting from the consensus
         */
        void setSelection(Loc2D loc2d){
            this.loc2d = loc2d;
            updateEnablement();
        }
        
        private void updateEnablement(){
            this.setEnabled(initName != null && loc2d != null && loc2d.height() == 1);
        }

        public void setInitName(String initName) {
            this.initName = initName;
            updateEnablement();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            InputPanel inputPanel = new InputPanel("Region Name");
            String pName = initName + " extraction";
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            pName = folder.getNewElementName(pName);
            
            inputPanel.setInitInputText(pName);
            inputPanel.setExistingNames(folder.getElementNames());
            inputPanel.setForbiddenChars(AnnotatedSeq.forbiddenChars);
            DialogDescriptor a = new DialogDescriptor(inputPanel, "Extract Region");
            inputPanel.setDialogDescriptor(a);
            inputPanel.validateInput();
            
            Object answer = DialogDisplayer.getDefault().notify(a);
            if(answer.equals(DialogDescriptor.OK_OPTION)){
                MSAScroll msaScroll = UIUtil.getParent(comp, MSAScroll.class);
                String nameNew = inputPanel.getInputText();
                MSA msa = msaScroll.msaRef.get();
                String seq = msa.getSeq(initName, loc2d.toLoc());
                AnnotatedSeq as = new AnnotatedSeq();
                as.setAccession(nameNew);
                as.setName(nameNew);
                as.setSequence(seq);
                as.setCircular(false);
                as.setDesc(String.format("Extraction from %s(%d-%d)", msa.getName(), loc2d.getX1(), loc2d.getX2()));
                as.setCreationDate(new Date());
                as.setLastModifiedDate(new Date());
                as.getSequenceProperties().put(AsHelper.MOL_TYPE, msa.isDnaByGuess()?AsHelper.DNA : AsHelper.AA);
                                
                as.setFolder(folder);
                IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
                domainUtil.persist(as);
                
                // update the UI
                IFolderService serviceFolder = Lookup.getDefault().lookup(IFolderService.class);
                Folder updatedFolder = serviceFolder.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
                BannerTC.getInstance().updateFolder(updatedFolder);
                ExplorerTC.getInstance().updateFolder(updatedFolder);
            }
        }
    }
    
    static class EditNameAction extends AbstractAction{
    
        WeakReference<JComponent> compRef;
        WeakReference<MSAScroll> msaScrollRef;
        StringList existingNames;
        String initTextInput;
        
        EditNameAction(){
            this(null);
        }
        
        EditNameAction(JComponent comp){
            super("Edit Name...");
            if(comp != null){
                this.compRef = new WeakReference<JComponent>(comp);
            }
        }
        
        void setComp(JComponent comp){
            compRef = new WeakReference<JComponent>(comp);
        }
        
        void setExistingNames(StringList existingNames){
            this.existingNames = existingNames;
        }
        
        void setInitTextInput(String initTextInput){
            this.initTextInput = initTextInput;            
            setEnabled(initTextInput != null && !initTextInput.equalsIgnoreCase("consensus"));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {            
            InputPanel inputPanel = new InputPanel("New Name:");
            if(existingNames != null){
                inputPanel.setExistingNames(existingNames.toArray(new String[existingNames.size()]));
            }
            if(initTextInput != null){
                inputPanel.setInitInputText(initTextInput);
            }
            DialogDescriptor dd = new DialogDescriptor(inputPanel, "Edit Name");
            inputPanel.setDialogDescriptor(dd);
            inputPanel.validateInput();
            
            Object answer = DialogDisplayer.getDefault().notify(dd);
            if(answer.equals(DialogDescriptor.OK_OPTION)){
                final String nameNew = inputPanel.getInputText();
                IMSAEditor editor = UIUtil.getParent(compRef.get(), IMSAEditor.class);
                editor.getMsa().renameEntry(initTextInput, nameNew);
                                
                getMsaScroll().getRowHeaderUI().createUIObjects();
                getMsaScroll().getRowHeaderUI().revalidate();
                getMsaScroll().getRowHeaderUI().repaint();
                
                getMsaScroll().getViewUI().getMsaComp().createUIObjects();
                getMsaScroll().getViewUI().getMsaComp().repaint();
                
                editor.setCanSave();
            }
        }
        
        private MSAScroll getMsaScroll(){
            if(msaScrollRef == null){
                MSAScroll msaScroll = UIUtil.getParent(compRef.get(), MSAScroll.class);
                msaScrollRef = new WeakReference<MSAScroll>(msaScroll);
            }
            return msaScrollRef.get();
        }
    }
}
