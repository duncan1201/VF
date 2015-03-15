/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.database;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.FileHelper;
import com.gas.database.core.backup.api.IBackupService;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
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
id = "com.gas.main.ui.actions.BackupLocalDBAction")
@ActionRegistration(displayName = "#CTL_BackupLocalDBAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 0)
})
@Messages("CTL_BackupLocalDBAction=Back Up Data...")
public final class BackupLocalDBAction extends AbstractAction {

    private IBackupService backupService = Lookup.getDefault().lookup(IBackupService.class);
    static final String TITLE = "Back Up Data(deprecated)";
    
    public BackupLocalDBAction(){
        super(TITLE + "...", ImageHelper.createImageIcon(ImageNames.DB_BACKUP_16));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Frame frame = WindowManager.getDefault().getMainWindow();

        JFileChooser jc = new JFileChooser();
        jc.setDialogType(JFileChooser.SAVE_DIALOG);
        jc.setAcceptAllFileFilterUsed(false);
        jc.addChoosableFileFilter(new FileNameExtensionFilter("Vector Friends Backup File", IBackupService.EXT));        
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        
        final File dir = jc.getCurrentDirectory();
        final String newName = FileHelper.getNewFileName(dir, df.format(new Date()), IBackupService.EXT);
        final File file = new File(dir, String.format("%s.%s", newName, IBackupService.EXT));
        
        jc.setSelectedFile(file);

        boolean done = false;
        while (!done) {
            int answer = jc.showSaveDialog(frame);
            if (answer == JFileChooser.APPROVE_OPTION) {

                final File selectedFile = jc.getSelectedFile();

                boolean present = selectedFile.exists();
                boolean overwrite = false;
                if (present) {
                    DialogDescriptor.Confirmation cf = new DialogDescriptor.Confirmation(String.format("\"%s\" already exists.\n Do you want to replace it?", selectedFile.getName()), TITLE, DialogDescriptor.YES_NO_CANCEL_OPTION);
                    Integer answer2 = (Integer) DialogDisplayer.getDefault().notify(cf);
                    if (answer2.equals(DialogDescriptor.OK_OPTION)) {
                        overwrite = true;
                    }else if(answer2.equals(DialogDescriptor.CANCEL_OPTION)){
                        return;
                    }
                }
                
                if(overwrite || !present){
                    _backup(selectedFile);
                }
                done = true;
            } else if (answer == JFileChooser.CANCEL_OPTION) {
                done = true;
            }

        }

    }

    private void _backup(final File file) {
        Frame owner = WindowManager.getDefault().getMainWindow();
        ProgressHelper.showProgressDialogAndRun(owner, new com.gas.common.ui.progress.ProgRunnable(){

            @Override
            public void run(com.gas.common.ui.progress.ProgressHandle handle) {
                File tmpFile = FileHelper.getUniqueFile(true);
                handle.progress(10);
                handle.progress("Preparing to backup...");
                backupService.script(tmpFile);
                handle.progress(80);
                handle.progress("Backing up the data...");
                FileHelper.zip(file, tmpFile);                
            }

            @Override
            public void done(ProgressHandle handle) {
            }

        }, TITLE);
                
    }
}
