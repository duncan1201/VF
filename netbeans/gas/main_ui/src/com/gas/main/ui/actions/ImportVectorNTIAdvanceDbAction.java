/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.vector.advanced.api.IImportDBService;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.ImportVectorNTIAdvanceDbAction")
@ActionRegistration(displayName = "#CTL_ImportVectorNTIAdvanceDbAction")
@ActionReferences({
    @ActionReference(path = "Menu/File/Import", position = 2430)
})
@Messages("CTL_ImportVectorNTIAdvanceDbAction=Import Vector NTI Advanced\u00AE Database")
public final class ImportVectorNTIAdvanceDbAction extends AbstractAction {

    private static final String VNTI_ADVANCE_FOLDER_NAME = "VNTI Database";
    private static String TITLE = String.format("Import Vector NTI Advanced%s Database", Unicodes.TRADEMARK);
    
    public ImportVectorNTIAdvanceDbAction(){
        super(TITLE);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final Frame frame = WindowManager.getDefault().getMainWindow();
        final JFileChooser jc = new JFileChooser();
        jc.setDialogType(JFileChooser.OPEN_DIALOG);
        jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jc.setMultiSelectionEnabled(false);
        File dir = getDbDir(); 
        if (dir != null) {
            jc.setSelectedFile(dir);
        }else{
            String errorMsg = String.format("Vector NTI Advanced%s Database not found", Unicodes.TRADEMARK);
            String inst = String.format("Please install Vector NTI Advanced%s first", Unicodes.TRADEMARK);
            String msg = String.format(CNST.ERROR_FORMAT, errorMsg, inst);
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            return;
        }
        int answer = UIUtil.showDialog(jc, frame);        
        if (answer == JFileChooser.APPROVE_OPTION) {
            final File dbDir = jc.getSelectedFile();
            if (!dbDir.getName().equals(VNTI_ADVANCE_FOLDER_NAME)) {
                String errorMsg = String.format("\"%s\" is not a Vector NTI Advanced%s database directory", dbDir.getName(), Unicodes.TRADEMARK);
                String ins = "Please choose a folder named \"VNTI Database\" (e.g., C:\\VNTI Database)<br/><br/> Do you want to retry?";
                String msg = String.format(CNST.MSG_FORMAT, errorMsg, ins);
                DialogDescriptor.Confirmation m = new DialogDescriptor.Confirmation(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                Object answer2 = DialogDisplayer.getDefault().notify(m);
                if(answer2.equals(DialogDescriptor.YES_OPTION)){
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            ImportVectorNTIAdvanceDbAction a = new ImportVectorNTIAdvanceDbAction();
                            a.actionPerformed(null);
                        }
                    });
                    return;
                }else{
                    return;
                }
            }
            ProgressHelper.showProgressDialogAndRun(frame, "Importing VNTI Advanced Database", new ProgRunnable() {
                Folder currentFolder;
                IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
                Folder newFolder;

                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress("importing...");
                    IImportDBService importService = Lookup.getDefault().lookup(IImportDBService.class);
                    newFolder = importService.receive(dbDir);
                    String oldName = newFolder.getName();
                    handle.progress("saving...");
                    currentFolder = ExplorerTC.getInstance().getSelectedFolder();
                    String newName = currentFolder.getNewChildName(oldName);
                    if(!newName.equals(oldName)){
                        newFolder.setName(newName);
                    }
                    // it's important to get a new instance of "cFolder" because
                    currentFolder = folderService.loadWithDataAndParentAndChildren(currentFolder.getHibernateId());
                    currentFolder.addFolder(newFolder);


                    folderService.merge(currentFolder);
                }

                @Override
                public void done(ProgressHandle handle) {
                    newFolder = currentFolder.getChild(newFolder.getName());
                    if (newFolder != null) {
                        newFolder.setParent(currentFolder);
                        ExplorerTC.getInstance().addNewFolder(newFolder);
                        ExplorerTC.getInstance().setSelectedFolder(newFolder);
                    }
                }
            }, String.format("Import VNTI Advanced%s Database", Unicodes.TRADEMARK));
        }
    }

    private File getDbDir() {
        File ret = null;
        ret = new File("C:\\" + VNTI_ADVANCE_FOLDER_NAME);
        if (ret.exists()) {
            return ret;
        } else {
            return null;
        }
    }
}
