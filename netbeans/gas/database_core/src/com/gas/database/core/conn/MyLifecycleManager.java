/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.conn;

import com.gas.common.ui.misc.CNST;
import com.gas.database.core.H2ServerUtil;
import com.gas.database.core.backup.api.BackupOption;
import com.gas.database.core.backup.api.IBackupService;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = LifecycleManager.class, position = 1)
public class MyLifecycleManager extends LifecycleManager {

    private IBackupService backupSvc = Lookup.getDefault().lookup(IBackupService.class);

    @Override
    public void exit() {
        H2ServerUtil.stopTcpServer();

        if (backupSvc.isMarkForRestoreFromBackupDir()) {
            backupSvc.restoreFromBackupDir();
        } else if (BackupOption.ALERT_BEFORE_CLOSING.equals(backupSvc.getBackupOption())) {
            alertBeforeClosing();
        } else if (BackupOption.AUTO_BEFORE_CLOSING.equals(backupSvc.getBackupOption())) {
            backupSvc.backupToBackupDir();
        } else if (backupSvc.getDefaultBackupOption().equals(BackupOption.ALERT_BEFORE_CLOSING)) {
            alertBeforeClosing();
        }

        Collection<LifecycleManager> c = getAllOtherLifecycleManagers();
        for (Iterator<LifecycleManager> i = c.iterator(); i.hasNext();) {
            LifecycleManager lm = i.next();
            lm.exit();
        }
    }

    private void alertBeforeClosing() {
        String line1 = "Do you want to backup the datbase?";
        String line2;
        Long backupTime = backupSvc.getLastBackupTime();
        if (backupTime == null) {
            line2 = "There is no backup";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd hh:mm a");
            line2 = String.format("The last backup was %s", format.format(new Date(backupTime)));
        }
        String msg = String.format(CNST.MSG_FORMAT, line1, line2);
        String title = "Backup database alert";
        DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
        Object answer = DialogDisplayer.getDefault().notify(c);
        if (answer.equals(DialogDescriptor.YES_OPTION)) {
            backupSvc.backupToBackupDir();
        }
    }

    @Override
    public void saveAll() {
        Collection<LifecycleManager> c = getAllOtherLifecycleManagers();
        for (Iterator<LifecycleManager> i = c.iterator(); i.hasNext();) {
            LifecycleManager lm = i.next();
            if (lm == this) {
                lm.saveAll();
            }
        }
    }

    private Collection<LifecycleManager> getAllOtherLifecycleManagers() {
        Collection<LifecycleManager> ret = new ArrayList<LifecycleManager>();
        Collection<LifecycleManager> c = Lookup.getDefault().lookup(new Lookup.Template(LifecycleManager.class)).allInstances();
        for (Iterator<LifecycleManager> i = c.iterator(); i.hasNext();) {
            LifecycleManager lm = i.next();
            if (lm != this) {
                ret.add(lm);
            }
        }
        return ret;
    }

    @Override
    public void markForRestart() {
        Collection<LifecycleManager> c = getAllOtherLifecycleManagers();
        for (Iterator<LifecycleManager> i = c.iterator(); i.hasNext();) {
            LifecycleManager lm = i.next();
            if (lm != this) {
                lm.markForRestart();
            }
        }
    }

    @Override
    public void exit(int status) {
        Collection<LifecycleManager> c = getAllOtherLifecycleManagers();
        for (Iterator<LifecycleManager> i = c.iterator(); i.hasNext();) {
            LifecycleManager lm = i.next();
            lm.exit(status);
        }
    }
}
