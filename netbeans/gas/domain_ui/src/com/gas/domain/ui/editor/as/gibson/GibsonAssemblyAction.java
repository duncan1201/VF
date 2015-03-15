/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.primer3.GbOutput;
import com.gas.domain.core.primer3.GbOutputHelper;
import com.gas.domain.core.primer3.IUserInputFactory;
import com.gas.domain.core.primer3.UserInput;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class GibsonAssemblyAction extends AbstractAction {

    static Logger logger = Logger.getLogger(GibsonAssemblyAction.class.getName());
    IAnnotatedSeqService asSvc = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    static final String TITLE = "Isothermal Assembly";

    public GibsonAssemblyAction(){
        this(TITLE + "...");
    }
    
    public GibsonAssemblyAction(String title) {
        super(title);
        putValue(Action.SMALL_ICON, ImageHelper.createImageIcon(ImageNames.ISOTHERMAL_16));
        putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.ISOTHERMAL_24));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final AnnotatedSeqList seqs = BannerTC.getInstance().getCheckedNucleotides();
        
        final IGibsonService gibsonSvc = Lookup.getDefault().lookup(IGibsonService.class);
        boolean valid = gibsonSvc.checkErrors(seqs);
        if(!valid){
            return;
        }
        
        final AnnotatedSeqList seqsFull = new AnnotatedSeqList(asSvc.getFull(seqs));
        IUserInputFactory userInputFactory = Lookup.getDefault().lookup(IUserInputFactory.class);
        UserInput userInput = userInputFactory.getP3WEB_V_3_0_0();

        final GibsonAssemblyPanel panel = new GibsonAssemblyPanel(seqsFull, userInput);
        DialogDescriptor dd = new DialogDescriptor(panel, TITLE);
        panel.setDialogDescriptor(dd);
        panel.validateInput();

        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            Frame frame = WindowManager.getDefault().getMainWindow();
            ProgressHelper.showProgressDialogAndRun(frame, new ProgRunnable() {
                final Folder folder = ExplorerTC.getInstance().getSelectedFolder();
                
                IFolderService folderSvc = Lookup.getDefault().lookup(IFolderService.class);
                AnnotatedSeq as;
                
                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress("Assembling");
                    GbOutput gbOutput = panel.getGbOutput();                   

                    as = gibsonSvc.assembly(panel.getAsList());
                    as.setGbOutput(gbOutput);
                    as.setFolder(folder);
                    StringList existingNames = folder.getElementNames();
                    String newName = StrUtil.getNewName("Isothermal Assembly", existingNames);
                    existingNames.add(newName);
                    as.setName(newName);
                    as.setLocus("N/A");
                    as.setDesc(String.format("assembly of %d sequences", seqs.size()));

                    asSvc.persist(as);
                    
                    List<AnnotatedSeq> oligos = GbOutputHelper.toOligos(gbOutput, as.getLength());
                    for (AnnotatedSeq oligo : oligos) {
                        newName = StrUtil.getNewName(oligo.getName(), existingNames);
                        existingNames.add(newName);
                        oligo.setName(newName);
                        oligo.setFolder(folder);
                        asSvc.persist(oligo);
                    }

                }

                @Override
                public void done(ProgressHandle handle) {
                    Folder folderUpdated = folderSvc.loadWithDataAndParents(folder.getHibernateId());
                    BannerTC.getInstance().updateFolder(folderUpdated);
                    BannerTC.getInstance().refreshHeader();
                    ExplorerTC.getInstance().updateFolder(folderUpdated);
                                       
                    final String msg = String.format(CNST.MSG_FORMAT, "Isothermal assembly done", "Do you want to open the final construct?");
                    DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, TITLE, NotifyDescriptor.YES_NO_OPTION);
                    Object answer = DialogDisplayer.getDefault().notify(c);
                    if(answer.equals(DialogDescriptor.OK_OPTION)){
                        OpenASEditorAction action = new OpenASEditorAction(as);
                        action.actionPerformed(null);
                    }
                }
            }, TITLE);
        }
    }
}
