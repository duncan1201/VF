/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.new_;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.database.core.api.IDomainUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;

/**
 *
 * @author dq
 */
public class NewSeqAction extends AbstractAction {
    
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    
    public NewSeqAction(String title){
        super(title);
        putValue(Action.SMALL_ICON, ImageHelper.createImageIcon(ImageNames.NUCLEOTIDE_16));
        putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.NUCLEOTIDE_24));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        boolean valid = validate();
        if(!valid){
            return;
        }
        
        IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
        
        NewSeqPanel newSeqPanel = new NewSeqPanel();
        DialogDescriptor dd = new DialogDescriptor(newSeqPanel, "Create new sequence");
        newSeqPanel.setDialogDescriptor(dd);
        newSeqPanel.validateInput();
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if(answer.equals(DialogDescriptor.OK_OPTION)){
            Folder selectedFolder = ExplorerTC.getInstance().getSelectedFolder();
            
            boolean isDNAType = newSeqPanel.isDNAType();
            boolean isPrimerType = newSeqPanel.isPrimerType();
            boolean isProtein = newSeqPanel.isProteinType();
            AnnotatedSeq ret = new AnnotatedSeq();            
            ret.setName(newSeqPanel.getSeqName());
            ret.setDesc(newSeqPanel.getSeqDescription());
            ret.setSequence(newSeqPanel.getSeq());
            ret.setAccession("");            
            if(isProtein){
                ret.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.AA);
                ret.setCircular(false);
            }else if(isDNAType || isPrimerType){
                ret.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.DNA);
                ret.setCircular(false);
            }
            ret.setOligo(isPrimerType);
            
            ret.setFolder(selectedFolder);
            ret.setLastModifiedDate(new Date());
            
            domainUtil.persist(ret);
                        
            IFolderService folderSvc = Lookup.getDefault().lookup(IFolderService.class);
            Folder updatedFolder = folderSvc.loadWithDataAndChildren(selectedFolder.getHibernateId());
            ExplorerTC.getInstance().updateFolder(updatedFolder);
            BannerTC.getInstance().updateFolder(updatedFolder);

        }
    }   
    
    private boolean validate(){
        boolean ret = true;
        Folder folder = ExplorerTC.getInstance().getSelectedFolder();
        boolean isNCBIFolder = folder.isNCBIFolder();
        boolean isRecycleBin = folder.isRecycleBin();
        
        if(isNCBIFolder || isRecycleBin){
            ret = false;
            String msg = String.format(CNST.MSG_FORMAT, "The current selected folder is not a data folder", "Please select a data folder to create new sequence");
            DialogDescriptor.Message dd = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(dd);
        }
        return ret;
    }
    
}
