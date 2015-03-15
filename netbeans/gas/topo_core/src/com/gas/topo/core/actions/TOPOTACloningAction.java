/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.actions;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.topo.core.api.ITOPOTAService;
import com.gas.topo.core.ta.TACloningPanel;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
id = "com.gas.topo.core.actions.TOPOTACloningAction")
@ActionRegistration(displayName = "#CTL_TOPOTACloningAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/TOPO", position = 2430, separatorAfter = 2431)
})
@NbBundle.Messages("CTL_TOPOTACloningAction=TOPO"+Unicodes.TRADEMARK+" TA Cloning...")
public class TOPOTACloningAction extends AbstractAction {

    private ITOPOTAService topoTaService = Lookup.getDefault().lookup(ITOPOTAService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);

    public TOPOTACloningAction() {
        super(String.format("TOPO%s TA Cloning...", Unicodes.TRADEMARK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> objs = BannerTC.getInstance().getCheckedNucleotides();
        List<AnnotatedSeq> fulls = asService.getFull(objs);
        final TACloningPanel panel = new TACloningPanel();
        boolean valid = validate(fulls, panel);
        if (!valid) {
            return;
        }
        
        DialogDescriptor descriptor = new DialogDescriptor(panel, String.format("TOPO%s TA Cloning", Unicodes.TRADEMARK));
        panel.setDialogDescriptor(descriptor);
        panel.validateInput();
        Integer answer = (Integer) DialogDisplayer.getDefault().notify(descriptor);

        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            AnnotatedSeq insert = panel.getInsert();
            AnnotatedSeq vector = panel.getVector();
            
            final Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            final String insertFolderPath = folderService.loadWithParents(insert.getFolder().getHibernateId()).getAbsolutePath();
            final String vectorFolderPath = folderService.loadWithParents(vector.getFolder().getHibernateId()).getAbsolutePath();
            
            Operation operation = new Operation();
            operation.setNameEnum(Operation.NAME.TOPO_TA);
            operation.setDate(new Date());
            Operation.Participant part = new Operation.Participant(insertFolderPath + "\\" + insert.getName(), true);
            Operation.Participant part2 = new Operation.Participant(vectorFolderPath + "\\" + vector.getName(), true);
            operation.addParticipant(part);
            operation.addParticipant(part2);
            
            List<AnnotatedSeq> clones = topoTaService.ligate(insert, vector);
            if (!clones.isEmpty()) {                
                for(AnnotatedSeq clone: clones){
                    final String newName = folder.getNewElementName("TOPO TA Clone");
                    clone.setName(newName);
                    clone.setOperation(operation.clone());
                    folder.addObject(clone);                    
                    //clone.setFolder(folder);
                    asService.persist(clone);
                }
                
                Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
                
                BannerTC.getInstance().updateFolder(updatedFolder);
                ExplorerTC.getInstance().updateFolder(updatedFolder);
                
                String title = String.format("TOPO%s TA Cloning", Unicodes.TRADEMARK);
                String msg = String.format("<html>TOPO%s TA clones created<br/><br/><br/>Do you want to open them?</html>", Unicodes.TRADEMARK);
                DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
                Object answerOpen = DialogDisplayer.getDefault().notify(c);
                if(answerOpen.equals(DialogDescriptor.OK_OPTION)){
                    Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
                    for(AnnotatedSeq clone: clones){
                        clone = asService.getFullByHibernateId(clone.getHibernateId(), rootFolder);
                        OpenASEditorAction action = new OpenASEditorAction(clone);
                        action.actionPerformed(null);
                    }
                }
            }         
        }
    }

    private boolean validate(List<AnnotatedSeq> fulls, TACloningPanel cloningPanel) {
        boolean ret = true;
        final String titleError = String.format("Cannot perform TOPO%s TA cloning", Unicodes.TRADEMARK);
        if (fulls.size() > 2) {
            final String msg = String.format(CNST.ERROR_FORMAT, "More than two nucleotides selected", "Please select no more than two nucleotides");
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(titleError);
            DialogDisplayer.getDefault().notify(m);
            ret = false;
        } else {
            for(AnnotatedSeq full: fulls){
                ITOPOTAService.STATE insertState = topoTaService.isInsertValid(full, ITOPOTAService.STATE.class);
                ITOPOTAService.STATE vectorState = topoTaService.isVectorValid(full, ITOPOTAService.STATE.class);
                //ITOPOTAService.STATE vectorState = topoTaService.isVectorValid(full, ITOPOTAService.STATE.class);
                //String insertMsg = topoTaService.isInsertValid(full);
                //String vectorMsg = topoTaService.isVectorValid(full);
                if(insertState != ITOPOTAService.STATE.VALID && vectorState != ITOPOTAService.STATE.VALID){                    
                    final String errorMsg = String.format("\"%s\" is neither a valid TOPO%s TA insert nor a valid TOPO%s TA vector", full.getName(), Unicodes.TRADEMARK, Unicodes.TRADEMARK);
                    final String insMsg = createInstructionMsg(insertState, vectorState);
                    final String msg = String.format(CNST.ERROR_FORMAT, errorMsg, insMsg);
                    DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                    m.setTitle(titleError);
                    DialogDisplayer.getDefault().notify(m);
                    ret = false;
                    break;
                }
                if(vectorState == ITOPOTAService.STATE.VALID){
                    cloningPanel.setVector(full);
                }else if(insertState == ITOPOTAService.STATE.VALID){
                    cloningPanel.setInsert(full);
                }
            }
            
        }
        return ret;
    }
    
    private String createInstructionMsg(ITOPOTAService.STATE insertState, ITOPOTAService.STATE vectorState){
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("A TOPO%sTA insert must", Unicodes.TRADEMARK));
        StringList strList = new StringList();
        String msg = "be linearized";
        if(insertState == ITOPOTAService.STATE.NOT_LINEAR){
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);
        
        msg = "have 3' A overhangs at both ends";
        if(insertState == ITOPOTAService.STATE.NO_3_A_OVERHANGS){
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);
        
        ret.append(StrUtil.toHtmlList(strList));
        
        ret.append("<br/>");
        
        ret.append(String.format("A TOPO%s TA vector must", Unicodes.TRADEMARK));
        strList = new StringList();
        msg = "be linearized";
        if(vectorState == ITOPOTAService.STATE.NOT_LINEAR){
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);
        
        msg = "have sequence 5'-CCTTT-3' at both ends";
        if(vectorState == ITOPOTAService.STATE.NO_CCCTT){
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);
        
        msg = "have 3' T overhangs at both ends";
        if(vectorState == ITOPOTAService.STATE.NO_3_A_OVERHANGS){
            msg = String.format("<font color='red'>%s</font>", msg);
        }
        strList.add(msg);        
        
        ret.append(StrUtil.toHtmlList(strList));
        
        return ret.toString();
    }
}
