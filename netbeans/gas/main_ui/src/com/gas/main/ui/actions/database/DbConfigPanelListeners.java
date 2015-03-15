/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.database;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.backup.api.BackupOption;
import com.gas.database.core.backup.api.IBackupService;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.ref.WeakReference;
import javax.swing.JFileChooser;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class DbConfigPanelListeners {

    static class ActionListner implements ActionListener {

        private IBackupService backupSvc = Lookup.getDefault().lookup(IBackupService.class);
        private WeakReference<DbConfigPanel> ref;

        ActionListner(DbConfigPanel p) {
            ref = new WeakReference<DbConfigPanel>(p);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals(DbConfigPanel.CMD_CHANGE_DB_FILE)) {
                handleChangeDbFile();
            } else if (cmd.equals(DbConfigPanel.CMD_CHANGE_BACKUP_DIR)) {
                handleChangeBackupDir();
            } else if (cmd.equals(DbConfigPanel.CMD_ALERT_OPTION)
                    || cmd.equals(DbConfigPanel.CMD_AUTO_OPTION) || cmd.equals(DbConfigPanel.CMD_MANUAL_OPTION)) {
                BackupOption backupOption = BackupOption.findByName(cmd);
                if (backupOption != null) {
                    backupSvc.saveBackupOption(backupOption);
                }
            } else if (cmd.equals(DbConfigPanel.CMD_BACKUP_TO_BACKUP_DIR)) {
                handleBackupToBackupDir();
            } else if (cmd.equals(DbConfigPanel.CMD_RESTORE_BACKUP_DIR)) {
                handleRestoreFromBackupDir();
            }
        }

        private void handleBackupToBackupDir() {
            boolean enoughSpace = backupSvc.isBackupDirEnoughSpace();
            if (enoughSpace) {
                backupSvc.backupToBackupDir();                
                String msg = "Backup completed successfully";
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(m);
                ref.get().updateLastBackupTime();
            } else {                
                String line1 = "There is not enough disk space in the backup directory";
                String msg = String.format(CNST.MSG_FORMAT, line1, "Please choose another backup directory");
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(m);
            }
        }

        private void handleRestoreFromBackupDir() {
            RestoreFromBackupDirPanel p = new RestoreFromBackupDirPanel();
            DialogDescriptor dd = new DialogDescriptor(p, "Restore from backup directory");
            Object answer = DialogDisplayer.getDefault().notify(dd);
            if(answer.equals(DialogDescriptor.OK_OPTION)){
                File selectedFile = p.getSelectedFile();
                backupSvc.markForRestoreFromBackupDir(true, selectedFile);
                LifecycleManager.getDefault().markForRestart();
                LifecycleManager.getDefault().exit();
            }
        }

        private void handleChangeBackupDir() {
            final String title = "Change default backup directory";
            Frame frame = WindowManager.getDefault().getMainWindow();
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(title);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int answer = UIUtil.showDialog(fc, frame);
            if (answer == JFileChooser.APPROVE_OPTION) {
                File dir = fc.getSelectedFile();
                boolean isBackupDirEmpty = backupSvc.isBackupDirEmpty();
                boolean moveBackups = false;
                if(!isBackupDirEmpty){
                    String line1 = "Do you want to move the backups to the new backup directory?";               
                    String msg = String.format(CNST.MSG_FORMAT, line1, dir.getAbsolutePath());
                    DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);               
                    moveBackups = DialogDisplayer.getDefault().notify(c).equals(DialogDescriptor.YES_OPTION);                
                }
                backupSvc.updateBackupDir(dir, moveBackups);
                ref.get().updateBackupDir(dir);                
            }
        }

        private void handleChangeDbFile() {
            IDbConnSettingsService connSvc = Lookup.getDefault().lookup(IDbConnSettingsService.class);
            connSvc.promptAndSaveDatabaseFile(true);
        }
    }
}
