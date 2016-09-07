/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.imports;

import com.gas.common.ui.FileFormat;
import com.gas.common.ui.misc.FileNameExtFilterComparator;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.LogUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.api.IDomainUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.domain.core.FileImportServiceFinder;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqs;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.msa.Apr;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.nexus.api.Nexus;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.imports.ImportFromFileAction")
@ActionRegistration(displayName = "#CTL_ImportFromFileAction")
@ActionReferences({
    @ActionReference(path = "Menu/File/Import", position = 2425)
})
@Messages("CTL_ImportFromFileAction=Import From File...")
public final class ImportFromFileAction extends AbstractAction {

    IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
    IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
    static String TITLE = "Import From File";
    private static Logger log = Logger.getLogger(ImportFromFileAction.class.getName());
    
    
    public ImportFromFileAction() {
        super(TITLE + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        final Frame frame = WindowManager.getDefault().getMainWindow();
                
        
        
        final JFileChooser jc = new JFileChooser();
        jc.setAcceptAllFileFilterUsed(false);
        jc.setMultiSelectionEnabled(true);
        jc.setDialogType(JFileChooser.OPEN_DIALOG);
        List<FileNameExtensionFilter> filters = getFileFilters();
        for (FileNameExtensionFilter f : filters) {
            jc.addChoosableFileFilter(f);
        }
        
        int answer = UIUtil.showDialog(jc, frame);
        
        if (answer == JFileChooser.APPROVE_OPTION) {
            ProgressHelper.showProgressDialogAndRun(frame, new ProgRunnable() {
                
                IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
                Folder folder;
                IFolderElementList folderElementList = new IFolderElementList();

                @Override
                public void run(ProgressHandle handle) {                    
                    final FileFilter selectedFileFilter = jc.getFileFilter();
                    FileFormat format = FileFormat.getByDesc(selectedFileFilter.getDescription());
                    IFileImportService fileImportSvc = FileImportServiceFinder.find(format);
                    handle.setIndeterminate(false);
                    folder = ExplorerTC.getInstance().getSelectedFolder();
                    File[] files = jc.getSelectedFiles();
                    for (int i = 0; i < files.length; i++) {
                        handle.progress((i + 1) * 100/ files.length);
                        handle.progress(String.format("Importing %d of %d", i + 1, files.length));
                        File file = files[i];
                        Object obj = fileImportSvc.receive(file);
                        boolean importFail =  obj == null || (obj instanceof Collection && CommonUtil.isNullOrEmpty((Collection)obj));
                        if(importFail){
                            tryToReportImportError(file);
                            continue;
                        }
                        if (obj instanceof Collection && !CommonUtil.isNullOrEmpty((Collection)obj)) {
                            Collection col = (Collection) obj;
                            for (Object o : col) {                                
                                IFolderElement folderElement = (IFolderElement) o;
                                folderElement.setFolder(folder);
                                domainUtil.persist(folderElement);                              
                            }
                            if (col.size() == 1 && files.length < 2) {                                
                                folderElementList.add((IFolderElement) col.iterator().next());
                            }
                        } else if (obj instanceof IFolderElement) {
                            processFolderElement((IFolderElement) obj, folder);
                            
                            if (files.length < 2) {                         
                                folderElementList.add((IFolderElement) obj);
                            }
                        } else if (obj instanceof Nexus) {
                            MSA msa = msaService.toMSA((Nexus) obj);
                            processFolderElement(msa, folder);
                            if(files.length < 2){                                
                                folderElementList.add((IFolderElement) msa);
                            }
                        } else if (obj instanceof Apr) {
                            MSA msa = msaService.toMSA((Apr) obj);
                            processFolderElement(msa, folder);
                            if(files.length < 2){                                
                                folderElementList.add((IFolderElement) msa);
                            }
                        } else if (obj instanceof Fasta){
                            AnnotatedSeq as = AnnotatedSeqs.create((Fasta)obj);
                            processFolderElement(as, folder);
                            folderElementList.add(as);
                        }
                        
                        Folder folderWithData = folderService.loadWithData(folder.getHibernateId());
                        ExplorerTC.getInstance().updateFolder(folderWithData);
                    }
                    Folder updatedFolder = folderService.loadWithDataAndParentAndChildren(folder.getHibernateId());
                    BannerTC.getInstance().updateFolder(updatedFolder);
                }

                @Override
                public void done(ProgressHandle handle) {
                    if(!folderElementList.isEmpty()){
                        for(IFolderElement folderElement: folderElementList){
                            domainUtil.openEditor(folderElement);
                        }
                    }
                    Folder updatedFolder = folderService.loadWithDataAndParentAndChildren(folder.getHibernateId());
                    BannerTC.getInstance().updateFolder(updatedFolder);
                }
            }, TITLE);
        }
    }
    
    private void tryToReportImportError(File file) {        
        String fileName = file.getName();
        String title = TITLE;
        String msg = String.format("File '%s' cannot be recognized. Click 'Yes' to report the problem", fileName);        
        DialogDescriptor.Confirmation confirm = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
        Object answer = DialogDisplayer.getDefault().notify(confirm);
        if(answer.equals(DialogDescriptor.YES_OPTION)){
            reportImportError(file);
        }
    }
    
    private void reportImportError(File file) {
        LogUtil.severe(log, "Cannot import file");
    }

    private void processFolderElement(IFolderElement folderElement, Folder folder) {
        String nameNew = folder.getNewElementName(folderElement.getName());
        folderElement.setName(nameNew);
        folderElement.setFolder(folder);
        domainUtil.persist(folderElement);
    }

    private List<FileNameExtensionFilter> getFileFilters() {
        List<FileNameExtensionFilter> ret = new ArrayList<FileNameExtensionFilter>();
        ret.add(new FileNameExtensionFilter(FileFormat.ABX.getDesc(), FileFormat.ABX.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.APR.getDesc(), FileFormat.APR.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.EMBL.getDesc(), FileFormat.EMBL.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.FASTA.getDesc(), FileFormat.FASTA.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.GenBank.getDesc(), FileFormat.GenBank.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.GenPept.getDesc(), FileFormat.GenPept.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.NEXUS.getDesc(), FileFormat.NEXUS.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.SCF.getDesc(), FileFormat.SCF.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.VNTI_OLIGOS.getDesc(), FileFormat.VNTI_OLIGOS.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.VNTI_NUCLEOTIDES.getDesc(), FileFormat.VNTI_NUCLEOTIDES.getExts()));
        ret.add(new FileNameExtensionFilter(FileFormat.VNTI_PROTEINS.getDesc(), FileFormat.VNTI_PROTEINS.getExts()));        
        
        Collections.sort(ret, new FileNameExtFilterComparator());
        return ret;
    }
}
