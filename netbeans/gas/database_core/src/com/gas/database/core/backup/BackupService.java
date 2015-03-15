/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.backup;

import com.gas.common.ui.util.FileHelper;
import com.gas.database.core.backup.api.BackupOption;
import com.gas.database.core.backup.api.IBackupService;
import static com.gas.database.core.backup.api.IBackupService.H2DB_EXT;
import static com.gas.database.core.backup.api.IBackupService.KEY_BACKUPDIR;
import static com.gas.database.core.backup.api.IBackupService.KEY_BACKUP_OPTION;
import static com.gas.database.core.backup.api.IBackupService.KEY_LAST_BACKUP_TIME;
import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IBackupService.class)
public class BackupService implements IBackupService {

    private static final String PATTERN = "-yyyy-MM-dd-hh-mm-ss";
    private SimpleDateFormat format = new SimpleDateFormat(PATTERN);
    private IDbConnSettingsService dbConnSettingsSvc = Lookup.getDefault().lookup(IDbConnSettingsService.class);
    private boolean markForRestoreFromBackupDir;
    private File backupDbFile;

    @Override
    public void script(File file) {

        String sql = String.format("SCRIPT DROP to '%s'", file.toString());

        IDbConnSettingsService service = Lookup.getDefault().lookup(IDbConnSettingsService.class);
        DbConnSettings settings = service.getDbConnSettings();

        SQLHelper.execute(settings, sql.toString());
    }

    @Override
    public boolean isMarkForRestoreFromBackupDir() {
        return markForRestoreFromBackupDir;
    }

    @Override
    public void restoreFromBackupDir() {
        DbConnSettings settings = dbConnSettingsSvc.getDbConnSettings();
        File dbFile = settings.getDatabaseFile();
        FileHelper.copy(backupDbFile, dbFile);
    }

    @Override
    public void updateBackupDir(File newBackupDir, boolean moveBackups) {
        if (moveBackups) {
            File[] dbFiles = getDbFilesFromBackupDir();
            for (File dbFile : dbFiles) {
                File newDbFile = new File(newBackupDir, dbFile.getName());
                FileHelper.copy(dbFile, newDbFile);
            }
        }
        Preferences pref = Preferences.userNodeForPackage(BackupService.class);
        pref.put(KEY_BACKUPDIR, newBackupDir.getAbsolutePath());
    }

    @Override
    public void updateBackupDir(File backupDir) {
        updateBackupDir(backupDir, false);
    }

    @Override
    public void setLastBackupTime(long time) {
        Preferences pref = Preferences.userNodeForPackage(BackupService.class);
        pref.putLong(KEY_LAST_BACKUP_TIME, time);
    }

    @Override
    public Long getLastBackupTime() {
        Preferences pref = Preferences.userNodeForPackage(BackupService.class);
        long ret = pref.getLong(KEY_LAST_BACKUP_TIME, 0);
        if (ret == 0) {
            return null;
        } else {
            return ret;
        }
    }

    @Override
    public File getBackupDir() {
        Preferences pref = Preferences.userNodeForPackage(BackupService.class);
        String path = pref.get(KEY_BACKUPDIR, "");
        if (path.isEmpty()) {
            return null;
        } else {
            return new File(path);
        }
    }

    @Override
    public File getDefaultBackupDir() {
        File homeDir = new File(System.getProperty("user.home"));
        File ret = new File(homeDir, "VectorFriends/db_backups");
        if (!ret.exists()) {
            ret.mkdir();
        }
        return ret;
    }

    @Override
    public BackupOption getDefaultBackupOption() {
        return BackupOption.ALERT_BEFORE_CLOSING;
    }

    @Override
    public BackupOption getBackupOption() {
        Preferences pref = Preferences.userNodeForPackage(BackupService.class);
        String str = pref.get(KEY_BACKUP_OPTION, null);
        if (str != null) {
            BackupOption ret = BackupOption.findByName(str);
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public void saveBackupOption(BackupOption backupOption) {
        Preferences pref = Preferences.userNodeForPackage(BackupService.class);
        pref.put(KEY_BACKUP_OPTION, backupOption.name());
    }

    @Override
    public boolean isBackupDirEnoughSpace() {
        DbConnSettings settings = dbConnSettingsSvc.getDbConnSettings();
        File dbFile = settings.getDatabaseFile();
        File dir = getBackupDir();

        long fileSize = dbFile.length();
        long freeSpace = dir.getFreeSpace();
        return freeSpace > fileSize;
    }

    @Override
    public void backupToBackupDir() {
        DbConnSettings settings = dbConnSettingsSvc.getDbConnSettings();
        File dbFile = settings.getDatabaseFile();
        File dir = getBackupDir();
        if (dir == null) {
            dir = getDefaultBackupDir();
        }
        File destFile = createBackupFile(dir, dbFile);
        FileHelper.copy(dbFile, destFile);
        setLastBackupTime(System.currentTimeMillis());
    }

    @Override
    public void markForRestoreFromBackupDir(boolean markForRestoreFromBackupDir, File backupDbFile) {
        this.markForRestoreFromBackupDir = markForRestoreFromBackupDir;
        this.backupDbFile = backupDbFile;
    }

    private File createBackupFile(File dir, File sourceFile) {
        File[] existingFiles = dir.listFiles(createDbFileFilter());
        
        List<String> existingNames = new ArrayList<String>();
        for (File file : existingFiles) {
            existingNames.add(file.getName());
        }
        String name = proposeFileName(sourceFile);
        while (existingNames.contains(name)) {
            name = proposeFileName(sourceFile);
        }
        return new File(dir, name);
    }

    @Override
    public String getOriginalFileName(File dbBackupFile) {
        String ret = dbBackupFile.getName();
        ret = ret.substring(0, ret.length() - H2DB_EXT.length());
        if (ret.length() > PATTERN.length()) {
            ret = ret.substring(0, ret.length() - PATTERN.length());
        }
        return ret;
    }

    @Override
    public boolean isBackupDirEmpty() {
        File dir = getBackupDir();
        File[] ret = dir.listFiles(createDbFileFilter());
        return ret == null || ret.length == 0;        
    }

    private FileFilter createDbFileFilter() {
        FileFilter ret = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                } else {
                    String name = file.getName();
                    return name.endsWith(H2DB_EXT);
                }
            }
        };
        return ret;
    }
    
    public long getDbFilesSizeFromBackupDir(){
        long ret = 0;
        File[] files = getDbFilesFromBackupDir();
        for(File file: files){
            ret += file.length();
        }
        return ret;
    }

    @Override
    public File[] getDbFilesFromBackupDir() {
        File dir = getBackupDir();
        File[] ret = dir.listFiles(createDbFileFilter());
        return ret;
    }

    private String proposeFileName(File sourceFile) {
        String name = sourceFile.getName();

        StringBuilder ret = new StringBuilder();
        ret.append(name.substring(0, name.length() - H2DB_EXT.length()));
        ret.append(format.format(new Date()));
        ret.append(H2DB_EXT);

        return ret.toString();
    }
}
