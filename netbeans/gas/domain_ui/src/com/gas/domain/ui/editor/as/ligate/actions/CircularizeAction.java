/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate.actions;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.InputPanel;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import static com.gas.domain.ui.editor.as.ligate.actions.LigateAction.TITLE;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class CircularizeAction extends AbstractAction {

    private final static String TITLE = "Circularize";
    
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);

    public CircularizeAction() {
        super(TITLE + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int numCircularized = 0;
        Integer fromFolderHibernateId = null;        
        List<AnnotatedSeq> objs = BannerTC.getInstance().getCheckedObjects(AnnotatedSeq.class);
        for (AnnotatedSeq as : objs) {            
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            
            boolean valid = validate(full);
            if(!valid){
                continue;
            }
            
            fromFolderHibernateId = full.getFolder().getHibernateId();
            Folder fromFolder = folderService.loadWithDataAndChildren(fromFolderHibernateId);
            StringList elementNames = fromFolder.getElementNames(AnnotatedSeq.class);            
            
            final String nameNew = fromFolder.getNewElementName(full.getName());
            InputPanel inputPanel = new InputPanel("Name:");
            inputPanel.setInitInputText(nameNew);
            inputPanel.setExistingNames(elementNames.toArray(new String[elementNames.size()]));
            DialogDescriptor dd = new DialogDescriptor(inputPanel, String.format("Circularize '%s'", full.getName()));
            inputPanel.setDialogDescriptor(dd);

            Object answer = DialogDisplayer.getDefault().notify(dd);
            if (!answer.equals(DialogDescriptor.OK_OPTION)) {
                return;
            }
            AnnotatedSeq circularized = AsHelper.circularize(full);
            circularized.setName(nameNew);
            circularized.setLastModifiedDate(new Date());
            circularized.setCreationDate(new Date());
            
            asService.save(circularized);
            
            numCircularized++;
        }
        
        if(numCircularized > 0){
            Folder updatedFolder = folderService.loadWithDataAndParentAndChildren(fromFolderHibernateId);
            BannerTC.getInstance().updateFolder(updatedFolder);
            ExplorerTC.getInstance().updateFolder(updatedFolder);

            String msg ;
            if(numCircularized == 1){
                msg = String.format(CNST.MSG_FORMAT, numCircularized + " molecule circularized", "");
            }else{
                msg = String.format(CNST.MSG_FORMAT, numCircularized + " molecules circularized", "");
            }
            DialogDescriptor.Message c = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);            
            DialogDisplayer.getDefault().notify(c);        
        }
    }

    private boolean validate(AnnotatedSeq full) {
        final String titleError = "Cannot circularize";
        boolean isNucleotide = full.isNucleotide();
        if (!isNucleotide) {

            String msg = String.format(CNST.MSG_FORMAT, "Cannot circularize " + full.getName() , "It is a protein", full.getName());
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(titleError);
            DialogDisplayer.getDefault().notify(m);
            return false;
        }
        boolean endsCompatible = AsHelper.areEndsCompatible(full);
        if (!endsCompatible) {

            String msgError = "";
            if (full.getOverhangSize() == 1) {
                Overhang overhang = full.getOverhangItr().next();
                boolean startOverhang = overhang.isStartOverhang();
                int startPos;
                int endPos;
                if (startOverhang) {
                    startPos = 1;
                    endPos = overhang.getLength();
                } else {
                    startPos = full.getLength() - overhang.getLength() + 1;
                    endPos = full.getLength();
                }
                msgError = String.format("Only one overhang found at %d - %d bp", startPos, endPos);
            } else if (full.getOverhangSize() == 2) {
                final Overhang overhangStart = full.getStartOverhang();
                final String ovSeqStart = full.getOverhangSeq(overhangStart);
                final Overhang overhangEnd = full.getEndOverhang();
                final String ovSeqEnd = full.getOverhangSeq(overhangEnd);
                msgError = String.format("The %s overhang 5'-%s-3' at %d - %d bp is incompatible with the %s overhang 5'-%s-3' at %d - %d bp", overhangStart.isFivePrime() ? "5'" : "3'", ovSeqStart, 1, overhangStart.getLength(), overhangEnd.isFivePrime() ? "5'" : "3'", ovSeqEnd, full.getLength() - ovSeqEnd.length() + 1, full.getLength());
            }
            final String in = "Please select a nucleotide with two compatible ends";
            final String msg = String.format(CNST.ERROR_FORMAT, msgError, in);
            DialogDescriptor.Message mg = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            mg.setTitle(titleError);
            DialogDisplayer.getDefault().notify(mg);
            return false;
        }
        return true;
    }
}
