/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.actions;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.IDigestAction;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.enzyme.core.service.api.IDigestService;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class DigestAction extends AbstractAction implements IDigestAction{

    private IDigestService digestService = Lookup.getDefault().lookup(IDigestService.class);    
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private RMap.EntryList selectedEntries = new RMap.EntryList();
    private AnnotatedSeq as;
    
    public DigestAction(){
        super("Digest (no enzymes selected)", ImageHelper.createImageIcon(ImageNames.SCISSOR_16));
    }
    
    public void setAs(AnnotatedSeq as){
        this.as = as;
    }
    
    @Override
    public void setSelectedEntries(RMap.EntryList entries){
        if(entries != null && !entries.isEmpty()){
            selectedEntries = entries;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(selectedEntries.isEmpty()){
            return;
        }        
        
        Set<RMap.Entry> entries = as.getRmap().getSortedEntries(selectedEntries.getAllNames());
        List<AnnotatedSeq> digested = digestService.digest(as, new ArrayList<RMap.Entry>(entries));
  
        Folder folder = ExplorerTC.getInstance().getSelectedFolder();
        for(AnnotatedSeq d: digested){
            String newName = folder.getNewElementName(d.getName());
            d.setName(newName);
        }                

        updateOtherTopComponents(digested);
        
        displayVisualCues(digested);
    }
    
    private void displayVisualCues(List<AnnotatedSeq> digested){               
        final String title = String.format("Digestion", "");
        final String msg = String.format("<html>%d fragment%s created<br/><br/>Do you want to open %s?</html>", digested.size(), digested.size() > 1 ? "s":"", digested.size() > 1?"them":"it");
        DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
        Object answer = DialogDisplayer.getDefault().notify(c);
        if(answer.equals(DialogDescriptor.OK_OPTION)){
            Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
            for(AnnotatedSeq d : digested){
                AnnotatedSeq dFull = asService.getFullByHibernateId(d.getHibernateId(), rootFolder);
                OpenASEditorAction action = new OpenASEditorAction(dFull);
                action.actionPerformed(null);
            }
        }
    }
    
    private void updateOtherTopComponents(List<AnnotatedSeq> digested){
        Folder folder = ExplorerTC.getInstance().getSelectedFolder();        
        for(AnnotatedSeq d: digested){
            d.setFolder(folder);
            asService.persist(d);
        }
        Folder updatedFolder = folderService.loadWithDataAndParentAndChildren(folder.getHibernateId());

        ExplorerTC.getInstance().updateFolder(updatedFolder);
        BannerTC.getInstance().updateFolder(updatedFolder);    
    }
}

    
